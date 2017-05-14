/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.interfaces;

import com.hostandguest.entities.Booking;
import com.hostandguest.entities.Review;
import com.hostandguest.entities.User;
import com.hostandguest.util.Satisfaction;
import java.io.File;
import java.util.List;

/**
 * 
 */
public interface IReview {
    /**
     * the concerned user to the review is attached to the review instance
     * @param review
     * @param video file provided by user
     * @return true if the insertion was successful, false if not, false also covers any exception / check that occured
     */
    public boolean addReview(Review review, File video);
    
    /**
     * uploads a file to the web folder using Apache HttpComponents
     * @param video video file to upload
     * @param user_id used for unique file name
     * @return the url of the video
     */
    public String uploadFile(File video, int user_id);
    
    /**
     * current user is needed to make sure its the user who owns the review is the one calling this
     * @param review_id 
     * @param current_user the currently connected user
     * @param videoUrl
     * @return true if deleting was successful, false if not, false also covers any exception / check that occured
     */
    public boolean deleteReview(int review_id, User current_user, String videoUrl);
    /**
     * @param property_id
     * @param orderBy optional clause ordering based on user selection
     * @return list of reviews based on the given property id
     */
    public List<Review> getReviewsOfProperty(int property_id, String orderBy);
    /**
     * checks the interval between the booking date and the current date
     * @param property_id
     * @param user_id
     * @return the reservations that the user is allowed to use to review a property
     */
    public List<Booking> getPropertyConcernedReservationList(int property_id, int user_id);
    /**
     * Analyses the words using a dictionary and determines the user mood
     * @param comment the user comment to analyse
     * @return satisfaction level and the mood type
     */
    public Satisfaction determineSatisfactionLevel(String comment);
}
