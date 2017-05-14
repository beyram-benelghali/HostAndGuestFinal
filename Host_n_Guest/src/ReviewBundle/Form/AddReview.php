<?php

namespace ReviewBundle\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\RangeType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use BookingBundle\Entity\Booking;

class AddReview extends AbstractType
{
    private $reservation_list;

    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $this->reservation_list = $options['reservation_list'];

        $builder
            ->add('comment', TextareaType::class, array(
                'attr' => array(
                    'style' => 'width : 100%',

            )))
            ->add('price_quality', RangeType::class, array(
                'attr' => array(
                    'min' => 0,
                    'max' => 5,
                    'step' => 1,
                    'class' => 'range-slider__range'
                )
            ))
            ->add('lieu', RangeType::class, array(
                'attr' => array(
                    'min' => 0,
                    'max' => 5,
                    'step' => 1,
                    'class' => 'range-slider__range'
                )
            ))
            ->add('precision', RangeType::class, array(
                'attr' => array(
                    'min' => 0,
                    'max' => 5,
                    'step' => 1,
                    'class' => 'range-slider__range'
                )
            ))
            ->add('communication', RangeType::class, array(
                'attr' => array(
                    'min' => 0,
                    'max' => 5,
                    'step' => 1,
                    'class' => 'range-slider__range'
                )
            ))
            ->add('cleanliness', RangeType::class, array(
                'attr' => array(
                    'min' => 0,
                    'max' => 5,
                    'step' => 1,
                    'class' => 'range-slider__range'
                )
            ))
            ->add('booking', ChoiceType::class, array(
                    'choices'  => $this->reservation_list,
                    'label' => 'Booking Date',
                    'choice_label' => function ($value) {
                        return strtoupper($value->getBookingDate()->format('Y-m-d'));
                    },
                ))
            ->add('Submit', SubmitType::class)
            ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'reservation_list' => "Booking"
        ));
    }

    public function getName()
    {
        return 'review_bundle_add_review';
    }
}
