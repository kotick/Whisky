/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.CourseDTO;
import entity.Course;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Local;

/**
 *
 * @author kotick
 */
@Local
public interface CourseManagementSBLocal {

    public Collection<CourseDTO> selectCoursesByTeacher(String username);

    Course getCourse(Long idCourse);
}
