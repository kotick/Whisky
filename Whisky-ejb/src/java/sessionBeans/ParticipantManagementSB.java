/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.AttendanceDTO;
import DTOs.ParticipantDTO;
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
        password_list= "adsfasdfasd";
        if (password.equals(password_list)){
            return true;
        }
        else{return false;}
      
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

    public void persist(Object object) {
        em.persist(object);
    }
}
