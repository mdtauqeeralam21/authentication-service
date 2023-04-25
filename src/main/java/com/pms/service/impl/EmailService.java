package com.pms.service.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.pms.exception.AuthenticationServiceException;

@Service
public class EmailService {
	
	
	public boolean sendEmail(String subject, String message, String to) throws AuthenticationServiceException {
		
		String from = "revaturetech2023@gmail.com";
		
		boolean flag = false;
		
		
		// variable for gmail
		
		String host = "smtp.gmail.com";
		
		//get the system properties
		
		Properties properties = System.getProperties();
		System.out.println("Properties : "+properties);
		
		//setting important information to properties object
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", 465);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		//to get the session object
		 Session session = Session.getInstance(properties, new Authenticator() {
			 
			 @Override
			 protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication("revaturetech2023@gmail.com", "fskxyflpatiaprdu");
				 
			 }
		});
		session.setDebug(true);
		
		//compose the message 
		
		MimeMessage mimeMessage = new MimeMessage(session);
		
		try {		
			
			// from email
			mimeMessage.setFrom(from);
			
			//adding recepient to message			
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			
			//adding subject tomessage
			mimeMessage.setSubject(subject);
			
			
			//adding text to message
			//mimeMessage.setText(message);
			mimeMessage.setContent(message,"text/html");
			
			//send the message using Transport class
			Transport.send(mimeMessage);
			
			System.out.println("Sent success...");
			
			flag=true;
			
			
		} catch (Exception e) {
			throw new AuthenticationServiceException("Message not sent");
			
		}
		
		
		return flag;
		
	}
	

}
