/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.UserDTO;
import entity.User;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kotick
 */
@Stateless
public class UserManagementSB implements UserManagementSBLocal {
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public LinkedList<UserDTO> selectAllUser() {
        Collection<User> resultQuery;
        LinkedList<UserDTO> result = new LinkedList<UserDTO>();
        UserDTO userDTOTemp;
        Query q = em.createNamedQuery("User.getAllUser", User.class);        
        resultQuery = (Collection<User>) q.getResultList();
        for(User iter: resultQuery){
            userDTOTemp = new UserDTO();
            userDTOTemp.setFirstName(iter.getFirstName());
            userDTOTemp.setLastName(iter.getLastName());
            result.add(userDTOTemp);
        }
        return result;
    }
    
    
    
}
