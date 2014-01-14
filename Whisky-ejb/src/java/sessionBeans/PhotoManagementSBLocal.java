/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import classes.photoConfirmation;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.ejb.Local;

/**
 *
 * @author Kay
 */
@Local
public interface PhotoManagementSBLocal {

    public photoConfirmation save_predict(byte[] foto, Long idParticipant);
}
