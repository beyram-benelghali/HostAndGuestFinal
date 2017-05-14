/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.hostandguest.entities.Review;
import com.hostandguest.entities.User;
import com.hostandguest.util.currentInstance;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 */
public class FXML_ReviewAddController implements Initializable {
    
    @FXML
    private TextArea txtAreaComment;
    
    @FXML
    private JFXSlider sliderPQ;
    
    @FXML
    private JFXSlider sliderPos;
    
    @FXML
    private JFXSlider sliderPre;
    
    @FXML
    private JFXSlider sliderCom;
    
    @FXML
    private JFXSlider sliderClean;
    
    @FXML
    private JFXComboBox cbBookingDate;
    
    @FXML
    private JFXButton btnSubmit;
    
    @FXML
    private JFXButton btnChooseVideoFile;
    
    protected int property_id;
    
    private User current_user = new UserService().getUserById(User.currentUser);
    // used to refresh list after closing this window if review is added successfully
    protected FXML_ReviewMainController callingInstance;
    
    private File chosenVideoFileToUpload = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnChooseVideoFile.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Video File To Upload");
            
            // Setting extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP4 Files (*.mp4)", "*.mp4");
            
            fileChooser.getExtensionFilters().add(extFilter);
            
            // Show open file dialog
            chosenVideoFileToUpload = fileChooser.showOpenDialog(btnChooseVideoFile.getScene().getWindow());
            
            // retrieving file extension to verify it 
            String extension = "";

            int extensionBeginIndex = chosenVideoFileToUpload.getName().lastIndexOf('.');
            
            if (extensionBeginIndex > 0) {
                extension = chosenVideoFileToUpload.getName().substring(extensionBeginIndex + 1);
            }
            
            if (!chosenVideoFileToUpload.exists() || ((chosenVideoFileToUpload.length() / (1024 * 1024)) >= 35)
                    || !extension.equals("mp4"))
            {   
                Alert alert = new Alert(Alert.AlertType.ERROR);
                
                alert.setTitle("File Error");
                
                if (!extension.equals("mp4"))
                    alert.setHeaderText("Extension Must Be MP4");
                else if (chosenVideoFileToUpload.exists())
                    alert.setHeaderText("Maximum File Size is 35MB");
                else
                    alert.setHeaderText("You Must Choose A File");
                
                alert.show();
                
                chosenVideoFileToUpload = null;
            }
            else
            {
                btnChooseVideoFile.setText(chosenVideoFileToUpload.getName());
            }
        });
        
        btnSubmit.setOnAction((event) -> {
            if (ValidateForm())
            {
                Review review = new Review();

                review.setComment(txtAreaComment.getText());
                review.setPrice_quality((int) sliderPQ.getValue());
                review.setLieu((int) sliderPos.getValue());
                review.setPrecision((int) sliderPre.getValue());
                review.setCommunication((int) sliderCom.getValue());
                review.setCleanliness((int) sliderClean.getValue());

                new ReviewService().getPropertyConcernedReservationList(property_id, current_user.getId()).forEach((bookingItem) -> {
                    // if seleected date and found date are equal set booking
                    if (bookingItem.getBookingDate().equals(cbBookingDate.getSelectionModel().getSelectedItem()))
                        review.setBooking(bookingItem);
                });

                review.setDateComment(new Date());
                
                if (new ReviewService().addReview(review, chosenVideoFileToUpload))
                {    
                    // getting scene and closing window
                    btnSubmit.getScene().getWindow().hide();
                    
                    callingInstance.refresh("");
                }
                else
                {
                    Alert alert = new Alert(AlertType.ERROR);
                    
                    alert.setTitle("Error");
                    alert.setHeaderText("An Error Occured while adding the review");

                    alert.show();
                }
            }
        });
    }
    
    protected void isAllowedToReview()
    {
        if (new ReviewService().getPropertyConcernedReservationList(property_id, current_user.getId()).isEmpty())
        {
            btnSubmit.getScene().getWindow().hide();
            
            Alert alert = new Alert(AlertType.ERROR);
            
            alert.setTitle("Leaving Review Error");
            alert.setHeaderText("No Bookings Left without review for you to be allowed to review");
            
            alert.show();
        }
    }
    
    private boolean ValidateForm()
    {
        boolean isValid = true;
        
        // must declare a validator per field
        final ContextMenu txtAreaValidator = new ContextMenu();
        txtAreaValidator.setAutoHide(false);
        
        if (txtAreaComment.getText().isEmpty())
        {
            txtAreaValidator.getItems().clear();
                    txtAreaValidator.getItems().add(new MenuItem("Please provide your feedback"));
                    txtAreaValidator.show(txtAreaComment, Side.RIGHT, 10, 0);
                    isValid = false;
        }
        
        // used for clearing valdiator once focused
        // declared here because the user might immediately give a proper comment value making this a waste time (execution)
        txtAreaComment.focusedProperty().addListener(
                new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> arg0, 
                            Boolean oldPropertyValue, Boolean newPropertyValue) {
                        if (newPropertyValue) {
                            txtAreaValidator.hide();
                        }
                    }
                });
         
        return isValid;
    }
    
    protected void setCbValues()
    {
        new ReviewService().getPropertyConcernedReservationList(property_id, current_user.getId()).forEach((booking) -> {
            cbBookingDate.getItems().add(booking.getBookingDate());
        });
        
        cbBookingDate.getSelectionModel().selectFirst();
    }
}
