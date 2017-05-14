<?php

namespace PropertyBundle\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class PropertyType extends AbstractType
{
    /**
     * {@inheritdoc}
     */
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('nbRooms')
            ->add('price')
            ->add('description',TextareaType::class,array())
            ->add('location')
            ->add('imagesPath',FileType::class,array('multiple' => true))
            ->add('equipements', ChoiceType::class, array(
                "multiple" => true,
                "expanded" => true,
                'choices'  => array(
                    'WIFI' => 'WIFI',
                    'TV' => 'TV',
                    'Kitchen' => 'Kitchen',
                ),
            ))
            ->add('Save',SubmitType::class);
    }
    
    /**
     * {@inheritdoc}
     */
    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'data_class' => 'PropertyBundle\Entity\Property'
        ));
    }

    /**
     * {@inheritdoc}
     */
    public function getBlockPrefix()
    {
        return 'propertybundle_property';
    }


}
