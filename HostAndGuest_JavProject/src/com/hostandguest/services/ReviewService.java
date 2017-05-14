package com.hostandguest.services;

import com.hostandguest.connection.MyConnection;
import com.hostandguest.entities.Booking;
import com.hostandguest.entities.Review;
import com.hostandguest.entities.User;
import com.hostandguest.interfaces.IReview;
import com.hostandguest.util.AnalyticDictionary;
import com.hostandguest.util.Satisfaction;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * 
 */
public class ReviewService implements IReview {
    private final Connection connection = MyConnection.getInstance().getConnection();
    
    @Override
    public boolean addReview(Review review, File video) {
        Date current_date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(current_date.getTime()); 
        
        try
        {
            // due to file upload we need to know the future id of the review to set the url properly
            String getLastId = "SELECT id FROM `review`order by id DESC LIMIT 1";
            
            PreparedStatement preparedStatement = connection.prepareStatement(getLastId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            int futureId = 1;
            
            if (resultSet.next())
                futureId = resultSet.getInt("id") + 1;
            
            
            String uploadedFileUrl = null;
            
            if (video != null)
                uploadedFileUrl = uploadFile(video, futureId);
            
            // case where reservation is already reviwed is taken care of in the 
            // getPropertyConcernedReservationList method
            
            // verify that the user using this is the one owning the reservation
//            if (review.getBooking().getGuest().getId() == current_user.getId())
            {
                // updating reservation to set reviewed attribute to true
                String updateReservationQuery = "UPDATE booking SET reviewed = true WHERE id = ?";
                
                preparedStatement = connection.prepareStatement(updateReservationQuery);
                preparedStatement.setInt(1, review.getBooking().getId());
                
                if (preparedStatement.executeUpdate() > 0)
                {
                    // adding review
                    String insertReviewQuery = "INSERT INTO review VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(insertReviewQuery);
                    preparedStatement.setInt(1, futureId);
                    preparedStatement.setInt(2, review.getBooking().getId());
                    preparedStatement.setString(3, review.getComment());
                    preparedStatement.setString(4, uploadedFileUrl);
                    preparedStatement.setDate(5, sqlDate);
                    preparedStatement.setInt(6, review.getPrice_quality());
                    preparedStatement.setInt(7, review.getLieu());
                    preparedStatement.setInt(8, review.getPrecision());
                    preparedStatement.setInt(9, review.getCommunication());
                    preparedStatement.setInt(10, review.getCleanliness());
                    
                    Satisfaction determineSatisfaction = determineSatisfactionLevel(review.getComment());
                    preparedStatement.setInt(11, determineSatisfaction.getsLevel());
                    preparedStatement.setInt(12, determineSatisfaction.getMood());
                    
                    if (preparedStatement.executeUpdate() > 0)
                        return true;
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ReviewService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    @Override
    public boolean deleteReview(int review_id, User current_user, String videoUrl) {
        String getBookingQuery = "SELECT b.guest_id, b.id FROM review r INNER JOIN booking b ON b.id = r.booking_id WHERE r.id = ?";
        try
        {
            if (deleteUploadedFile(review_id, videoUrl))
            {
                PreparedStatement preparedStatement = connection.prepareStatement(getBookingQuery);
                preparedStatement.setInt(1, review_id);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next())
                {
                    if (resultSet.getInt(1) == current_user.getId())
                    {
                        // updating booking reviwed value
                        String updateBookingQuery = "UPDATE booking SET reviewed = 0 WHERE id = ?";
                        preparedStatement = connection.prepareStatement(updateBookingQuery);
                        preparedStatement.setInt(1, resultSet.getInt(2));
                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0)
                        {
                            // deleting review
                            String deleteReviewQuery = "DELETE FROM review WHERE id = ?";
                            preparedStatement = connection.prepareStatement(deleteReviewQuery);
                            preparedStatement.setInt(1, review_id);
                            rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0)
                                return true;
                        }
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ReviewService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    private boolean deleteUploadedFile(int review_id, String videoUrl)
    {
        try
        {
            if (videoUrl != null)
            {
                HttpClient httpclient = HttpClientBuilder.create().build();
                ArrayList<NameValuePair> postParameters = new ArrayList<>();
                HttpPost httppost = new HttpPost("http://localhost/host_and_guest_webservices/deleteFile.php");

                postParameters.add(new BasicNameValuePair("videoName", String.valueOf(review_id)));

                httppost.setEntity(new UrlEncodedFormEntity(postParameters));

                // Service returns 1 for deleted 0 if not
                HttpResponse response = httpclient.execute(httppost);

                InputStream body = response.getEntity().getContent();
                
                try (Scanner scanner = new Scanner(body))
                {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    if (responseBody.endsWith("1"))
                        return true;
                }
            }
            else
            {
                return true;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(ReviewService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    @Override
    public List<Review> getReviewsOfProperty(int property_id, String orderBy) {
        List<Review> listReviews = new ArrayList<>();
        String query = "SELECT r.id, r.givenComment, r.date_comment, r.price_quality, r.lieu, r.precisionToDescription, r.communication, r.cleanliness, "
                        + "r.satisfaction_level, r.mood, r.video_url, b.id, b.term, b.reviewed, b.booking_date, u.last_name, u.first_name, u.id " +
                        "FROM review r " +
                        "INNER JOIN booking b ON b.id = r.booking_id " +
                        "INNER JOIN fos_user u on u.id = b.guest_id " +
                        "WHERE b.property_id = ? "
                        + orderBy;
        
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, property_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                Review review = new Review();
                Booking booking = new Booking();
                User user = new User();
                
                review.setId(resultSet.getInt(1));
                review.setComment(resultSet.getString(2));
                review.setDateComment(resultSet.getDate(3));
                review.setPrice_quality(resultSet.getInt(4));
                review.setLieu(resultSet.getInt(5));
                review.setPrecision(resultSet.getInt(6));
                review.setCommunication(resultSet.getInt(7));
                review.setCleanliness(resultSet.getInt(8));
                review.setSatisfaction_level(resultSet.getInt(9));
                review.setMood(resultSet.getInt(10));
                review.setVideo_url(resultSet.getString(11));
                
                booking.setId(resultSet.getInt(12));
                booking.setTerm(resultSet.getInt(13));
                booking.setReviewed(resultSet.getBoolean(14));
                booking.setBookingDate(resultSet.getDate(15));
                
                user.setLast_name(resultSet.getString(16));
                user.setFirst_name(resultSet.getString(17));
                user.setId(resultSet.getInt(18));
                
                booking.setGuest(user);
                review.setBooking(booking);
                
                listReviews.add(review);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ReviewService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listReviews;
    }

    @Override
    public List<Booking> getPropertyConcernedReservationList(int property_id, int user_id) {
        List<Booking> listBookings = new ArrayList<>();
        String selectQuery = "SELECT b.* " +
                            "FROM booking b INNER JOIN property p ON p.id = b.property_id " +
                            "WHERE b.reviewed = 0 " +
                            "AND DATE_ADD(booking_date, INTERVAL term DAY) <= CURRENT_DATE() " +
                            "AND b.guest_id = ? " +
                            "AND p.id = ? " +
                            "ORDER BY `b`.`booking_date` DESC";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, property_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                Booking booking = new Booking();
                User user = new User();
                
                user.setId(resultSet.getInt("guest_id"));
                
                booking.setId(resultSet.getInt("id"));
                booking.setGuest(user);
                booking.setTerm(resultSet.getInt("term"));
                booking.setReviewed(resultSet.getBoolean("reviewed"));
                booking.setBookingDate(resultSet.getDate("booking_date"));
                
                listBookings.add(booking);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ReviewService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listBookings;
    }

    @Override
    public Satisfaction determineSatisfactionLevel(String comment) {
        int mood = 0, sLevel = 0, satisfaction = 0;
        int happyLevel = 0, angerLevel = 0, sadnessLevel = 0;
        // if the number of words between the negation and the next word is more than a certain amount 
        // or a dot is found
        // this will consider the negation is about the last emotion word
        int wordGap = 0;
        String lastMatchWord = "";
        boolean negationFound = false;
        // evaluated is used due to the negation
        // valuing each word found that repsents an emotion
        // and later in the negation process
        // if the negation concerns the last word it will reduce the value of the appropriate emotion 
        boolean evaluated = false;
        
        List<String> commentWords = Arrays.asList(comment.split("\\s+"));
        for (String word : commentWords)
        {
            if (AnalyticDictionary.getNEGATION().contains(word))
            {
                negationFound = true;
                wordGap = 0;
                continue;
            }
            
            // marks end of sentence
            if (word.charAt(word.length() - 1) == '.' || word.charAt(0) == '.')
            {
                lastMatchWord = "";
                negationFound = false;
                wordGap = 0;
                
                // if dot is at the beggining of the word we still check the word
                if (word.charAt(0) == '.')
                    word = word.substring(1, word.length());
                else
                    continue;
            }
                
            if (negationFound)
            {   
                // if more than 5 words passed and still no emotion found 
                // this will mark last found word as the one meant for the negation
                // second part of the condition is if the word is the last word in the sentence
                if (wordGap >= 5 || commentWords.indexOf(word) == commentWords.size() - 1)
                {
                    if (AnalyticDictionary.getJOY().contains(lastMatchWord))
                    {
                        happyLevel--;
                        angerLevel++;
                    }
                    else if (AnalyticDictionary.getANGER().contains(lastMatchWord))
                    {
                        angerLevel--;
                        happyLevel++;
                    }
                    else if (AnalyticDictionary.getSADNESS().contains(lastMatchWord))
                    {
                        sadnessLevel--;
                        happyLevel++;
                    }
                    else if (AnalyticDictionary.getJOY().contains(word))
                    {
                        angerLevel++;
                    }
                    else if (AnalyticDictionary.getANGER().contains(word) || AnalyticDictionary.getSADNESS().contains(word))
                    {
                        happyLevel++;
                    }
                    
                    lastMatchWord = "";
                    wordGap = 0;
                    negationFound = false;
                    continue;
                }
                
                // the gap is else below maximum and therefore 
                // check if the word represents an emotion 
                // knowing we're in the negation 
                
                if (AnalyticDictionary.getJOY().contains(word))
                {
                    angerLevel++;
                    
                    negationFound = false;
                    lastMatchWord = "";
                    wordGap = 0;
                }
                else if (AnalyticDictionary.getANGER().contains(word) || AnalyticDictionary.getSADNESS().contains(word))
                {
                    happyLevel++;
                    
                    negationFound = false;
                    lastMatchWord = "";
                    wordGap = 0;
                }
                
                continue;
            }
            
            // evaluating every word that matches an emotion
            // and later on if the gap exceeds the maximum 
            // reduce based on what was leveled up
            if (AnalyticDictionary.getJOY().contains(word))
            {
                lastMatchWord = word;
                happyLevel++;
            }
            else if (AnalyticDictionary.getANGER().contains(word))
            {
                lastMatchWord = word;
                angerLevel++;
            }
            else if (AnalyticDictionary.getSADNESS().contains(word))
            {
                lastMatchWord = word;
                sadnessLevel++;
            }
            
            wordGap++;
        }
        
        // based on the results determine mood and satisfaction level
        // in equality cases it will be undertermined
        
        // undertermined means nothing was evaluated 
        // might be a language barrier or not enough words
        if (happyLevel == 0 & angerLevel == 0 & sadnessLevel == 0)
            return new Satisfaction(0, 0);
        
        if (happyLevel > angerLevel + sadnessLevel)
            mood = 1;
        else if (sadnessLevel > happyLevel && sadnessLevel > angerLevel)
            mood = 2;
        else if (angerLevel > happyLevel && angerLevel > sadnessLevel)
            mood = 3;
        
        // satisfaction represents how high was he satisfied based on the total emotion keywords expressed
        sLevel = (int) ((double) happyLevel / (happyLevel + sadnessLevel + angerLevel) * 9);
        
        return new Satisfaction(mood, sLevel);
    }

    @Override
    public String uploadFile(File video, int user_id) {
        try
        {
            HttpClient httpclient = HttpClientBuilder.create().build();
            
            HttpPost httppost = new HttpPost("http://localhost/host_and_guest_webservices/upload.php");
            
            httppost.setProtocolVersion(new ProtocolVersion("HTTP", 1, 1));            
            
            HttpEntity resEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("userfile", video, ContentType.create("video/mp4"), String.valueOf(user_id) + ".mp4")
                    .build();
            
            httppost.setEntity(resEntity);
            
            httpclient.execute(httppost);
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(ReviewService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return "http://localhost/host_and_guest_webservices/files/" + user_id + ".mp4";
    }

}
