/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Property;
import com.hostandguest.services.PropertyService;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BEYRAM-BG
 */
public class PropertyModelController implements Initializable {

    @FXML
    public Label location;
    @FXML
    public Label price;
    @FXML
    public Label description;
    @FXML
    public ImageView imageProp;
    @FXML
    public JFXButton detailButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            controller.hostProp.setId(""+p.getHost_id());
            controller.anchorDetailId.setId(p.getId()+"");
            controller.prepListImg(p.getImagesPath());
            for(int i = 0 ; i< p.getEquipements().size();i++){
                HBox hb = new HBox();
                hb.setPrefHeight(73);
                hb.setPrefWidth(115);
             //    File file = new File(property.getImagesPath().get(0).toString().replace("../../../", "C:/xampp/htdocs/PHPstormProjects/Host_n_Guest/").replace("/", "//"));
             //   model_controller.imageProp.setImage(new Image(file.toURI().toURL().toString()));
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

    
    
    
}
