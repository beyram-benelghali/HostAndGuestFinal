<?php

namespace ExperienceBundle\Repository;


use Doctrine\ORM\EntityRepository;
use UserBundle\Entity\User;
use BookingBundle\Entity\Booking;
use PropertyBundle\Entity\Property;


class ExperienceRepository extends EntityRepository
{
    public function getHostProperties($hostid)
    {
        return $this->getEntityManager()->createQuery(
            "
            select p
            from PropertyBundle:Property p
            WHERE p.host = :hid
            "
        )->setParameter('hid',$hostid)
            ->getResult();
    }
}

