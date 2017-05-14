/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Message;
import com.hostandguest.entities.User;
import com.hostandguest.services.MessageService;
import com.hostandguest.services.UserService;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marouane
 */
public class UserContactListController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXListView listeContact;
     @FXML
    private ImageView home;
     
    public void showListView(){
        MessageService ms = new MessageService();
        ObservableList<String> listContact = FXCollections.observableArrayList();
        for(User user : ms.getUserList(User.currentUser)){
            listContact.add(user.getUsername()+" ("+ms.countUnreadMessages(User.currentUser, user.getId())+" New messages)");
        }
        listeContact.setItems(listContact);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showListView();
    }    
    
    
    @FXML
    void showHistorique(MouseEvent event) {
        MessageService ms = new MessageService();

        UserService us = new UserService();
        System.out.println("Clicked on : "+ listeContact.getSelectionModel().getSelectedItem());
        String myString=(listeContact.getSelectionModel().getSelectedItem().toString());
        String username=myString.substring(0,myString.indexOf("("));
        Message.currentUserConversation=(us.getUserByUsername(username)).getId();
        ms.isReadTrue(User.currentUser, Message.currentUserConversation);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat.fxml"));
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
