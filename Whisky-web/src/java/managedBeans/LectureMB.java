package managedBeans;

import DTOs.ParticipantDTO;
import entity.Course;
import java.util.Collection;
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
    private Collection<ParticipantDTO> lectureList;


    public LectureMB() {
    }
    @PostConstruct
    void init(){
        idLecture = lectureConversation.getId();
        lectureList= participantManagementSB.selectParticipantByLecture(idLecture);
    }
    
    public void createLecture(Long idCourse){
        Course actualCourse = courseManagementSB.getCourse(idCourse);
        idLecture2 = lectureManagementSB.createLecture("","","",actualCourse);
        this.attendanceConversationMB.beginConversation();
        this.attendanceConversationMB.setId(idLecture2);
        session.redirect("/faces/teacher/attendance.xhtml?cid=".concat(this.attendanceConversationMB.getConversation().getId().toString()));
    }


    public Collection<ParticipantDTO> getLectureList() {
        return lectureList;
    }

    public void setLectureList(Collection<ParticipantDTO> lectureList) {
        this.lectureList = lectureList;
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
