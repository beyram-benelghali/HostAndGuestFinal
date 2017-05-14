/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Property;
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
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BEYRAM-BG
 */
public class UpdatePropertyController implements Initializable {

    
    @FXML
    public AnchorPane AnchorUpdateId;
    @FXML
    public JFXTextField RoomNb;
    @FXML
    public JFXTextField Location;
    @FXML
    public JFXTextField Price;
    @FXML
    public JFXButton addPicButton;
    @FXML
    public JFXCheckBox tvCheck;
    @FXML
    public JFXCheckBox KitchenCheck;
    @FXML
    public JFXCheckBox WifiCheck;
    @FXML
    public JFXTextArea Description;
    @FXML
    public JFXButton savePropButton;
    private List<File> selectedFiles = new ArrayList<File>(); ;
    private List<Object> imgPaths = new ArrayList<Object>();;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Images files");
            fileChooser.setInitialDirectory(new File("C:\\"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images File", "*.jpeg", "*.jpg", "*.png"));

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
                selectedFiles = new ArrayList<File>();
                addPicButton.setText("Add Pictures ...");
            }
    }

    @FXML
    private void saveProp(ActionEvent event) {
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
            System.out.println("size" + selectedFiles.size() );
            if(! selectedFiles.isEmpty())
            {
                saveFileRoutine(selectedFiles);
            }
        } catch (IOException ex) {
            Logger.getLogger(UpdatePropertyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        PropertyService ps = new PropertyService();
        Property p = ps.getPropertyById(Integer.parseInt(AnchorUpdateId.getId()));
        p.setDescription(descr);
        p.setEnabled(true);
        List<Object> lastImages = new ArrayList<Object>();
       // lastImages.addAll(p.getImagesPath());
        lastImages.addAll(imgPaths);
        p.getImagesPath().forEach(f -> lastImages.add(f.toString()));
        //imgPaths.forEach(e ->  {System.out.println(e);});
        //imgPaths.forEach(e -> {System.out.println(e);});
        
        
        //imgPathlastImagess.add("Hello BG");
        System.out.println(lastImages);
        p.setEquipements(equipementList);
        p.setHost_id(2);
        p.setImagesPath(lastImages);
        p.setLocation(loc);
        p.setNbRooms(nbroom);
        p.setPrice(price);
        p.setPublicationDate(new Date());
        p.setReported(false);
        ps.updateProperty(p);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Your property was Updated !");
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
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Property Error");
            alert.setContentText("Please complete all the fields !");
            alert.setHeaderText(null);
            alert.showAndWait();
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
    
        private void saveFileRoutine(List<File> files)
			throws IOException{
            String path = "C:\\xampp\\htdocs\\PHPstormProjects\\Host_n_Guest\\web\\images\\uploads\\";
            for(File file : files) {
                Files.copy(file.toPath(),
                    (new File(path + file.getName())).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            }
    }
}
