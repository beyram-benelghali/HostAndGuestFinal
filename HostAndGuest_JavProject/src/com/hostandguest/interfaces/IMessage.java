/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.interfaces;

import com.hostandguest.entities.Message;
import com.hostandguest.entities.User;
import java.util.ArrayList;

/**
 *
 * @author The
 */
public interface IMessage {
    public void newThread(int createdBy,int receiver);
    public int getThreadId(int createdBy,int receiver);
    public void addMessage(Message message, int receiver);
    public int getLastMessageId(int sender);
    public ArrayList<Message> getAllMessage(int user1, int user2);
    public ArrayList<User> getUserList(int user);
    public int sendMail(String subject, String text, String destinataire, String copyDest);
    public void isReadTrue(int user1, int user2);
    public int countUnreadMessages(int user1, int user2);
}