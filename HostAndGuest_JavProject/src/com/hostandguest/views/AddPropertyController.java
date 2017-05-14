/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.services.PropertyService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BEYRAM-BG
 */
public class AddPropertyController implements Initializable {

    @FXML
    private JFXTextField RoomNb;
    @FXML
    private JFXTextField Location;
    @FXML
    private JFXTextField Price;
    @FXML
    private JFXButton addPicButton;
    @FXML
    private JFXCheckBox tvCheck;
    @FXML
    private JFXCheckBox KitchenCheck;
    @FXML
    private JFXCheckBox WifiCheck;
    @FXML
    private JFXButton savePropButton;
    @FXML
    private JFXTextArea Description;
    private List<File> selectedFiles ;
    private List<Object> imgPaths ;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }   
    
    @FXML
    void checkNumber(KeyEvent event) {
        System.out.println(event.getText());
        try {
                long number = Long.parseLong(((JFXTextField)event.getSource()).getText());
            } catch (Exception e) {
                ((JFXTextField)event.getSource()).setText(((JFXTextField)event.getSource()).getText().substring(0,((JFXTextField)event.getSource()).getText().length()-1));
            }
        
    }
    
    @FXML
    void AddPic(ActionEvent event) {
        imgPaths = new ArrayList<>();
        selectedFiles = new ArrayList<>();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Images files");
            fileChooser.setInitialDirectory(new File("C:\\"));
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Images File", "*.jpeg", "*.jpg", "*.png"));

            selectedFiles = fileChooser.showOpenMultipleDialog((Stage) addPicButton.getScene().getWindow());

            if (selectedFiles != null) {
                selectedFiles.forEach(f -> imgPaths.add("../../../web/images/uploads/"+f.getName()));
                if(selectedFiles.size()>1){
                    addPicButton.setText(selectedFiles.size() + " Images Selected");
                }
                if(selectedFiles.size()==1){
                    addPicButton.setText(selectedFiles.size() + " Image Selected");
                }
            }
            else {
                addPicButton.setText("Add Pictures ...");
            }
    }
    
    @FXML
    void saveProp(ActionEvent event) {                
        try {
        int nbroom = Integer.parseInt(RoomNb.getText());
        int price = Integer.parseInt(Price.getText());
        String loc = Location.getText();
        String descr = Description.getText();
        System.out.println(nbroom + " " + price + " " + loc);
        List<Object> equipementList = new ArrayList<Object>();
        if (tvCheck.isSelected()) {
            equipementList.add(tvCheck.getText());
        } 
        if (WifiCheck.isSelected()) {
            equipementList.add(WifiCheck.getText());
        } 
        if (KitchenCheck.isSelected()) {
            equipementList.add(KitchenCheck.getText());
        } 
        System.out.println(equipementList.toString());
        try {
            saveFileRoutine(selectedFiles);
        } catch (IOException ex) {
            Logger.getLogger(AddPropertyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        PropertyService ps = new PropertyService();
        Property p = new Property();
        p.setDescription(descr);p.setEnabled(true);p.setEquipements(equipementList);
        p.setHost_id(User.currentUser);p.setImagesPath(imgPaths);p.setLocation(loc);
        p.setNbRooms(nbroom);p.setPrice(price);p.setPublicationDate(new Date());
        p.setReported(false);
        ps.addProperty(p);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Your property was added !");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
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
        } catch(Exception e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Add Property Error");
            alert.setContentText("Please complete all the fields !");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
    
    private void saveFileRoutine(List<File> files)
			throws IOException{
            String path = "C:\\xampp\\htdocs\\PHPstormProjects\\Host_n_Guest\\web\\images\\uploads\\";
            for(File file : files) {
                Files.copy(file.toPath(),
                    (new File(path + file.getName())).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
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
