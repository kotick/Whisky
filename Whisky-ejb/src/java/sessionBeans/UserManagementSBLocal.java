/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.UsuarioDTO;
import java.util.LinkedList;
import javax.ejb.Local;

/**
 *
 * @author kotick
 */
@Local
public interface UserManagementSBLocal {

    LinkedList<UsuarioDTO> selectAllUser();
    
}
