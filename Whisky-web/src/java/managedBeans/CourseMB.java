package managedBeans;

import DTOs.CourseDTO;
import DTOs.LectureDTO;
import entity.Course;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import sessionBeans.CourseJpaController;
import sessionBeans.CourseManagementSBLocal;
import sessionBeans.LectureManagementSBLocal;
import sessionBeans.exceptions.RollbackFailureException;

@Named(value = "courseMB")
@RequestScoped
public class CourseMB {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");; 
    @Resource 
    UserTransaction utx; 

    public Collection<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(Collection<Course> courseList) {
        this.courseList = courseList;
    }

    @Inject
    CourseConversationMB courseConversation;
    @Inject
    LectureConversationMB lectureConversation;
    @Inject
    SessionMB session;
    @EJB
    private LectureManagementSBLocal lectureManagement;
    private CourseJpaController courseJpa;
    private Long id;
    private Collection<LectureDTO> lectureList;
    private Collection<Course> courseList;
    private String name;

   
    

    public CourseMB() {
        
    }

    @PostConstruct
    void init() {
        if (courseConversation.getId() != null) {
            id = courseConversation.getId();
            lectureList = lectureManagement.selectLectureByCourses(id);
        }
        courseJpa = new CourseJpaController(utx,emf);
        courseList = courseJpa.findCourseEntities();
        
       
    }

    public void list(Long id) {
        this.lectureConversation.beginConversation();
        this.lectureConversation.setId(id);
        session.redirect("/faces/teacher/list.xhtml?cid=".concat(this.lectureConversation.getConversation().getId().toString()));
    }


    public Collection<LectureDTO> getLectureList() {
        return lectureList;
    }
    public void addCourse(){
       Course newCourse = new Course();
       newCourse.setName(name);
       newCourse.setParticipant(null);
       courseJpa = new CourseJpaController(utx,emf);
        try {
            courseJpa.create(newCourse);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    public void setLectureList(Collection<LectureDTO> lectureList) {
        this.lectureList = lectureList;
    }
    /* public Collection<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(Collection<Course> courseList) {
        this.courseList = courseList;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
