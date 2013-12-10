/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.LectureDTO;
import entity.Course;
import entity.Lecture;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kotick
 */
@Stateless
public class LectureManagementSB implements LectureManagementSBLocal {
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public LectureManagementSB() {
    }
    
    @Override
    public Collection<LectureDTO> selectLectureByCourses(Long id) {
        Collection<Lecture> resultQuery;
        Collection<LectureDTO> result = new LinkedList<LectureDTO>();
        LectureDTO lectureDTOTemp;
        Query q = em.createNamedQuery("Lecture.getLecture", Lecture.class);
        q.setParameter("idcourse", id);
        
        resultQuery = (Collection<Lecture>) q.getResultList();
        for(Lecture iter: resultQuery){
            lectureDTOTemp = new LectureDTO();
            lectureDTOTemp.setDate(iter.getDate());
            lectureDTOTemp.setId(iter.getId());
            result.add(lectureDTOTemp);
        }
        return result;
    }    

    @Override
    public void createLecture(String date, String timeIni, String timeFin,Course Course) {
        Lecture newLecture = new Lecture();
        newLecture.setDate(date);
        newLecture.setStartingTime(timeIni);
        newLecture.setFinishingTime(timeFin);
        newLecture.setCourse(Course);
        em.persist(newLecture);
        
        
    }

}
