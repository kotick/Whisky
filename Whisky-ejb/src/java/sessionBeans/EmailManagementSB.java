/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.ParticipantDTO;
import classes.Report;
import java.net.PasswordAuthentication;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.ejb.Schedule;
import com.sun.mail.iap.Protocol;
import entity.Course;
import entity.Participant;
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
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sessionBeans.studentStatisticsSBLocal;

/**
 *
 * @author Valeria
 */
@Stateless
public class EmailManagementSB implements EmailManagementSBLocal {
    @EJB
    private CourseManagementSBLocal courseManagementSB;

    @EJB
    private studentStatisticsSBLocal staticsSB;
    @Resource(name = "mail/sending")
    private String myEmail = "valeria.asencio.c@gmail.com";
    private String myPass = "hermosapie";
    private String servidorSMTP = "smtp.gmail.com";
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;
    private String puertoEnvio = "465";

    @Override
    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        //System.out.println("Estoy en la wea");

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
    @Schedule(dayOfWeek = "Sun", hour = "7")
    public void hunter() {
        System.out.println("Esto es un print");
        long idParticipant;
        long idCourse;
        String email;
        String body;
        
        Collection<ParticipantDTO> participants = getAllByRol("Student");
        Collection<Course> coursesTemp;
        
        for (ParticipantDTO iter : participants) {
            Collection<Report> collectionReport = new LinkedList<>();
            coursesTemp=iter.getCourses();
            email=iter.getEmail();

            
            for(Course course : coursesTemp){
                Report tempReport = new Report();

                idParticipant= iter.getId();
                idCourse= course.getId();// id del course
                tempReport.setCourse(courseManagementSB.getCourse(idCourse).getName());
                tempReport.setPercent(String.valueOf(staticsSB.getPercetageAssistance(idCourse, idParticipant)));  
                collectionReport.add(tempReport);
           
            }
            body=constructBody(collectionReport);
            try {
                sendEmail(email, "Reporte de asistencia sistema Whisky", body);
            } catch (MessagingException ex) {
                Logger.getLogger(EmailManagementSB.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
            
            
        }
    }
    public String constructBody(Collection<Report> data){
        String body;
        body = "Estimado alumno, tus porcentajes de asistencia registrados en nuestro sistema son:\n";
        for(Report t: data){
            body=body.concat(t.getCourse()).concat("\t").concat(t.getPercent()).concat("% \n");
        }
        body=body.concat("\n\n Saludos Cordiales.");
        System.out.println(body);
        return body;
    
    }

    public Collection<ParticipantDTO> getAllByRol(String rol) {

        Collection<Participant> resultQuery;
        Collection<ParticipantDTO> result = new LinkedList<ParticipantDTO>();
        ParticipantDTO participantDTOTemp;

        Query q = em.createNamedQuery("Participant.getAllByType", Participant.class);
        q.setParameter("rol", rol);

        resultQuery = (Collection<Participant>) q.getResultList();

        for (Participant iter : resultQuery) {
            participantDTOTemp = new ParticipantDTO();
            participantDTOTemp.setFirstName(iter.getFirstName());
            participantDTOTemp.setLastName(iter.getLastName());
            participantDTOTemp.setId(iter.getId());
            participantDTOTemp.setRut(iter.getRut());
            participantDTOTemp.setEmail(iter.getEmail());
            participantDTOTemp.setCourses(iter.getCourses());
            result.add(participantDTOTemp);
        }
        return result;
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
