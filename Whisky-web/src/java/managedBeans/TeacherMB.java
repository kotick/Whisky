package managedBeans;

import DTOs.CourseDTO;
import DTOs.ParticipantDTO;
import JpaControllers.CourseJpaController;
import JpaControllers.ParticipantJpaController;
import JpaControllers.RoleJpaController;
import entity.Course;
import entity.Participant;
import entity.Role;
import java.util.Collection;
import java.util.LinkedList;
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
import javax.transaction.UserTransaction;
import sessionBeans.CourseManagementSBLocal;
import sessionBeans.UtilitiesSBLocal;
import sessionBeans.exceptions.NonexistentEntityException;
import sessionBeans.exceptions.RollbackFailureException;

@Named(value = "teacherMB")
@RequestScoped
public class TeacherMB {
    @EJB
    private UtilitiesSBLocal utilitiesSB;

    @Inject
    LoginConversationMB loginConversation;
    @Inject
    CourseConversationMB courseConversation;
    @Inject
    SessionMB session;
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    private ParticipantJpaController participantJpa;
    private CourseJpaController courseJpa;
    private RoleJpaController roleJpa;
    @EJB
    private CourseManagementSBLocal cursoManagementSB;
    
    private Collection<CourseDTO> courseList;
    private CourseDTO[] courseToAdd;
    private String username;
    private Collection<ParticipantDTO> teacherList;
    //Datos participant
    private String firstName;
    private String lastName;
    private String email;
    private String rut;


    public TeacherMB() {
    }

    @PostConstruct
    void init() {
        courseList = new LinkedList<CourseDTO>();
        if (loginConversation.getUsername() != null) {
            username = loginConversation.getUsername();
            courseList = cursoManagementSB.selectCoursesByTeacher(username);
        }
        participantJpa = new ParticipantJpaController(utx, emf);

        teacherList = participantJpa.getAllByRol("Teacher");

    }

    public void lecture(Long id) {
        this.courseConversation.beginConversation();
        this.courseConversation.setId(id);
        session.redirect("/faces/teacher/lecture.xhtml?cid=".concat(this.courseConversation.getConversation().getId().toString()));
    }

    public void addTeacher() {
        courseJpa = new CourseJpaController(utx, emf);
        roleJpa = new RoleJpaController(utx,emf);
        Long idTemp;
        Course temp;
        Role rol;
        String password;
        Collection<Course> coursesTemp = new LinkedList<Course>();
        Participant newParticipant = new Participant();
        newParticipant.setFirstName(firstName);
        newParticipant.setLastName(lastName);
        newParticipant.setEmail(email);
        newParticipant.setRut(rut);
        rol = roleJpa.getRol("Teacher");
        newParticipant.setRol(rol);
        newParticipant.setPhoto("C:");
        try {
            password = utilitiesSB.stringToMD5("1234");
            newParticipant.setPassword(password);
        } catch (Exception ex) {
            Logger.getLogger(TeacherMB.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (CourseDTO iter : courseToAdd) {
            idTemp = iter.getId();
            temp = courseJpa.findCourse(idTemp);
            coursesTemp.add(temp);
        }
        newParticipant.setCourses(coursesTemp);
        
        try {
            participantJpa.create(newParticipant);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void eraseTeacher(Long id){
        try {
            participantJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }


    public Collection<CourseDTO> getCourseList() {
        return courseList;
    }

    public void setCourseList(LinkedList<CourseDTO> courseList) {
        this.courseList = courseList;
    }

    public CourseDTO[] getCourseToAdd() {
        return courseToAdd;
    }

    public void setCourseToAdd(CourseDTO[] courseToAdd) {
        this.courseToAdd = courseToAdd;
    }

    public Collection<ParticipantDTO> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(Collection<ParticipantDTO> teacherList) {
        this.teacherList = teacherList;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRut() {
        return rut;
    }
}
