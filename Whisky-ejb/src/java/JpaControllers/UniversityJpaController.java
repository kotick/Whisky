/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JpaControllers;

import JpaControllers.exceptions.NonexistentEntityException;
import JpaControllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Participant;
import entity.University;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yani
 */
public class UniversityJpaController implements Serializable {

    public UniversityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(University university) throws RollbackFailureException, Exception {
        if (university.getParticipants() == null) {
            university.setParticipants(new ArrayList<Participant>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Participant> attachedParticipants = new ArrayList<Participant>();
            for (Participant participantsParticipantToAttach : university.getParticipants()) {
                participantsParticipantToAttach = em.getReference(participantsParticipantToAttach.getClass(), participantsParticipantToAttach.getId());
                attachedParticipants.add(participantsParticipantToAttach);
            }
            university.setParticipants(attachedParticipants);
            em.persist(university);
            for (Participant participantsParticipant : university.getParticipants()) {
                participantsParticipant.getUniversities().add(university);
                participantsParticipant = em.merge(participantsParticipant);
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

    public void edit(University university) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            University persistentUniversity = em.find(University.class, university.getId());
            List<Participant> participantsOld = persistentUniversity.getParticipants();
            List<Participant> participantsNew = university.getParticipants();
            List<Participant> attachedParticipantsNew = new ArrayList<Participant>();
            for (Participant participantsNewParticipantToAttach : participantsNew) {
                participantsNewParticipantToAttach = em.getReference(participantsNewParticipantToAttach.getClass(), participantsNewParticipantToAttach.getId());
                attachedParticipantsNew.add(participantsNewParticipantToAttach);
            }
            participantsNew = attachedParticipantsNew;
            university.setParticipants(participantsNew);
            university = em.merge(university);
            for (Participant participantsOldParticipant : participantsOld) {
                if (!participantsNew.contains(participantsOldParticipant)) {
                    participantsOldParticipant.getUniversities().remove(university);
                    participantsOldParticipant = em.merge(participantsOldParticipant);
                }
            }
            for (Participant participantsNewParticipant : participantsNew) {
                if (!participantsOld.contains(participantsNewParticipant)) {
                    participantsNewParticipant.getUniversities().add(university);
                    participantsNewParticipant = em.merge(participantsNewParticipant);
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
                Long id = university.getId();
                if (findUniversity(id) == null) {
                    throw new NonexistentEntityException("The university with id " + id + " no longer exists.");
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
            University university;
            try {
                university = em.getReference(University.class, id);
                university.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The university with id " + id + " no longer exists.", enfe);
            }
            List<Participant> participants = university.getParticipants();
            for (Participant participantsParticipant : participants) {
                participantsParticipant.getUniversities().remove(university);
                participantsParticipant = em.merge(participantsParticipant);
            }
            em.remove(university);
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

    public List<University> findUniversityEntities() {
        return findUniversityEntities(true, -1, -1);
    }

    public List<University> findUniversityEntities(int maxResults, int firstResult) {
        return findUniversityEntities(false, maxResults, firstResult);
    }

    private List<University> findUniversityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(University.class));
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

    public University findUniversity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(University.class, id);
        } finally {
            em.close();
        }
    }

    public int getUniversityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<University> rt = cq.from(University.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
