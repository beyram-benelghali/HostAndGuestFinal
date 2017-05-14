/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Toshiba
 */
public class FXML_BookingModelController implements Initializable {

    @FXML
    protected Label lblUserName;
    @FXML
    protected Label lblBknDate;
    @FXML
    protected Label lblBknTerm;
    @FXML
    protected Label lblNbRooms;
    @FXML
    protected Label lblPrice;
    @FXML
    protected JFXButton btnAnnuler;
    @FXML
    protected JFXButton btnToProperty;
    @FXML
    protected JFXButton btnExportPDF;
    @FXML
    protected JFXButton btnAcquireGift;
    @FXML
    protected JFXButton btnUpdateBooking;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
