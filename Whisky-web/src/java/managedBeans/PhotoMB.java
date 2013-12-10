/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entity.Lecture;
import entity.Participant;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
@Named(value = "photoMB")
@RequestScoped
public class PhotoMB {
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
         
        if (photoManagementSB.save_predict(foto, idParticipant)){
            
            actualParticipant=participantManagementSB.getParticipant(idParticipant);
            actualLecture=lectureManagementSB.getLecturebyId(idLecture);
            attendanceManagementSB.addAttendance(actualParticipant, actualLecture);
            System.out.println("yey te reconozco");
            //Crear la clase con la foto, y notificar que el wn está logeado
        
        }
        else{
            System.out.println("No te reconozco");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o contraseña incorrecta", "Login inválido"));
        }
       
        
        
       
        
        
        
        }
}
