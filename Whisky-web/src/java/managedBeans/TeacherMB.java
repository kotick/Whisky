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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    CourseConversationMB courseConversation;
    @Inject
    UniversityConversationMB universityConversation;
    @Inject
    EditConversationMB editConversation;
    @Inject
    SessionMB session;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    private ParticipantJpaController participantJpa;
    private UniversityJpaController universityJpa;
    private CourseJpaController courseJpa;
    private RoleJpaController roleJpa;
    @EJB
    private CourseManagementSBLocal cursoManagementSB;
    private Collection<CourseDTO> courseList;
    private CourseDTO[] courseToAdd;
    private University[] universitiesToAdd;
    private CourseDataModel allCoursesByUniversity;
    private String username;
    private Collection<ParticipantDTO> teacherList;
    private List<ParticipantDTO> filteredTeachers;
    //Datos participant
    private Long idUniversity;
    private String firstName;
    private String lastName;
    private String email;
    private String rut;

    public TeacherMB() {
    }

    @PostConstruct
    void init() {
        courseList = new LinkedList<CourseDTO>();
        if (universityConversation.getUsername() != null) {
            username = universityConversation.getUsername();
            idUniversity = universityConversation.getId();
            courseList = cursoManagementSB.selectCoursesByTeacher(username, idUniversity);
        }
        participantJpa = new ParticipantJpaController(utx, emf);

        teacherList = participantJpa.getAllByRol("Teacher");

    }

    public void lecture(Long id, String name) {
        this.courseConversation.beginConversation();
        this.courseConversation.setId(id);
        this.courseConversation.setName(name);
        session.redirect("/faces/teacher/lecture.xhtml?cid=".concat(this.courseConversation.getConversation().getId().toString()));
    }

    private Participant createTeacher() {
        Participant newParticipant = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (firstName.equalsIgnoreCase("") || lastName.equalsIgnoreCase("") || email.equalsIgnoreCase("") || rut.equalsIgnoreCase("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obligatorios no pueden ser vacíos", "Error al agregar"));

        } else if (!utilitiesSB.validateRut(rut)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El rut ingresado es inválido", "Error al agregar"));

        } else if (!utilitiesSB.validateEmail(email)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El email ingresado es inválido", "Error al agregar"));

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
            rol = roleJpa.getRol("Teacher");
            newParticipant.setRol(rol);
            newParticipant.setPhoto("C:");

            Collection<University> universityTemp = new LinkedList<University>();
            for (University iter : universitiesToAdd) {
                universityTemp.add(iter);
            }
            newParticipant.setUniversities(universityTemp);

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

    public void editTeacher(Long id) {
        this.editConversation.beginConversation();
        this.editConversation.setIdParticipant(id);
        //TODO Cambiar página de direccionamiento
        session.redirect("/faces/admin/editTeacher.xhtml?cid=".concat(this.editConversation.getConversation().getId().toString()));

    }

    public void addTeacher() {
        FacesContext context = FacesContext.getCurrentInstance();
        Participant newParticipant = createTeacher();

        try {
            if (newParticipant != null) {
                participantJpa.create(newParticipant);
                Flash flash = context.getExternalContext().getFlash();
                flash.setKeepMessages(true);
                flash.setRedirect(true);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profesor agregado con éxito", ""));
                session.redirect("/faces/admin/teacherMaintainer.xhtml");
            }
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eraseTeacher(Long id) {
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

    public void coursesByUniversity() {
        LinkedList<CourseDTO> courses = new LinkedList<CourseDTO>();
        courseJpa = new CourseJpaController(utx, emf);
        for (University iter : universitiesToAdd) {
            courses.addAll(courseJpa.getCourseForUniversity(iter.getId()));
        }


        allCoursesByUniversity = new CourseDataModel(courses);
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

    public CourseDataModel getAllCoursesByUniversity() {
        return allCoursesByUniversity;
    }

    public void setAllCoursesByUniversity(CourseDataModel allCoursesByUniversity) {
        this.allCoursesByUniversity = allCoursesByUniversity;
    }

    public University[] getUniversitiesToAdd() {
        return universitiesToAdd;
    }

    public void setUniversitiesToAdd(University[] universitiesToAdd) {
        this.universitiesToAdd = universitiesToAdd;
    }

    public void setTeacherList(Collection<ParticipantDTO> teacherList) {
        this.teacherList = teacherList;
    }

    public List<ParticipantDTO> getFilteredTeachers() {
        return filteredTeachers;
    }

    public void setFilteredTeachers(List<ParticipantDTO> filteredTeachers) {
        this.filteredTeachers = filteredTeachers;
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

    public Long getIdUniversity() {
        return idUniversity;
    }

    public void setIdUniversity(Long idUniversity) {
        this.idUniversity = idUniversity;
    }
}
