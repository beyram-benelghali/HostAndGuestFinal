/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.services;

import com.hostandguest.connection.MyConnection;
import com.hostandguest.entities.Message;
import com.hostandguest.entities.User;
import com.hostandguest.interfaces.IMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author amri04
 */
public class MessageService implements IMessage{

    private static String SMTP_HOST1="smtp.gmail.com";
    private static String LOGIN_SMTP1="marouane.amri@esprit.tn";
    private static String IMAP_ACCOUNT1="imap.gmail.com";
    private static String PASSWORD_SMTP1="Esprit1893";

    private Connection con = null;
    
    public MessageService() {
        con = MyConnection.getInstance().getConnection();
    }
    
    
    
    @Override
    public void addMessage(Message message, int receiver) {
        try {
            int threadId =getThreadId(message.getSender_id(), receiver);
            if(threadId == -1){
                newThread(message.getSender_id(), receiver);
                threadId =getThreadId(message.getSender_id(), receiver);
            }
            
            String query = "Insert Into  Message (thread_id,sender_id,body,created_at) VALUES (?,?,?,?) ";
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, threadId);
            preparedStatement.setInt(2, message.getSender_id());
            preparedStatement.setString(3, message.getBody());
            preparedStatement.setDate(4,new java.sql.Date(new Date().getTime()));
            preparedStatement.executeUpdate(); 

            /***** Message Metedata **************/
            int messageId =getLastMessageId(message.getSender_id());
            String queryMessageMetadata = "Insert Into  message_metadata (message_id,participant_id,is_read) VALUES (?,?,?) ";
            PreparedStatement preparedStatement2 = con.prepareStatement(queryMessageMetadata);
            preparedStatement2.setInt(1, messageId);
            preparedStatement2.setInt(2, message.getSender_id());
            preparedStatement2.setBoolean(3,false);
            preparedStatement2.executeUpdate(); 
            preparedStatement2.setInt(1, messageId);
            preparedStatement2.setInt(2, receiver);
            preparedStatement2.setBoolean(3,false);
            preparedStatement2.executeUpdate(); 
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

    @Override
    public ArrayList<Message> getAllMessage(int user1,int user2) {
        ArrayList<Message> listResult = new ArrayList<Message>();
        int threadId = getThreadId(user1,user2);
        if(threadId != -1){
        try {     
            String querySelect = "SELECT * FROM message WHERE thread_id ="+threadId+" AND (sender_id="+user1+" OR sender_id ="+user2+")";
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                Message m = new Message();
                m.setId(rs.getInt("id"));
                m.setBody(rs.getString("body"));
                m.setThread_id(rs.getInt("thread_id"));
                m.setSender_id(rs.getInt("sender_id"));
                m.setCreated_at(rs.getDate("created_at"));
                listResult.add(m);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }                     
        return listResult;
    }
        return null;
    }


    @Override
    public void newThread(int createdBy,int receiver) {
        try {
            String query = "Insert Into  Thread (created_by_id,subject,created_at,is_spam) VALUES (?,?,?,?) ";
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, createdBy);
            preparedStatement.setString(2, createdBy +"_"+receiver);     
            preparedStatement.setDate(3, new java.sql.Date(new Date().getTime()));
            preparedStatement.setBoolean(4,false);
            preparedStatement.executeUpdate(); 
            
            int threadId = getThreadId(createdBy, receiver);
            
            String queryThreadMetadata = "Insert Into  thread_metadata (thread_id,participant_id,is_deleted) VALUES (?,?,?) ";
            PreparedStatement preparedStatement2 = con.prepareStatement(queryThreadMetadata);
            preparedStatement2.setInt(1, threadId);
            preparedStatement2.setInt(2,receiver);     
            preparedStatement2.setBoolean(3,false); 
            preparedStatement2.executeUpdate(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getThreadId(int createdBy, int receiver) {
        try {
            String querySelectId = "select id from  Thread where subject = '"+createdBy +"_"+receiver+"' OR subject ='"+receiver +"_"+createdBy+"'";
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(querySelectId);
            if(rs.first()){
            return rs.getInt("id");
            }
            else
                return -1;
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
       
     }

    @Override
    public int getLastMessageId(int sender) {
        try {
            String query = "Select id from Message where sender_id ="+sender+" order by id Desc LIMIT 1";
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt("id");
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        }

    @Override
    public ArrayList<User> getUserList(int userId) {
        UserService us = new UserService();
        ArrayList<User> userList = new ArrayList<User>();
        try {
            String query = "SELECT trim(leading'"+userId+"_' FROM subject) FROM hostnguest.thread  where subject like '"+userId+"\\_%'  UNION SELECT trim(trailing'_"+userId+"' FROM subject) FROM hostnguest.thread  where subject like '%\\_"+userId+"'";
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                userList.add(us.getUserById(rs.getInt(1)));
            }
            rs.close();
            return userList;
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // Java Mail API
    @Override
    public int sendMail(String subject, String text, String destinataire, String copyDest) {

       Properties props = new Properties();  
       props.setProperty("mail.transport.protocol", "smtp");     
       props.setProperty("mail.host", "smtp.gmail.com");  
       props.put("mail.smtp.auth", "true");  
       props.put("mail.smtp.port", "465");  
       props.put("mail.debug", "true");  
       props.put("mail.smtp.socketFactory.port", "465");  
       props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
       props.put("mail.smtp.socketFactory.fallback", "false");  
       Session session = Session.getDefaultInstance(props,  
       new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {  
          return new PasswordAuthentication(LOGIN_SMTP1,PASSWORD_SMTP1);  
      }  
      });  
   // Création du message
        MimeMessage message = new MimeMessage(session);
    try {
        message.setText(text);
        message.setSubject(subject);
        message.addRecipients(javax.mail.Message.RecipientType.TO, destinataire);
       if(!"".equals(copyDest)) message.addRecipients(javax.mail.Message.RecipientType.CC, copyDest);
    } catch (MessagingException e) {
        e.printStackTrace();
        return -1;
    }

    //  Envoi du message
        Transport transport = null;
    try {
        transport = session.getTransport("smtp");
        transport.connect(LOGIN_SMTP1, PASSWORD_SMTP1);
        if("".equals(copyDest)){
                    transport.sendMessage(message, new Address[] { new InternetAddress(destinataire) });
        }
        else{
            transport.sendMessage(message, new Address[] { new InternetAddress(destinataire),
                                           new InternetAddress(copyDest) });            
        }

    } catch (MessagingException e) {
        e.printStackTrace();
        return -1;
    } finally {
        try {
            if (transport != null) {
                transport.close();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            return -1;
        }
    }
    return 1;
}

    @Override
    public void isReadTrue(int user1, int user2) {
        try {
            String messageIdList="";
            for(Message mess : this.getAllMessage(user1, user2)){
                    messageIdList += mess.getId()+",";
            }
            messageIdList=messageIdList.substring(0,messageIdList.length()-1);
            System.out.println(messageIdList);
            String query = "UPDATE message_metadata SET  is_read=1 WHERE message_id IN ("+messageIdList+" ) AND participant_id !="+User.currentUser+" ";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int countUnreadMessages(int user1, int user2) {
         try {
            String messageIdList="";
            for(Message mess : this.getAllMessage(user1, user2)){
                if(mess.getSender_id()!=User.currentUser){
                    messageIdList += mess.getId()+",";
                }  
            }
            if(messageIdList!="")messageIdList=messageIdList.substring(0,messageIdList.length()-1);
            
            String query = "SELECT Count(*) FROM  message_metadata WHERE is_read=0 AND message_id IN ("+messageIdList+" ) AND participant_id !="+User.currentUser+" ";
                        System.out.println(query); 
            Statement  stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }       
    }
    
    
}
