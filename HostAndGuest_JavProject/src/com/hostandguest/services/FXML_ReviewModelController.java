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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 */
public class FXML_ReviewModelController implements Initializable {
    
    @FXML
    protected Label lblUserName;
    
    @FXML
    protected Label lblCommendDate;
    
    @FXML
    protected Label lblBookingDate;
    
    @FXML
    protected Label lblComment;
    
    @FXML
    protected JFXButton deleteReview;
    
    @FXML
    protected JFXButton linkPlayVideo;
    
    /*
        Satisfaction items
    */
    @FXML
    protected Label lblSatisfactionLevel;
    
    @FXML
    protected ImageView satifGauge;
    
    @FXML
    protected ImageView moodFace;
    
    /*
        Price Quality Rate Fields
    */
    @FXML
    private ImageView PQRate_1;
    
    @FXML
    private ImageView PQRate_2;
    
    @FXML
    private ImageView PQRate_3;
    
    @FXML
    private ImageView PQRate_4;
    
    @FXML
    private ImageView PQRate_5;
    
    /*
        Position Rate Fields
    */
    @FXML
    private ImageView PosRate_1;
    
    @FXML
    private ImageView PosRate_2;
    
    @FXML
    private ImageView PosRate_3;
    
    @FXML
    private ImageView PosRate_4;
    
    @FXML
    private ImageView PosRate_5;
    
    /*
        Cleanliess Rate Fields
    */
    @FXML
    private ImageView CleanRate_1;
    
    @FXML
    private ImageView CleanRate_2;
    
    @FXML
    private ImageView CleanRate_3;
    
    @FXML
    private ImageView CleanRate_4;
    
    @FXML
    private ImageView CleanRate_5;
    
    /*
        Precision Rate Fields
    */
    @FXML
    private ImageView PrRate_1;
    
    @FXML
    private ImageView PrRate_2;
    
    @FXML
    private ImageView PrRate_3;
    
    @FXML
    private ImageView PrRate_4;
    
    @FXML
    private ImageView PrRate_5;
    
    /*
        Communication Rate Fields
    */
    @FXML
    private ImageView ComRate_1;
    
    @FXML
    private ImageView ComRate_2;
    
    @FXML
    private ImageView ComRate_3;
    
    @FXML
    private ImageView ComRate_4;
    
    @FXML
    private ImageView ComRate_5;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    /*
        Below methods were created because field instances are only up after initialization (not null)
    */
    public ImageView[] getPQRateArray()
    {
        return  new ImageView[]{PQRate_1, PQRate_2, PQRate_3, PQRate_4, PQRate_5};
    }
    
    public ImageView[] getPosRateArray()
    {
        return  new ImageView[]{PosRate_1, PosRate_2, PosRate_3, PosRate_4, PosRate_5};
    }
    
    public ImageView[] getCleanRateArray()
    {
        return  new ImageView[]{CleanRate_1, CleanRate_2, CleanRate_3, CleanRate_4, CleanRate_5};
    }
    
    public ImageView[] getPrRateArray()
    {
        return  new ImageView[]{PrRate_1, PrRate_2, PrRate_3, PrRate_4, PrRate_5};
    }
    
    public ImageView[] getComRateArray()
    {
        return  new ImageView[]{ComRate_1, ComRate_2, ComRate_3, ComRate_4, ComRate_5};
    }
}
