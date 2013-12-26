package managedBeans;

import DTOs.ParticipantDTO;
import entity.Course;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import sessionBeans.CourseManagementSBLocal;
import sessionBeans.LectureManagementSBLocal;

import sessionBeans.ParticipantManagementSBLocal;


@Named(value = "lectureMB")
@RequestScoped
public class LectureMB {
    @Inject AttendanceConversationMB attendanceConversationMB ;
    @Inject SessionMB session;
    @EJB
    private CourseManagementSBLocal courseManagementSB;
    @EJB
    private LectureManagementSBLocal lectureManagementSB;
    @Inject LectureConversationMB lectureConversation;
    
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    private Long idCourse;
    private Long idLecture;
    private Long idLecture2;
    private Collection<ParticipantDTO> participantList;


    public LectureMB() {
    }
    @PostConstruct
    void init(){
        idLecture = lectureConversation.getId();
        participantList= participantManagementSB.selectParticipantByLecture(idLecture);
    }
    
    public void createLecture(Long idCourse){
        Course actualCourse = courseManagementSB.getCourse(idCourse);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        idLecture2 = lectureManagementSB.createLecture(dateFormat.format(date),"","",actualCourse);
        this.attendanceConversationMB.beginConversation();
        this.attendanceConversationMB.setId(idLecture2);
        session.redirect("/faces/teacher/attendance.xhtml?cid=".concat(this.attendanceConversationMB.getConversation().getId().toString()));
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

    public Long getIdLecture() {
        return idLecture;
    }

    public void setIdLecture(Long idLecture) {
        this.idLecture = idLecture;
    }

}
