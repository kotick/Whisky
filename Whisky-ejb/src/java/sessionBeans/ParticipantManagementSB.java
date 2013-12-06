/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.ParticipantDTO;
import entity.Participant;
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
public class ParticipantManagementSB implements ParticipantManagementSBLocal {
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public ParticipantManagementSB() {
    }

    @Override
    public LinkedList<ParticipantDTO> selectAllUser() {
        Collection<Participant> resultQuery;
        LinkedList<ParticipantDTO> result = new LinkedList<ParticipantDTO>();
        ParticipantDTO userDTOTemp;
        Query q = em.createNamedQuery("Participant.getAllUser", Participant.class);        
        resultQuery = (Collection<Participant>) q.getResultList();
        for(Participant iter: resultQuery){
            userDTOTemp = new ParticipantDTO();
            userDTOTemp.setFirstName(iter.getFirstName());
            userDTOTemp.setLastName(iter.getLastName());
            result.add(userDTOTemp);
        }
        return result;
    }
}
