/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.Local;
import javax.mail.MessagingException;

/**
 *
 * @author Valeria
 */
@Local
public interface EmailManagementSBLocal {

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException;
    
}
