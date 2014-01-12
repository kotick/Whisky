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

@Named(value = "courseMB")
@RequestScoped
public class UniversityMB {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    private UniversityJpaController universityJpa;
    private Collection<University> universityList;

    public UniversityMB() {
    }

    @PostConstruct
    void init() {
        universityJpa= new UniversityJpaController(utx,emf);
        universityList= universityJpa.findUniversityEntities();
    }
}