package sessionBeans;

import entity.Attendance;
import entity.Lecture;
import entity.Participant;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AttendanceManagementSB implements AttendanceManagementSBLocal {
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    
    public AttendanceManagementSB() {
    }

    @Override
    public void addAttendance(Participant participant, Lecture lecture){
    
          Attendance clase = new Attendance();
          clase.setLecture(lecture);
          clase.setParticipant(participant);
          clase.setPresent(true);
          em.persist(clase);
          
    
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
