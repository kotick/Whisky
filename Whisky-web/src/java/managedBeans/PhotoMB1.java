/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import classes.photoConfirmation;
import entity.Lecture;
import entity.Participant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import org.primefaces.event.CaptureEvent;
import sessionBeans.AttendanceManagementSBLocal;
import sessionBeans.LectureManagementSBLocal;
import sessionBeans.ParticipantManagementSBLocal;
import sessionBeans.PhotoManagementSBLocal;

/**
 *
 * @author Kay
 */
@Named(value = "photoMB1")
@RequestScoped
public class PhotoMB1 {
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
    private Lecture actualLecture;
    private Participant actualParticipant;  
    private Long idLecture;
    private Long idParticipant;
    private FileInputStream imagen;
    public PhotoMB1() {
    }
    @PostConstruct
    void init() {
       idLecture = photoConversationMB.getIdLecture();
       idParticipant = photoConversationMB.getIdParticipant();
      // imagen = photoManagementSB.oli();
        //this.id = 1;
        //System.out.println(id+" id recibido de la lista");
        
    }

    public FileInputStream getImagen() {
        return imagen;
    }

    public void setImagen(FileInputStream imagen) {
        this.imagen = imagen;
    }
     public void oncapture(CaptureEvent captureEvent) {  
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("entro a oncapture");
         byte[] foto = captureEvent.getData();
        long id_bla = 5;
        
        photoConfirmation confirmacion = photoManagementSB.save_predict(foto,id_bla);
         
        if (confirmacion.isValidado()){
            
            actualParticipant=participantManagementSB.getParticipant(idParticipant);
            actualLecture=lectureManagementSB.getLecturebyId(idLecture);
           // attendanceManagementSB.addAttendance(actualParticipant, actualLecture);
            
            attendanceManagementSB.addAttendance(actualParticipant, actualLecture, confirmacion.getDireccionFoto());
            
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
