package managedBeans;

import DTOs.ParticipantDTO;
import JpaControllers.ParticipantJpaController;
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
import JpaControllers.UniversityJpaController;
import entity.Participant;
import entity.University;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import sessionBeans.LectureManagementSBLocal;
import sessionBeans.UtilitiesSBLocal;
import sessionBeans.exceptions.NonexistentEntityException;
import sessionBeans.exceptions.RollbackFailureException;

@Named(value = "universityMB")
@RequestScoped
public class UniversityMB {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    @Inject
    LoginConversationMB loginConversation;
    @Inject
    UniversityConversationMB universityConversation;
    @Inject
    SessionMB session;
    @EJB
    private UtilitiesSBLocal utilitiesSB;
    private String username;
    private Long idUniversity;
    private UniversityJpaController universityJpa;
    private Collection<University> universityList;
    private UniversityDataModel allUniversityList;
    private String rut;
    private String name;
    private String address;
    private ParticipantDataModel allTeachers;
    private ParticipantDataModel allStudents;
    private ParticipantDTO[] studentsToAdd;
    private ParticipantDTO[] teachersToAdd;
    private ParticipantJpaController participantJpa;

    public UniversityMB() {
    }

    @PostConstruct
    void init() {
        if (loginConversation.getUsername() != null) {

            username = loginConversation.getUsername();
        }
        LinkedList<University> universitiesTemp = new LinkedList<University>();
        universityJpa = new UniversityJpaController(utx, emf);
        universityList = universityJpa.findUniversityEntities();
        universitiesTemp.addAll(universityList);
        allUniversityList = new UniversityDataModel((LinkedList<University>) universitiesTemp);


    }

    public void addUniversity() {
        University newUniversity = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (name.equals("") && rut.equals("") && address.equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obligatorios no pueden ser vacíos", "Error al agregar"));

        } else if (!utilitiesSB.validateRut(rut)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El rut ingresado es inválido", "Error al agregar"));
            /* TODO F:agregar mensaje más descriptivo, verificar dígito verificador, el formato 123123123-2" etc... */
        } else {

            participantJpa = new ParticipantJpaController(utx, emf);
            newUniversity = new University();
            List<Participant> participants = new LinkedList<Participant>();
            Participant participantTemp;
            newUniversity.setName(name);
            newUniversity.setRut(rut);
            newUniversity.setAddress(address);

            for (ParticipantDTO iter : studentsToAdd) {
                participantTemp = participantJpa.findParticipant(iter.getId());
                participants.add(participantTemp);
            }
            for (ParticipantDTO iter : teachersToAdd) {
                participantTemp = participantJpa.findParticipant(iter.getId());
                participants.add(participantTemp);
            }
            newUniversity.setParticipants(participants);
            try {
                universityJpa = new UniversityJpaController(utx, emf);
                universityJpa.create(newUniversity);
                Flash flash = context.getExternalContext().getFlash();
                flash.setKeepMessages(true);
                flash.setRedirect(true);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Universidad agregada con éxito", ""));
                /* TODO F:redirigir al listado de alumnos */
                session.redirect("/faces/admin/universityMaintainer.xhtml");
            } catch (JpaControllers.exceptions.RollbackFailureException ex) {
                Logger.getLogger(UniversityMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(UniversityMB.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

    }

    public void setSessionUniversity() {
        this.universityConversation.beginConversation();
        this.universityConversation.setId(idUniversity);
        this.universityConversation.setUsername(username);
        session.redirect("/faces/teacher/course.xhtml?cid=".concat(this.universityConversation.getConversation().getId().toString()));

    }

    public void eraseUniversity(Long id) {
        universityJpa = new UniversityJpaController(utx, emf);
        try {
            universityJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CourseMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Collection<University> getUniversityList() {
        return universityList;
    }

    public void setUniversityList(Collection<University> universityList) {
        this.universityList = universityList;
    }

    public Long getIdUniversity() {
        return idUniversity;
    }

    public void setIdUniversity(Long idUniversity) {
        this.idUniversity = idUniversity;
    }

    public UniversityDataModel getAllUniversityList() {
        return allUniversityList;
    }

    public void setAllUniversityList(UniversityDataModel allUniversityList) {
        this.allUniversityList = allUniversityList;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public ParticipantDTO[] getStudentsToAdd() {
        return studentsToAdd;
    }

    public void setStudentsToAdd(ParticipantDTO[] studentsToAdd) {
        this.studentsToAdd = studentsToAdd;
    }

    public ParticipantDTO[] getTeachersToAdd() {
        return teachersToAdd;
    }

    public void setTeachersToAdd(ParticipantDTO[] teachersToAdd) {
        this.teachersToAdd = teachersToAdd;
    }
}
