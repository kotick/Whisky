/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.LectureDTO;
import entity.Course;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author kotick
 */
@Local
public interface LectureManagementSBLocal {

    public Collection<LectureDTO> selectLectureByCourses(Long id);

    void createLecture(String date, String timeIni, String timeFin,Course Course);
    
}
