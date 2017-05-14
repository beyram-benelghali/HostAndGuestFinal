<?php

namespace ReviewBundle\Repository;


use Doctrine\DBAL\Query\QueryBuilder;
use Doctrine\ORM\EntityRepository;
use ReflectionClass;
use ReflectionProperty;
use ReviewBundle\Entity\Review;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Session\Session;
use UserBundle\Entity\User;
use BookingBundle\Entity\Booking;
use PropertyBundle\Entity\Property;

class ReviewRepository extends EntityRepository
{
    public function reviewAuthorizationAndReservation($user_id, $property_id)
    {
        $current_date = new \DateTime('today');

        // supposing bookings dont get deleted, we will retrieve the booking list based on date
        $booking_list = $this->getEntityManager()->createQuery(
            "select b 
                      from PropertyBundle:Property p, BookingBundle:Booking b
                      where p.id = b.property
                      and b.reviewed = 0 
                      and b.guest = :uid
                      and p.id = :pid
                      order by b.bookingDate DESC 
            ")
            ->setParameter('uid', $user_id)
            ->setParameter('pid', $property_id)
            ->getResult();

        $found_any = false;
        foreach ($booking_list as $key => $value)
        {
            $book_date = $value->getBookingDate();
            if ($book_date->add(new \DateInterval('P'. $value->getTerm() .'D')) <= $current_date)
            {
                $found_any = true;
            }
            else
            {
                unset($booking_list[$key]);
            }
        }

        if ($found_any)
            return $booking_list;
        else
            return null;
    }

    public function getReviewsOfProperty($property_id, Request $request, Session $session)
    {
        // Paginator cannot support two FROM components or composite identifiers, because it cannot predict the total count in the database.

        /*$count = $this->getEntityManager()->createQuery(
            "select COUNT (r) 
                      from BookingBundle:Booking b, ReviewBundle:Review r
                      where b.id = r.booking
                      and b.property = :pid
            ")
            ->setParameter('pid', $property_id)
            ->getSingleScalarResult();

        // we return the query and let Knp Paginator handle the rest
            $this->getEntityManager()->createQuery(
            "select r 
                      from BookingBundle:Booking b, ReviewBundle:Review r
                      where b.id = r.booking
                      and b.property = :pid
            ")
            ->setParameter('pid', $property_id)
            ->setHint('knp_paginator.count', $count);*/
           // ->getResult();

        $qb = $this->getEntityManager()->createQueryBuilder();

        $qb
            ->select('r')
            ->from('ReviewBundle:Review', 'r')
            ->innerJoin('BookingBundle:Booking', 'b', 'WITH', 'b.id = r.booking')
            ->where('b.property = ?1')
            ->setParameter('1', $property_id);

        // Knp Paginator resets the request everytime the page is changed
        // therefore below logic is needed

        {
            if ($request->get('submitSortForm') != null)
            {
                if ($session->get('orderLevel') != null)
                {
                    $session->remove('orderLevel');
                }
            }

            $orderBy = $session->get('orderLevel') == null ?
                $request->get('orderLevel') == null ?
                    'ASC' : $request->get('orderLevel') : $session->get('orderLevel');

            // orderLevel is a radio input
            // its session value will never be empty (null)
            // except at the first page call
            $session->set('orderLevel', $orderBy);

            //You can't use placeholders for dynamical build of DQL query
            // You have to code it by your own

            $reflect = new ReflectionClass(new Review());
            $props = $reflect->getProperties(ReflectionProperty::IS_PROTECTED);

            foreach ($props as $prop) {
                $propertyName = $prop->getName();

                // when user submits we reset the session attributes we set
                if ($request->get($propertyName) == null  && $request->get('submitSortForm') != null)
                {
                    if ($session->get($propertyName) != null)
                    {
                        $session->remove($propertyName);
                    }
                }

                if ($request->get($propertyName) != null || $session->get($propertyName) != null)
                {
                    // QueryBuilder orderBy() doesn't work with multiple columns
                    // unless you specify all the columns at once [orderBy('a, b, c, d')]
                    $qb->addOrderBy('r.' . $propertyName, $orderBy);

                    if ($session->get($propertyName) == null)
                    {
                        $session->set($propertyName, $propertyName); // key, value
                    }
                }
            }
        }

        return $qb;
    }
}