<?php

namespace MessagingBundle\Controller;


use FOS\MessageBundle\Controller\MessageController as msController;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use FOS\MessageBundle\EntityManager\MessageManager;
use FOS\MessageBundle\EntityManager\ThreadManager;
use MessagingBundle\Entity\Message;
use MessagingBundle\Entity\MessageMetadata;
use MessagingBundle\Entity\Thread;
use MessagingBundle\Entity\ThreadMetadata;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use UserBundle\Entity\User;

class MessageController extends msController
{
    public function indexAction($name)
    {
        return $this->render('', array('name' => $name));
    }

    public function sendToAction(Request $request)
    {
        $mess = $request->get('message');
        $userId =$request->get('userId');

        $composer = $this->container->get('fos_message.composer');
        $user = $this->getDoctrine()->getRepository(User::class)->find($userId);
        $this->sendNotificationTo($this->getUser()->getFirstName().' a dit : '.$mess,$userId);
        // tester si un thread avec user exite deja
        $t1 = $this->getDoctrine()->getRepository(Thread::class)->findOneBy(array('subject' => $this->getUser()->getId().'_'.$userId));
        $t2 = $this->getDoctrine()->getRepository(Thread::class)->findOneBy(array('subject' => $userId.'_'.$this->getUser()->getId()));

        if($t1 != null ) $t=$t1;
        elseif ($t2!=null) $t=$t2;
        //si n'existe pas
        if($t1 == null && $t2 == null){
            $message = $composer->newThread()
                ->setSender($this->getUser())
                ->addRecipient($user)
                ->setSubject($this->getUser()->getId().'_'.$userId)
                ->setBody($mess)
                ->getMessage();

            $sender = $this->container->get('fos_message.sender');
            $sender->send($message);
            // Si existe
        }else{
            $message = $composer->reply($t)
                ->setSender($this->getUser())
                ->setBody($mess)
                ->getMessage();
            $sender = $this->container->get('fos_message.sender');
            $sender->send($message);
        }
        return new Response('');

    }

    public function loadConversationAction(Request $request)
    {
        $userId =$request->get('userId');
        $this->get('twig')->addGlobal('currentConversation', $userId);
       // $provider = $this->container->get('fos_message.provider');
        $currentUser= $this->getUser();
        $user = $this->getDoctrine()->getRepository(User::class)->find($userId);
        $thread = $this->getDoctrine()->getRepository(Thread::class)->findOneBy(array('subject' => $userId.'_'.$currentUser->getId()));
        if($thread == null){
            $thread = $this->getDoctrine()->getRepository(Thread::class)->findOneBy(array('subject' => $currentUser->getId().'_'.$userId));
        }
        foreach ($thread->getMessages() as $message) {
            $minutesDiff= date_diff($message->getCreatedAt(),new \DateTime())->format("il y a %i m %h h %a j");

            if($message->getSender() == $this->getUser()){
                $html ='<div class="row msg_container base_sent">
                        <div class="col-md-10 col-xs-10">
                            <div class="messages msg_sent">
                                <p>'.$message->getBody().'</p>
                                <time datetime="'.$message->getCreatedAt()->format('Y-m-d H:i:s').'">'.$message->getSender()->getLastName().' • '.$minutesDiff.'</time>
                            </div>
                        </div>
                        <div class="col-md-2 col-xs-2 avatar">
                            <img src="http://www.bitrebels.com/wp-content/uploads/2011/02/Original-Facebook-Geek-Profile-Avatar-1.jpg" class=" img-responsive ">
                        </div>
                    </div>';
            }
            else{
                $html = ' <div class="row msg_container base_receive">
                        <div class="col-md-2 col-xs-2 avatar">
                            <img src="http://www.bitrebels.com/wp-content/uploads/2011/02/Original-Facebook-Geek-Profile-Avatar-1.jpg" class=" img-responsive ">
                        </div>
                        <div class="col-md-10 col-xs-10">
                            <div class="messages msg_receive">
                                <p>'.$message->getBody().'</p>
                                <time datetime="2009-11-13T20:00">'.$message->getSender()->getLastName().' • '.$minutesDiff.'</time>
                            </div>
                        </div>
                    </div>';

            }
            echo $html;

        }
        echo '<span id="username" hidden>'.$user->getLastName().' '.$user->getFirstName().'</span>';
        echo '<span id="nbMessages" hidden>'.count($thread->getMessages()).'</span>';
        return new Response('');
    }

    public function getUserListAction()
    {
        $mm = new MessageManager($this->getDoctrine()->getEntityManager(),Message::class,MessageMetadata::class);
        $th = new ThreadManager($this->getDoctrine()->getEntityManager(),Thread::class,ThreadMetadata::class,$mm);
        //echo $this->getUser();
        $threads=$th->findParticipantInboxThreads($this->getUser());
        $listuser = array();


        $provider = $this->container->get('fos_message.provider');

        /*
        $thread =$provider->getThread(10);
        $participants = $thread->getParticipants();
        echo $participants[0]->getId();
        echo $participants[1]->getId();
        */
        foreach ($threads as $thread) {
            $threadr =$provider->getThread($thread->getId());
            $participants = $threadr->getParticipants();

           // echo $participants[0];
          //  echo $provider->getThread(21);
                if($participants[0] == $this->getUser()){
                    array_push($listuser,$participants[1]);
                }else{
                    array_push($listuser,$participants[0]);
                }
          //  echo  print_r($participants);
                //echo $listuser;
               // echo $listuser[0]->getLastName();

                // echo '<a onclick="setChat(' . $sender->getId() . ')" class="list-group-item">' . $sender . '</a>';
        }
        return $this->render('MessagingBundle:chat:chatlist.html.twig',array('listuser'=>$listuser,'user'=>$this->getUser()));

    }

    public function sendNotificationTo($message,$userName){
        echo'<h1>Wooow</h1>';
        $content = array(
            "en" => $message
        );

        $fields = array(
            'app_id' => "076081d3-cbe8-4fed-925a-b09d50ecb266",
            'included_segments' => array('All'),
            'data' => array("foo" => "bar"),
            'contents' => $content,
            //'web_buttons'=> array(array("id" => "like-button", "text"=> "Like", "icon"=> "http://i.imgur.com/N8SN8ZS.png", "url"=> "http://test1804.me.pn/indexs.html")),
            'chrome_web_icon'=>'https://img4.hostingpics.net/pics/902118appletouchicon144x144precomposed.png',
            'filters' => array(array("field" => "tag", "key" => "userid", "relation" => "=", "value" => $userName)),
        );

        $fields = json_encode($fields);
        print("\nJSON sent:\n");
        print($fields);

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "https://onesignal.com/api/v1/notifications");
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json; charset=utf-8',
            'Authorization: Basic M2I5OGI4OWQtMTYwNi00NWRkLTkxNTYtMGUzYzUyN2ZlNjE5'));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_HEADER, FALSE);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);

        $response = curl_exec($ch);
        curl_close($ch);

        $return["allresponses"] = $response;
        $return = json_encode( $return);

        print("\n\nJSON received:\n");
        print($return);
        print("\n");
    }
}
