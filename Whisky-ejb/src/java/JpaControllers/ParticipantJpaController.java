/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JpaControllers;

import DTOs.ParticipantDTO;
import JpaControllers.exceptions.NonexistentEntityException;
import JpaControllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.University;
import java.util.ArrayList;
import java.util.Collection;
import entity.Course;
import entity.Participant;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yani
 */
public class ParticipantJpaController implements Serializable {

    public ParticipantJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Participant participant) throws RollbackFailureException, Exception {
        if (participant.getUniversities() == null) {
            participant.setUniversities(new ArrayList<University>());
        }
        if (participant.getCourses() == null) {
            participant.setCourses(new ArrayList<Course>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<University> attachedUniversities = new ArrayList<University>();
            for (University universitiesUniversityToAttach : participant.getUniversities()) {
                universitiesUniversityToAttach = em.getReference(universitiesUniversityToAttach.getClass(), universitiesUniversityToAttach.getId());
                attachedUniversities.add(universitiesUniversityToAttach);
            }
            participant.setUniversities(attachedUniversities);
            Collection<Course> attachedCourses = new ArrayList<Course>();
            for (Course coursesCourseToAttach : participant.getCourses()) {
                coursesCourseToAttach = em.getReference(coursesCourseToAttach.getClass(), coursesCourseToAttach.getId());
                attachedCourses.add(coursesCourseToAttach);
            }
            participant.setCourses(attachedCourses);
            em.persist(participant);
            for (University universitiesUniversity : participant.getUniversities()) {
                universitiesUniversity.getParticipants().add(participant);
                universitiesUniversity = em.merge(universitiesUniversity);
            }
            for (Course coursesCourse : participant.getCourses()) {
                coursesCourse.getParticipant().add(participant);
                coursesCourse = em.merge(coursesCourse);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Participant participant) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Participant persistentParticipant = em.find(Participant.class, participant.getId());
            Collection<University> universitiesOld = persistentParticipant.getUniversities();
            Collection<University> universitiesNew = participant.getUniversities();
            Collection<Course> coursesOld = persistentParticipant.getCourses();
            Collection<Course> coursesNew = participant.getCourses();
            Collection<University> attachedUniversitiesNew = new ArrayList<University>();
            for (University universitiesNewUniversityToAttach : universitiesNew) {
                universitiesNewUniversityToAttach = em.getReference(universitiesNewUniversityToAttach.getClass(), universitiesNewUniversityToAttach.getId());
                attachedUniversitiesNew.add(universitiesNewUniversityToAttach);
            }
            universitiesNew = attachedUniversitiesNew;
            participant.setUniversities(universitiesNew);
            Collection<Course> attachedCoursesNew = new ArrayList<Course>();
            for (Course coursesNewCourseToAttach : coursesNew) {
                coursesNewCourseToAttach = em.getReference(coursesNewCourseToAttach.getClass(), coursesNewCourseToAttach.getId());
                attachedCoursesNew.add(coursesNewCourseToAttach);
            }
            coursesNew = attachedCoursesNew;
            participant.setCourses(coursesNew);
            participant = em.merge(participant);
            for (University universitiesOldUniversity : universitiesOld) {
                if (!universitiesNew.contains(universitiesOldUniversity)) {
                    universitiesOldUniversity.getParticipants().remove(participant);
                    universitiesOldUniversity = em.merge(universitiesOldUniversity);
                }
            }
            for (University universitiesNewUniversity : universitiesNew) {
                if (!universitiesOld.contains(universitiesNewUniversity)) {
                    universitiesNewUniversity.getParticipants().add(participant);
                    universitiesNewUniversity = em.merge(universitiesNewUniversity);
                }
            }
            for (Course coursesOldCourse : coursesOld) {
                if (!coursesNew.contains(coursesOldCourse)) {
                    coursesOldCourse.getParticipant().remove(participant);
                    coursesOldCourse = em.merge(coursesOldCourse);
                }
            }
            for (Course coursesNewCourse : coursesNew) {
                if (!coursesOld.contains(coursesNewCourse)) {
                    coursesNewCourse.getParticipant().add(participant);
                    coursesNewCourse = em.merge(coursesNewCourse);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = participant.getId();
                if (findParticipant(id) == null) {
                    throw new NonexistentEntityException("The participant with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Participant participant;
            try {
                participant = em.getReference(Participant.class, id);
                participant.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The participant with id " + id + " no longer exists.", enfe);
            }
            Collection<University> universities = participant.getUniversities();
            for (University universitiesUniversity : universities) {
                universitiesUniversity.getParticipants().remove(participant);
                universitiesUniversity = em.merge(universitiesUniversity);
            }
            Collection<Course> courses = participant.getCourses();
            for (Course coursesCourse : courses) {
                coursesCourse.getParticipant().remove(participant);
                coursesCourse = em.merge(coursesCourse);
            }
            em.remove(participant);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Participant> findParticipantEntities() {
        return findParticipantEntities(true, -1, -1);
    }

    public List<Participant> findParticipantEntities(int maxResults, int firstResult) {
        return findParticipantEntities(false, maxResults, firstResult);
    }

    private List<Participant> findParticipantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Participant.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Participant findParticipant(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Participant.class, id);
        } finally {
            em.close();
        }
    }

    public int getParticipantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Participant> rt = cq.from(Participant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Participant getParticipantById(Long id) {
        EntityManager em = getEntityManager();


        Query q = em.createNamedQuery("Participant.getParticipantById", Participant.class);
        q.setParameter("id", id);
        return (Participant) q.getSingleResult();
    }
        public Collection<ParticipantDTO> getAllByRolAndUniversity(Long idUniversity, String rol) {
        EntityManager em = getEntityManager();
        Collection<Participant> resultQuery;
        Collection<ParticipantDTO> result = new LinkedList<ParticipantDTO>();
        ParticipantDTO participantDTOTemp;

        Query q = em.createNamedQuery("Participant.getAllByTypeAndUniversity", Participant.class);
        q.setParameter("rol", rol);
        q.setParameter("idUniversity", idUniversity);

        resultQuery = (Collection<Participant>) q.getResultList();

        for (Participant iter : resultQuery) {
            participantDTOTemp = new ParticipantDTO();
            participantDTOTemp.setFirstName(iter.getFirstName());
            participantDTOTemp.setLastName(iter.getLastName());
            participantDTOTemp.setId(iter.getId());
            participantDTOTemp.setRut(iter.getRut());
            result.add(participantDTOTemp);
        }
        return result;
    }

    public Collection<ParticipantDTO> getAllByRol( String rol) {
        EntityManager em = getEntityManager();
        Collection<Participant> resultQuery;
        Collection<ParticipantDTO> result = new LinkedList<ParticipantDTO>();
        ParticipantDTO participantDTOTemp;

        Query q = em.createNamedQuery("Participant.getAllByTypeAndUniversity", Participant.class);
        q.setParameter("rol", rol);

        resultQuery = (Collection<Participant>) q.getResultList();

        for (Participant iter : resultQuery) {
            participantDTOTemp = new ParticipantDTO();
            participantDTOTemp.setFirstName(iter.getFirstName());
            participantDTOTemp.setLastName(iter.getLastName());
            participantDTOTemp.setId(iter.getId());
            participantDTOTemp.setRut(iter.getRut());
            result.add(participantDTOTemp);
        }
        return result;
    }

    
    public Collection<ParticipantDTO> getParticipantInClass(Long id, String rol) {
        EntityManager em = getEntityManager();
        Collection<Participant> resultQuery;
        Collection<ParticipantDTO> result = new LinkedList<ParticipantDTO>();
        ParticipantDTO participantDTOTemp;

        Query q = em.createNamedQuery("Participant.getParticipantInClass", Participant.class);
        q.setParameter("rol", rol);
        q.setParameter("id", id);

        resultQuery = (Collection<Participant>) q.getResultList();

        for (Participant iter : resultQuery) {
            participantDTOTemp = new ParticipantDTO();
            participantDTOTemp.setFirstName(iter.getFirstName());
            participantDTOTemp.setLastName(iter.getLastName());
            participantDTOTemp.setId(iter.getId());
            participantDTOTemp.setRut(iter.getRut());
            participantDTOTemp.setEmail(iter.getEmail());
            result.add(participantDTOTemp);
        }
        return result;
    }

    public Collection<ParticipantDTO> getParticipantOutClass(Long id, String rol) {
        EntityManager em = getEntityManager();
        Collection<Participant> resultQuery;
        Collection<ParticipantDTO> result = new LinkedList<ParticipantDTO>();
        ParticipantDTO participantDTOTemp;

        Query q = em.createNamedQuery("Participant.getParticipantOutClass", Participant.class);
        q.setParameter("rol", rol);
        q.setParameter("id", id);

        resultQuery = (Collection<Participant>) q.getResultList();

        for (Participant iter : resultQuery) {
            participantDTOTemp = new ParticipantDTO();
            participantDTOTemp.setFirstName(iter.getFirstName());
            participantDTOTemp.setLastName(iter.getLastName());
            participantDTOTemp.setId(iter.getId());
            participantDTOTemp.setRut(iter.getRut());
            result.add(participantDTOTemp);
        }
        return result;
    }
}
