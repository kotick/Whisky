package managedBeans;

import DTOs.CourseDTO;
import java.util.Collection;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import sessionBeans.CourseManagementSBLocal;

@Named(value = "teacherMB")
@RequestScoped
public class TeacherMB {
    @Inject LoginConversationMB loginConversation;
  
    @EJB
    private CourseManagementSBLocal cursoManagementSB;
    private Collection<CourseDTO> courseList;
    private String username;

    public TeacherMB() {
    }
    @PostConstruct
    void init(){
        username = loginConversation.getUsername();
        courseList= cursoManagementSB.selectCoursesByTeacher(username);
        
    }

    public CourseManagementSBLocal getCursoManagementSB() {
        return cursoManagementSB;
    }

    public void setCursoManagementSB(CourseManagementSBLocal cursoManagementSB) {
        this.cursoManagementSB = cursoManagementSB;
    }

    public Collection<CourseDTO> getCourseList() {
        return courseList;
    }

    public void setCourseList(LinkedList<CourseDTO> courseList) {
        this.courseList = courseList;
    }
    
}
