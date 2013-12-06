package sessionBeans;

import DTOs.CourseDTO;
import entity.Course;
import entity.CourseParticipant;
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
    public LinkedList<CourseDTO> selectCoursesByTeacher() {
        Collection<CourseParticipant> resultQuery;
        LinkedList<CourseDTO> result = new LinkedList<CourseDTO>();
        CourseDTO userDTOTemp;
        Query q = em.createNamedQuery("CourseParticipant.getCursos", CourseParticipant.class);
        q.setParameter("idparticipant", 0);
        
        resultQuery = (Collection<CourseParticipant>) q.getResultList();
        for(CourseParticipant iter: resultQuery){
            userDTOTemp = new CourseDTO();
            userDTOTemp.setName(iter.getCourse().getName());
            userDTOTemp.setId(iter.getCourse().getId());
            result.add(userDTOTemp);
        }
        return result;
    }
    
}
