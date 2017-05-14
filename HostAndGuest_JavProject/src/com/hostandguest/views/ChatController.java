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
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Marouane
 */
public class ChatController implements Initializable {

    @FXML
    private JFXTextArea zoneMessage;
    @FXML
    private Button bouttonEnvoyer;
    @FXML
    private JFXListView historique;
    @FXML
    private Label title;
    @FXML
    private Label userChat;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                UserService us = new UserService();
        userChat.setText(us.getUserById(Message.currentUserConversation).getUsername());
        loadMessages();
        historique.scrollTo(historique.getItems().size());

        Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(2500),
        ae -> loadMessages()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }  
    
    @FXML
    void onSendMessage(ActionEvent event) {
        MessageService ms = new MessageService();
        Message m = new Message();
        m.setBody(zoneMessage.getText());
        m.setSender_id(User.currentUser);
        ms.addMessage(m, Message.currentUserConversation);
        loadMessages();
        zoneMessage.setText("");
    }
        @FXML
    private Button back;

    @FXML
    void onBack(ActionEvent event) {
        MessageService ms = new MessageService();
        ms.isReadTrue(User.currentUser, Message.currentUserConversation);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userContactList.fxml"));
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
    
    public void loadMessages(){
        MessageService ms = new MessageService();
        UserService us = new UserService();
        ArrayList<Message> messgaeList = ms.getAllMessage(User.currentUser, Message.currentUserConversation);
        if(messgaeList != null){
        ObservableList<String> histo = FXCollections.observableArrayList();
        String mess ="";
        for (Message message : messgaeList) {
            if(message.getSender_id() == User.currentUser){
                mess="Me : "+message.getBody();
            }
            else{
                mess=(us.getUserById(Message.currentUserConversation)).getUsername()+" : "+message.getBody();
            }
            histo.add(mess);
        }
        historique.setItems(histo);

                }
    }
}
