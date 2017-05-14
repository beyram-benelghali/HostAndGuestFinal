/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.services.PropertyService;
import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BEYRAM-BG
 */
public class MyListPropertyController implements Initializable {

    @FXML
    public JFXListView listProperty;
    protected User current_user = new User();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        current_user.setId(User.currentUser);
        refresh();
    }    
    
    public void refresh(){
        listProperty.getItems().clear();
        ObservableList<Parent> observableProp = FXCollections.observableArrayList();
        for (Property property : new PropertyService().getUserProperty(current_user))
            {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyPropertyModel.fxml"));
                Parent model = (Parent)fxmlLoader.load();
                MyPropertyModelController model_controller = fxmlLoader.<MyPropertyModelController>getController();
                model_controller.description.setText("Description : " + property.getDescription());
                model_controller.price.setText("Price : " + property.getPrice() + " TND");
                model_controller.location.setText("Location : " + property.getLocation().replace("/", "//"));
                model_controller.detailButton.setId(property.getId()+"");
                model_controller.updateButton.setId(property.getId()+"");
                model_controller.deleteButton.setId(property.getId()+"");
                File file = new File(property.getImagesPath().get(0).toString().replace("../../../", "C:/xampp/htdocs/PHPstormProjects/Host_n_Guest/").replace("/", "//"));
                model_controller.imageProp.setImage(new Image(file.toURI().toURL().toString()));
                observableProp.add(model);
            } catch (IOException ex) {
                Logger.getLogger(ListPropertyController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
        listProperty.setItems(FXCollections.observableArrayList(observableProp));
    }
    
    @FXML
    void goHome(MouseEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeProperty.fxml"));
        Parent model;
        try {
            model = (Parent)fxmlLoader.load();
            Stage stage = (Stage) ((Node)event.getTarget()).getScene().getWindow();
            Scene scene = new Scene(model);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomePropertyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
