/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import java.net.PasswordAuthentication;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.ejb.Schedule;
import com.sun.mail.iap.Protocol;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import javax.activation.*;
import javax.annotation.PostConstruct;

/**
 *
 * @author Valeria
 */
@Stateless
public class EmailManagementSB implements EmailManagementSBLocal {
    
    
@Resource(name = "mail/sending")
    
    private String myEmail = "valeria.asencio.c@gmail.com";
    private String myPass = "hermosapie";
    private String servidorSMTP = "smtp.gmail.com";
    private String puertoEnvio = "465";
    
    @Override
    public void sendEmail(String toEmail,String subject, String body)throws MessagingException{
    System.out.println("Estoy en la wea");
    
            Properties props = new Properties();
        
        props.put("mail.smtp.user", myEmail);
        props.put("mail.smtp.host", servidorSMTP);
        props.put("mail.smtp.port", puertoEnvio);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", puertoEnvio);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        //SecurityManager security = System.getSecurityManager();
       // Multipart mp = new MimeMultipart();


        
        try {
            Authenticator auth = new autentificadorSMTP();
            
            Session session = Session.getInstance(props, auth);
            //session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            msg.setText(body);
            msg.setSubject(subject);

            
            
            
            msg.setFrom(new InternetAddress(myEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }

    
    }
    private class autentificadorSMTP extends javax.mail.Authenticator {
        @Override
        public javax.mail.PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(myEmail, myPass);
        }
    }
    @PostConstruct
    @Schedule(minute="*/1",hour="*")
    public void hunter(){
        System.out.println("esto es un print");
    }


}
