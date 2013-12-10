/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.AttendanceDTO;
import DTOs.ParticipantDTO;
import entity.Attendance;
import com.sun.mail.smtp.DigestMD5;
import entity.Participant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
    @EJB
    private UtilitiesSBLocal utilitiesSB;
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;
   

    

    public ParticipantManagementSB() {
    }
    
    @Override
    public boolean checkEmailPassword(String email, String password){
        
         LinkedList<AttendanceDTO> lista = utilitiesSB.selectPasswordByEmail(email);
        
        String password_list=lista.getFirst().getPassword();
        try {
            String pass_md5= utilitiesSB.stringToMD5(password_list);
        } catch (Exception ex) {
            Logger.getLogger(ParticipantManagementSB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (password.equals(password_list)){
            return true;
        }
        else{return false;}
      
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

    public void persist(Object object) {
        em.persist(object);
    }
}
