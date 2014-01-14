/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.LectureDTO;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author Kay
 */
@Local
public interface CourseStatisticsSBLocal {

    public Collection<LectureDTO> getLectureByCourseId(Long idCourse);

    public int getPresentQuantityBy(Long idLecture);

    public int getTotalQuantityBy(Long idLecture);
    
}
