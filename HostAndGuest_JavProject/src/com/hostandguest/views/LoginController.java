/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.views;


import com.Pherialize.MixedArray;
import com.Pherialize.Pherialize;
import com.hostandguest.views.AdminController;
import com.hostandguest.connection.MyConnection;
import com.hostandguest.entities.User;
import com.hostandguest.services.UserCourant;
import com.hostandguest.utils.BCrypt;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Asus
 */
public class LoginController implements Initializable {

    @FXML
    private Button connect;
    @FXML
    private Label label;
    @FXML
    private TextField username_box;
    @FXML
    private TextField password_box;
    @FXML
    private Label label1;
    @FXML
    private Label label11;
    @FXML
    private Label invalid_label;
    @FXML
    private Button signin ;
    @FXML
    private Label valide;
    public static User user ;
    ObservableList<User> userdata ;
    
    @FXML
    private void handleButtonAction(ActionEvent evenement) throws IOException {

                ArrayList <Integer> Listvalide = isValidCredentials();
                Listvalide.forEach(e -> System.out.println(e));
                      if (Listvalide.get(0)!=-1 && Listvalide.get(1)==1  )
            {
                       System.out.println();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hostandguest/views/Admin.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            
            AdminController home_admin = fxmlLoader.<AdminController>getController();
                home_admin.current_user.setId(Listvalide.get(0)); 
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Admin panel");
                stage.show();
                connect.getScene().getWindow().hide();
                
                
                
            }
           else if (Listvalide.get(0)!=-1 && Listvalide.get(2)==1) {
               
               FXMLLoader fxmlLoaderuser = new FXMLLoader(getClass().getResource("/com/hostandguest/views/HomeProperty.fxml"));
            Parent rootuser = (Parent) fxmlLoaderuser.load();
               Stage stage = new Stage();
                Scene scene = new Scene(rootuser);
                stage.setScene(scene);
                stage.setTitle("User panel");
                stage.show();
                connect.getScene().getWindow().hide();
           }
           else if (Listvalide.get(2)==1)
           {
               username_box.clear();
               password_box.clear();
               valide.setText("Account banned !");
               
           } 
           else {
               username_box.clear();
               password_box.clear();
               valide.setText("Invalide Creditential !");
               
           }
         System.out.println("ok");
        
           
   
                    }
    
    @FXML
    private void creercompte (ActionEvent evenement) throws IOException {
                                       
            FXMLLoader fxmlsign = new FXMLLoader(getClass().getResource("/com/hostandguest/views/Signin.fxml"));
                Parent sign ;              
                sign = (Parent) fxmlsign.load();                
                SigninController signin_panel = fxmlsign.<SigninController>getController();
                Stage stage = new Stage();                
                Scene scene = new Scene(sign);                             
                stage.setScene(scene);
                stage.setTitle("Signin Panel");
                stage.show();
                signin.getScene().getWindow().hide(); 
            
    }
   
    private ArrayList <Integer> isValidCredentials() {
        int id=-1;
        int admin = 0 ;
        int notbanned = 0;

        Connection c =  MyConnection.getInstance().getConnection();
        Statement stmt = null;
        Statement stmt1 = null;
        MixedArray type_user_serialisable;
        String passwordenc=null ;
        try {
            
            
            
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            stmt1 = c.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT * FROM fos_user WHERE USERNAME= " + "'" + username_box.getText() + "'");
           while (rs1.next())
           {
               
             passwordenc ="$2a"+ rs1.getString("PASSWORD").substring(3);
               System.out.println("password encrypted is : "+passwordenc);
            
           }
            rs1.close();
            stmt1.close(); 
            if (BCrypt.checkpw(password_box.getText(), passwordenc))
            {
            ResultSet rs = stmt.executeQuery( "SELECT * FROM fos_user WHERE USERNAME= " + "'" + username_box.getText() + "'" 
           /* + " AND PASSWORD= " + "'" +  BCrypt.checkpw(password_box.getText(), passwordenc)+ "'" */);
            System.out.println("done with crypting");
            System.out.println(BCrypt.checkpw(password_box.getText(), passwordenc));
            
          //  ResultSet rs1 = stmt.executeQuery("SELECT * FROM fos_user where User");
            //+ BCrypt.hashpw(password_box.getText(), BCrypt.gensalt(12)) +
        /*    boolean iscrypted=false ;
            for (int i = 0; i < userdata.size(); i++) {
            if (username_box.getText().equals(userdata.get(i).getUsername()) && BCrypt.checkpw(password_box.getText(), userdata.get(i).getPassword()))
            {   iscrypted = true ;
            System.out.println(iscrypted);
            }
            else
            {  
                iscrypted = false ;
                System.out.println(iscrypted);
                System.out.println("not ok crypt !");
            }
            }
            */
            
            while ( rs.next() ) {
                 if (rs.getString("USERNAME") != null && rs.getString("PASSWORD") != null ) { 
                     String  username = rs.getString("USERNAME");
                     System.out.println( "USERNAME = " + username );
                     String password = rs.getString("PASSWORD");
//                     System.out.println("PASSWORD encrypted = " +BCrypt.checkpw(password_box.getText(),rs.getString("PASSWORD")));                    
                     type_user_serialisable = Pherialize.unserialize(rs.getString("roles")).toArray();
                     if (!type_user_serialisable.isEmpty())
                     if (type_user_serialisable.getString(0).equals("ROLE_ADMIN"))
                         admin = 1;
                     System.out.println(BCrypt.hashpw(password_box.getText(), BCrypt.gensalt(12)));

                    
                     if (rs.getBoolean("ENABLED"))
                         notbanned = 1 ;
                     
                     String type_user = rs.getString("roles");
                     System.out.println("type = " + type_user);
                     
                     id = rs.getInt("id");
                     System.out.println("id user is : " +id);
                     UserCourant.idusercourant=id;
                     User.currentUser=id;
                     System.out.println("heloooooo" + User.currentUser);
                     
                 }  
            }
            rs.close();
            stmt.close();
            } /* here ends the if test of bcrypt */
           
            } catch ( Exception e ) {
                e.printStackTrace();
                System.exit(0);
            }
            System.out.println("Operation done successfully");
            return new ArrayList <Integer> (Arrays.asList(id,admin,notbanned));
        
    }
    
    private boolean IsAdmin()
    {
    final String DB_URL = "jdbc:mysql://localhost:3306/hostnguest" ;
    final String USER = "root";
    final String pass ="";

        boolean admin = false ;
        Connection c =  MyConnection.getInstance().getConnection();
        Statement stmt = null;
        try {
            
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery( "SELECT * FROM fos_user WHERE USERNAME= " + "'" + username_box.getText() + "'" 
            + " AND PASSWORD= " + "'" + password_box.getText() + "'");
            
            
            while ( rs.next() ) {
                 if ( rs.getString("roles").equals(c)) { 
                    /* String  username = rs.getString("USERNAME");
                     System.out.println( "USERNAME = " + username );
                     String password = rs.getString("PASSWORD");
                     System.out.println("PASSWORD = " + password);   */                 
                     String type_user = rs.getString("roles");
                     System.out.println("type = " + type_user);
                     System.out.println("test admin effectué");
                     
                     admin = true;
                 }  
            }
            rs.close();
            stmt.close();
           
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Operation done successfully");
            return admin;
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        
    /*   signin.setOnAction((event) -> {
            if (isValidCredentials())
            {
                User user = new User();

                user.setUsername(username_box.getText());
                user.setPassword(password_box.getText());
                
                 System.out.println("ok");
               
    } 
             System.out.println("ok");
    
}); */
       
    }
    
}
               
