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
public class PropertyDetailsAdminController implements Initializable {

    @FXML
    protected Label lblNbrooms;
    @FXML
    protected Label lblPrice;
    @FXML
    protected Label lblPublicationdate;
    @FXML
    protected Label lblenabled;
    @FXML
    protected Label lblbanned;
    @FXML
    protected Hyperlink Disable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    // rien a faire ici
    
}
