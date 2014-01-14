/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JpaControllers;

import DTOs.CourseDTO;
import JpaControllers.exceptions.NonexistentEntityException;
import JpaControllers.exceptions.RollbackFailureException;
import entity.Course;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Participant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yani
 */
public class CourseJpaController implements Serializable {

    public CourseJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Course course) throws RollbackFailureException, Exception {
        if (course.getParticipant() == null) {
            course.setParticipant(new ArrayList<Participant>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Participant> attachedParticipant = new ArrayList<Participant>();
            for (Participant participantParticipantToAttach : course.getParticipant()) {
                participantParticipantToAttach = em.getReference(participantParticipantToAttach.getClass(), participantParticipantToAttach.getId());
                attachedParticipant.add(participantParticipantToAttach);
            }
            course.setParticipant(attachedParticipant);
            em.persist(course);
            for (Participant participantParticipant : course.getParticipant()) {
                participantParticipant.getCourses().add(course);
                participantParticipant = em.merge(participantParticipant);
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

    public void edit(Course course) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Course persistentCourse = em.find(Course.class, course.getId());
            Collection<Participant> participantOld = persistentCourse.getParticipant();
            Collection<Participant> participantNew = course.getParticipant();
            Collection<Participant> attachedParticipantNew = new ArrayList<Participant>();
            for (Participant participantNewParticipantToAttach : participantNew) {
                participantNewParticipantToAttach = em.getReference(participantNewParticipantToAttach.getClass(), participantNewParticipantToAttach.getId());
                attachedParticipantNew.add(participantNewParticipantToAttach);
            }
            participantNew = attachedParticipantNew;
            course.setParticipant(participantNew);
            course = em.merge(course);
            for (Participant participantOldParticipant : participantOld) {
                if (!participantNew.contains(participantOldParticipant)) {
                    participantOldParticipant.getCourses().remove(course);
                    participantOldParticipant = em.merge(participantOldParticipant);
                }
            }
            for (Participant participantNewParticipant : participantNew) {
                if (!participantOld.contains(participantNewParticipant)) {
                    participantNewParticipant.getCourses().add(course);
                    participantNewParticipant = em.merge(participantNewParticipant);
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
                Long id = course.getId();
                if (findCourse(id) == null) {
                    throw new NonexistentEntityException("The course with id " + id + " no longer exists.");
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
            Course course;
            try {
                course = em.getReference(Course.class, id);
                course.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The course with id " + id + " no longer exists.", enfe);
            }
            Collection<Participant> participant = course.getParticipant();
            for (Participant participantParticipant : participant) {
                participantParticipant.getCourses().remove(course);
                participantParticipant = em.merge(participantParticipant);
            }
            em.remove(course);
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

  public List<CourseDTO> findCourseEntities() {
        return findCourseEntities(true, -1, -1);
    }

    public List<CourseDTO> findCourseEntities(int maxResults, int firstResult) {
        return findCourseEntities(false, maxResults, firstResult);
    }

    private List<CourseDTO> findCourseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        Collection<Course> resultQuery;
        Collection<CourseDTO> result = new LinkedList<CourseDTO>();
        CourseDTO courseDTOTemp;
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Course.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }

            resultQuery = q.getResultList();
            for (Course iter : resultQuery) {
                courseDTOTemp = new CourseDTO();
                courseDTOTemp.setName(iter.getName());
                courseDTOTemp.setId(iter.getId());
                result.add(courseDTOTemp);
            }
            return (List<CourseDTO>) result;
        } finally {
            em.close();
        }
    }

    public Course findCourse(Long id) {
        EntityManager em = getEntityManager();

        try {
            return em.find(Course.class, id);

        } finally {
            em.close();
        }
    }

    public Collection<CourseDTO> getCourseForParticipant(Long id) {
        EntityManager em = getEntityManager();
        Collection<Course> resultQuery;
        Collection<CourseDTO> result = new LinkedList<CourseDTO>();
        CourseDTO courseDTOTemp;

        Query q = em.createNamedQuery("Course.getCourseForParticipant", Course.class);
        q.setParameter("id", id);

        resultQuery = (Collection<Course>) q.getResultList();

        for (Course iter : resultQuery) {
            courseDTOTemp = new CourseDTO();
            courseDTOTemp.setName(iter.getName());

            courseDTOTemp.setId(iter.getId());

            result.add(courseDTOTemp);
        }
        return result;
    }
    
    public Collection<CourseDTO> getCourseForUniversity(Long id){
        EntityManager em = getEntityManager();
        Collection<Course> resultQuery;
        Collection<CourseDTO> result = new LinkedList<CourseDTO>();
        CourseDTO courseDTOTemp;

        Query q = em.createNamedQuery("Course.getCourseForUniversity", Course.class);
        q.setParameter("id", id);

        resultQuery = (Collection<Course>) q.getResultList();

        for (Course iter : resultQuery) {
            courseDTOTemp = new CourseDTO();
            courseDTOTemp.setName(iter.getName());

            courseDTOTemp.setId(iter.getId());

            result.add(courseDTOTemp);
        }
        return result;
        
    }

    public Collection<CourseDTO> getNotCourseForParticipant(Long id) {
        EntityManager em = getEntityManager();
        Collection<Course> resultQuery;
        Collection<CourseDTO> result = new LinkedList<CourseDTO>();
        CourseDTO courseDTOTemp;

        Query q = em.createNamedQuery("Course.getNotCourseForParticipant", Course.class);
        q.setParameter("id", id);

        resultQuery = (Collection<Course>) q.getResultList();

        for (Course iter : resultQuery) {
            courseDTOTemp = new CourseDTO();
            courseDTOTemp.setName(iter.getName());

            courseDTOTemp.setId(iter.getId());

            result.add(courseDTOTemp);
        }
        return result;
    }



    public int getCourseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Course> rt = cq.from(Course.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
