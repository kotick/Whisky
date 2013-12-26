/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import sessionBeans.UtilitiesSBLocal;
import sessionBeans.exceptions.NonexistentEntityException;
import sessionBeans.exceptions.RollbackFailureException;

/**
 *
 * @author Yani
 */
@Named(value = "editMB")
@RequestScoped
public class EditMB {

    @Inject
    EditConversationMB editConversation;
    @Inject
    SessionMB session;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    @EJB
    private UtilitiesSBLocal utilitiesSB;
    private CourseJpaController courseJpa;
    private ParticipantJpaController participantJpa;
    private Long id;
    private String name;
    private ParticipantDTO[] participantsToAdd;
    private ParticipantDTO[] participantsToRemove;
    private Collection<Participant> participants;
    private String firstName;
    private String lastName;
    private String email;
    private String rut;
    private CourseDTO[] courseToAdd;
    private CourseDTO[] courseToRemove;
    private Collection<Course> courses;

    public EditMB() {
    }

    @PostConstruct
    void init() {
        if (editConversation.getIdCourse() != null) {
            courseJpa = new CourseJpaController(utx, emf);
            //participantJpa = new ParticipantJpaController(utx, emf);
            id = editConversation.getIdCourse();
            //id= Long.valueOf("33");
            Course courseTemp = courseJpa.findCourse(id);
            name = courseTemp.getName();
            participants = courseTemp.getParticipant();

        } else if (editConversation.getIdParticipant() != null) {
            participantJpa = new ParticipantJpaController(utx, emf);
            id = editConversation.getIdParticipant();
            Participant participantTemp = participantJpa.findParticipant(id);
            firstName = participantTemp.getFirstName();
            lastName = participantTemp.getLastName();
            rut = participantTemp.getRut();
            email = participantTemp.getEmail();
            courses = participantTemp.getCourses();
        }


    }

    public void editCourse() {
        participantJpa = new ParticipantJpaController(utx, emf);
        FacesContext context = FacesContext.getCurrentInstance();
        if (name.equalsIgnoreCase("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obligatorios no pueden ser vacíos", "Error al agregar"));

        } else {
            Course courseTemp = new Course();
            courseTemp.setName(name);
            courseTemp.setId(id);
            Participant temp;
            Long idTemp;
            for (ParticipantDTO iter : participantsToRemove) {
                idTemp = iter.getId();
                temp = participantJpa.getParticipantById(idTemp);
                participants.remove(temp);
            }

            for (ParticipantDTO iter : participantsToAdd) {
                idTemp = iter.getId();
                temp = participantJpa.getParticipantById(idTemp);

                participants.add(temp);
            }
            courseTemp.setParticipant(participants);
            try {
                courseJpa.edit(courseTemp);
                
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(EditMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(EditMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(EditMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void editParticipant() {
        Participant newParticipant = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (firstName.equals("") && lastName.equals("") && email.equals("") && rut.equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obligatorios no pueden ser vacíos", "Error al agregar"));

        } else if (!utilitiesSB.validateRut(rut)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar", "Rut inválido"));
            /* TODO F:agregar mensaje más descriptivo, verificar dígito verificador, el formato 123123123-2" etc... */
        } else if (!utilitiesSB.validateEmail(email)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar", "Email inválido"));
            /* TODO F:agregar mensaje más descriptivo, verificar formato con @ y agregar el caso de que sea un correo duplicado en el validador */
        } else {
            newParticipant = participantJpa.findParticipant(id);
            courseJpa = new CourseJpaController(utx, emf);
            Long idTemp;
            Course temp;
            newParticipant.setFirstName(firstName);
            newParticipant.setLastName(lastName);
            newParticipant.setEmail(email);
            newParticipant.setRut(rut);

            for (CourseDTO iter : courseToRemove) {
                idTemp = iter.getId();
                temp = courseJpa.findCourse(idTemp);
                courses.remove(temp);
            }

            for (CourseDTO iter : courseToAdd) {
                idTemp = iter.getId();
                temp = courseJpa.findCourse(idTemp);
                courses.add(temp);
            }
            newParticipant.setCourses(courses);
            try {
                participantJpa.edit(newParticipant);
                if(newParticipant.getRol().getName().equalsIgnoreCase("student")){
                    session.redirect("/faces/admin/studentMaintainer.xhtml");

                }
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(EditMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(EditMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(EditMB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CourseDTO[] getCourseToAdd() {
        return courseToAdd;
    }

    public void setCourseToAdd(CourseDTO[] courseToAdd) {
        this.courseToAdd = courseToAdd;
    }

    public CourseDTO[] getCourseToRemove() {
        return courseToRemove;
    }

    public void setCourseToRemove(CourseDTO[] courseToRemove) {
        this.courseToRemove = courseToRemove;
    }

    public ParticipantDTO[] getParticipantsToAdd() {
        return participantsToAdd;
    }

    public void setParticipantsToAdd(ParticipantDTO[] participantsToAdd) {
        this.participantsToAdd = participantsToAdd;
    }

    public ParticipantDTO[] getParticipantsToRemove() {
        return participantsToRemove;
    }

    public void setParticipantsToRemove(ParticipantDTO[] participantsToRemove) {
        this.participantsToRemove = participantsToRemove;
    }
}
