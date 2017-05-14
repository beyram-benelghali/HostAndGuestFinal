<?php

namespace ReviewBundle\Controller;

use ReflectionClass;
use ReflectionProperty;
use ReviewBundle\Entity\Review;
use ReviewBundle\Form\AddReview;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\Session;

class ReviewController extends Controller
{
    /**
     * get list of reviews of given property
     * @param integer $pid property id
     * @param Request $request
     * @return array
    */
    public function getReviewsAction($pid, Request $request)
    {
        $em = $this->getDoctrine()->getManager();

        // sorting occurs in the repository method called below
        $reviews = $em->getRepository('ReviewBundle:Review')->getReviewsOfProperty($pid, $request, $this->get('session'));

        $paginator  = $this->get('knp_paginator');

        /*
         * instead of giving page and limit a static value we use the request
         * static value would mean this would go off and pull back the entire dataset - all 1 million rows (for example) -
         * then pass them into the paginator which would slice and dice them and give us the tiny portion of records we actually wanted.
         * This is incredibly inefficient, and the problem gets worse the more the table grows.
         * Instead, if we tweak this only slightly, and rather than pass in the result to our paginator, we pass in the as-yet-unrun query,
         * then KNP Paginator can alter our query to ensure it only requests the tiny sliver of data we really wanted:
         */
        /*
         * query ($reviews) is a ParameterBag (container of key / values)
         */
        $pagination = $paginator->paginate(
            $reviews,
            $request->query->getInt('page', 1) /*page number*/,
            $request->query->getInt('limit', 1) /*limit per page*/
            //array('distinct' => false)
        );

        return $pagination;
    }

    /**
     * get list of reviews of given property
     * @param integer $pid property id
     * @param Request $request
     * @return mixed
     */
    public function addReviewAction(Request $request, $pid)
    {
        $em = $this->getDoctrine()->getManager();

        // make sure the user is allowed to comment first
        $user_id = $this->get('security.token_storage')->getToken()->getUser();
        // if user is not logged in user will be a string of value 'anon.' (default)
        if ($user_id != "anon.")
            $user_id->getId();

        // returns the reservation list if allowed else null
        $reservation_list = $em->getRepository('ReviewBundle:Review')->reviewAuthorizationAndReservation($user_id, $pid);

        if ($reservation_list == null)
            //return $this->render('@Review/Review/add.html.twig', array('is_not_allowed' => false));
            return false;

        $review = new Review();

        $form = $this->createForm(AddReview::class, $review, array('reservation_list' => $reservation_list));

        $form->handleRequest($request);

        if ($form->isValid() && $form->isSubmitted())
        {
            $reservation = $em->getRepository('BookingBundle:Booking')->find($review->getBooking()->getId());
            $reservation->setReviewed(1);

            $current_date = new \DateTime('today');
            $review->setDateComment($current_date);

            $em->persist($review);
            $em->persist($reservation);
            $em->flush();

            return $this->redirectToRoute('property_detailProp', array("id" => $pid));
        }

        //return $this->render('@Review/Review/add.html.twig', array('review_add_form' => $form->createView()));
        return $form->createView();
    }

    /**
     * remove review
     * @param integer $rid review_id
     * @return mixed
     */
    public  function deleteReviewAction($rid)
    {
        // user verification
        // default redirects to login page
        if (!$this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY')) {
            throw $this->createAccessDeniedException();
        }

        // getting the user id
        $user_id = $this->get('security.token_storage')->getToken()->getUser()->getId();

        $em = $this->getDoctrine()->getManager();

        // retrieving the review
        $review = $em->getRepository('ReviewBundle:Review')->findOneBy(array("id" => $rid));

        if ($review == null)
            // assuming this is an error route
            return $this->redirectToRoute('user_homepage');

        // making sure its the user who owns the review that is deleting it
        if ($review->getBooking()->getGuest()->getId() != $user_id)
            // assuming this is an error route
            return $this->redirectToRoute('user_homepage');

        // getting the property id for later redirection
        $pid = $review->getBooking()->getProperty()->getId();
        // resetting the booking reviewed field in case the user wants to review the reservation at a later time, again.
        $review->getBooking()->setReviewed(false);

        $em->remove($review);
        $em->flush();

        return $this->redirectToRoute('property_detailProp', array("id" => $pid));
    }
}
