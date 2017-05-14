/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Property;
import com.hostandguest.services.PropertyService;
import com.jfoenix.controls.JFXButton;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.swing.GroupLayout;

/**
 * FXML Controller class
 *
 * @author BEYRAM-BG
 */
public class MyPropertyModelController implements Initializable {
    @FXML
    public Label hostProp;
    @FXML
    public ImageView imageProp;
    @FXML
    public Label location;
    @FXML
    public Label description;
    @FXML
    public Label price;
    @FXML
    public JFXButton detailButton;
    @FXML
    public JFXButton updateButton;
    @FXML
    public JFXButton deleteButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }  
    
    @FXML
    void deleteButtonClick(ActionEvent event) {
        PropertyService ps = new PropertyService();
        Property p = new Property();
        p.setId(Integer.parseInt(((JFXButton)event.getSource()).getId()));
        ps.removeProperty(p);
        System.out.println("Property "+ ((JFXButton)event.getSource()).getId() + " Deleted !! ");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyListProperty.fxml"));
        try {
            Parent model = (Parent)fxmlLoader.load();
            Stage stage = (Stage) ((Node)event.getTarget()).getScene().getWindow();
            Scene scene = new Scene(model);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MyPropertyModelController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    @FXML
    void detailButtonClick(ActionEvent event) {
        System.out.println("Detail Action");
        PropertyService ps = new PropertyService();
        Property p = ps.getPropertyById(Integer.parseInt(((JFXButton)event.getSource()).getId()));
        System.out.println(p);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PropertyDetails.fxml"));
        try {
            Parent model = (Parent)fxmlLoader.load();
            PropertyDetailsController controller = fxmlLoader.getController();
            //controller.propDescrip.setText(p.getDescription());
            Text txt = new Text(p.getDescription());
            txt.wrappingWidthProperty().bind(controller.scrollDescrip.widthProperty());
            txt.setStyle("-fx-highlight-text-fill: #c4d8de;-fx-font-weight: bold");
            controller.scrollDescrip.setContent(txt);
            controller.propPrice.setText(p.getPrice()+ " TND");
            controller.propLocation.setText(p.getLocation());
            controller.propRoomNb.setText(p.getNbRooms()+"");
            controller.propPubDate.setText(p.getPublicationDate().toString());
            controller.hostProp.setText(controller.hostProp.getText() + " " + p.getHost_id());
            controller.hostProp.setId(" " + p.getHost_id());
            //controller..setId(" " + p.getHost_id());
            controller.anchorDetailId.setId(p.getPrice()+"");
            controller.prepListImg(p.getImagesPath());
            controller.bookicon.setVisible(false);
            controller.booktxt.setVisible(false);
            controller.reporticon.setVisible(false);
            controller.reporttxt.setVisible(false);
            controller.hostProp.setVisible(false);
            controller.hosticon.setVisible(false);
            //controller.lblAccessReviews.setVisible(false);
            controller.lblGiftList.setVisible(false);
            controller.giftImg.setVisible(false);
            controller.mailImg.setVisible(false);controller.mailtxt.setVisible(false);
            for(int i = 0 ; i< p.getEquipements().size();i++){
                HBox hb = new HBox();
                hb.setPrefHeight(73);
                hb.setPrefWidth(115);
                ImageView img = new ImageView(new File("src\\resources\\badge.png").toURI().toURL().toString());
                img.setFitHeight(36);
                img.setFitWidth(35);
                Label lab = new Label(p.getEquipements().get(i).toString());
                lab.setPadding(new Insets(5, 0, 0, 0)); 
                lab.setStyle("-fx-font-weight: bold;-fx-font-size: 15px;");
                hb.getChildren().add(img);
                hb.getChildren().add(lab);
               controller.TypeEquip.getChildren().add(hb);
            }
            
            Stage stage = (Stage) ((Node)event.getTarget()).getScene().getWindow();
            Scene scene = new Scene(model);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MyPropertyModelController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    @FXML
    void updateButtonClick(ActionEvent event) {
        System.out.println("Update Action");
        PropertyService ps = new PropertyService();
        Property p = ps.getPropertyById(Integer.parseInt(((JFXButton)event.getSource()).getId()));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UpdateProperty.fxml"));
        try {
            Parent model = (Parent)fxmlLoader.load();
            UpdatePropertyController controller = fxmlLoader.getController();
            controller.RoomNb.setText(p.getNbRooms()+"");
            controller.Price.setText(p.getPrice()+"");
            controller.Location.setText(p.getLocation());
            controller.Description.setText(p.getDescription()+"");
            controller.AnchorUpdateId.setId(p.getId()+"");
            for (int i=0; i<p.getEquipements().size();i++){
                if(p.getEquipements().get(i).toString().equals("WIFI")){
                    controller.WifiCheck.setSelected(true);
                }
                if(p.getEquipements().get(i).toString().equals("TV")){
                    controller.tvCheck.setSelected(true);
                }
                if(p.getEquipements().get(i).toString().equals("Kitchen")){
                    controller.KitchenCheck.setSelected(true);
                }                
            }
            Stage stage = (Stage) ((Node)event.getTarget()).getScene().getWindow();
            Scene scene = new Scene(model);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MyPropertyModelController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
}
