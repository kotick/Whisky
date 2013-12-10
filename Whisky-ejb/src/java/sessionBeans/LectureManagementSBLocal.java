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
 * @author kotick
 */
@Local
public interface LectureManagementSBLocal {

    public Collection<LectureDTO> selectLectureByCourses(Long id);
    
}
