/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.hostandguest.connection.MyConnection;
import com.hostandguest.entities.Booking;
import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import com.hostandguest.interfaces.IBooking;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toshiba
 */
public class BookingService implements IBooking {
    private final Connection connection = MyConnection.getInstance().getConnection();
    private PreparedStatement pst;
    
    @Override
    public int add(Booking booking, boolean isGift) {
        try
        {
            String req = "insert into booking values (null, ?, ?, ?, 0, ?, ?, ?, 0)";
            
            if (isGift)
                req = "insert into booking (id, property_id, isGift, term, reviewed, booking_date, nb_reserved_rooms, "
                        + "total_amount) values (null, ?, ?, ?, 0, ?, ?, ?)";
                
            pst = connection.prepareStatement(req);
             
            // booking date verfication (superior than current date) is done in the concerned fx controller
            pst.setInt(1, booking.getProperty().getId());
            
            if (!isGift)
                pst.setInt(2, booking.getGuest().getId());
            else
                pst.setInt(2, 1);
            
            pst.setInt(3, booking.getTerm());
            pst.setDate(4,new Date(booking.getBookingDate().getTime()));
            pst.setInt(5,booking.getNbr_rooms_reserved());
            pst.setInt(6, (int) booking.getTotal_amount());
                
            if (pst.executeUpdate() > 0)
                return 0;
             
         }
        catch (SQLException ex)
        {
            Logger.getLogger(BookingService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 1;
    }
    
    /**
     * 
     * @param property_id
     * @param bookingDate
     * @param term
     * @param updateRoom used when updating a booking 
     * @param booking_id can be null only in case updateRoom is false 
     * @return 
     */
    public int getFreeRooms(int property_id, Date bookingDate, int term, boolean updateRoom, Integer booking_id)
    {
        String getNbRooms = "SELECT (p.nb_rooms - COALESCE(SUM(b.nb_reserved_rooms), 0)) as FreeRooms\n" +
                            "FROM booking b, property p\n" +
                            "WHERE b.property_id = p.id\n" +
                            "AND (booking_date BETWEEN ? AND DATE_ADD(?,INTERVAL ? DAY))\n" +
                            "AND p.id = ?\n";
        
        if (updateRoom)
            getNbRooms += "AND b.id != ?";
        
        try
        {
            pst = connection.prepareStatement(getNbRooms);
            
            pst.setDate(1, bookingDate);
            pst.setDate(2, bookingDate);
            pst.setInt(3, term);
            pst.setInt(4, property_id);
            
            if (updateRoom)
            {
                if (booking_id == null)
                    throw new NullPointerException("null booking id");
                
                pst.setInt(5, booking_id);
            }
            
            ResultSet resultSet = pst.executeQuery();
            
            if (resultSet.next())
                return resultSet.getInt(1);
        }
        catch (SQLException | NullPointerException ex)
        {
            Logger.getLogger(BookingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    @Override
    public int UpdateBooking(Booking b) {
        try
        {
            // verif booking before update if nothing changed no updates 
            // this is due to sql inserting new line when no data changed and returning 0 rows affected
            Booking bookingInDB = findBy(b.getId());
            
            // declared equals method in booking based on nb_reserved_rooms, term and booking_date
            if (bookingInDB.equals(b))
                return 2;
                
            String req="update booking set term=?, booking_date=?, nb_reserved_rooms=?, total_amount=? where id=?";
            
            pst=connection.prepareStatement(req);
            pst.setInt(1,b.getTerm());
            pst.setDate(2,new Date(b.getBookingDate().getTime()));
            pst.setInt(3,b.getNbr_rooms_reserved());
            pst.setInt(4, (int) b.getTotal_amount());
            pst.setInt(5, b.getId());
             
            if (pst.executeUpdate() > 0)
                return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(BookingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 1;
    }
    
    @Override
    public Booking findBy(int bookingID)
    {
        Booking booking = new Booking();
         
        try
        {
            String getQuery = "select * from booking where id = ?";
            
            pst = connection.prepareStatement(getQuery);
            pst.setInt(1, bookingID);
            
            ResultSet resultSet = pst.executeQuery();
            
            if (resultSet.next())
            {
                booking.setNbr_rooms_reserved(resultSet.getInt("nb_reserved_rooms"));
                booking.setTerm(resultSet.getInt("term"));
                booking.setBookingDate(resultSet.getDate("booking_date"));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(BookingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return booking;
    }
    
    @Override
    public boolean removeById(int id) {
         try {
             String req="DELETE FROM booking WHERE id=?";
             pst=connection.prepareStatement(req);
             pst.setInt(1,id);
             pst.executeUpdate();
             return true;
         } catch (SQLException ex) {
             Logger.getLogger(BookingService.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         return false;
    }
    
    @Override
    public List<Booking> findAll(User currentUser, boolean showGiftList, int property_id) {
        List<Booking> listBooking=new ArrayList<>();
              
       try {
            String req ="select b.id, property_id, term, booking_date, nb_reserved_rooms, total_amount, first_name, last_name "
                    + "from booking b, fos_user u "
                    + "where guest_id = u.id and guest_id = ?";
            
            if (showGiftList)
                req ="select id, property_id, term, booking_date, nb_reserved_rooms, total_amount "
                    + "from booking b "
                    + "where isGift = 1 and property_id = ?";
            
             pst=connection.prepareStatement(req);
             
             if (!showGiftList)
                pst.setInt(1, currentUser.getId());
             else
                 pst.setInt(1, property_id);
             
             ResultSet resultat = pst.executeQuery();
             
             while(resultat.next())
             {
                 Booking b= new Booking();
                 Property property = new Property();
                 User user = new User();
                 
                 property.setId(resultat.getInt("property_id"));
                 
                 if (!showGiftList)
                 {
                     user.setFirst_name(resultat.getString("first_name"));
                     user.setLast_name(resultat.getString("last_name"));
                 }
                 
                 b.setId(resultat.getInt("id"));
                 b.setProperty(property);
                 b.setGuest(user);
                 
                 b.setTerm(resultat.getInt("term"));
                 b.setNbr_rooms_reserved(resultat.getInt("nb_reserved_rooms"));
                 b.setTotal_amount(resultat.getFloat("total_amount"));
                 b.setBookingDate(resultat.getDate("booking_date"));
                 
                 listBooking.add(b);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(BookingService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listBooking;  
   }

    @Override
    public boolean AcquireGift(Booking booking) {
        try
        {
            String acquireQuery = "UPDATE Booking SET guest_id = ?, isGift = 0 where id = ?";
            
            pst = connection.prepareStatement(acquireQuery);
            
            pst.setInt(1, booking.getGuest().getId());
            pst.setInt(2, booking.getId());
            
            if (pst.executeUpdate() > 0)
                return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(BookingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
