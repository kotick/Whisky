/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.LectureDTO;
import entity.Course;
import entity.Lecture;
import entity.Participant;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
    @Inject AttendanceManagementSBLocal amSB;
            
    

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
            lectureDTOTemp.setTime(iter.getStartingTime());
            result.add(lectureDTOTemp);
        }
        return result;
    }    

    @Override
    public Long createLecture(String date, String timeIni, String timeFin,Course Course) {
        Lecture newLecture = new Lecture();
        newLecture.setDate(date);
        newLecture.setStartingTime(timeIni);
        newLecture.setFinishingTime(timeFin);
        newLecture.setCourse(Course);
        em.persist(newLecture);
        em.flush();
        
        return newLecture.getId();
    }

    @Override
    public Lecture getLecturebyId(Long idLecture) {
        Query q = em.createNamedQuery("Lecture.getLecturebyId", Lecture.class);
        q.setParameter("idLecture", idLecture);
        return (Lecture) q.getSingleResult();
    }

    @Override
    public Lecture getLecturebyCourse(Long idCourse) {
        Query q = em.createNamedQuery("Lecture.getLecture", Lecture.class);
        q.setParameter("idcourse", idCourse);
        return (Lecture) q.getSingleResult();
    }
    
    @Override
    public void fillLecture(Long idLecture){
        Lecture lecture= getLecturebyId(idLecture);
        Collection<Participant> resultQuery=lecture.getCourse().getParticipant();
        for(Participant iter: resultQuery){
            amSB.addAttendance(iter,lecture); 
        }
    }
  

}
