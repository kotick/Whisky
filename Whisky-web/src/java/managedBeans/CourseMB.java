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
import javax.transaction.UserTransaction;
import JpaControllers.CourseJpaController;
import JpaControllers.ParticipantJpaController;
import entity.Participant;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import sessionBeans.LectureManagementSBLocal;
import sessionBeans.exceptions.NonexistentEntityException;
import sessionBeans.exceptions.RollbackFailureException;

@Named(value = "courseMB")
@RequestScoped
public class CourseMB {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    @Inject
    CourseStatisticsConversation courseStatisticsConversation;
    @Inject
    CourseConversationMB courseConversation;
    @Inject
    EditConversationMB editConversation;
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
    private List<CourseDTO> filteredCourses;
    private String name;
    private String nameCourse;
    private ParticipantDTO[] participantsToAdd;
    private CourseDataModel allCourses;

    public CourseMB() {
    }

    @PostConstruct
    void init() {
        if (courseConversation.getId() != null) {
            id = courseConversation.getId();
            lectureList = lectureManagement.selectLectureByCourses(id);
            nameCourse = courseConversation.getName();
        }
        courseJpa = new CourseJpaController(utx, emf);
        courseList = courseJpa.findCourseEntities();
        allCourses = new CourseDataModel((LinkedList<CourseDTO>) courseList);
    }

    public void list(Long id, String date) {
        this.lectureConversation.beginConversation();
        this.lectureConversation.setIdLecture(id);
        this.lectureConversation.setDateLecture(date);
        this.lectureConversation.setNameCourse(nameCourse);
        session.redirect("/faces/teacher/list.xhtml?cid=".concat(this.lectureConversation.getConversation().getId().toString()));
    }

    public void letsGoToLectureMaintainer(Long id) {
        this.lectureConversation.beginConversation();
        this.lectureConversation.setIdCourse(id);
        session.redirect("/faces/admin/lectureMaintainer.xhtml?cid=".concat(this.lectureConversation.getConversation().getId().toString()));

    }

    public void letsGoTocourseStatistics(Long id) {
        System.out.println(id);
        this.courseStatisticsConversation.beginConversation();
        this.courseStatisticsConversation.setIdCourse(id);
        session.redirect("/faces/teacher/courseStatistics.xhtml?cid=".concat(this.courseStatisticsConversation.getConversation().getId().toString()));
    }

    public CourseDataModel courseForParticipant(Long id) {
        return new CourseDataModel((LinkedList<CourseDTO>) courseJpa.getCourseForParticipant(id));

    }

    public CourseDataModel notCourseForParticipant(Long id) {
        return new CourseDataModel((LinkedList<CourseDTO>) courseJpa.getNotCourseForParticipant(id));
    }

    private Course createCourse() {
        Course newCourse = null;
        FacesContext context = FacesContext.getCurrentInstance();

        if (name.equalsIgnoreCase("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obligatorios no pueden ser vacíos", "Error al agregar"));

        } else {
            newCourse = new Course();
            participantJpa = new ParticipantJpaController(utx, emf);
            Long idTemp;
            Participant temp;
            Collection<Participant> participantTemp = new LinkedList<Participant>();

            newCourse.setName(name);

            for (ParticipantDTO iter : participantsToAdd) {
                idTemp = iter.getId();
                temp = participantJpa.getParticipantById(idTemp);
                participantTemp.add(temp);
            }
            newCourse.setParticipant(participantTemp);
        }
        return newCourse;

    }

    public void addCourse() {
        FacesContext context = FacesContext.getCurrentInstance();
        Course newCourse = createCourse();
        courseJpa = new CourseJpaController(utx, emf);
        try {
            if (newCourse != null) {
                courseJpa.create(newCourse);
                Flash flash = context.getExternalContext().getFlash();
                flash.setKeepMessages(true);
                flash.setRedirect(true);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Curso agregado con éxito", ""));
                session.redirect("/faces/admin/courseMaintainer.xhtml");

            }
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editCourse(Long id) {
        this.editConversation.beginConversation();
        this.editConversation.setIdCourse(id);
        //TODO Cambiar página de direccionamiento
        session.redirect("/faces/admin/editCourse.xhtml?cid=".concat(this.lectureConversation.getConversation().getId().toString()));

    }

    public void eraseCourse(Long id) {
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

    public List<CourseDTO> getFilteredCourses() {
        return filteredCourses;
    }

    public void setFilteredTeachers(List<CourseDTO> filteredCourses) {
        this.filteredCourses = filteredCourses;
    }

    public CourseDataModel getAllCourses() {
        return allCourses;
    }

    public void setAllCourses(CourseDataModel allCourses) {
        this.allCourses = allCourses;
    }

    public ParticipantDTO[] getParticipantsToAdd() {
        return participantsToAdd;
    }

    public void setParticipantsToAdd(ParticipantDTO[] participantsToAdd) {
        this.participantsToAdd = participantsToAdd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }
}
