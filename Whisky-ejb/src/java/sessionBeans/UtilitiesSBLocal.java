/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.AttendanceDTO;
import java.util.LinkedList;
import javax.ejb.Local;

/**
 *
 * @author Kay
 */
@Local
public interface UtilitiesSBLocal {

    public String stringToMD5(String clear) throws Exception;

    public LinkedList<AttendanceDTO> selectPasswordByEmail(String email);

    public Long selectFirstIdByEmail(String email);

    boolean validateRut(String rut);

    boolean validateEmail(String email);


}
