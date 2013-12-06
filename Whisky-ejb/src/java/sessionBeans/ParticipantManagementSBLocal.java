/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.ParticipantDTO;
import java.util.LinkedList;
import javax.ejb.Local;

/**
 *
 * @author kotick
 */
@Local
public interface ParticipantManagementSBLocal {

    LinkedList<ParticipantDTO> selectAllUser();
    
}
