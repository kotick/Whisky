/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.AttendanceDTO;
import java.io.File;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import sessionBeans.PhotoManagementSBLocal;
import org.primefaces.event.CaptureEvent;
import sessionBeans.ParticipantManagementSBLocal;
import sessionBeans.UtilitiesSBLocal;
import managedBeans.Utilities;

/**
 *
 * @author Kay
 */
@Named(value = "attendanceMB")
@RequestScoped
public class AttendanceMB {
    @Inject PhotoConversationMB photoConversation;
    @Inject AttendanceConversationMB attendanceConversation ;
    @Inject SessionMB session;
    @EJB
    private UtilitiesSBLocal utilitiesSB;
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    @EJB
    private PhotoManagementSBLocal photoManagementSB;
    private Long id;
    private String email;
    private String password;
    
    /**
     * Creates a new instance of AttendanceMB
     */
    public AttendanceMB() {
    }
    @PostConstruct
    void init(){
        id = attendanceConversation.getId();
        
    }
    
    public void checkEmailPassword(){
    
        System.out.println(email);
        System.out.println(password);
        FacesContext context = FacesContext.getCurrentInstance();
        if(participantManagementSB.checkEmailPassword(email, password)){
            id = utilitiesSB.selectFirstIdByEmail(email);
                //Comprobar si el wn que se logea pertenece al curso
                // Si pertenece al curso, que se vaya a la otra vista de la foto
                //sino que te diga que no pertenece al curso
            letsGoToTakePhoto();
        }
       else{
           System.out.println("Usuario o contraseña incorrectos");
           context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o contraseña incorrecta", "Login inválido"));

       }
        
        
    }
    
    
    public void letsGoToTakePhoto(){
        this.photoConversation.beginConversation();
        this.photoConversation.setId(id);
        session.redirect("/faces/teacher/photo.xhtml?cid=".concat(this.photoConversation.getConversation().getId().toString()));
    }
    
   

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    }

