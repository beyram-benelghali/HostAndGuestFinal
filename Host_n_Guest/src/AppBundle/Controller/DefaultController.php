<?php

namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

class DefaultController extends Controller
{
    /**
     * @Route("/", name="homepage")
     */
    public function indexAction(Request $request)
    {
        $em = $this->getDoctrine()->getManager();
        $usr= $this->getUser();
        $logements = $em->getRepository("PropertyBundle:Property")->findBy(array(),array(),3);
        if($usr!=null){
            $roles = $usr->getRoles();
            // If is a admin we redirect to the backoffice area
            if (in_array('ROLE_ADMIN', $roles, true) )
            {
                return $this->redirect($this->generateUrl('user_adminpage'));
            }
            return $this->render('UserBundle::indexUser.html.twig',array('user'=>$usr,'listhome'=>$logements));
        }
        return $this->render('indexBase.html.twig',array('listhome'=>$logements));
        //return $this->render('UserBundle:indexUser.html.twig',array('user'=>$usr));

        // replace this example code with whatever you need
       /* return $this->render('default/index.html.twig', [
            'base_dir' => realpath($this->getParameter('kernel.root_dir').'/..').DIRECTORY_SEPARATOR,
        ]);*/
    }
}
