/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.AttendanceDTO;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import sessionBeans.PhotoManagementSBLocal;
import sessionBeans.ParticipantManagementSBLocal;
import sessionBeans.UtilitiesSBLocal;

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
    private Collection<AttendanceDTO> attendanceList;
    private List<AttendanceDTO> filteredAttendances;
    private Long idLecture;
    private Long idParticipant;
    private String email;
    private String password;
    
    /**
     * Creates a new instance of AttendanceMB
     */
    public AttendanceMB() {
    }
    @PostConstruct
    void init(){
        idLecture = attendanceConversation.getId();
        
        
    }
    
    public void checkEmailPassword(){
    
        System.out.println(email);
        System.out.println(password);

        FacesContext context = FacesContext.getCurrentInstance();
        try{
         if(participantManagementSB.checkEmailPassword(email, password)){
            idParticipant = utilitiesSB.selectFirstIdByEmail(email);
            letsGoToTakePhoto();
        }

            else{
           System.out.println("Usuario o contraseña incorrectos");
           context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o contraseña incorrecta", "Login inválido"));

       }

        }

        catch(Exception e){
          System.out.println("Usuario o contraseña incorrectos");
          context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o contraseña incorrecta", "Login inválido"));
        }

        
    }
    
    
    public void letsGoToTakePhoto(){
        this.photoConversation.beginConversation();
        this.photoConversation.setIdParticipant(idParticipant);
        this.photoConversation.setIdLecture(idLecture);
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

    public Collection<AttendanceDTO> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(Collection<AttendanceDTO> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public List<AttendanceDTO> getFilteredAttendances() {
        return filteredAttendances;
    }

    public void setFilteredAttendances(List<AttendanceDTO> filteredAttendances) {
        this.filteredAttendances = filteredAttendances;
    }
    
    }

