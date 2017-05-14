/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.User;
import com.hostandguest.entities.Message;
import com.hostandguest.services.MessageService;
import com.hostandguest.services.UserService;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marouane
 */
public class SendMailController implements Initializable {

    @FXML
    private JFXTextField subject;
    @FXML
    private JFXTextArea mailContent;
    @FXML
    private Button send;
    @FXML
    private JFXTextField cc;
    
    @FXML
    private Label userName;
        @FXML
    private Label emailSent;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserService us = new UserService();
        userName.setText((us.getUserById(Message.currentUserMail)).getUsername());
    }    

    @FXML
    private void onMailSend(ActionEvent event) {
        MessageService ms = new MessageService();
        UserService us = new UserService();
        String sender=(us.getUserById(User.currentUser)).getUsername();
        String destinataire =(us.getUserById(Message.currentUserMail)).getEmail();
        String content = sender + " Vous a dit : "+mailContent.getText(); 
        if(ms.sendMail(subject.getText(), content, destinataire, cc.getText()) == 1){
            emailSent.setText("Email sent Succesfully ! ");
        }
        else{
             emailSent.setText("Failed to send email ! ");
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
