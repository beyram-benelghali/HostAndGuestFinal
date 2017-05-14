<?php

namespace BookingBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity
 */
class Booking
{
    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    protected $id;

    /**
     * @ORM\Column(type="integer")
     */
    protected $term;

    /**
     * @ORM\Column(type="integer")
     */
    protected $nbReservedRooms;

    /**
     * @ORM\Column(type="integer")
     */
    protected $totalAmount;

    /**
     * @ORM\Column(type="boolean")
     */
    protected $reviewed;

    /**
     * @ORM\Column(type="date")
     */
    protected $bookingDate;

    /**
     * @ORM\ManyToOne(targetEntity="PropertyBundle\Entity\Property")
     */
    protected $property;

    /**
     * @ORM\ManyToOne(targetEntity="UserBundle\Entity\User")
     */
    protected $guest;

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $id
     */
    public function setId($id)
    {
        $this->id = $id;
    }

    /**
     * @return mixed
     */
    public function getTerm()
    {
        return $this->term;
    }

    /**
     * @param mixed $term
     */
    public function setTerm($term)
    {
        $this->term = $term;
    }

    /**
     * @return mixed
     */
    public function getNbReservedRooms()
    {
        return $this->nbReservedRooms;
    }

    /**
     * @param mixed $nbReservedRooms
     */
    public function setNbReservedRooms($nbReservedRooms)
    {
        $this->nbReservedRooms = $nbReservedRooms;
    }

    /**
     * @return mixed
     */
    public function getReviewed()
    {
        return $this->reviewed;
    }

    /**
     * @param mixed $reviewed
     */
    public function setReviewed($reviewed)
    {
        $this->reviewed = $reviewed;
    }

    /**
     * @return mixed
     */
    public function getBookingDate()
    {
        return $this->bookingDate;
    }

    /**
     * @param mixed $bookingDate
     */
    public function setBookingDate($bookingDate)
    {
        $this->bookingDate = $bookingDate;
    }

    /**
     * @return mixed
     */
    public function getProperty()
    {
        return $this->property;
    }

    /**
     * @param mixed $property
     */
    public function setProperty($property)
    {
        $this->property = $property;
    }

    /**
     * @return mixed
     */
    public function getGuest()
    {
        return $this->guest;
    }

    /**
     * @param mixed $guest
     */
    public function setGuest($guest)
    {
        $this->guest = $guest;
    }

    /**
     * @return mixed
     */
    public function getTotalAmount()
    {
        return $this->totalAmount;
    }

    /**
     * @param mixed $totalAmount
     */
    public function setTotalAmount($totalAmount)
    {
        $this->totalAmount = $totalAmount;
    }
}
