package com.example.demo.Service;

import com.example.demo.Modal.Email;

import jakarta.mail.MessagingException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;




public interface EmailService {



    void sendEmail(Email email) throws MessagingException, MessagingException;


    void order_email(Email email, ObjectId id) throws MessagingException;
}
