package managedBeans;

import DTOs.LectureDTO;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import sessionBeans.LectureManagementSBLocal;

@Named(value = "courseMB")
@RequestScoped
public class CourseMB {
    @Inject CourseConversationMB courseConversation;
    @Inject LectureConversationMB lectureConversation ;
    @Inject SessionMB session;

    @EJB
    private LectureManagementSBLocal lectureManagement;
    private Long id;
    private Collection<LectureDTO> lectureList;

    public CourseMB() {
    }
    @PostConstruct
    void init(){
        id = courseConversation.getId();
        lectureList= lectureManagement.selectLectureByCourses(id);
    }
    
    public void list(Long id){
        this.lectureConversation.beginConversation();
        this.lectureConversation.setId(id);
        session.redirect("/faces/teacher/list.xhtml?cid=".concat(this.lectureConversation.getConversation().getId().toString()));
    }
    

    public Collection<LectureDTO> getLectureList() {
        return lectureList;
    }

    public void setLectureList(Collection<LectureDTO> lectureList) {
        this.lectureList = lectureList;
    }
    
}
