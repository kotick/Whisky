/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.Local;

/**
 *
 * @author Kay
 */
@Local
public interface PhotoManagementSBLocal {

    public boolean save_predict(byte[] foto, long id);
    
}
