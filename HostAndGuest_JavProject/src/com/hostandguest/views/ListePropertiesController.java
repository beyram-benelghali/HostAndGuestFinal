/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.services.PropertyService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class ListePropertiesController implements Initializable {

    @FXML
    private ListView PropertyList;
    @FXML
    private CheckBox Isbanned;
    @FXML
    private CheckBox Isenabled;
    @FXML
    private Button find;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    //Methodes d'affichage et de ban !
          public void affiche()
    {
        try
        {
            PropertyList.getItems().clear();
            
            ObservableSet<Parent> observableSet = FXCollections.observableSet();
            
            
            for (Property properties : new PropertyService().getAllProperty())
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/PropertyDetailsAdmin.fxml"));
                Parent model = (Parent)fxmlLoader.load();
                PropertyDetailsAdminController model_controller = fxmlLoader.<PropertyDetailsAdminController>getController();
                
                model_controller.lblNbrooms.setText(String.valueOf(properties.getNbRooms()));
                model_controller.lblPrice.setText( String.valueOf(properties.getPrice()));
                model_controller.lblPublicationdate.setText(properties.getPublicationDate().toString());
                model_controller.lblbanned.setText(String.valueOf(properties.isReported()));
                model_controller.lblenabled.setText(String.valueOf(properties.isEnabled()));
                model_controller.Disable.setOnAction((event) -> {
                        new PropertyService().hideProperty(properties);
                        affiche();
                    });
                
                observableSet.add(model);
            }
            PropertyList.setItems(FXCollections.observableArrayList(observableSet));
        }
        catch (IOException ex)
        {
            Logger.getLogger(ListeUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
         

    }
          public void recherche()
    {
        find.setOnAction((ActionEvent event) -> {
            
            if (!Isbanned.isSelected()&& !Isenabled.isSelected() )
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("the proprety must be chosen as banned or enabled !");
                alert.show();
            }
            else if (Isenabled.isSelected())
            {
                String critere = "where enabled=1 ";
                afficherecherche(critere);
 
            }
            else if (Isbanned.isSelected())
            {
                String critere = "where reported=1 ";
                afficherecherche(critere);
                
            }
            else if (Isbanned.isSelected()&& Isenabled.isSelected() )
            {
                String critere = "where enabled=1 && reported=1 ";
                afficherecherche(critere);

            }
        
                
                
        });
    }

    private void afficherecherche(String critere) {
      try
        {
            PropertyList.getItems().clear();
            
            ObservableSet<Parent> observableSet = FXCollections.observableSet();
            
            
            for (Property properties : new PropertyService().getAllPropertyByCritere(critere))
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/PropertyDetailsAdmin.fxml"));
                Parent model = (Parent)fxmlLoader.load();
                PropertyDetailsAdminController model_controller = fxmlLoader.<PropertyDetailsAdminController>getController();
                
                model_controller.lblNbrooms.setText(String.valueOf(properties.getNbRooms()));
                model_controller.lblPrice.setText( String.valueOf(properties.getPrice()));
                model_controller.lblPublicationdate.setText(properties.getPublicationDate().toString());
                model_controller.lblbanned.setText(String.valueOf(properties.isReported()));
                model_controller.lblenabled.setText(String.valueOf(properties.isEnabled()));
                model_controller.Disable.setOnAction((event) -> {
                        new PropertyService().hideProperty(properties);
                        affiche();
                    });
                
                observableSet.add(model);
            }
            PropertyList.setItems(FXCollections.observableArrayList(observableSet));
        }
        catch (IOException ex)
        {
            Logger.getLogger(ListeUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
