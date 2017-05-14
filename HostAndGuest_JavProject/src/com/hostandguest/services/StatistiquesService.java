/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.hostandguest.views.SigninController;
import com.hostandguest.connection.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asus
 */
public class StatistiquesService {
    final String DB_URL = "jdbc:mysql://localhost:3306/hostnguest" ;
    final String USER = "root";
    final String pass ="";
    private Connection con ;
    private PreparedStatement pst ;
    
    public StatistiquesService (){
        con = MyConnection.getInstance().getConnection();
        System.out.println("connecté");
        
        
    }
    public int CountUsers ()
    {
        int nbusers = 0 ;
        try {
            
            String COUNTUSER = "Select username from fos_user";
            PreparedStatement preparedStatement = con.prepareStatement(COUNTUSER);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                nbusers++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nbusers ;
    }
        public int CountProperties ()
    {
        int nbproperties = 0 ;
        try {
            
            String countproperties = "Select id from property";
            PreparedStatement preparedStatement = con.prepareStatement(countproperties);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                nbproperties++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nbproperties ;
    }
            public int CountReviews ()
    {
        int nbReviews = 0 ;
        try {
            
            String countreviews = "Select id from review";
            PreparedStatement preparedStatement = con.prepareStatement(countreviews);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                nbReviews++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nbReviews ;
    }
    public int CountUsersBanned ()
    {
        int nbBanned = 0 ;
        try {
            
            String countuserbanned = "Select id from fos_user where enabled=0";
            PreparedStatement preparedStatement = con.prepareStatement(countuserbanned);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                nbBanned++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nbBanned ;
    }
    public int CountUsersNotBanned ()
    {
        int nbnotBanned = 0 ;
        try {
            
            String usernotbanned = "Select id from fos_user where enabled=1";
            PreparedStatement preparedStatement = con.prepareStatement(usernotbanned);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                nbnotBanned++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nbnotBanned ;
    }
            
    
}
