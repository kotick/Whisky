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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import sessionBeans.UtilitiesSBLocal;
import sessionBeans.exceptions.RollbackFailureException;

@Named(value = "studentMB")
@RequestScoped
public class StudentMB {
    @EJB
    private UtilitiesSBLocal utilitiesSB;
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    private ParticipantJpaController participantJpa;
    private CourseJpaController courseJpa;
    private RoleJpaController roleJpa;
    
    private Collection<ParticipantDTO> studentList;
    private CourseDTO[] courseToAdd;
    private ParticipantDataModel allStudents;
        //Datos participant
    private String firstName;
    private String lastName;
    private String email;
    private String rut;



    public StudentMB() {
    }

    @PostConstruct
    void init() {

        participantJpa = new ParticipantJpaController(utx, emf);
        studentList = participantJpa.getAllByRol("Student");
        allStudents = new ParticipantDataModel((LinkedList<ParticipantDTO>) studentList);
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
        rol = roleJpa.getRol("Student");
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
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    
    public Collection<ParticipantDTO> getStudentList() {
        return studentList;
    }

    public void setStudentList(Collection<ParticipantDTO> studentList) {
        this.studentList = studentList;
    }

    public CourseDTO[] getCourseToAdd() {
        return courseToAdd;
    }

    public void setCourseToAdd(CourseDTO[] courseToAdd) {
        this.courseToAdd = courseToAdd;
    }
    
    
    public ParticipantDataModel getAllStudents() {
        return allStudents;
    }

    public void setAllStudents(ParticipantDataModel allStudents) {
        this.allStudents = allStudents;
    }
}
