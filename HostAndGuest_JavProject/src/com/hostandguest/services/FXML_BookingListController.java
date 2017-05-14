/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.hostandguest.entities.Booking;
import com.hostandguest.entities.User;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.hostandguest.entities.Property;
import com.hostandguest.util.currentInstance;
import com.hostandguest.views.MyPropertyModelController;
import com.hostandguest.views.PropertyDetailsController;
import com.jfoenix.controls.JFXButton;
import com.sun.javafx.stage.StageHelper;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;


/**
 * FXML Controller class
 *
 * @author Toshiba
 */
public class FXML_BookingListController implements Initializable {
    @FXML
    private ListView lstUserBookings;
    
    public User current_user = new UserService().getUserById(User.currentUser);
    
    public boolean showGiftList = false;
    
    public int property_id;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        refresh();
    }    
    
    public void refresh()
    {
        try
        {
            lstUserBookings.getItems().clear();
            
            List<Booking> bookingsList = new BookingService().findAll(current_user, showGiftList, property_id);
            
            if (bookingsList.isEmpty())
            {
                Alert isEmptyDialog = new Alert(Alert.AlertType.INFORMATION);
                
                isEmptyDialog.setTitle("Bookings List");
                isEmptyDialog.setHeaderText("No Bookings Available To Preview");
                
                isEmptyDialog.showAndWait();
            }
            else
            {
                ObservableSet<Parent> observableSet = FXCollections.observableSet();

                for (Booking booking : bookingsList)
                {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_BookingModel.fxml"));
                    Parent model = (Parent)fxmlLoader.load();
                    FXML_BookingModelController model_controller = fxmlLoader.<FXML_BookingModelController>getController();

                    if (!showGiftList)
                        model_controller.lblUserName.setText(booking.getGuest().getLast_name() + " " + booking.getGuest().getFirst_name());
                    else
                        model_controller.lblUserName.setText("FREE");

                    model_controller.lblBknDate.setText(model_controller.lblBknDate.getText() + " " + booking.getBookingDate().toString());
                    model_controller.lblBknTerm.setText(model_controller.lblBknTerm.getText() + " " + booking.getTerm());
                    model_controller.lblNbRooms.setText(model_controller.lblNbRooms.getText() + " " + booking.getNbr_rooms_reserved());
                    model_controller.lblPrice.setText(model_controller.lblPrice.getText() + " " + booking.getTotal_amount());

                    if (showGiftList)
                    {
                        model_controller.btnAnnuler.setVisible(false);
                        model_controller.btnToProperty.setVisible(false);
                        model_controller.btnExportPDF.setVisible(false);
                        model_controller.btnUpdateBooking.setVisible(false);

                        model_controller.btnAcquireGift.setOnAction((event) -> {
                            booking.setGuest(current_user);

                            if (new BookingService().AcquireGift(booking))
                            {
                                showGiftList = false;
                                refresh();
                            }
                        });
                    }
                    else
                    {
                        model_controller.btnAcquireGift.setVisible(false);

                        if (booking.getBookingDate().compareTo(new Date()) >= 0)
                        model_controller.btnAnnuler.setOnAction((e) -> {
                            Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);

                            if (new BookingService().removeById(booking.getId()))
                            {
                                alertDialog.setTitle("Reservation Canceling");
                                alertDialog.setHeaderText("Reservation Canceled Successfully");
                            }
                            else
                            {
                                alertDialog.setAlertType(Alert.AlertType.ERROR);
                                alertDialog.setTitle("Error During Cancellation");
                                alertDialog.setHeaderText("An Error Occured During The Cancellation of your Reservation");
                            }

                            alertDialog.show();
                            refresh();
                            });
                        else
                        {
                            model_controller.btnAnnuler.setVisible(false);
                            model_controller.btnUpdateBooking.setVisible(false);
                        }

                        model_controller.btnToProperty.setOnAction((e) -> {
                            PropertyService ps = new PropertyService();
                            Property p = ps.getPropertyById(booking.getProperty().getId());
                            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/hostandguest/views/PropertyDetails.fxml"));
                            try {
                                Parent model2 = (Parent)fxmlLoader2.load();
                                PropertyDetailsController controller = fxmlLoader2.getController();
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
                                
                                Stage stage = StageHelper.getStages().get(0);
                                Scene scene = new Scene(model2);
                                stage.setScene(scene);
                                
                                lstUserBookings.getScene().getWindow().hide();
                                StageHelper.getStages().get(0).hide();
                                stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(MyPropertyModelController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        model_controller.btnExportPDF.setOnAction((e) -> {
                            Alert theDialog = new Alert(Alert.AlertType.ERROR);

                            if (saveToPDF(booking))
                            {
                                theDialog.setAlertType(Alert.AlertType.INFORMATION);
                                theDialog.setTitle("Save Information");
                                theDialog.setHeaderText("Reservation Saved to Location successfully");
                            }
                            else
                            {
                                theDialog.setTitle("Save Error");
                                theDialog.setHeaderText("An Error Occured While Exporting To PDF");
                            }

                            theDialog.show();
                        });

                        model_controller.btnUpdateBooking.setOnAction(e ->{
                            setBookingUpdate(booking);
                        });
                    }

                    observableSet.add(model);
                }
                lstUserBookings.setItems(FXCollections.observableArrayList(observableSet));
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(FXML_BookingListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean saveToPDF(Booking booking) {
        try
        {
            // get the user to provide save directory
            DirectoryChooser directoryChooser = new DirectoryChooser();
                    
            directoryChooser.setTitle("Choose Save Location");
                    
            File selectedDir = directoryChooser.showDialog(new Stage());
                    
            String fileName = booking.getGuest().getLast_name() + " " + booking.getGuest().getFirst_name() + " " 
                    + booking.getBookingDate().toString() + ".pdf";
            
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            
            doc.addPage(page);
            
            PDPageContentStream content = new PDPageContentStream(doc, page);
            
            // setting content
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 26);
            content.newLineAtOffset(220, 750);
            content.showText("Reservation Form");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(80, 700);
            content.showText("Name : ");
            content.endText();
        
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 14);
            content.newLineAtOffset(250, 700);
            content.showText(booking.getGuest().getLast_name() + " " + booking.getGuest().getFirst_name());
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(80,650);
            content.showText("Reservation Date : ");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 14);
            content.newLineAtOffset(250,650);
            content.showText(booking.getBookingDate().toString());
            content.endText();
        
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(80,600);
            content.showText("Term : ");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 14);
            content.newLineAtOffset(250,600);
            content.showText(String.valueOf(booking.getTerm()));
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(80,550);
            content.showText("Number of Rooms : ");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 14);
            content.newLineAtOffset(250,550);
            content.showText(String.valueOf(booking.getNbr_rooms_reserved()));
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(80,500);
            content.showText("Total Amount : ");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 14);
            content.newLineAtOffset(250,500);
            content.showText(String.valueOf(booking.getTotal_amount()));
            content.endText();
            //
            
            content.close();
            doc.save(selectedDir.getAbsolutePath() + "\\" + fileName);
            doc.close();
            
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(FXML_BookingListController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * uses the BookingAdd view as the update view
     * @param booking 
     */
    private void setBookingUpdate(Booking booking) {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_BookingAdd.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            FXML_BookingAddController addController = fxmlLoader.<FXML_BookingAddController>getController();
            
            addController.btnGift.setVisible(false);
            addController.datepk.setValue(new java.sql.Date(booking.getBookingDate().getTime()).toLocalDate());
            addController.txtterme.setText(String.valueOf(booking.getTerm()));
            addController.txtnbrchambre.setText(String.valueOf(booking.getNbr_rooms_reserved()));
            addController.btnsubmit.setText("Update My Booking");
            
            addController.btnsubmit.setOnAction(e -> {
                addController.addBooking(false, booking.getId());
            });
            
            // temp
            User currentUser = new User();
                currentUser.setId(2);
                currentUser.setLast_name("Coller");
                currentUser.setFirst_name("Jack");
            
            Property relatedProperty = new Property();
                relatedProperty.setId(1);
                relatedProperty.setNbRooms(2);
                relatedProperty.setPrice(555);
            
            addController.currentUser = currentUser;
            addController.relatedProperty = relatedProperty;
            // end temp
            
            currentInstance.setBookingListStage((Stage) lstUserBookings.getScene().getWindow());
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Update your booking");
            stage.show();
        }
        catch (IOException ex)
        {
            Logger.getLogger(FXML_BookingListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
