/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.ParticipantDTO;
import entity.Attendance;
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
    public Collection<ParticipantDTO> selectParticipantByLecture(Long id) {
        Collection<Attendance> resultQuery;
        Collection<ParticipantDTO> result = new LinkedList<ParticipantDTO>();
        ParticipantDTO participantDTOTemp;
        Query q = em.createNamedQuery("Attendance.getParticipantByLecture", Attendance.class);
        q.setParameter("idlecture", id);
        
        resultQuery = (Collection<Attendance>) q.getResultList();
        for(Attendance iter: resultQuery){
            participantDTOTemp = new ParticipantDTO();
            participantDTOTemp.setFirstName(iter.getParticipant().getFirstName());
            participantDTOTemp.setLastName(iter.getParticipant().getLastName());
            result.add(participantDTOTemp);
        }
        return result;
    }
}
