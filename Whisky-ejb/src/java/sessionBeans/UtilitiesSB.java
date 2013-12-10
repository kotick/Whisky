/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;


import DTOs.AttendanceDTO;
import entity.Participant;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kay
 */
@Stateless
public class UtilitiesSB implements UtilitiesSBLocal {
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public UtilitiesSB() {
    }
    @Override
    public LinkedList<AttendanceDTO> selectPasswordByEmail(String email) {
        
        Collection<Participant> resultQuery ;
        LinkedList<AttendanceDTO> result = new LinkedList<>();
        AttendanceDTO userDTOTemp;
        Query q = em.createNamedQuery("Participant.getPassword", Participant.class);
        q.setParameter("username", email);
        
        resultQuery = (Collection<Participant>) q.getResultList();
        for(Participant iter: resultQuery){
            userDTOTemp = new AttendanceDTO();
            userDTOTemp.setPassword(iter.getPassword());
            result.add(userDTOTemp);
        }
        return result;
    } 
    @Override
   public Long selectFirstIdByEmail(String email) {
        
        Collection<Participant> resultQuery ;
        LinkedList<AttendanceDTO> result = new LinkedList<AttendanceDTO>();
        AttendanceDTO userDTOTemp;
        Query q = em.createNamedQuery("Participant.getId", Participant.class);
        q.setParameter("username", email);
        
        resultQuery = (Collection<Participant>) q.getResultList();
        for(Participant iter: resultQuery){
            userDTOTemp = new AttendanceDTO();
            userDTOTemp.setId(iter.getId());
            result.add(userDTOTemp);
        }
       return result.getFirst().getId();
     
    } 
    @Override
    public String stringToMD5(String clear) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] b = md.digest(clear.getBytes());

    int size = b.length;
    StringBuffer h = new StringBuffer(size);
    for (int i = 0; i < size; i++) {
    int u = b[i] & 255;
    if (u < 16) {
    h.append("0").append(Integer.toHexString(u));
    } else {
    h.append(Integer.toHexString(u));
    }
    }
    return h.toString();
}

}
