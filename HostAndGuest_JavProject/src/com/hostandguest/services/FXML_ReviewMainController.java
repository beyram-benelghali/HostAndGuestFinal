/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.hostandguest.entities.Review;
import com.hostandguest.entities.User;
import com.hostandguest.util.DesiredField;
import com.hostandguest.util.VideoPlayerService;
import com.hostandguest.util.currentInstance;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

/**
 * FXML Controller class
 */
public class FXML_ReviewMainController implements Initializable {
    
    @FXML
    private JFXListView reviewsList;
    
    @FXML
    private JFXButton btnAddReview;
    
    @FXML
    private CheckComboBox<String> filterPropertiesCheckCB;
    
    @FXML
    private JFXComboBox filterOrderTypeCB;
    
    @FXML
    private JFXButton btnApplyFilter;
    
    // set when called by the property fx controller
    // we will give it a default value for now
    private User current_user = new UserService().getUserById(User.currentUser);
    public int property_id = 0;
    
    // TODO ADD FILTER STUFF
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setFilterItems();
        
        // setting listview expansion
        reviewsList.setExpanded(Boolean.TRUE);
        reviewsList.depthProperty().set(1);
        
        // retrieves the list of reviews
//        refresh("");
        
        // setting add review button action
        btnAddReview.setOnAction((event) ->{
            try
            {   
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_ReviewAdd.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                FXML_ReviewAddController add_controller = fxmlLoader.<FXML_ReviewAddController>getController();
                
                add_controller.property_id = property_id;
                add_controller.callingInstance = this;
                add_controller.setCbValues();
                
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(root);
                scene.getStylesheets().add(
                        FXML_ReviewAddController.class.getResource("/resources/validationStyle.css")
                                .toExternalForm());
                stage.setScene(scene);
                stage.setTitle("Leave A Review");
                stage.show();
                
                // called at the end despite can be called way earlier but if called earlier throws NullPointerException
                add_controller.isAllowedToReview();
            }
            catch (IOException ex)
            {
                Logger.getLogger(FXML_ReviewMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void setFilterItems()
    {
        setCBItems();
        setCheckCB();
        setFilterBtn();
    }
    
    private void setCBItems()
    {
        filterOrderTypeCB.getItems().add("ASC");
        filterOrderTypeCB.getItems().add("DESC");
        
        filterOrderTypeCB.getSelectionModel().selectFirst();
    }
    
    private void setCheckCB()
    {
        // create the data to show in the CheckComboBox 
        final ObservableList<String> fields = FXCollections.observableArrayList();
    
        for (Field field : Review.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(DesiredField.class))
                fields.add(field.getAnnotation(DesiredField.class).shownValue());
        }
        
        filterPropertiesCheckCB.getItems().addAll(fields);
        
        // and listen to the relevant events (e.g. when the selected indices or 
        // selected items change).
//        filterPropertiesCheckCB.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
//            System.out.println(filterPropertiesCheckCB.getCheckModel().getCheckedItems());
//        });
    }
    
    private void setFilterBtn()
    {
        btnApplyFilter.setOnAction((event) -> {
            if (filterPropertiesCheckCB.getCheckModel().getCheckedItems().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            
                alert.setTitle("Filter Error");
                alert.setHeaderText("You must select at least one property to filter");

                alert.show();
            }
            else
            {
                // disable the button to not have the user call more than once every X seconds
                btnApplyFilter.setDisable(true);
                
                String orderBy = "order by ";
                
                for (String field : filterPropertiesCheckCB.getCheckModel().getCheckedItems())
                {
                    // some field have different names in the database
                    switch (field)
                    {
                        case "date comment":
                            orderBy += "date_comment,";
                            break;
                        case "price quality":
                            orderBy += "price_quality,";
                            break;
                        case "position":
                            orderBy += "lieu,";
                            break;
                        case "precision":
                            orderBy += "precisionToDescription,";
                            break;
                        case "satisfaction level":
                            orderBy += "satisfaction_level,";
                            break;
                        default:
                            orderBy += field + ",";
                    }
                    
                    // this is correct aswell but above might be the better solution
//                    for (Field classField : Review.class.getDeclaredFields())
//                    {
//                        if (classField.isAnnotationPresent(DesiredField.class))
//                            if (classField.getAnnotation(DesiredField.class).shownValue().equals(field))
//                                orderBy += classField.getAnnotation(DesiredField.class).databaseProperName() + ",";
//                    }
                }
                
                orderBy = orderBy.substring(0, orderBy.length() -1 );
                orderBy += " " + filterOrderTypeCB.getSelectionModel().getSelectedItem();
                
                refresh(orderBy);
                delayRefresh(5000);
            }
        });
    }
    
    private void delayRefresh(long millis)
    {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run()  {
                        // enable the filter button we disabled in the setFilterBtn function
                        btnApplyFilter.setDisable(false);
                    }
                }, millis);
    }
    
    // used to reduce code repetition
    private void setRate(ImageView[] imageViews, int limit)
    {
        for (int count = 0; count < limit; count++)
        {
            imageViews[count].setImage(new Image(getClass().getResourceAsStream("/resources/star_full.png")));
        }
    }
    
    public void refresh(String orderBy)
    {
        try
        {
            reviewsList.getItems().clear();
            
            ObservableSet<Parent> observableSet = FXCollections.observableSet();
            
            for (Review review : new ReviewService().getReviewsOfProperty(property_id, orderBy))
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/FXML_ReviewModel.fxml"));
                Parent model = (Parent)fxmlLoader.load();
                FXML_ReviewModelController model_controller = fxmlLoader.<FXML_ReviewModelController>getController();
                
                model_controller.lblUserName.setText(review.getBooking().getGuest().getLast_name() + " " + review.getBooking().getGuest().getFirst_name());
                model_controller.lblCommendDate.setText("Comment Date - " + review.getDateComment().toString());
                model_controller.lblBookingDate.setText("Booking Date - " + review.getBooking().getBookingDate().toString());
                model_controller.lblComment.setText(review.getComment());
                
                switch (review.getMood())
                {
                    case 1:
                        model_controller.moodFace.setImage(new Image(getClass().getResourceAsStream("/resources/happyface.png")));
                        break;
                    case 2:
                        model_controller.moodFace.setImage(new Image(getClass().getResourceAsStream("/resources/sadface.png")));
                        break;
                    case 3:
                        model_controller.moodFace.setImage(new Image(getClass().getResourceAsStream("/resources/angryface.png")));
                        break;
                    default:
                        model_controller.moodFace.setImage(new Image(getClass().getResourceAsStream("/resources/pokerface.png")));
                }
                
                model_controller.lblSatisfactionLevel.setText(
                        review.getSatisfaction_level() != 0 ? 
                                Integer.toString(review.getSatisfaction_level() + 1) : 
                                Integer.toString(review.getSatisfaction_level()));
                
                // done this way because the space between each grade in the imageview is not equal
                switch (review.getSatisfaction_level())
                {
                    case 9:
                    {
                        model_controller.satifGauge.setLayoutY(5);
                        model_controller.lblSatisfactionLevel.setLayoutY(0);
                        model_controller.lblSatisfactionLevel.setLayoutX(454);
                        break;
                    }
                    case 8:
                        model_controller.satifGauge.setLayoutY(20);
                        model_controller.lblSatisfactionLevel.setLayoutY(15);
                        break;
                    case 7:
                        model_controller.satifGauge.setLayoutY(37.2);
                        model_controller.lblSatisfactionLevel.setLayoutY(32.2);
                        break;
                    case 6:
                        model_controller.satifGauge.setLayoutY(51.1);
                        model_controller.lblSatisfactionLevel.setLayoutY(46.1);
                        break;
                    case 5:
                        model_controller.satifGauge.setLayoutY(63);
                        model_controller.lblSatisfactionLevel.setLayoutY(58);
                        break;
                    case 4:
                        model_controller.satifGauge.setLayoutY(78.5);
                        model_controller.lblSatisfactionLevel.setLayoutY(73.5);
                        break;    
                    case 3:
                        model_controller.satifGauge.setLayoutY(98);
                        model_controller.lblSatisfactionLevel.setLayoutY(93);
                        break;
                    case 2:
                        model_controller.satifGauge.setLayoutY(118.3);
                        model_controller.lblSatisfactionLevel.setLayoutY(113.3);
                        break;
                    case 1:
                        model_controller.satifGauge.setLayoutY(135.9);
                        model_controller.lblSatisfactionLevel.setLayoutY(130.9);
                        break;
                    case 0:
                        // when the mood is known it will default to this
                        if (review.getMood() != 0)
                        {
                            model_controller.satifGauge.setLayoutY(154.5);
                            model_controller.lblSatisfactionLevel.setLayoutY(149.5);
                        }
                        break;
                    default:
                        model_controller.satifGauge.setLayoutY(168);
                        model_controller.lblSatisfactionLevel.setLayoutY(163);
                }
                
                if (review.getBooking().getGuest().getId() != current_user.getId())
                    model_controller.deleteReview.setVisible(false);
                else
                {
                    model_controller.deleteReview.setOnAction((event) -> {
                        new ReviewService().deleteReview(review.getId(), current_user, review.getVideo_url());
                        refresh(orderBy);
                    });
                }
                
                if (review.getVideo_url() == null || review.getVideo_url().isEmpty())
                {
                    model_controller.linkPlayVideo.setVisible(false);
                }
                else
                {
                    model_controller.linkPlayVideo.setOnAction((event) -> {
                        VideoPlayerService videoPlayerService = new VideoPlayerService();
                        
                        videoPlayerService.MEDIA_URL = review.getVideo_url();
                        videoPlayerService.start(new Stage());
                    });
                }
                
                setRate(model_controller.getPQRateArray(), review.getPrice_quality());
                setRate(model_controller.getPosRateArray(), review.getLieu());
                setRate(model_controller.getCleanRateArray(), review.getCleanliness());
                setRate(model_controller.getPrRateArray(), review.getPrecision());
                setRate(model_controller.getComRateArray(), review.getCommunication());
                
                observableSet.add(model);
            }
            reviewsList.setItems(FXCollections.observableArrayList(observableSet));
        }
        catch (IOException ex)
        {
            Logger.getLogger(FXML_ReviewMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
