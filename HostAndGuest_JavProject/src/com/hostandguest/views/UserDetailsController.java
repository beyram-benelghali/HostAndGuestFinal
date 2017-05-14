/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class UserDetailsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    protected Label id_detail ;
    @FXML
    protected Label username_detail ;
    @FXML
    protected Label email_detail ;
    @FXML
    protected Label ban_detail ;
    @FXML
    protected Hyperlink delete ;
    @FXML
    protected Hyperlink ban ;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
