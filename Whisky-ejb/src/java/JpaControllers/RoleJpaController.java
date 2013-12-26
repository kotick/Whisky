/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JpaControllers;

import entity.Participant;
import entity.Role;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yani
 */
public class RoleJpaController implements Serializable {

    public RoleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Role getRol(String rol) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Role.getRol", Role.class);
        q.setParameter("rol", rol);
        return (Role) q.getSingleResult();
    }
}
