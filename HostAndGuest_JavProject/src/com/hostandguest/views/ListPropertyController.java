/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.services.PropertyService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BEYRAM-BG
 */

public class ListPropertyController implements Initializable {

    @FXML
    public JFXListView listProperty;
    @FXML
    private JFXTextField locationtxt;
    @FXML
    private JFXButton searchBut;
    @FXML
    private JFXButton refreshBut;
    @FXML
    private JFXComboBox sortCombo;
    
    protected User current_user = new User();
    private ArrayList<Property>  listProp;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        current_user.setId(2);
        prepComboBox();
        listProp = new PropertyService().getAllProperty();
        refresh(listProp);
    }    
    
    protected void refresh(ArrayList<Property> listProp){
        listProperty.getItems().clear();
        ObservableList<Parent> observableProp = FXCollections.observableArrayList();
        for (Property property : listProp)
            {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PropertyModel.fxml"));
                Parent model = (Parent)fxmlLoader.load();
                PropertyModelController model_controller = fxmlLoader.<PropertyModelController>getController();
                model_controller.description.setText("Description : " + property.getDescription());
                model_controller.price.setText("Price : " + property.getPrice() + " TND");
                model_controller.location.setText("Location : " + property.getLocation().replace("/", "//"));
                model_controller.detailButton.setId(property.getId()+"");
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
    void refreshList(ActionEvent event) {
        refresh(listProp);
    }
    
    @FXML
    void searchWithLocation(ActionEvent event) {
        System.out.println(locationtxt.getText());
        if(locationtxt.getText().isEmpty()){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Search Property Error");
        alert.setContentText("Please fill the location field !");
        alert.setHeaderText(null);
        alert.showAndWait();
        } else {
            listProperty.getItems().clear();
        ObservableList<Parent> observableProp = FXCollections.observableArrayList();
        for (Property property : new PropertyService().searchProperty(locationtxt.getText()))
            {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PropertyModel.fxml"));
                Parent model = (Parent)fxmlLoader.load();
                PropertyModelController model_controller = fxmlLoader.<PropertyModelController>getController();
                model_controller.description.setText("Description : " + property.getDescription());
                model_controller.price.setText("Price : " + property.getPrice() + " TND");
                model_controller.location.setText("Location : " + property.getLocation().replace("/", "//"));
                model_controller.detailButton.setId(property.getId()+"");
                File file = new File(property.getImagesPath().get(0).toString().replace("../../../", "C:/xampp/htdocs/PHPstormProjects/Host_n_Guest/").replace("/", "//"));
                model_controller.imageProp.setImage(new Image(file.toURI().toURL().toString()));
                observableProp.add(model);
            } catch (IOException ex) {
                Logger.getLogger(ListPropertyController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
        listProperty.setItems(FXCollections.observableArrayList(observableProp));
          //  PropertyService ps = new PropertyService();
          //  ArrayList<Property> res = ps.searchProperty(locationtxt.getText());
            
        }
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
   
    @FXML
    void shortWith(ActionEvent event) {
        String checked = ((Label)((JFXComboBox) event.getSource()).getSelectionModel().getSelectedItem()).getText();
        System.out.println(checked);
        switch(checked){
            case "Price" : 
                listProp = sortWithPrice();
                refresh(listProp);
                break;
            case "Location" :
                listProp = sortWithLocation();
                refresh(listProp);
                break;
        }
    }
    
    public ArrayList<Property> sortWithLocation(){
       ArrayList<Property> listSorted = new ArrayList<>();
       listSorted = this.listProp ;
       listSorted.sort((o1, o2) -> o1.getLocation().compareTo(o2.getLocation()));
        return listSorted;
    }
    
    public ArrayList<Property> sortWithPrice(){
       ArrayList<Property> listSorted = new ArrayList<>();
       listSorted = this.listProp ;
        listSorted.sort((o1, o2) -> {
        return new Integer(o1.getPrice()).compareTo(new Integer(o2.getPrice()));
        });
        return listSorted;
    }
    
    public void prepComboBox(){
        sortCombo.getItems().add(new Label("Price"));
        sortCombo.getItems().add(new Label("Location"));
        sortCombo.setEditable(false);
        sortCombo.setPromptText("Sort With");
    }
}
