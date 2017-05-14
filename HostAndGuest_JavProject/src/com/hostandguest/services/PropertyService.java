/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.Pherialize.MixedArray;
import com.Pherialize.Pherialize;
import com.Pherialize.PhpSerialize;
import com.hostandguest.connection.MyConnection;
import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.interfaces.IProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BEYRAM-BG
 */

public class PropertyService implements IProperty
{
    private Connection con = null;

    public PropertyService() {
        con = MyConnection.getInstance().getConnection();
        System.out.println("Connected to database !");
    }
    
    
    
    @Override
    public void addProperty(Property p) {
        try {
            String queryInsert = "INSERT INTO property (host_id,nb_rooms,price,description,equipements,images_path,location,reported,enabled,publication_date) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(queryInsert);
            preparedStatement.setInt(1, p.getHost_id());            
            preparedStatement.setInt(2, p.getNbRooms());
            preparedStatement.setInt(3, p.getPrice());            
            preparedStatement.setString(4, p.getDescription());
            preparedStatement.setString(5, PhpSerialize.serialize(p.getEquipements()));
            preparedStatement.setString(6,PhpSerialize.serialize(p.getImagesPath()) );            
            preparedStatement.setString(7, p.getLocation());
            preparedStatement.setBoolean(8, false);            
            preparedStatement.setBoolean(9, true);
            preparedStatement.setDate(10, new java.sql.Date(new Date().getTime()));
            preparedStatement.executeUpdate(); 
            System.out.println("Property Addedd !");
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void removeProperty(Property p) {
        try {
            String queryDelete = "DELETE FROM property WHERE id = " + p.getId() ;;
            PreparedStatement preparedStatement = con.prepareStatement(queryDelete);
            preparedStatement.executeUpdate(queryDelete);
            System.out.println("Property Removed !");
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateProperty(Property p) {
        try {
            String queryInsert = "UPDATE property SET nb_rooms= ? ,price= ? ,description= ? ,equipements= ? ,images_path= ? ,location= ? ,reported= ? ,enabled= ? Where id= ?";
            PreparedStatement preparedStatement = con.prepareStatement(queryInsert);           
            preparedStatement.setInt(1, p.getNbRooms());
            preparedStatement.setInt(2, p.getPrice());            
            preparedStatement.setString(3, p.getDescription());
            preparedStatement.setString(4, PhpSerialize.serialize(p.getEquipements()));
            preparedStatement.setString(5, PhpSerialize.serialize(p.getImagesPath()));            
            preparedStatement.setString(6, p.getLocation());
            preparedStatement.setBoolean(7, p.isReported());            
            preparedStatement.setBoolean(8, p.isEnabled());
            preparedStatement.setInt(9, p.getId()); 
            preparedStatement.executeUpdate();  
            System.out.println("Property Updated !");            
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<Property> getAllProperty() {
        ArrayList<Property> listResult = new ArrayList<Property>();
        try {
            String querySelect = "SELECT * FROM property";
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                MixedArray listEquip;
                MixedArray listImg;
                listEquip = Pherialize.unserialize(rs.getString("equipements")).toArray();
                listImg = Pherialize.unserialize(rs.getString("images_path")).toArray();
                Property p = new Property();
                p.setId(rs.getInt("id"));
                p.setHost_id(rs.getInt("host_id"));
                p.setPrice(rs.getInt("price"));
                p.setNbRooms(rs.getInt("nb_rooms"));
                p.setLocation(rs.getString("location"));
                p.setPublicationDate(rs.getDate("publication_date"));
                p.setDescription(rs.getString("description"));
                p.setEquipements(new ArrayList(listEquip.values()));
                p.setImagesPath(new ArrayList(listImg.values()));
                p.setEnabled(rs.getBoolean("enabled"));
                p.setReported(rs.getBoolean("reported"));
                listResult.add(p);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("getAllProperty 100% !");        
        return listResult;
    }

    @Override
    public ArrayList<Property> getUserProperty(User u) {
        ArrayList<Property> listResult = new ArrayList<Property>();
        try {
            String querySelect = "SELECT * FROM property where host_id = " + u.getId();
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                MixedArray listEquip;
                MixedArray listImg;
                listEquip = Pherialize.unserialize(rs.getString("equipements")).toArray();
                listImg = Pherialize.unserialize(rs.getString("images_path")).toArray();
                Property p = new Property();
                p.setId(rs.getInt("id"));
                p.setHost_id(rs.getInt("host_id"));
                p.setPrice(rs.getInt("price"));
                p.setNbRooms(rs.getInt("nb_rooms"));
                p.setLocation(rs.getString("location"));
                p.setPublicationDate(rs.getDate("publication_date"));
                p.setDescription(rs.getString("description"));
                p.setEquipements(new ArrayList(listEquip.values()));
                p.setImagesPath(new ArrayList(listImg.values()));
                p.setEnabled(rs.getBoolean("enabled"));
                p.setReported(rs.getBoolean("reported"));
                listResult.add(p);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("getUserProperty 100% !");                
        return listResult;
    }

    @Override
    public void reportProperty(Property p) {
        try {
            String queryReport = "UPDATE property SET reported = " + 1 + " where id =" + p.getId() ;
            PreparedStatement preparedStatement = con.prepareStatement(queryReport);
            preparedStatement.executeUpdate(queryReport);
            System.out.println("Property Reported !"); 
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void hideProperty(Property p) {
        try {
            String queryHide = "UPDATE property SET enabled = " + 0 + " where id =" + p.getId() ;
            PreparedStatement preparedStatement = con.prepareStatement(queryHide);
            preparedStatement.executeUpdate(queryHide);
            System.out.println("Property Hidden !"); 
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Property getPropertyById(int id) {
         Property prop = null ;
        try {
            String querySelect = "SELECT * FROM property where id = " + id;
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                MixedArray listEquip;
                MixedArray listImg;
                listEquip = Pherialize.unserialize(rs.getString("equipements")).toArray();
                listImg = Pherialize.unserialize(rs.getString("images_path")).toArray();
                Property p = new Property();
                p.setId(rs.getInt("id"));
                p.setHost_id(rs.getInt("host_id"));
                p.setPrice(rs.getInt("price"));
                p.setNbRooms(rs.getInt("nb_rooms"));
                p.setLocation(rs.getString("location"));
                p.setPublicationDate(rs.getDate("publication_date"));
                p.setDescription(rs.getString("description"));
                p.setEquipements(new ArrayList(listEquip.values()));
                p.setImagesPath(new ArrayList(listImg.values()));
                p.setEnabled(rs.getBoolean("enabled"));
                p.setReported(rs.getBoolean("reported"));
                prop = p ;
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("get Property Details 100% !");                
        return prop;
    }

    @Override
    public ArrayList<Property> searchProperty(String location) {
        ArrayList<Property> listResult = new ArrayList<Property>();
        try {
            String querySelect = "SELECT * FROM property WHERE location LIKE '%" + location + "%'";
            System.out.println(querySelect);
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                MixedArray listEquip;
                MixedArray listImg;
                listEquip = Pherialize.unserialize(rs.getString("equipements")).toArray();
                listImg = Pherialize.unserialize(rs.getString("images_path")).toArray();
                Property p = new Property();
                p.setId(rs.getInt("id"));
                p.setHost_id(rs.getInt("host_id"));
                p.setPrice(rs.getInt("price"));
                p.setNbRooms(rs.getInt("nb_rooms"));
                p.setLocation(rs.getString("location"));
                p.setPublicationDate(rs.getDate("publication_date"));
                p.setDescription(rs.getString("description"));
                p.setEquipements(new ArrayList(listEquip.values()));
                p.setImagesPath(new ArrayList(listImg.values()));
                p.setEnabled(rs.getBoolean("enabled"));
                p.setReported(rs.getBoolean("reported"));
                listResult.add(p);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return listResult;
    }
    @Override
    public ArrayList<Property> getAllPropertyByCritere(String critere) {
        ArrayList<Property> listResult = new ArrayList<Property>();
        try {
            String querySelect = "SELECT * FROM property " +critere+ "";
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                MixedArray listEquip;
                MixedArray listImg;
                listEquip = Pherialize.unserialize(rs.getString("equipements")).toArray();
                listImg = Pherialize.unserialize(rs.getString("images_path")).toArray();
                Property p = new Property();
                p.setId(rs.getInt("id"));
                p.setHost_id(rs.getInt("host_id"));
                p.setPrice(rs.getInt("price"));
                p.setNbRooms(rs.getInt("nb_rooms"));
                p.setLocation(rs.getString("location"));
                p.setPublicationDate(rs.getDate("publication_date"));
                p.setDescription(rs.getString("description"));
                p.setEquipements(new ArrayList(listEquip.values()));
                p.setImagesPath(new ArrayList(listImg.values()));
                p.setEnabled(rs.getBoolean("enabled"));
                p.setReported(rs.getBoolean("reported"));
                listResult.add(p);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PropertyService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("getAllProperty 100% !");        
        return listResult;
    }
}
