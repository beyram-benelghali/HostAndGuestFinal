/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.interfaces;

import com.hostandguest.entities.Property;
import com.hostandguest.entities.User;
import java.util.ArrayList;

/**
 *
 * @author The
 */
public interface IProperty {
    public void addProperty(Property p);
    public void removeProperty(Property p);
    public void updateProperty(Property p);
    public ArrayList<Property> searchProperty(String location);
    public ArrayList<Property> getAllProperty();
    public ArrayList<Property> getUserProperty(User u);
    public void reportProperty(Property p);
    public void hideProperty(Property p);
    public Property getPropertyById (int id);
    public ArrayList<Property> getAllPropertyByCritere(String orderBy);
}
