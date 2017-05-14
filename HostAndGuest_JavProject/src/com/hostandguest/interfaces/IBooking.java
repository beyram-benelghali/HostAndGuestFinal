/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.interfaces;

import com.hostandguest.entities.Booking;
import com.hostandguest.entities.User;
import java.util.List;

/**
 *
 * @author The
 */
public interface IBooking {
    public List<Booking> findAll(User currentUser, boolean showGiftList, int property_id);
    public int add(Booking b, boolean isGift);
    public boolean removeById(int id);
    public boolean AcquireGift(Booking booking);
    public Booking findBy(int bookingID);
    /**
     * @param b
     * @return 0 for success, 1 for error, 2 for equals
     */
    public int UpdateBooking(Booking b);
}
