/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.util;

import javafx.stage.Stage;

/**
 *
 * @author Unlucky
 */
public class currentInstance {
    // used to make sure there is only one review window open
    private static Stage reviewStage;
    
    // used after the update to redirect to the same open instance if any
    private static Stage bookingListStage;
    private static Stage bookingAddStage;

    public static Stage getReviewStage() {
        return reviewStage;
    }

    public static Stage getBookingListStage() {
        return bookingListStage;
    }

    public static void setBookingListStage(Stage bookingListStage) {
        currentInstance.bookingListStage = bookingListStage;
    }
   public static void setReviewStage(Stage reviewStage) {
        currentInstance.reviewStage = reviewStage;
    }

    public static Stage getBookingAddStage() {
        return bookingAddStage;
    }

    public static void setBookingAddStage(Stage bookingAddStage) {
        currentInstance.bookingAddStage = bookingAddStage;
    }
}
