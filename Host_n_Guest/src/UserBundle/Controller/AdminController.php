<?php

namespace UserBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class AdminController extends Controller
{

    public function PanelAction()
    {
        $em= $this->getDoctrine()->getManager();
        $userManager = $this->get('fos_user.user_manager');
        $users = $userManager->findUsers();
        $nbUsers = count($users);
        $properties = $em->getRepository ('PropertyBundle:Property')->findAll();
        $nbProperties = count($properties);
        $bookings = $em->getRepository ('BookingBundle:Booking')->findAll();
        $nbBookings = count($bookings);
        return $this->render("UserBundle:Users:adminPanel.html.twig",array("admin"=>$this->getUser(),"listBooking"=>$bookings,"listprop"=>$properties,"nbUsers"=>$nbUsers,"nbProperties"=>$nbProperties,"nbBookings"=>$nbBookings));
    }

    public function panelPropertyAction()
    {
        $em= $this->getDoctrine()->getManager();
        $properties = $em->getRepository ('PropertyBundle:Property')->findAll();
        $nbProperties = count($properties);
        return $this->render("UserBundle:Users:propertyPanel.html.twig",array("admin"=>$this->getUser(),"listprop"=>$properties,"nbProperties"=>$nbProperties));
    }

    public function panelUserAction()
    {
        $userManager = $this->get('fos_user.user_manager');
        $users = $userManager->findUsers();
        $userss = array();
        foreach ($users as $use) {
            if (! in_array('ROLE_ADMIN', $use->getRoles(), true) )
            {
                array_push($userss,$use);
            }
        }
        $nbUsers = count($userss);
        return $this->render("UserBundle:Users:userPanel.html.twig",array("admin"=>$this->getUser(),"users"=>$userss,"nbUsers"=>$nbUsers));
    }

    public function panelBookAction()
    {
        $em= $this->getDoctrine()->getManager();
        $bookings = $em->getRepository ('BookingBundle:Booking')->findAll();
        $nbBookings = count($bookings);
        return $this->render("UserBundle:Users:bookingPanel.html.twig",array("admin"=>$this->getUser(),"bookings"=>$bookings,"nbBookings"=>$nbBookings));
    }

    public function setHiddenAction($id){
        $em= $this->getDoctrine()->getManager();
        $propertie = $em->getRepository ('PropertyBundle:Property')->find($id);
        $propertie->setEnabled(false);
        $em->persist($propertie);
        $em->flush();
        return $this->redirectToRoute('user_adminPanelProperty');
    }

    public function bannedAction($id){
        $em= $this->getDoctrine()->getManager();
        $user= $em->getRepository('UserBundle\Entity\User')->find($id);
        $user->setEnabled(false);
        $em->flush($user);
        return $this->redirectToRoute('user_adminPanelUser');

    }
}
