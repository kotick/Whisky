/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.AttendanceDTO;
import DTOs.ParticipantDTO;
import JpaControllers.ParticipantJpaController;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.transaction.UserTransaction;
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

    @Inject
    PhotoConversationMB photoConversation;
    @Inject
    AttendanceConversationMB attendanceConversation;
    @Inject
    SessionMB session;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    @EJB
    private UtilitiesSBLocal utilitiesSB;
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    @EJB
    private PhotoManagementSBLocal photoManagementSB;
    private ParticipantJpaController participantJpa;
    private Collection<AttendanceDTO> attendanceList;
    private List<AttendanceDTO> filteredAttendances;
    private Long idLecture;
    private Long idCourse;
    private Long idParticipant;
    private String email;
    private String password;

    /**
     * Creates a new instance of AttendanceMB
     */
    public AttendanceMB() {
    }

    @PostConstruct
    void init() {
        idLecture = attendanceConversation.getIdLecture();
        idCourse = attendanceConversation.getIdCourse();


    }

    public void checkEmailPassword() {
        Collection<ParticipantDTO> participants;
        boolean contain = false;
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            participantJpa = new ParticipantJpaController(utx, emf);
            participants = participantJpa.getParticipantInClass(idCourse, "Student");
            for (ParticipantDTO iter : participants) {
                System.out.println(iter.getEmail());
                if (iter.getEmail().equalsIgnoreCase(email)) {
                    
                    contain = true;
                    break;
                }

            }
            if (contain) {
                if (participantManagementSB.checkEmailPassword(email, password)) {
                    idParticipant = utilitiesSB.selectFirstIdByEmail(email);
                    letsGoToTakePhoto();
                } else {
                    System.out.println("Usuario o contraseña incorrectos");
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o contraseña incorrecta", "Login inválido"));

                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no pertenece a este curso", "Login inválido"));

            }

        } catch (Exception e) {
            System.out.println("Usuario o contraseña incorrectos");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o contraseña incorrecta", "Login inválido"));
        }


    }

    public void letsGoToTakePhoto() {
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
