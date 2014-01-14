package managedBeans;

import DTOs.CourseDTO;
import DTOs.ParticipantDTO;
import JpaControllers.CourseJpaController;
import JpaControllers.ParticipantJpaController;
import JpaControllers.RoleJpaController;
import JpaControllers.UniversityJpaController;
import entity.Course;
import entity.Participant;
import entity.Role;
import entity.University;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import sessionBeans.UtilitiesSBLocal;
import sessionBeans.exceptions.NonexistentEntityException;
import sessionBeans.exceptions.RollbackFailureException;

@Named(value = "studentMB")
@RequestScoped
public class StudentMB {

    @Inject
    EditConversationMB editConversation;
    @Inject
    SessionMB session;
    @EJB
    private UtilitiesSBLocal utilitiesSB;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    private ParticipantJpaController participantJpa;
    private UniversityJpaController universityJpa;
    private CourseJpaController courseJpa;
    private RoleJpaController roleJpa;
    private Collection<ParticipantDTO> studentList;
    private List<ParticipantDTO> filteredStudents;
    private CourseDTO[] courseToAdd;
    //Datos participant
    private Long idUniversity;
    private String firstName;
    private String lastName;
    private String email;
    private String rut;
    private CourseDataModel allCoursesByUniversity;
    private ParticipantDataModel allTeachers;
    private ParticipantDataModel allStudents;

    public StudentMB() {
    }

    @PostConstruct
    void init() {

        participantJpa = new ParticipantJpaController(utx, emf);
        studentList = participantJpa.getAllByRol("Student");
        allTeachers = new ParticipantDataModel((LinkedList<ParticipantDTO>)participantJpa.getAllByRol("Teacher"));
        allStudents = new ParticipantDataModel((LinkedList<ParticipantDTO>) participantJpa.getStudentWithoutUniversity());


    }

    public void coursesByUniversity() {
        courseJpa = new CourseJpaController(utx, emf);
        allCoursesByUniversity = new CourseDataModel((LinkedList<CourseDTO>) courseJpa.getCourseForUniversity(idUniversity));
    }

    public ParticipantDataModel studentInClass(Long id) {
        return new ParticipantDataModel((LinkedList<ParticipantDTO>) participantJpa.getParticipantInClass(id, "Student"));

    }

    public ParticipantDataModel studentOutClass(Long id) {
        return new ParticipantDataModel((LinkedList<ParticipantDTO>) participantJpa.getParticipantOutClass(id, "Student"));
    }

    private Participant createStudent() {
        Participant newParticipant = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (firstName.equals("") && lastName.equals("") && email.equals("") && rut.equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obligatorios no pueden ser vacíos", "Error al agregar"));

        } else if (!utilitiesSB.validateRut(rut)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El rut ingresado es inválido", "Error al agregar"));
            /* TODO F:agregar mensaje más descriptivo, verificar dígito verificador, el formato 123123123-2" etc... */
        } else if (!utilitiesSB.validateEmail(email)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El email ingresado es inválido", "Error al agregar"));
            /* TODO F:agregar mensaje más descriptivo, verificar formato con @ y agregar el caso de que sea un correo duplicado en el validador */
        } else if (!utilitiesSB.checkDoubleEmail(email)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El email ya ocupado", "Error al agregar"));

        } else {
            newParticipant = new Participant();
            courseJpa = new CourseJpaController(utx, emf);
            roleJpa = new RoleJpaController(utx, emf);
            Long idTemp;
            Course temp;
            Role rol;
            String password;
            Collection<Course> coursesTemp = new LinkedList<Course>();

            newParticipant.setFirstName(firstName);
            newParticipant.setLastName(lastName);
            newParticipant.setEmail(email);
            newParticipant.setRut(rut);
            Collection<University> universityTemp = new LinkedList<University>();
            if (idUniversity != null) {
                universityTemp.add(findUniversity(idUniversity));
                newParticipant.setUniversities(universityTemp);
            }

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

        }
        return newParticipant;
    }

    public void editStudent(Long id) {
        this.editConversation.beginConversation();
        this.editConversation.setIdParticipant(id);
        //TODO Cambiar página de direccionamiento
        session.redirect("/faces/admin/editStudent.xhtml?cid=".concat(this.editConversation.getConversation().getId().toString()));

    }

    public void addStudent() {
        FacesContext context = FacesContext.getCurrentInstance();
        Participant newParticipant = createStudent();

        try {
            if (newParticipant != null) {
                participantJpa.create(newParticipant);
                Flash flash = context.getExternalContext().getFlash();
                flash.setKeepMessages(true);
                flash.setRedirect(true);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Alumno agregado con éxito", ""));
                /* TODO F:redirigir al listado de alumnos */
                session.redirect("/faces/admin/studentMaintainer.xhtml");

            }
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eraseStudent(Long id) {
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

    private University findUniversity(Long id) {
        universityJpa = new UniversityJpaController(utx, emf);
        return universityJpa.findUniversity(id);
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

    public CourseDataModel getAllCoursesByUniversity() {
        return allCoursesByUniversity;
    }

    public void setAllCoursesByUniversity(CourseDataModel allCoursesByUniversity) {
        this.allCoursesByUniversity = allCoursesByUniversity;
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

    public List<ParticipantDTO> getFilteredStudents() {
        return filteredStudents;
    }

    public void setFilteredStudents(List<ParticipantDTO> filteredStudents) {
        this.filteredStudents = filteredStudents;
    }

    public CourseDTO[] getCourseToAdd() {
        return courseToAdd;
    }

    public void setCourseToAdd(CourseDTO[] courseToAdd) {
        this.courseToAdd = courseToAdd;
    }

    public Long getIdUniversity() {
        return idUniversity;
    }

    public void setIdUniversity(Long idUniversity) {
        this.idUniversity = idUniversity;
    }

    public ParticipantDataModel getAllTeachers() {
        return allTeachers;
    }

    public void setAllTeachers(ParticipantDataModel allTeachers) {
        this.allTeachers = allTeachers;
    }

    public ParticipantDataModel getAllStudents() {
        return allStudents;
    }

    public void setAllStudents(ParticipantDataModel allStudents) {
        this.allStudents = allStudents;
    }
    
    
}
