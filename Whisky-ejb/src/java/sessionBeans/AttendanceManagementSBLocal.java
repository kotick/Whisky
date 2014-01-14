/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entity.Lecture;
import entity.Participant;
import javax.ejb.Local;

/**
 *
 * @author Kay
 */
@Local
public interface AttendanceManagementSBLocal {

    public void addAttendance(Participant participant, Lecture lecture);

    public void addAttendance(Participant participant, Lecture lecture, String photoLocation);
}
