<?php

namespace BookingBundle\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class AjouterRservation extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('term', IntegerType::class, array(
                'attr' => array(
                    'min' => 1
                )
            ))
            ->add('bookingDate',DateType::class,array(
                'widget' => 'single_text'
            ))
            ->add('nbReservedRooms', IntegerType::class, array(
                'label' => 'Rooms To Book',
                'attr' => array(
                    'min' => 1
                )
            ))
            ->add('card_number', IntegerType::class, array(
                'mapped' => false
            ))
            ->add('Reserver',SubmitType::class);
    }

    public function configureOptions(OptionsResolver $resolver)
    {

    }

    public function getName()
    {
        return 'booking_bundle_ajouter_rservation';
    }
}
