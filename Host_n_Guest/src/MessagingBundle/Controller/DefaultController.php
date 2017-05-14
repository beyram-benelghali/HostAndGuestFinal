<?php

namespace MessagingBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class DefaultController extends Controller
{
    public function indexAction()
    {
        return $this->render('MessagingBundle:Default:index.html.twig');
    }
}
