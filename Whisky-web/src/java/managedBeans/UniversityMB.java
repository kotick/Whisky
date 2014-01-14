package managedBeans;

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
    private String username;
    private Long idUniversity;
    private UniversityJpaController universityJpa;
    private Collection<University> universityList;
    private UniversityDataModel allUniversityList;

    public UniversityMB() {
        LinkedList<University> universitiesTemp = new LinkedList<University>();
        universityJpa = new UniversityJpaController(utx, emf);
        universityList = universityJpa.findUniversityEntities();
        universitiesTemp.addAll(universityList);
        allUniversityList = new UniversityDataModel( (LinkedList<University>) universitiesTemp);
    }

    @PostConstruct
    void init() {
        if (loginConversation.getUsername() != null) {
            
            username = loginConversation.getUsername();
        }

        
    }

    public void setSessionUniversity() {
        this.universityConversation.beginConversation();
        this.universityConversation.setId(idUniversity);
        this.universityConversation.setUsername(username);
        session.redirect("/faces/teacher/course.xhtml?cid=".concat(this.universityConversation.getConversation().getId().toString()));

    }

    public void eraseUniversity(Long id) {
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
}
