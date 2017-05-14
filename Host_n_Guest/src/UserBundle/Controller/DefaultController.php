<?php

namespace UserBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\RedirectResponse ;

class DefaultController extends Controller
{
    public function indexAction()
    {
        $em = $this->getDoctrine()->getManager();
        $usr= $this->getUser();
        $logements = $em->getRepository("PropertyBundle:Property")->findBy(array(),array(),3);
        return $this->render('UserBundle::indexUser.html.twig',array('user'=>$usr,'listhome'=>$logements));
    }

    public function adminAction()
    {
        return $this->redirect($this->generateUrl('user_adminpage'));
    }

    public function userHomeAction()
    {
        // get current user
        $em = $this->getDoctrine()->getManager();
        $usr= $this->getUser();
        $logements = $em->getRepository("PropertyBundle:Property")->findBy(array(),array(),3);
        return $this->render('UserBundle::indexUser.html.twig',array('user'=>$usr,'listhome'=>$logements));
    }


}
