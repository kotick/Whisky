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
    
    public Collection<CourseDTO> selectCoursesByTeacher(String username) {
        Collection<Course> resultQuery;
        Collection<CourseDTO> result = new LinkedList<CourseDTO>();
        CourseDTO userDTOTemp;
        Query q = em.createNamedQuery("Course.getCursos", Course.class);
        q.setParameter("usernameMail", username);
        
        resultQuery = (Collection<Course>) q.getResultList();
        for(Course iter: resultQuery){
            userDTOTemp = new CourseDTO();
            userDTOTemp.setName(iter.getName());
            userDTOTemp.setId(iter.getId());
            result.add(userDTOTemp);
        }
        return result;
    }
    
}
