/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.connection.MyConnection;
import com.hostandguest.entities.Property;
import com.hostandguest.entities.Review;
import com.hostandguest.entities.User;
import com.hostandguest.interfaces.IUser;
import com.hostandguest.services.UserService;
import com.hostandguest.services.UserService;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.hostandguest.utils.BCrypt;

/**
 * FXML Controller class
 *
 * @author Asus
 */
    
public class SigninController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField username_box;
    @FXML
    private TextField password_box;
    @FXML
    private TextField email_box;
    @FXML
    private TextField lastname_box;
    @FXML
    private TextField firstname_box;
    @FXML
    private Label label_username;
    @FXML
    private Label label_password;
    @FXML
    private Label label_email;
    @FXML
    private Label label_lastname;
    @FXML
    private Label label_firstname;
    @FXML
    private Button signin;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                signin.setOnAction((event) -> {
            
            
                User user = new User();

                user.setUsername(username_box.getText());
                user.setUsernameCanonical(username_box.getText().substring(0, 1).toUpperCase()+ username_box.getText().substring(1));
                user.setPassword(BCrypt.hashpw(password_box.getText(),BCrypt.gensalt(12) ));
                user.setEmail(email_box.getText());
                user.setEmailCanonical(email_box.getText().substring(0, 1).toUpperCase()+ email_box.getText().substring(1));
                user.setLast_name(lastname_box.getText());
                user.setFirst_name(firstname_box.getText());
                String [] rol = {};
                user.setRoles(rol);              
                new UserService().ajoutuser(user);
                    try {
                        signafterlogin();
                    } catch (IOException ex) {
                        Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                




              
            
                        });
                
    }
    public void signafterlogin () throws IOException
    {
        Stage stage = new Stage ();
                 stage.close();        
                Parent root = FXMLLoader.load(getClass().getResource("/com/hostandguest/views/login.fxml")); 
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
        
    }


      @FXML
    private void handleButtonAction(ActionEvent evenement) throws IOException {

        
    }
    
    
}
