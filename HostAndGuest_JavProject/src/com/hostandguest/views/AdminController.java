/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;

import com.hostandguest.entities.User;
import com.hostandguest.services.StatistiquesService;
import com.hostandguest.services.UserCourant;
import com.hostandguest.services.UserService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.NumberAxisBuilder;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class AdminController implements Initializable {

    @FXML
    private AnchorPane home_page;
    @FXML
    private Button list_users;
    @FXML
    private Button list_statistiques;
    @FXML
    private Button list_properties;
    @FXML
    protected Button disconnect ;
    public User current_user = new User ();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Here !");
        
    }

    @FXML
    private void deco (ActionEvent event) throws IOException {

        Stage stage = (Stage) disconnect.getScene().getWindow();
        stage.close();        
        Parent root = FXMLLoader.load(getClass().getResource("/com/hostandguest/views/login.fxml")); 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("login panel");
        stage.show();
        UserCourant.idusercourant=-1 ;
    }
    
    

    @FXML
    private void listeusers (ActionEvent event) throws IOException {
         

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/ListeUsers.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            ListeUsersController home_admin_listeusers = fxmlLoader.<ListeUsersController>getController();
            home_admin_listeusers.current_user.setId(current_user.getId());
            
            home_admin_listeusers.affiche();
                Stage stage = new Stage();            
                Scene scene = new Scene(root);                             
                stage.setScene(scene);
                stage.setTitle("Admin panel");
                stage.show();
                User u = new User();
                u.setId(UserCourant.idusercourant);
        try {
            System.out.println(new UserService().getusernamebyid(u));
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }

    @FXML
    private void statistiques(ActionEvent event) {
   
        Stage primaryStage = new Stage ();
        Group root = new Group();
        Scene scene = new Scene(root,500,530);
        primaryStage.setScene(scene);
        
        init(primaryStage);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("FX Chart Demo");
        primaryStage.show();
        System.out.println(UserCourant.idusercourant);
    }

    @FXML
    private void listeproperties(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/ListeProperties.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            ListePropertiesController home_admin_liseproperties = fxmlLoader.<ListePropertiesController>getController();
                home_admin_liseproperties.affiche();
                Stage stage = new Stage();            
                Scene scene = new Scene(root);                             
                stage.setScene(scene);
                stage.setTitle("Admin panel");
                stage.show();
    }
    @FXML
    public void disconnect (ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/login.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            LoginController home_admin_liseproperties = fxmlLoader.<LoginController>getController();
                Stage stage = new Stage();            
                Scene scene = new Scene(root);                             
                stage.setScene(scene);
                stage.setTitle("Login Panel");
                stage.show();
        UserCourant.idusercourant = 0 ;
        
    }
    private void init(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root,500,530);
        primaryStage.setScene(scene);

        StackedAreaChart stackedAreaChart = createStackedChart();
        BarChart barChart = createBarChart();
        LineChart lineChart = createLineChart();
        PieChart pieChart = createPieChart();
        ScatterChart scatterChart = createScatterChart();
        
       // TitledPane t1 = new TitledPane("Stacked Chart", stackedAreaChart);
        TitledPane t2 = new TitledPane("Bar Chart", barChart);
      //  TitledPane t3 = new TitledPane("Line Chart", lineChart);
        TitledPane t4 = new TitledPane("Pie Chart", pieChart);
       // TitledPane t5 = new TitledPane("Scatter Chart", scatterChart);


        Accordion accordion = new Accordion();
      //  accordion.getPanes().add(t1);
        accordion.getPanes().add(t2);
       // accordion.getPanes().add(t3);
        accordion.getPanes().add(t4);
       // accordion.getPanes().add(t5);
        root.getChildren().add(accordion);
    }

    
    public void start(Stage primaryStage) {
        init(primaryStage);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("FX Chart Demo");
        primaryStage.show();
    }

    public StackedAreaChart createStackedChart() {
        NumberAxis xAxis = NumberAxisBuilder.create()
                .label("x axe ")
                .lowerBound(1.0d)
                .upperBound(9.0d)
                .tickUnit(2.0d).build();

        NumberAxis yAxis = NumberAxisBuilder.create()
                .label("y axe")
                .lowerBound(0.0d)
                .upperBound(30.0d)
                .tickUnit(2.0d).build();

        ObservableList<StackedAreaChart.Series> areaChartData = FXCollections.observableArrayList(
                new StackedAreaChart.Series("Series 1", FXCollections.observableArrayList(
                        new StackedAreaChart.Data(0, 4),
                        new StackedAreaChart.Data(2, 5),
                        new StackedAreaChart.Data(4, 4),
                        new StackedAreaChart.Data(6, 2),
                        new StackedAreaChart.Data(8, 6),
                        new StackedAreaChart.Data(10, 8)
                )),
                new StackedAreaChart.Series("Series 2", FXCollections.observableArrayList(
                        new StackedAreaChart.Data(0, 8),
                        new StackedAreaChart.Data(2, 2),
                        new StackedAreaChart.Data(4, 9),
                        new StackedAreaChart.Data(6, 7),
                        new StackedAreaChart.Data(8, 5),
                        new StackedAreaChart.Data(10, 7)
                )),
                new StackedAreaChart.Series("Series 3", FXCollections.observableArrayList(
                        new StackedAreaChart.Data(0, 2),
                        new StackedAreaChart.Data(2, 5),
                        new StackedAreaChart.Data(4, 8),
                        new StackedAreaChart.Data(6, 6),
                        new StackedAreaChart.Data(8, 9),
                        new StackedAreaChart.Data(10, 7)
                ))
        );
        return new StackedAreaChart(xAxis, yAxis, areaChartData);
    }
    
    
    public BarChart createBarChart(){
         String[] years = {"2017"};
         StatistiquesService s = new StatistiquesService();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(years));
        NumberAxis yAxis = new NumberAxis("Nombre total ", 0.0d, 50.0d, 10.0d);
        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
            new BarChart.Series("Users", FXCollections.observableArrayList(
               new BarChart.Data(years[0],s.CountUsers() )
            )),
            new BarChart.Series("Properties", FXCollections.observableArrayList(
               new BarChart.Data(years[0], s.CountProperties())
            )),
            new BarChart.Series("Reviews", FXCollections.observableArrayList(
               new BarChart.Data(years[0], s.CountReviews())
            ))
        );
        return  new BarChart(xAxis, yAxis, barChartData, 25.0d);
    }

    private LineChart createLineChart() {
       NumberAxis xAxis = new NumberAxis("Values for X-Axis", 0, 3, 1);
        NumberAxis yAxis = new NumberAxis("Values for Y-Axis", 0, 3, 1);
        ObservableList<XYChart.Series<Double,Double>> lineChartData = FXCollections.observableArrayList(
            new LineChart.Series<Double,Double>("Series 1", FXCollections.observableArrayList(
                new XYChart.Data<Double,Double>(0.0, 1.0),
                new XYChart.Data<Double,Double>(1.2, 1.4),
                new XYChart.Data<Double,Double>(2.2, 1.9),
                new XYChart.Data<Double,Double>(2.7, 2.3),
                new XYChart.Data<Double,Double>(2.9, 0.5)
            )),
            new LineChart.Series<Double,Double>("Series 2", FXCollections.observableArrayList(
                new XYChart.Data<Double,Double>(0.0, 1.6),
                new XYChart.Data<Double,Double>(0.8, 0.4),
                new XYChart.Data<Double,Double>(1.4, 2.9),
                new XYChart.Data<Double,Double>(2.1, 1.3),
                new XYChart.Data<Double,Double>(2.6, 0.9)
            ))
        );
        return new LineChart(xAxis, yAxis, lineChartData);
    }
    
    
     public PieChart createPieChart() {
         StatistiquesService s = new StatistiquesService();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
             new PieChart.Data("Users not Banned", s.CountUsersNotBanned()),
             new PieChart.Data("Users Banned", s.CountUsersBanned())

         );
        return new PieChart(pieChartData);
    }

    private ScatterChart createScatterChart() {
        NumberAxis xAxis = new NumberAxis("X-Axis", 0d, 8.0d, 1.0d);
        NumberAxis yAxis = new NumberAxis("Y-Axis", 0.0d, 5.0d, 1.0d);
        ObservableList<XYChart.Series> data = FXCollections.observableArrayList(
            new ScatterChart.Series("Series 1", FXCollections.<ScatterChart.Data>observableArrayList(
                new XYChart.Data(0.2, 3.5),
                new XYChart.Data(0.7, 4.6),
                new XYChart.Data(1.8, 1.7),
                new XYChart.Data(2.1, 2.8),
                new XYChart.Data(4.0, 2.2),
                new XYChart.Data(4.1, 2.6),
                new XYChart.Data(4.5, 2.0),
                new XYChart.Data(6.0, 3.0),
                new XYChart.Data(7.0, 2.0),
                new XYChart.Data(7.8, 4.0)
            ))
        );
        return new ScatterChart(xAxis, yAxis, data);
    }
  
    
}
