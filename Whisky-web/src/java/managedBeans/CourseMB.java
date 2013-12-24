package managedBeans;

import DTOs.CourseDTO;
import DTOs.LectureDTO;
import DTOs.ParticipantDTO;
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
import JpaControllers.CourseJpaController;
import JpaControllers.ParticipantJpaController;
import entity.Participant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.TransferEvent;
import sessionBeans.CourseManagementSBLocal;
import sessionBeans.LectureManagementSBLocal;
import sessionBeans.exceptions.NonexistentEntityException;
import sessionBeans.exceptions.RollbackFailureException;

@Named(value = "courseMB")
@RequestScoped
public class CourseMB {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    ; 
    @Resource
    UserTransaction utx;
    @Inject
    CourseConversationMB courseConversation;
    @Inject
    LectureConversationMB lectureConversation;
    @Inject
    SessionMB session;
    @EJB
    private LectureManagementSBLocal lectureManagement;
    private CourseJpaController courseJpa;
    private ParticipantJpaController participantJpa;
    private Long id;
    private Collection<LectureDTO> lectureList;
    private Collection<CourseDTO> courseList;
    private String name;
    private ParticipantDTO[] participants;
    private CourseDataModel allCourses;

    public CourseMB() {
    }

    @PostConstruct
    void init() {
        if (courseConversation.getId() != null) {
            id = courseConversation.getId();
            lectureList = lectureManagement.selectLectureByCourses(id);
        }
        courseJpa = new CourseJpaController(utx, emf);
        courseList = courseJpa.findCourseEntities();
        allCourses = new CourseDataModel((LinkedList<CourseDTO>) courseList);


    }

    public void list(Long id) {
        this.lectureConversation.beginConversation();
        this.lectureConversation.setId(id);
        session.redirect("/faces/teacher/list.xhtml?cid=".concat(this.lectureConversation.getConversation().getId().toString()));
    }

    public void addCourse() {
        participantJpa = new ParticipantJpaController(utx, emf);
        Long idTemp;
        Participant temp;
        Collection<Participant> participantTemp = new LinkedList<Participant>();
        Course newCourse = new Course();
        newCourse.setName(name);

        for (ParticipantDTO iter : participants) {
            idTemp = iter.getId();
            temp = participantJpa.getParticipantById(idTemp);
            participantTemp.add(temp);
        }
        newCourse.setParticipant(participantTemp);
        courseJpa = new CourseJpaController(utx, emf);
        try {
            courseJpa.create(newCourse);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void eraseCourse(Long id){
        try {
            courseJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public Collection<LectureDTO> getLectureList() {
        return lectureList;
    }

    public Collection<CourseDTO> getCourseList() {
        return courseList;
    }

    public void setCourseList(Collection<CourseDTO> courseList) {
        this.courseList = courseList;
    }

    public void setLectureList(Collection<LectureDTO> lectureList) {
        this.lectureList = lectureList;
    }

    public CourseDataModel getAllCourses() {
        return allCourses;
    }

    public void setAllCourses(CourseDataModel allCourses) {
        this.allCourses = allCourses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParticipantDTO[] getParticipants() {
        return participants;
    }

    public void setParticipants(ParticipantDTO[] participants) {
        this.participants = participants;
    }
}
