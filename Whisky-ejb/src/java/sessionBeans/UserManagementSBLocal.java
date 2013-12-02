/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.UserDTO;
import java.util.LinkedList;
import javax.ejb.Local;

/**
 *
 * @author kotick
 */
@Local
public interface UserManagementSBLocal {

    LinkedList<UserDTO> selectAllUser();
    
}
