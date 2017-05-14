<?php

namespace ExperienceBundle\Form;

use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class AddExperience extends AbstractType
{
    private $propertiesList;

    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $this->propertiesList=$options["propertiesList"];

        $builder
            ->add('name', TextareaType::class)
            ->add('description', TextareaType::class)
            ->add('summary',TextareaType::class)
            ->add('Submit', SubmitType::class);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'propertiesList' => 'property'
        ));
    }

    public function getName()
    {
        return 'experience_bundle_add_experience';
    }
}
