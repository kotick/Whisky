package managedBeans;

import DTOs.LectureDTO;
import DTOs.ParticipantDTO;
import JpaControllers.AttendanceJpaController;
import JpaControllers.LectureJpaController;
import JpaControllers.ParticipantJpaController;
import entity.Attendance;
import entity.Course;
import entity.Lecture;
import entity.Participant;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
import sessionBeans.CourseManagementSBLocal;
import sessionBeans.LectureManagementSBLocal;

import sessionBeans.ParticipantManagementSBLocal;
import sessionBeans.exceptions.NonexistentEntityException;
import sessionBeans.exceptions.RollbackFailureException;

@Named(value = "lectureMB")
@RequestScoped
public class LectureMB {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    @Inject
    AttendanceConversationMB attendanceConversationMB;
    @Inject
    SessionMB session;
    @EJB
    private CourseManagementSBLocal courseManagementSB;
    @EJB
    private LectureManagementSBLocal lectureManagementSB;
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    @Inject
    LectureConversationMB lectureConversation;
    @Inject
    EditConversationMB editConversation;
    private ParticipantJpaController participantJpa;
    private AttendanceJpaController attendanceJpa;
    private LectureJpaController lectureJpa;
    private Long idCourse;
    private Long idLecture;
    private String date;
    private String startingDate;
    private String finishingDate;
    private ParticipantDTO[] participantsToAdd;
    private Collection<ParticipantDTO> participantList;
    private Collection<LectureDTO> lectureList;
    private List<LectureDTO> filteredLectures;

    public LectureMB() {
    }

    @PostConstruct
    void init() {

        idLecture = lectureConversation.getIdLecture();
        idCourse = lectureConversation.getCourse();
        participantList = participantManagementSB.selectParticipantByLecture(idLecture);

        lectureList = lectureManagementSB.selectLectureByCourses(idCourse);
        attendanceJpa = new AttendanceJpaController(utx, emf);
    }

    public void createLecture(Long idCourse) {
        Course actualCourse = courseManagementSB.getCourse(idCourse);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date date2 = new Date();
        idLecture = lectureManagementSB.createLecture(dateFormat.format(date2), "", "", actualCourse);
        fillLecture(idLecture);
        this.attendanceConversationMB.beginConversation();
        this.attendanceConversationMB.setIdLecture(idLecture);
        this.attendanceConversationMB.setIdCourse(idCourse);
        session.redirect("/faces/teacher/attendance.xhtml?cid=".concat(this.attendanceConversationMB.getConversation().getId().toString()));
    }

    public void addLecture() {
        Participant participantTemp;
        Attendance newAttendance;
        FacesContext context = FacesContext.getCurrentInstance();

        if (date.equalsIgnoreCase("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obligatorios no pueden ser vacíos", "Error al agregar"));

        } else {
            participantJpa = new ParticipantJpaController(utx, emf);
            Course actualCourse = courseManagementSB.getCourse(idCourse);
            idLecture = lectureManagementSB.createLecture(date, startingDate, finishingDate, actualCourse);
            Lecture actualLecture = lectureManagementSB.getLecturebyId(idLecture);
            Collection<ParticipantDTO> participants = participantJpa.getParticipantInClass(idCourse, "Student");
            for (ParticipantDTO iter : participants) {
                newAttendance = new Attendance();
                participantTemp = participantJpa.findParticipant(iter.getId());
                newAttendance.setPresent(false);
                newAttendance.setLecture(actualLecture);
                newAttendance.setParticipant(participantTemp);
                newAttendance.setPhoto(null);
                for (ParticipantDTO present : participantsToAdd) {
                    if (present.getId() == participantTemp.getId()) {
                        newAttendance.setPresent(true);
                    }
                }
                try {
                    attendanceJpa.create(newAttendance);
                    
                } catch (RollbackFailureException ex) {
                    Logger.getLogger(LectureMB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(LectureMB.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            Flash flash = context.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);
            session.redirect("/faces/admin/lectureMaintainer.xhtml");

        }
    }

    public void eraseLecture(Long id) {
        lectureJpa = new LectureJpaController(utx, emf);
        try {
            lectureJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editLecture(Long id) {
        this.editConversation.beginConversation();
        this.editConversation.setIdLecture(id);
        //TODO Cambiar página de direccionamiento
        session.redirect("/faces/admin/editLecture.xhtml?cid=".concat(this.editConversation.getConversation().getId().toString()));

    }

    //Getter and setter
    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getFinishingDate() {
        return finishingDate;
    }

    public void setFinishingDate(String finishingDate) {
        this.finishingDate = finishingDate;
    }

    public ParticipantDTO[] getParticipantsToAdd() {
        return participantsToAdd;
    }

    public void setParticipantsToAdd(ParticipantDTO[] participantsToAdd) {
        this.participantsToAdd = participantsToAdd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Collection<LectureDTO> getLectureList() {
        return lectureList;
    }

    public void setLectureList(Collection<LectureDTO> lectureList) {
        this.lectureList = lectureList;
    }

    public List<LectureDTO> getFilteredLectures() {
        return filteredLectures;
    }

    public void setFilteredLectures(List<LectureDTO> filteredLectures) {
        this.filteredLectures = filteredLectures;
    }

    public void fillLecture(Long idLecture) {
        lectureManagementSB.fillLecture(idLecture);


    }

    public Collection<ParticipantDTO> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(Collection<ParticipantDTO> participantList) {
        this.participantList = participantList;
    }

    public Long getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Long idCourse) {
        this.idCourse = idCourse;
    }
}
