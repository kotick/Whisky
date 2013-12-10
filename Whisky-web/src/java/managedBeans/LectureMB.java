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
    @EJB
    private CourseManagementSBLocal courseManagementSB;
    @EJB
    private LectureManagementSBLocal lectureManagementSB;
    @Inject LectureConversationMB lectureConversation;
    
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    private Long id;
    private Collection<ParticipantDTO> lectureList;

    public LectureMB() {
    }
    @PostConstruct
    void init(){
        id = lectureConversation.getId();
        lectureList= participantManagementSB.selectParticipantByLecture(id);
    }
    
    void createLecture(Long idCourse){
        Course actualCourse = courseManagementSB.getCourse(idCourse);
        lectureManagementSB.createLecture("","","",actualCourse);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<ParticipantDTO> getLectureList() {
        return lectureList;
    }

    public void setLectureList(Collection<ParticipantDTO> lectureList) {
        this.lectureList = lectureList;
    }

}
