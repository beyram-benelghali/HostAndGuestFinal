/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;
import com.Pherialize.PhpSerialize;
import com.hostandguest.views.SigninController;
import com.hostandguest.connection.MyConnection;
import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.interfaces.IUser ;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Asus
 */
public class UserService implements IUser {
    final String DB_URL = "jdbc:mysql://localhost:3306/hostnguest" ;
    final String USER = "root";
    final String pass ="";
    private Connection con ;
    private PreparedStatement pst ;
    
    
    public UserService (){
        con = MyConnection.getInstance().getConnection();
        System.out.println("connecté");
        
        
    }
    @Override
    public void delete(User u) {
        try {
            String querydelete = "DELETE FROM fos_user WHERE id=?";
            PreparedStatement preparedStatement = con.prepareStatement(querydelete);
            preparedStatement.setInt(1,u.getId());
            preparedStatement.executeUpdate(); 
            System.out.println("user deleted");
        } catch (SQLException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void ajoutuser(User u) {
        try {
            String queryInsert = "INSERT INTO fos_user (id,username,username_canonical,email,email_canonical,password,enabled,roles,last_name,first_name,last_login) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(queryInsert);
            preparedStatement.setInt(1, u.getId());
            preparedStatement.setString(2, u.getUsername());                       
            preparedStatement.setString(3, u.getUsernameCanonical());
            preparedStatement.setString(4,u.getEmail());
            preparedStatement.setString(5,u.getEmailCanonical());
            preparedStatement.setString(6,u.getPassword());
            preparedStatement.setBoolean(7, true);
            //preparedStatement.setString(8,PhpSerialize.serialize(u.getRoles()) );   
            preparedStatement.setString(8,PhpSerialize.serialize(new ArrayList<>(Arrays.asList(u.getRoles()))) );  
            preparedStatement.setString(9, u.getLast_name());
            preparedStatement.setString(10, u.getFirst_name());
            preparedStatement.setDate(11, new java.sql.Date(new Date().getTime()));
            preparedStatement.executeUpdate(); 
            System.out.println("user added");
        } catch (SQLException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @Override
    public List<User> RechercheUsers(String critere) {
        List<User> listusers = new ArrayList<>();
        String query = "SELECT id,username,email,last_name,first_name FROM fos_user WHERE username=? "  ;
                        
        
        try
        {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, critere);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                User user = new User();
                
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setEmail(resultSet.getString(3));
                user.setLast_name(resultSet.getString(4));
                user.setFirst_name(resultSet.getString(5));
                listusers.add(user);
            }
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listusers;
    }


    @Override
    public void banuser(User u) {
    String sql = "update fos_user set enabled=? where id=? " ;
        try {
            pst= con.prepareStatement(sql);
            pst.setBoolean(1, !u.isEnabled());
            pst.setInt(2,u.getId());
            pst.executeUpdate();
           // pst.setBoolean(0,u.setEnabled(true));
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
           }

    @Override
    public void updateuser(User u) {
       String sql = "update fos_user set (id,username,username_canonical,email,email_canonical,password,last_name,first_name) Values (?,?,?,?,?,?,?,?)";
       try {
            pst = con.prepareStatement(sql);
            pst.setInt(1,u.getId());
            pst.setString(2,u.getUsername());
            pst.setString(3,u.getUsernameCanonical());
            pst.setString(4,u.getEmail());
            pst.setString(5,u.getEmailCanonical());
            pst.setString(6,u.getPassword());
            pst.setString(7,u.getLast_name());
            pst.setString(8,u.getFirst_name());
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void hideproprety(Property p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> listuser(User u) {
        List<User> listusers = new ArrayList<>();
        User us ;
        try{
            String sql = "select id,username,last_name,first_name,password,email,enabled from fos_user where id!=?" ;
            pst= con.prepareStatement(sql);
            pst.setInt(1, u.getId());
            System.out.println( u.getId());
            ResultSet resultat = pst.executeQuery();
            while (resultat.next())
            {
                us = new User();
                us.setId(resultat.getInt(1));
                us.setUsername(resultat.getString(2));
                us.setLast_name(resultat.getString(3));
                us.setFirst_name(resultat.getString(4));
                us.setPassword(resultat.getString(5));
                us.setEmail(resultat.getString(6));
                us.setEnabled(resultat.getBoolean("enabled"));
                
                listusers.add(us);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listusers ;
    }
    public String getusernamebyid(User u) throws SQLException 
    {
        String username = null ;
        User us = null ;
        String sql = "select username from fos_user where id=?" ;
        pst= con.prepareStatement(sql);
        pst.setInt(1, u.getId());
        ResultSet resultat = pst.executeQuery();
        while (resultat.next())
            {
                us = new User();
                us.setUsername(resultat.getString(1));
                username=us.getUsername();
        
        
    }
        return username ;
    }
    public User getUserById(int userId){
            try {
                String query = "Select * from fos_user where id ="+userId;
                Statement  stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setLast_name("last_name");
                u.setFirst_name("first_name");
                u.setEmail(rs.getString("email"));
                rs.close();
                return u;
            } catch (SQLException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }
     public User getUserByUsername(String username){
            try {
                String query = "Select * from fos_user where username ='"+username+"'";
                Statement  stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                rs.close();
                return u;
            } catch (SQLException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }

   
    
}
