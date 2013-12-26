/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.LectureDTO;
import entity.Course;
import entity.Lecture;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author kotick
 */
@Local
public interface LectureManagementSBLocal {

    public Collection<LectureDTO> selectLectureByCourses(Long id);

    Long createLecture(String date, String timeIni, String timeFin,Course Course);

    Lecture getLecturebyId(Long idLecture);

    Lecture getLecturebyCourse(Long idCourse);

    public void fillLecture(Long idLecture);
    

      
}
