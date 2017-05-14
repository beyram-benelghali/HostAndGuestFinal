<?php

namespace PropertyBundle\Controller;

use PropertyBundle\Entity\Property;
use PropertyBundle\Entity\property_picture;
use PropertyBundle\Form\PictureForm;
use PropertyBundle\Form\PropertyType;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

class PropertyController extends Controller
{
    public function addAction(Request $request)
    {
        $this->denyAccessUnlessGranted('ROLE_USER');
        $property = new Property();
        $form = $this->CreateForm(PropertyType::class,$property);
        $form->handleRequest($request);
        $pic_array = array();
        if($form->isValid() ){
            $user = $this->getUser();
            $em=$this->getDoctrine()->getManager();
            $files = $form->get('imagesPath')->getData();
            foreach ($files as $fl) {
                $name = $fl->getClientOriginalName();
                $dir = __DIR__.'/../../../web/images/uploads';
                $fl->move($dir, $name) ;
                array_push($pic_array, "../../../web/images/uploads/".$name);
            }
            $property->setHost($user);
            $property->setImagesPath($pic_array);
            $em->persist($property);
            $em->flush();
            return $this->redirectToRoute('property_mylistpage');
        }
        return $this->render('PropertyBundle:Property:add.html.twig', array('form'=>$form->createView(),'user'=>$this->getUser()));
    }

    public function updateAction(Request $request, $id){
        $this->denyAccessUnlessGranted('ROLE_USER');
        $em = $this->getDoctrine()->getManager();
        $logement = $em->getRepository("PropertyBundle:Property")->find($id);
        $pic_array = $logement->getImagesPath();
        $form = $this->CreateForm(PropertyType::class,$logement);
        $form->handleRequest($request);
        if($form->isValid() ){
            $em=$this->getDoctrine()->getManager();
            $files = $form->get('imagesPath')->getData();
            foreach ($files as $fl) {
                $name = $fl->getClientOriginalName();
                $dir = __DIR__.'/../../../web/images/uploads';
                $fl->move($dir, $name) ;
                array_push($pic_array, "../../../web/images/uploads/".$name);
            }
            $logement->setImagesPath($pic_array);
            $em->persist($logement);
            $em->flush();
            return $this->redirectToRoute('property_mylistpage');
        }

        return $this->render('PropertyBundle:Property:add.html.twig', array('form'=>$form->createView(),'user'=>$this->getUser()));
    }

    public function deleteAction($id){
        $em = $this->getDoctrine()->getManager();
        $logement = $em->getRepository("PropertyBundle:Property")->find($id);
        $em->remove($logement);
        $em->flush();
        return $this->redirectToRoute('property_mylistpage');
    }

    public function listAction()
    {
        $em = $this->getDoctrine()->getManager();
        $logement = $em->getRepository("PropertyBundle:Property")->findBy(array('host'=> $this->getUser(),'enabled'=>true));
       // var_dump(unserialize($logement));
        return $this->render("PropertyBundle:Property:list.html.twig",array('logements'=>$logement,'user'=>$this->getUser()));
    }

    public function getPropertyAction($id, Request $request) {
        $em = $this->getDoctrine()->getManager();
        $logement = $em->getRepository("PropertyBundle:Property")->find($id);

        $reviews = $this->get('review')->getReviewsAction($id, $request);

        $add_review_call = $this->get('review')->addReviewAction($request, $id);
        if ($add_review_call instanceof Response)
            return $add_review_call;

        $uid = $this->get('security.token_storage')->getToken()->getUser();
        if ($uid != "anon.")
            $uid->getId();

        $experiences = $this->get('experience')->affichExperienceAction($id);

        $add_experience_call = $this->get('experience')->addExperienceAction($request, $id);

        if ($add_experience_call instanceof Response)
            return $add_experience_call;

        return $this->render("PropertyBundle:Property:detail.html.twig",array('logement'=>$logement,'user'=>$this->getUser(),
            'reviews' => $reviews, 'add_review' => $add_review_call, 'experiences' => $experiences, 'experience_frm_add' => $add_experience_call, "uid" => $uid));
    }

    public function reportAction($id){
        $this->denyAccessUnlessGranted('ROLE_USER');
        $em = $this->getDoctrine()->getManager();
        $logement = $em->getRepository("PropertyBundle:Property")->find($id);
        $logement->setReported(true);
        //$logement->getHost();
        $em->persist($logement);
        $em->flush();
        return $this->redirectToRoute('property_detailProp',array('id'=>$id));
    }

    public function allRoomAction(){
        $em = $this->getDoctrine()->getManager();
        $logement = $em->getRepository("PropertyBundle:Property")->findBy(array('enabled'=>true));
        return $this->render("PropertyBundle:Property:allroom.html.twig",array('logement'=>$logement,'user'=>$this->getUser()));
    }

    public function feedAction()
    {
        $logs = $this->getDoctrine()->getRepository('PropertyBundle:Property')->findAll();
        $feed = $this->get('eko_feed.feed.manager')->get('property');
        $feed->addFromArray($logs);
        return new Response($feed->render('rss')); // ou 'atom'
    }

    public function searchPropertyAction(Request $request){
        $price = $request->query->get('price');
        $location = $request->query->get('location');
        $nbroom = $request->query->get('nbroom');
        $date = $request->query->get('date');
        $em=$this->getDoctrine()->getManager();
        $searchArray = array();
        $searchArray['enabled'] = true;
        if($nbroom != null){
            $searchArray['nbRooms'] = $nbroom;
        }
        if($location != null){
            $searchArray['location'] = $location;
        }
        if($price != null){
            $searchArray['price'] = $price;
        }
        if($date != null){
            $searchArray['date'] = $date;
        }
        $list = $em->getRepository('PropertyBundle:Property')->findBy($searchArray);
        return $this->render("PropertyBundle:Property:allroom.html.twig",array('logement'=>$list,'user'=>$this->getUser()));
    }

}
