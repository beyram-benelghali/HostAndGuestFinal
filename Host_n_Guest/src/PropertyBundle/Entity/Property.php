<?php

namespace PropertyBundle\Entity;

use DateTime;
use Doctrine\ORM\Mapping as ORM;
use Eko\FeedBundle\Item\Writer\ItemInterface;
use Symfony\Component\Validator\Constraints\Date;

/**
 * @ORM\Entity
 */
class Property implements ItemInterface
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
    protected $nbRooms;

    /**
     * @ORM\Column(type="integer")
     */
    protected $price;

    /**
     * @ORM\Column(type="date")
     */
    protected $publicationDate;

    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $description;

    /**
     * @ORM\Column( type="array" )
     */
    protected $equipements;

    /**
     * @ORM\Column( type="array" )
     */
    protected $imagesPath;

    /**
     * @return mixed
     */
    public function getImagesPath()
    {
        return $this->imagesPath;
    }

    /**
     * @param mixed $imagesPath
     */
    public function setImagesPath($imagesPath)
    {
        $this->imagesPath = $imagesPath;
    }


    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $location;

    /**
     * @ORM\Column(type="boolean")
     */
    protected $reported = false;

    /**
     * @ORM\Column(type="boolean")
     */
    protected $enabled = true;

    /**
     * @ORM\ManyToOne(targetEntity="UserBundle\Entity\User")
     */
    protected $host;

    /**
     * Property constructor.
     */
    public function __construct()
    {
        $this->publicationDate = new \DateTime;
    }

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
    public function getNbRooms()
    {
        return $this->nbRooms;
    }

    /**
     * @param mixed $nbRooms
     */
    public function setNbRooms($nbRooms)
    {
        $this->nbRooms = $nbRooms;
    }

    /**
     * @return mixed
     */
    public function getPrice()
    {
        return $this->price;
    }

    /**
     * @param mixed $price
     */
    public function setPrice($price)
    {
        $this->price = $price;
    }

    /**
     * @return mixed
     */
    public function getPublicationDate()
    {
        return $this->publicationDate;
    }

    /**
     * @param mixed $publicationDate
     */
    public function setPublicationDate($publicationDate)
    {
        $this->publicationDate = $publicationDate;
    }

    /**
     * @return mixed
     */
    public function getDescription()
    {
        return $this->description;
    }

    /**
     * @param mixed $description
     */
    public function setDescription($description)
    {
        $this->description = $description;
    }

    /**
     * @return mixed
     */
    public function getEquipements()
    {
        return $this->equipements;
    }

    /**
     * @param mixed $equipements
     */
    public function setEquipements($equipements)
    {
        $this->equipements = $equipements;
    }

    /**
     * @return mixed
     */
    public function getReported()
    {
        return $this->reported;
    }

    /**
     * @param mixed $reported
     */
    public function setReported($reported)
    {
        $this->reported = $reported;
    }

    /**
     * @return mixed
     */
    public function getEnabled()
    {
        return $this->enabled;
    }

    /**
     * @param mixed $enabled
     */
    public function setEnabled($enabled)
    {
        $this->enabled = $enabled;
    }

    /**
     * @return mixed
     */
    public function getHost()
    {
        return $this->host;
    }

    /**
     * @param mixed $host
     */
    public function setHost($host)
    {
        $this->host = $host;
    }

    /**
     * @return mixed
     */
    public function getLocation()
    {
        return $this->location;
    }

    /**
     * @param mixed $location
     */
    public function setLocation($location)
    {
        $this->location = $location;
    }


    /**
     * This method returns feed item title.
     *
     *
     * @return string
     */
    public function getFeedItemTitle()
    {
        return "Property N° ".$this->getId()." Located in ".$this->getLocation();
    }

    /**
     * This method returns feed item description (or content).
     *
     *
     * @return string
     */
    public function getFeedItemDescription()
    {
        return "<b>Description: </b>".$this->getDescription()."<br><b>Price:</b>  "
                .$this->getPrice()." TND<br> <b>Equipement: </b>".implode(" - ", $this->getEquipements());

            ;
    }

    /**
     * This method returns feed item URL link.
     *
     *
     * @return string
     */
    public function getFeedItemLink()
    {
       return "http://localhost/PHPstormProjects/Host_n_Guest/web/app_dev.php/property/detailProperty/".$this->getId();
    }

    /**
     * This method returns item publication date.
     *
     *
     * @return \DateTime
     */
    public function getFeedItemPubDate()
    {
        return $this->getPublicationDate();
    }
}
