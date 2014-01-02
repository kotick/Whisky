/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import JpaControllers.AttendanceJpaController;
import classes.photoConfirmation;
import entity.Attendance;
import entity.Lecture;
import entity.Participant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import org.primefaces.event.CaptureEvent;
import sessionBeans.AttendanceManagementSBLocal;
import sessionBeans.LectureManagementSBLocal;
import sessionBeans.ParticipantManagementSBLocal;
import sessionBeans.PhotoManagementSBLocal;
import sessionBeans.exceptions.NonexistentEntityException;
import sessionBeans.exceptions.RollbackFailureException;

/**
 *
 * @author Kay
 */
@Named(value = "photoMB")
@RequestScoped
public class PhotoMB {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    @EJB
    private AttendanceManagementSBLocal attendanceManagementSB;
    @EJB
    private LectureManagementSBLocal lectureManagementSB;
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    @Inject PhotoConversationMB photoConversationMB;
    @Inject SessionMB session;
    @EJB
    private PhotoManagementSBLocal photoManagementSB;
    private AttendanceJpaController attendanceJpa;
    private Lecture actualLecture;
    private Participant actualParticipant;  
    private Long idLecture;
    private Long idParticipant;
    public PhotoMB() {
    }
    @PostConstruct
    void init(){
       idLecture = photoConversationMB.getIdLecture();
       idParticipant = photoConversationMB.getIdParticipant();
        //this.id = 1;
        //System.out.println(id+" id recibido de la lista");
        
    }
     public void oncapture(CaptureEvent captureEvent) {  
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("entro a oncapture");
         byte[] foto = captureEvent.getData();
        
        
        photoConfirmation confirmacion = photoManagementSB.save_predict(foto, idParticipant);
         
        if (confirmacion.isValidado()){
            
            actualParticipant=participantManagementSB.getParticipant(idParticipant);
            actualLecture=lectureManagementSB.getLecturebyId(idLecture);
           // attendanceManagementSB.addAttendance(actualParticipant, actualLecture);
            attendanceJpa = new AttendanceJpaController(utx,emf);
            Attendance newAttendance= new Attendance();
            newAttendance.setLecture(actualLecture);
            newAttendance.setParticipant(actualParticipant);
            newAttendance.setPhoto(confirmacion.getDireccionFoto());
            newAttendance.setPresent(true);
            try {
                attendanceJpa.edit(newAttendance);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(PhotoMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(PhotoMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PhotoMB.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("te reconocí");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Estás presente","Registro con exito"));
            session.redirect("/faces/teacher/attendance.xhtml");
            
            //Crear la clase con la foto, y notificar que el wn está logeado
            
        
        }
        else{
            System.out.println("No te reconocí");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no reconocido facialmente","Inténtalo de nuevo"));
            session.redirect("/faces/teacher/attendance.xhtml");
           
        }
       
        
        
       
        
        
        
        }
}
