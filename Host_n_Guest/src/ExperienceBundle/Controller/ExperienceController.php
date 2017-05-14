<?php

namespace ExperienceBundle\Controller;

use ExperienceBundle\Entity\Experience;
use ExperienceBundle\Form\AddExperience;
use ExperienceBundle\Form\UpdateExperience;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

class ExperienceController extends Controller
{
    public function addExperienceAction (Request $request, $pid)
    {
        if (!$this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY')) {
            //throw $this->createAccessDeniedException();
        }
        // getting the user id
        //$user_id = $this->get('security.token_storage')->getToken()->getUser()->getId();

        $em = $this->getDoctrine()->getManager();

        $experience = new Experience();
        $form = $this->createForm(AddExperience::class,$experience);

        $form->handleRequest($request);

        if($form->isValid())
        {
            $properpty = $em->getRepository('PropertyBundle:Property')->find($pid);
            $currentDate = new \DateTime('today');

            $experience->setProperty($properpty);
            $experience->setPublicationDate($currentDate);

            $em->persist($experience);
            $em->flush();

            //return $this->redirectToRoute('experience_homepage');
            return $this->redirectToRoute('property_detailProp', array("id" => $pid));
        }

        //return $this->render('ExperienceBundle:Experience:AddExperience.html.twig', array('frm_add' => $form->createView()));
        return $form->createView();
    }

    public function affichExperienceAction ($pid)
    {
        $uid = 0;
        if ($this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY'))
                $uid = $this->get('security.token_storage')->getToken()->getUser()->getId();

        $em = $this->getDoctrine()->getManager();
        $experienceList = $em->getRepository('ExperienceBundle:Experience')->findBy(array('property' => $pid));

        //return $this->render('ExperienceBundle:Experience:affichage.html.twig',array('exp_lst' => $experienceList,"uid" => $uid));
        return $experienceList;
    }

    public function affichDetailedExperienceAction($id)
    {

        $em = $this->getDoctrine()->getManager();
        $experience = $em->getRepository('ExperienceBundle:Experience')->find($id);

        return $this->render('@Experience/Experience/detail.html.twig',array('experience' => $experience));
    }

    public function removeExperienceAction($id)
    {
        if (!$this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY'))
        {
            throw $this->createAccessDeniedException();
        }
        $user_id = $this->get('security.token_storage')->getToken()->getUser()->getId();

        $em = $this->getDoctrine()->getManager();
        $experience = $em->getRepository('ExperienceBundle:Experience')->find($id);
        $pid = $experience->getProperty()->getId();

        if($experience!=NULL)
        {
            if($experience->getProperty()->getHost()->getId()!=$user_id)
                //return new Response("You are not authorized to remove the experience");
                return $this->redirectToRoute('property_detailProp', array("id" => $pid));

            $em->remove($experience);
            $em->flush();
        }

        return $this->redirectToRoute('property_detailProp', array("id" => $pid));
    }

    public function updateExperienceAction($id,Request $request)
    {
        if (!$this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY'))
        {
            throw $this->createAccessDeniedException();
        }
        // getting the user id
        $user_id = $this->get('security.token_storage')->getToken()->getUser()->getId();

        $em = $this->getDoctrine()->getManager();

        $experience = $em->getRepository('ExperienceBundle:Experience')->find($id);
        $form = $this->createForm(UpdateExperience::class,$experience);

        $form->handleRequest($request);

        if($form->isSubmitted())
        {
            $em->persist($experience);
            $em->flush();

            return $this->redirectToRoute('experience_homepage');
        }

        return $this->render('ExperienceBundle:Experience:AddExperience.html.twig', array('frm_add' => $form->createView()));
    }

}
