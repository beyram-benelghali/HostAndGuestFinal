<?php

namespace ReviewBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity(repositoryClass="ReviewBundle\Repository\ReviewRepository")
 */
class Review
{
    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    private $id;

    /**
     * @ORM\Column(type="string", length=255, name="givenComment")
     */
    private $comment;

    /**
     * @ORM\Column(type="date")
     */
    protected $dateComment;

    /**
     * @ORM\Column(type="integer")
     */
    protected $price_quality;

    /**
     * @ORM\Column(type="integer")
     */
    protected $lieu;

    // changed name of column due to SQL reserved keywords
    // either quote "`field_name`" or give a new name "new_field_name"
    /**
     * @ORM\Column(type="integer", name="precisionToDescription")
     */
    protected $precision;

    /**
     * @ORM\Column(type="integer")
     */
    protected $communication;

    /**
     * @ORM\Column(type="integer")
     */
    protected $cleanliness;

    /**
     * @ORM\ManyToOne(targetEntity="BookingBundle\Entity\Booking")
     */
    private $booking;

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
    public function getComment()
    {
        return $this->comment;
    }

    /**
     * @param mixed $comment
     */
    public function setComment($comment)
    {
        $this->comment = $comment;
    }

    /**
     * @return mixed
     */
    public function getDateComment()
    {
        return $this->dateComment;
    }

    /**
     * @param mixed $dateComment
     */
    public function setDateComment($dateComment)
    {
        $this->dateComment = $dateComment;
    }

    /**
     * @return mixed
     */
    public function getPriceQuality()
    {
        return $this->price_quality;
    }

    /**
     * @param mixed $price_quality
     */
    public function setPriceQuality($price_quality)
    {
        $this->price_quality = $price_quality;
    }

    /**
     * @return mixed
     */
    public function getLieu()
    {
        return $this->lieu;
    }

    /**
     * @param mixed $lieu
     */
    public function setLieu($lieu)
    {
        $this->lieu = $lieu;
    }

    /**
     * @return mixed
     */
    public function getPrecision()
    {
        return $this->precision;
    }

    /**
     * @param mixed $precision
     */
    public function setPrecision($precision)
    {
        $this->precision = $precision;
    }

    /**
     * @return mixed
     */
    public function getCommunication()
    {
        return $this->communication;
    }

    /**
     * @param mixed $communication
     */
    public function setCommunication($communication)
    {
        $this->communication = $communication;
    }

    /**
     * @return mixed
     */
    public function getCleanliness()
    {
        return $this->cleanliness;
    }

    /**
     * @param mixed $cleanliness
     */
    public function setCleanliness($cleanliness)
    {
        $this->cleanliness = $cleanliness;
    }

    /**
     * @return mixed
     */
    public function getBooking()
    {
        return $this->booking;
    }

    /**
     * @param mixed $booking
     */
    public function setBooking($booking)
    {
        $this->booking = $booking;
    }
}
