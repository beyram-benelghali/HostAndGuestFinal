/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.interfaces;
import com.hostandguest.entities.User;
import com.hostandguest.entities.Property;
import java.util.List;

/**
 *
 * @author The
 */
public interface IUser {
    public List listuser (User u);
    public void ajoutuser (User u);
    public void banuser (User u);
    public void updateuser (User u);
    public void hideproprety (Property p);
    public void delete (User u);
    public List<User> RechercheUsers(String critere);
    public User getUserById(int userId) ;
    public User getUserByUsername(String username);
    
    
}
