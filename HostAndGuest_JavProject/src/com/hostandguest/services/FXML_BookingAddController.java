/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.hostandguest.entities.Booking;
import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.util.currentInstance;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Toshiba
 */
public class FXML_BookingAddController implements Initializable {
    /**
     * Initializes the controller class.
     */
    
    @FXML
    protected JFXButton btnsubmit;
    @FXML
    protected JFXTextField txtterme;
    @FXML
    protected JFXTextField txtnbrchambre;
    @FXML
    protected JFXTextField txtbancaire;
    @FXML
    protected DatePicker datepk;
    @FXML
    protected JFXButton btnGift;
    
    public Property relatedProperty = new Property();
    public User currentUser = new UserService().getUserById(User.currentUser);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setNumberOnlyField(txtterme);
        setNumberOnlyField(txtnbrchambre);
        setNumberOnlyField(txtbancaire);
        
        datepk.setEditable(false);
        
        btnGift.setOnAction((event) -> {
            addBooking(true, null);
        });
        
        btnsubmit.setOnAction ((event) -> {
            addBooking(false, null);
        });
    }

    // booking id used when updating
    protected void addBooking(boolean isGift, Integer booking_id)
    {
        if (validateForm(booking_id))
        {
            btnsubmit.setDisable(true);
            btnGift.setDisable(true);
                
            Booking booking = new Booking();
            
            if (booking_id != null)
                booking.setId(booking_id);
            
            booking.setBookingDate(Date.valueOf(datepk.getValue()));
            booking.setTotal_amount(relatedProperty.getPrice() * Integer.valueOf(txtterme.getText()));
            booking.setTerm(Integer.valueOf(txtterme.getText()));
            booking.setNbr_rooms_reserved(Integer.valueOf(txtnbrchambre.getText()));
            booking.setGuest(currentUser);
            booking.setProperty(relatedProperty);
                
            int didAddUpdate;
            
            if (booking_id == null)
                didAddUpdate = new BookingService().add(booking, isGift);
            else
                didAddUpdate = new BookingService().UpdateBooking(booking);
                
            switch (didAddUpdate) {
                case 0:
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Reservation Successful");
                    alert.setHeaderText("Reservation Added Successfully");
                    alert.showAndWait();
                    
                    // redirect to user reservation list
                    try
                        {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_BookingList.fxml"));
                            Parent root = (Parent)fxmlLoader.load();
                            FXML_BookingListController list_controller = fxmlLoader.<FXML_BookingListController>getController();
                            
                            list_controller.current_user = currentUser;
                            // user is only set after ui is loaded
                            list_controller.refresh();
                            
                            if (currentInstance.getBookingListStage() == null)
                                currentInstance.setBookingListStage(new Stage());
                            
                            // only open new window if not an update
                            Stage stage = currentInstance.getBookingListStage();
                            Scene scene = new Scene(root);

                            stage.setScene(scene);
                            stage.setTitle("My Reservations List");
                            stage.show();
                            
                            btnsubmit.getScene().getWindow().hide();
                        }
                        catch (IOException ex)
                        {
                            Logger.getLogger(FXML_BookingAddController.class.getName()).log(Level.SEVERE, null, ex);
                        }   break;
                }
                case 1:
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Reservation Unsuccessful");
                        alert.setHeaderText("An Error Occured");
                        btnsubmit.setDisable(false);
                        btnGift.setDisable(false);
                        alert.show();
                        break;
                    }
                default:
                {
                    // result equals 2 meaning nothing changed meaning no update to occur
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Reservation Unsuccessful");
                    alert.setHeaderText("Nothing To Update");
                    
                    btnsubmit.setDisable(false);
                    btnGift.setDisable(false);
                    
                    alert.show();
                    
                    break;
                }
            }
        }
        else
        {
            btnsubmit.setDisable(false);
            btnGift.setDisable(false);
            
            Alert nonValidFormDialog = new Alert(Alert.AlertType.ERROR);
            nonValidFormDialog.setTitle("Booking Error");
            nonValidFormDialog.setHeaderText("Please provide all fields and remember \n"
                    + "that you can't book more than the propery provides \n"
                    + "(Check Number Of Rooms and booking date + term)");
            nonValidFormDialog.show();
        }
    }
    
    private boolean validateForm(Integer booking_id) {
        if (datepk.getValue() == null || txtterme.getText().isEmpty() || txtbancaire.getText().isEmpty() || txtnbrchambre.getText().isEmpty())
        {
            return false;
        }
        
        int term = Integer.parseInt(txtterme.getText());
        
        if (term > 0 && term < 365)
        {
            if (datepk.getValue().isAfter(LocalDate.now()))
            {
                boolean checkNumberOfRooms = new BookingService().getFreeRooms(relatedProperty.getId(), Date.valueOf(datepk.getValue()), term, false, null) >= 
                        Integer.parseInt(txtnbrchambre.getText());
                
                if (booking_id != null)
                    checkNumberOfRooms = new BookingService().getFreeRooms(relatedProperty.getId(), Date.valueOf(datepk.getValue()), term, true, booking_id) >= 
                        Integer.parseInt(txtnbrchambre.getText());
                
                if (checkNumberOfRooms)
                {
                    return true;
                }
            }
        }
        
        return false;
    }

    private void setNumberOnlyField(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}