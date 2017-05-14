/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Message;
import com.hostandguest.entities.User;
import com.hostandguest.services.FXML_BookingAddController;
import com.hostandguest.services.FXML_BookingListController;
import com.hostandguest.services.UserService;
import com.hostandguest.util.MainAppGoogleMap;
import com.hostandguest.util.currentInstance;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BEYRAM-BG
 */
public class HomePropertyController implements Initializable {

    @FXML
    private AnchorPane AllPropAnchor;
    @FXML
    private AnchorPane MyPropAnchor;
    @FXML
    private JFXButton addPropButton;
    @FXML
    private AnchorPane GMapAnchor;
    @FXML
    private Label userLab;
    @FXML
    private ImageView loadImg;
    @FXML
    private Label userName;
    @FXML
    private Label lblUserBkns;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserService us = new UserService();     
        userName.setText(us.getUserById(User.currentUser).getUsername());
        
        lblUserBkns.setOnMouseClicked((event) -> {
            loadUserBookings();
        });
    }    
    
    @FXML
    void goAddProp(ActionEvent event) {
        System.out.println("Gooo");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProperty.fxml"));
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
    void goGmap(MouseEvent event) {
        System.out.println("Hello Gmap !");
        MainAppGoogleMap gmap = new MainAppGoogleMap();
        try {
           gmap.start(new Stage());
        } catch (Exception ex) {
            Logger.getLogger(HomePropertyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goListProp(MouseEvent event) {
        loadImg.setVisible(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ListProperty.fxml"));
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
                });
            }
        }, 2000);
        
    }

    @FXML
    void goMyListProp(MouseEvent event) {
        loadImg.setVisible(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyListProperty.fxml"));
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
                });
            }
        }, 2000);
        
    }
        @FXML
    void onOpenChat(MouseEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserContactList.fxml"));
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
    private ImageView chat;

    private void loadUserBookings() {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_BookingList.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            FXML_BookingListController list_controller = fxmlLoader.<FXML_BookingListController>getController();
            
            // user is only set after ui is loaded
            list_controller.refresh();
            
            if (currentInstance.getBookingListStage() == null)
                currentInstance.setBookingListStage(new Stage());
            
            // only open new window if not an update
            Stage stage = currentInstance.getBookingListStage();
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.setTitle("Property Gift List");
            stage.show();
        }
        catch (IOException ex)
        {
            Logger.getLogger(FXML_BookingAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
