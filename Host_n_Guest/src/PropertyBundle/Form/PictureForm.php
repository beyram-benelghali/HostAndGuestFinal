<?php
/**
 * Created by PhpStorm.
 * User: BEYRAM-BG
 * Date: 06/02/2017
 * Time: 19:23
 */

namespace PropertyBundle\Form;


use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class PictureForm extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('uploaded_image',FileType::class,array())
            ->add('Upload',SubmitType::class);
        ;
    }


    /**
     * {@inheritdoc}
     */
    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'data_class' => 'PropertyBundle\Entity\property_picture'
        ));
    }

    public function getName()
    {
        return 'propertybundle_PictureForm';
    }

}