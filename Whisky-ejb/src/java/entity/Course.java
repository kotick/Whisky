package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Course.getCursos", query = "SELECT c FROM Course c, Participant p WHERE p.email = :usernameMail and c member of p.courses"),
    @NamedQuery(name = "Course.getCurso", query = "SELECT c FROM Course c WHERE c.id =:idCourse"),
    @NamedQuery(name = "Course.getCourseForParticipant", query = "SELECT c FROM Participant u, Course c WHERE u.id = :id AND c member of u.courses"),
    @NamedQuery(name = "Course.getNotCourseForParticipant", query = "SELECT c FROM Participant u, Course c WHERE u.id = :id AND  c not member of u.courses"),
    @NamedQuery(name = "Course.getCourseForUniversity", query = "SELECT c FROM Course c WHERE c.university.id= :id"),

})
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer attendanceRequired;
    @ManyToOne
    private University university;
    
    @ManyToMany(mappedBy = "courses")
    private Collection<Participant> participant;

    public Collection<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(Collection<Participant> participant) {
        this.participant = participant;
    }

    public Integer getAttendanceRequired() {
        return attendanceRequired;
    }

    public void setAttendanceRequired(Integer attendanceRequired) {
        this.attendanceRequired = attendanceRequired;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Curso[ id=" + id + " ]";
    }
    
}
