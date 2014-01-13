package sessionBeans;

import DTOs.CourseDTO;
import entity.Course;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CourseManagementSB implements CourseManagementSBLocal {

    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public CourseManagementSB() {
    }

    @Override
    public Collection<CourseDTO> selectCoursesByTeacher(String username, Long id) {
        Collection<Course> resultQuery;
        Collection<CourseDTO> result = new LinkedList<CourseDTO>();
        CourseDTO userDTOTemp;
        Query q = em.createNamedQuery("Course.getCursos", Course.class);
        q.setParameter("usernameMail", username);
        q.setParameter("idUniversity", id);

        resultQuery = (Collection<Course>) q.getResultList();
        for (Course iter : resultQuery) {
            userDTOTemp = new CourseDTO();
            userDTOTemp.setName(iter.getName());
            userDTOTemp.setId(iter.getId());
            result.add(userDTOTemp);
        }
        return result;
    }

    @Override
    public Course getCourse(Long idCourse) {
        Query q = em.createNamedQuery("Course.getCurso", Course.class);
        q.setParameter("idCourse", idCourse);
        return (Course) q.getSingleResult();
    }
}
