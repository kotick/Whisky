package managedBeans;

import DTOs.CourseDTO;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import sessionBeans.CourseManagementSBLocal;

@Named(value = "teacherMB")
@RequestScoped
public class TeacherMB {
    
    @EJB
    private CourseManagementSBLocal cursoManagementSB;
    private LinkedList<CourseDTO> courseList;

    public TeacherMB() {
    }
    @PostConstruct
    void init(){
        courseList= cursoManagementSB.selectCoursesByTeacher();
        
    }

    public CourseManagementSBLocal getCursoManagementSB() {
        return cursoManagementSB;
    }

    public void setCursoManagementSB(CourseManagementSBLocal cursoManagementSB) {
        this.cursoManagementSB = cursoManagementSB;
    }

    public LinkedList<CourseDTO> getCourseList() {
        return courseList;
    }

    public void setCourseList(LinkedList<CourseDTO> courseList) {
        this.courseList = courseList;
    }

    
}
