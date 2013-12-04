/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.UsuarioDTO;
import entity.Usuario;
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
    public LinkedList<UsuarioDTO> selectAllUser() {
        Collection<Usuario> resultQuery;
        LinkedList<UsuarioDTO> result = new LinkedList<UsuarioDTO>();
        UsuarioDTO userDTOTemp;
        Query q = em.createNamedQuery("User.getAllUser", Usuario.class);        
        resultQuery = (Collection<Usuario>) q.getResultList();
        for(Usuario iter: resultQuery){
            userDTOTemp = new UsuarioDTO();
            userDTOTemp.setNombre(iter.getNombre());
            userDTOTemp.setApellido(iter.getNombre());
            result.add(userDTOTemp);
        }
        return result;
    }
    
    
    
}
