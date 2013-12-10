/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.ParticipantDTO;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author kotick
 */
@Local
public interface ParticipantManagementSBLocal {

    public Collection<ParticipantDTO> selectParticipantByLecture(Long id); 
    public boolean checkEmailPassword(String email, String password);

    
}
