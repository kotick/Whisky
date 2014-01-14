/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.LectureDTO;
import entity.Attendance;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kay
 */
@Stateless
public class studentStatisticsSB implements studentStatisticsSBLocal {
    @EJB
    private CourseStatisticsSBLocal courseStatisticsSB;
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    @Override
     public double getPercetageAssistance(Long idAlumno, Long idCurso){
    
        //Buscar todos los cursos del alumno X
        // Por cada curso, sacar el nombre del curso, todas las clases del curso 
        //y todas las clases donde él ha estado presente
         
         
        int totalClases =0;
        int asistencias=0;
        
        Collection<LectureDTO> lectures = courseStatisticsSB.getLectureByCourseId(idCurso);
        
        for (LectureDTO lecture: lectures){
            totalClases++;
            Collection<Attendance> resultQuery;
            Query q = em.createNamedQuery("Attendance.getAttendanceByIdLecture", Attendance.class);
            q.setParameter("idLecture", lecture.getId());
            resultQuery = (Collection<Attendance>) q.getResultList();
            for (Attendance iter : resultQuery) {

                if(iter.isPresent()  && (iter.getParticipant().getId()== idAlumno)){

                    asistencias++;
                }
            }
        }
        
        if (totalClases>0){
        return (asistencias/totalClases)*100;
        }
        
        else{return 100;}
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
