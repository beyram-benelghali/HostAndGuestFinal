<?php

namespace BookingBundle\Controller;

use BookingBundle\Entity\Booking;
use BookingBundle\Form\AjouterRservation;
use PropertyBundle\Entity\Property;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

class BookingController extends Controller
{
    public function addreservationAction(Request $request, $id_property)
    {
        if (!$this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY'))
            throw $this->createAccessDeniedException();

        $bkn = new Booking();

        $form = $this->createForm(AjouterRservation::class, $bkn);

        $form->handleRequest($request);

        if ($form->isValid()) {
            $em = $this->getDoctrine()->getManager();

            /**
             * @var Property $property
             */
            $property = $em->getRepository('PropertyBundle:Property')->find($id_property);

            $user = $this->get('security.token_storage')->getToken()->getUser();

            if ($user != $property->getHost())
            {
                $date = new \DateTime("now");

                if ($bkn->getBookingDate() >= $date )
                {
                    if ($bkn->getBookingDate() > $property->getPublicationDate())
                    {
                        $givenNbRooms = $form->get('nbReservedRooms')->getData();
                        $maxNbRooms = $property->getNbRooms();

                        if ($givenNbRooms <= $maxNbRooms)
                        {
                            $reserveTemp = new Booking();
                            $reserveTemp->setBookingDate($bkn->getBookingDate());

                            #begin reservation date check
                            $reservations = $em->getRepository('BookingBundle:Booking')->findAll();

                            $inputReservationBeginDate = $reserveTemp->getBookingDate();
                            $inputReservationEndDate = $bkn->getBookingDate()->add(new \DateInterval('P' . $bkn->getTerm() . 'D'));

                            $nbReservedRoomsAtDateInterval = 0;

                            /**
                             * @var Booking $reservationXXX
                             */
                            foreach ($reservations as $reservationXXX)
                            {
                                $reservationBeginDate = $reservationXXX->getBookingDate();
                                $reservationEndDate = $reservationXXX->getBookingDate()->add(new \DateInterval('P' . $reservationXXX->getTerm() . 'D'));

                                // date superflu date reservation
                                if ($inputReservationBeginDate <= $reservationEndDate && $inputReservationBeginDate >= $reservationBeginDate && $inputReservationEndDate >= $reservationBeginDate && $inputReservationEndDate <= $reservationEndDate)
                                {
                                    $nbReservedRoomsAtDateInterval += $reservationXXX->getNbReservedRooms();
                                }
                                else
                                {
                                    if ($inputReservationEndDate >= $reservationBeginDate)
                                    {
                                        if ($inputReservationBeginDate <= $reservationEndDate)
                                        {
                                            $nbReservedRoomsAtDateInterval += $reservationXXX->getNbReservedRooms();
                                        }
                                    }
                                }
                            }
                            #end reservation date check

                            // based on the number of rooms counted from the reservations that met the conditions
                            // we make sure he can rent N rooms
                            if ($maxNbRooms >= ($nbReservedRoomsAtDateInterval + $givenNbRooms))
                            {
                                $qb = $em->createQueryBuilder();

                                // getting the last id to set the booking new id
                                $last_bid = $qb->select('b.id')
                                    ->from('BookingBundle:Booking', 'b')
                                    ->orderBy('b.id', 'DESC')
                                    ->setMaxResults(1)
                                    ->getQuery()
                                    ->getSingleScalarResult();

                                $bkn->setId($last_bid + 1);

                                $bkn->setGuest($user);
                                $bkn->setProperty($property);
                                $bkn->setReviewed(false);

                                $bkn->setTotalAmount($givenNbRooms * $property->getPrice() * $bkn->getTerm());

                                $em->persist($bkn);
                                $em->flush();

                                return $this->redirectToRoute('booking_listBkn');
                            }
                        }
                    }
                }
            }
        }


        return $this->render('@Booking/Booking/addBkn.html.twig', array('frmAddbkn' => $form->createView(), 'user' => $this->getUser()));
    }

    // only current user's bookings list
    public function listReservationAction()
    {
        if (!$this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY'))
            throw $this->createAccessDeniedException();

        $user = $this->get('security.token_storage')->getToken()->getUser();

        $em = $this->getDoctrine()->getManager();

        // show cancel reservation only if delay didn't pass
        $current_date = new \DateTime('now');

        $reservations = $em->getRepository('BookingBundle:Booking')->findBy(array('guest' => $user));

        return $this->render('@Booking/Booking/list.html.twig', array('reservations' => $reservations, 'user' => $user, 'todayDate' => $current_date));
    }

    // annuler reservation
    // only if date of booking is not up yet
    public function deleteReservationAction($reservation_id)
    {
        if (!$this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY'))
            throw $this->createAccessDeniedException();

        $em = $this->getDoctrine()->getManager();

        /**
         * @var Booking $reservation
         */
        $reservation = $em->getRepository('BookingBundle:Booking')->find($reservation_id);

        if ($reservation != null)
        {
            // booking date must be greater than the current date
            $current_date = new \DateTime('now');
            if ($reservation->getBookingDate() > $current_date)
            {
                $user = $this->get('security.token_storage')->getToken()->getUser();

                // making sure that the user that is trying to cancel the reservation is the current user
                if ($user == $reservation->getGuest())
                {
                    $em->remove($reservation);
                    $em->flush();
                }
            }
        }

        return $this->redirectToRoute('booking_listBkn');
    }

    public function returnPDFResponseFromHTMLAction($reservation_id)
    {
        if (!$this->get('security.authorization_checker')->isGranted('IS_AUTHENTICATED_FULLY'))
            throw $this->createAccessDeniedException();

        $user = $this->get('security.token_storage')->getToken()->getUser();

        $em = $this->getDoctrine()->getManager();

        /**
         * @var Booking $reservation
         */
        $reservation = $em->getRepository('BookingBundle:Booking')->find($reservation_id);

        //set_time_limit(30); uncomment this line according to your needs
        // If you are not in a controller, retrieve of some way the service container and then retrieve it
        //$pdf = $this->container->get("white_october.tcpdf")->create('vertical', PDF_UNIT, PDF_PAGE_FORMAT, true, 'UTF-8', false);
        //if you are in a controlller use :
        $pdf = $this->get("white_october.tcpdf")->create('vertical', PDF_UNIT, PDF_PAGE_FORMAT, true, 'UTF-8', false);

        // set document information
        $pdf->SetAuthor('Host And Guest Corp.');
        $pdf->SetTitle($user->getLastName() . '_' . $user->getFirstName() . '\'s Reservation');
        $pdf->SetSubject('Reservation');

        // set default header data
        $pdf->SetHeaderData('image_demo.jpg', 10, $user->getLastName() . '\'s Reservation', 'by Host And Guest', array(0,64,255), array(0,64,128));
        $pdf->setFooterData(array(0,64,0), array(0,64,128));

        // set header and footer fonts
        $pdf->setHeaderFont(Array(PDF_FONT_NAME_MAIN, '', PDF_FONT_SIZE_MAIN));
        $pdf->setFooterFont(Array(PDF_FONT_NAME_DATA, '', PDF_FONT_SIZE_DATA));

        $pdf->setFontSubsetting(true);
        $pdf->SetFont('helvetica', '', 13, '', true);
        //$pdf->SetMargins(20,20,40, true);
        $pdf->AddPage();

        $html = '
                <div class="strip_all_rooms_list wow fadeIn" data-wow-delay="0.1s">
                        <div class="col-lg-6 col-md-6 col-sm-6">
                            <div class="rooms_list_desc">
                                <strong>Booking Date On </strong><i>' . $reservation->getBookingDate()->format('D M Y') . '</i>
                                <p>
                                    Property Description :
                                    ' . $reservation->getProperty()->getDescription() . '
                                </p>
                                <p>
                                    Booking Term :
                                    ' . $reservation->getTerm() . ' 
                                </p>
                                <p>
                                    Number of Reserved Rooms : 
                                    ' . $reservation->getNbReservedRooms() .'
                                </p>
                                <p>
                                    Total Amount : 
                                    ' . $reservation->getTotalAmount() .'<sup>$</sup>
                                </p>
                            </div>
                        </div>
                </div><!--End strip -->
                ';

        $filename = 'Reservation ' . $user->getLastName();

        $pdf->writeHTMLCell($w = 0, $h = 0, $x = '', $y = '', $html, $border = 0, $ln = 1, $fill = 0, $reseth = true, $align = '', $autopadding = true);
        $pdf->Output($filename.".pdf",'I'); // This will output the PDF as a response directly
    }
}
