/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author The
 */
public class MyConnection {
<<<<<<< HEAD
    private static final String DB_URL = "jdbc:mysql://localhost/hostandguest";
=======
    private static final String DB_URL = "jdbc:mysql://localhost/hng_db_full_project";
>>>>>>> origin/master
    private static final String USER = "root";
    private static final String PSW = "";
    private Connection connection;

    private static MyConnection instance = null;

    private MyConnection()
    {
        try 
        {
            connection = (Connection) DriverManager.getConnection(DB_URL, USER, PSW);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(MyConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public static MyConnection getInstance()
    {
        if (instance == null)
            instance = new MyConnection();
        
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
}
