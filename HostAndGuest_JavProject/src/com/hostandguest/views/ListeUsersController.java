/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.Review;
import com.hostandguest.entities.User;
import com.hostandguest.services.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class ListeUsersController implements Initializable {

    /**
     * Initializes the controller class.
     */
    protected User current_user = new User();

    public static final ObservableList names = 
        FXCollections.observableArrayList();
    protected User username = new User () ;
    
    @FXML
    private ListView list_users;
    @FXML
    private TextField username_find ;
    @FXML
    private Button find ;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         /*   names.addAll(
             "Adam", "Alex", "Alfred", "Albert",
             "Brenda", "Connie", "Derek", "Donny", 
             "Lynne", "Myrtle", "Rose", "Rudolph", 
             "Tony", "Trudy", "Williams", "Zach"
        );
         list_users.setItems(names); */
       //  affiche();

        
    }
    public void recherche ()
    {
        find.setOnAction((ActionEvent event) -> {
            
            if (username_find.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("champ de recherche vide !");
                alert.show();
            }
            String critere ;
            critere = username_find.getText();
            afficherecherche(critere);
            
            
    });
    } 
   
      public void afficherecherche(String critere)
    {
        try
        {
            list_users.getItems().clear();
            
            ObservableSet<Parent> observableSet = FXCollections.observableSet();
            
            
            for (User user : new UserService().RechercheUsers(critere))
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/UserDetails.fxml"));
                Parent model = (Parent)fxmlLoader.load();
                UserDetailsController model_controller = fxmlLoader.<UserDetailsController>getController();
                
                model_controller.id_detail.setText(String.valueOf(user.getId()));
                model_controller.username_detail.setText( user.getUsername().toString());
                model_controller.email_detail.setText(user.getEmail().toString());
                model_controller.ban_detail.setText(String.valueOf(user.isEnabled()));
                model_controller.ban.setOnAction((event) -> {
                        new UserService().banuser(user);
                        afficherecherche(critere);
                    });
                model_controller.delete.setOnAction((event) -> {
                        new UserService().delete(user);
                        afficherecherche(critere);
                    });
                observableSet.add(model);
            }
            list_users.setItems(FXCollections.observableArrayList(observableSet));
        }
        catch (IOException ex)
        {
            Logger.getLogger(ListeUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
         

    }
      
      public void affiche()
    {
        try
        {
            list_users.getItems().clear();
            
            ObservableSet<Parent> observableSet = FXCollections.observableSet();
            
            
            for (User user : new UserService().listuser(current_user))
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/UserDetails.fxml"));
                Parent model = (Parent)fxmlLoader.load();
                UserDetailsController model_controller = fxmlLoader.<UserDetailsController>getController();
                
                model_controller.id_detail.setText(String.valueOf(user.getId()));
                model_controller.username_detail.setText( user.getUsername().toString());
                model_controller.email_detail.setText(user.getEmail().toString());
                model_controller.ban_detail.setText(String.valueOf(user.isEnabled()));
                model_controller.ban.setOnAction((event) -> {
                        new UserService().banuser(user);
                        affiche();
                    });
                model_controller.delete.setOnAction((event) -> {
                        new UserService().delete(user);
                        affiche();
                    });
                observableSet.add(model);
            }
            list_users.setItems(FXCollections.observableArrayList(observableSet));
        }
        catch (IOException ex)
        {
            Logger.getLogger(ListeUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
         

    }    
    
}
