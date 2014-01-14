/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.AttendanceDTO;
import DTOs.LectureDTO;
import entity.Attendance;
import entity.Lecture;
import entity.Participant;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kay
 */
@Stateless
public class CourseStatisticsSB implements CourseStatisticsSBLocal {
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    
    
    @Override
    public Collection<LectureDTO> getLectureByCourseId(Long idCourse){
    
        Collection<Lecture> resultQuery;
        Collection<LectureDTO> result =new LinkedList<>();
        LectureDTO lectureDTOTemp;
        
        Query q = em.createNamedQuery("Lecture.getLecturebyCourseId", Lecture.class);
        q.setParameter("idCourse", idCourse);

        resultQuery = (Collection<Lecture>) q.getResultList();
        for (Lecture iter : resultQuery) {
            
            lectureDTOTemp = new LectureDTO();
            lectureDTOTemp.setId(iter.getId());
            lectureDTOTemp.setDate(iter.getFecha());
            result.add(lectureDTOTemp);
        }
        return result;
    
    }
    
    @Override
    public int getPresentQuantityBy(Long idLecture){
    
        Collection<Attendance> resultQuery;
        Collection<LectureDTO> result =new LinkedList<>();
        LectureDTO lectureDTOTemp;
        int temp=0;
        
        Query q = em.createNamedQuery("Attendance.getAttendanceByIdLecture", Attendance.class);
        q.setParameter("idLecture", idLecture);

        resultQuery = (Collection<Attendance>) q.getResultList();
        for (Attendance iter : resultQuery) {
            
            if(iter.isPresent()){
            
                temp++;
            }
        }
        return temp;
    
    }

    @Override
     public int getTotalQuantityBy(Long idCurso){
    
        Long idLecture = this.getSingleLectureByCourseId(idCurso);
        Collection<Attendance> resultQuery;
        
        int temp=0;
        
        Query q = em.createNamedQuery("Attendance.getAttendanceByIdLecture", Attendance.class);
        q.setParameter("idLecture", idLecture);

        resultQuery = (Collection<Attendance>) q.getResultList();
        return resultQuery.size();
        
        
    }
    public void persist(Object object) {
        em.persist(object);
    }

    private Long getSingleLectureByCourseId(Long idCurso) {
        
        Collection<Lecture> resultQuery;
        Collection<LectureDTO> result =new LinkedList<>();
        LectureDTO lectureDTOTemp;
        
        Query q = em.createNamedQuery("Lecture.getLecturebyCourseId", Lecture.class);
        q.setParameter("idCourse", idCurso);

        resultQuery = (Collection<Lecture>) q.getResultList();
        Lecture lecture =(Lecture)q.getSingleResult();
        return lecture.getId();
        
        
       
    }

}
