/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.hostandguest.connection.MyConnection;
import com.hostandguest.entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asus
 */
public class UserCourant {
    public static int idusercourant ;
    public static String usernamecourant ;
    final String DB_URL = "jdbc:mysql://localhost:3306/hostnguest" ;
    final String USER = "root";
    final String pass ="";
    private Connection con ;
    private PreparedStatement pst ;
    
    
    public UserCourant (){
        con = MyConnection.getInstance().getConnection();
        System.out.println("connecté");
        
        
    }
    
    public int Getuseridcourant (User u)
    {
        List<User> listusers = new ArrayList<>();
        User us ;
        try{
            String sql = "select id,username from fos_user where id=?" ;
            pst= con.prepareStatement(sql);
            pst.setInt(1, u.getId());
            System.out.println( u.getId());
            ResultSet resultat = pst.executeQuery();
            while (resultat.next())
            {
                us = new User();
                us.setId(resultat.getInt(1));
                us.setUsername(resultat.getString(2));
                listusers.add(us);
                idusercourant = us.getId();
                User.currentUser= us.getId();
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idusercourant ;
    }    
}
    

