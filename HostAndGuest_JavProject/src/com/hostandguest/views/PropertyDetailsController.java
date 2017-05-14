/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Message;
import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.services.FXML_BookingAddController;
import com.hostandguest.services.FXML_BookingListController;
import com.hostandguest.services.FXML_ReviewMainController;
import com.hostandguest.services.PropertyService;
import com.hostandguest.services.UserService;
import com.hostandguest.util.currentInstance;
import com.jfoenix.controls.JFXScrollPane;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BEYRAM-BG
 */
public class PropertyDetailsController implements Initializable {
    @FXML
    public Label propLocation;
    @FXML
    public Label propRoomNb;
    @FXML
    public Label propPubDate;
    @FXML
    public Label propId;
    @FXML
    public Label propPrice;
    @FXML
    public ScrollPane scrollDescrip;
    @FXML
    public HBox TypeEquip;
    @FXML
    public ListView<ImageView> imgListV;
    @FXML
    public Label hostProp;
    @FXML
    public AnchorPane anchorDetailId;
    @FXML
    public ImageView reporticon;
    @FXML
    public Label reporttxt;
    @FXML
    public ImageView bookicon;
    @FXML
    public Label booktxt;
    @FXML
    public ImageView hosticon;
    @FXML
    public Label lblAccessReviews;
    @FXML
    public Label lblGiftList;
<<<<<<< HEAD
    @FXML
    public ImageView giftImg;
    @FXML
    public ImageView mailImg;
    @FXML
    public Label mailtxt;
=======
    
>>>>>>> origin/master
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblAccessReviews.setOnMouseClicked((event) -> {
            goToReviews();
        });
        lblGiftList.setOnMouseClicked((event) -> {
            loadGiftList();
        });
    }
    
    public void prepListImg(List lst){
        ObservableList<ImageView> observableImg = FXCollections.observableArrayList();
        for(int i = 0 ; i<lst.size();i++){
            String linkImg = lst.get(i).toString().replace("../../../", "C:/xampp/htdocs/PHPstormProjects/Host_n_Guest/").replace("/", "//");
            System.out.println(linkImg);
            File file = new File(linkImg);
            try {
                ImageView imageView = new ImageView();
                imageView.setFitHeight(165);
                imageView.setFitWidth(475);
                imageView.setImage(new Image(file.toURI().toURL().toString()));
                observableImg.add(imageView);
            } catch (MalformedURLException ex) {
                Logger.getLogger(PropertyDetailsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        imgListV.setItems(FXCollections.observableArrayList(observableImg));
        
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
    void BookNow(MouseEvent event) {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_BookingAdd.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            FXML_BookingAddController add_controller = fxmlLoader.<FXML_BookingAddController>getController();
            
            add_controller.relatedProperty = new PropertyService().getPropertyById(Integer.parseInt(anchorDetailId.getId()));
            
            if (currentInstance.getBookingAddStage()== null)
                currentInstance.setBookingAddStage(new Stage());
            
            Scene scene = new Scene(root);
            Stage stage = currentInstance.getBookingAddStage();
            stage.setScene(scene);
            stage.setTitle("Book For A While");
            stage.show();
        }
        catch (IOException ex)
        {
            Logger.getLogger(PropertyDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void ReportProp(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure to report this property ?");
        alert.setContentText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
           PropertyService ps = new PropertyService();
           Property prop = new Property(); prop.setId(Integer.parseInt(anchorDetailId.getId()));
           ps.reportProperty(prop);
        } 
    }
    
   /* @FXML
    void ContactHost(MouseEvent event) {
        System.out.println("Hello Coach ! ");
    }
    */
    /**
     * Initializes the controller class.
     */
        @FXML
    void ContactHost(MouseEvent event) {
         System.out.println(Integer.parseInt(((hostProp.getId()))));
            Message.currentUserConversation= Integer.parseInt(((hostProp.getId())));
        //  System.out.println(Integer.parseInt(((hostProp.getId()))));
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
    void onSendMail(MouseEvent event) {
                    //System.out.println(Integer.parseInt(((hostProp.getText()).substring(15))));
                    System.out.println("mail"+hostProp.getId());
        Message.currentUserMail=Integer.parseInt(((hostProp.getId())));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sendMail.fxml"));
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
    
    private void goToReviews() {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_ReviewMain.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            FXML_ReviewMainController mainReviewController = fxmlLoader.<FXML_ReviewMainController>getController();

            mainReviewController.property_id = Integer.parseInt(anchorDetailId.getId());
            mainReviewController.refresh("");
            
            Scene scene = new Scene(root);
            
            if (currentInstance.getReviewStage() == null)
                currentInstance.setReviewStage(new Stage());
            
            Stage stage = currentInstance.getReviewStage();
            stage.setScene(scene);
            stage.setTitle("Review List");
            stage.show();
        }
        catch (IOException ex)
        {
            Logger.getLogger(PropertyDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadGiftList() {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_BookingList.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            FXML_BookingListController list_controller = fxmlLoader.<FXML_BookingListController>getController();
            
            list_controller.showGiftList = true;
            list_controller.property_id = Integer.parseInt(anchorDetailId.getId());
            
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
