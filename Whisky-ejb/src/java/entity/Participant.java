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
    @NamedQuery(name = "Participant.getAllParticipant", query = "SELECT u FROM Participant u"),
    @NamedQuery(name = "Participant.getParticipantByEmail", query = "SELECT u FROM Participant u WHERE u.email = :username"),
    //Revisar las query, cuales se usan y cuales no.
    @NamedQuery(name = "Participant.getAllUser", query = "SELECT u FROM Participant u"),
    @NamedQuery(name = "Participant.getPassword", query = "SELECT u FROM Participant u WHERE u.email = :username"),
    @NamedQuery(name = "Participant.getId", query = "SELECT u FROM Participant u WHERE u.email = :username"),
    @NamedQuery(name = "Participant.getUser", query = "SELECT u FROM Participant u WHERE u.id = :idParticipant"),
    @NamedQuery(name = "Participant.getAllByTypeAndUniversity", query = "SELECT p FROM Participant p, University u WHERE p.rol.name = :rol and u.id=:idUniversity and u member of p.universities"),
    @NamedQuery(name = "Participant.getAllByType", query = "SELECT p FROM Participant p WHERE p.rol.name = :rol"),
    @NamedQuery(name = "Participant.getParticipantById", query = "SELECT u FROM Participant u WHERE u.id = :id"),
    @NamedQuery(name = "Participant.getParticipantInClass", query = "SELECT u FROM Participant u, Course c WHERE c.id = :id AND u member of c.participant AND u.rol.name = :rol and c.university member of u.universities"),
    @NamedQuery(name = "Participant.getParticipantOutClass", query = "SELECT u FROM Participant u, Course c WHERE c.id = :id AND  u not member of c.participant AND u.rol.name = :rol and c.university member of u.universities"),})
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String rut;
    private String photo;
    private String password;
    @ManyToOne
    private Role rol;
    @ManyToMany
    private Collection<University> universities;
    @ManyToMany
    private Collection<Course> courses;

    public Collection<University> getUniversities() {
        return universities;
    }

    public void setUniversities(Collection<University> universities) {
        this.universities = universities;
    }

    public Collection<Course> getCourses() {
        return courses;
    }

    public void setCourses(Collection<Course> courses) {
        this.courses = courses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
        if (!(object instanceof Participant)) {
            return false;
        }
        Participant other = (Participant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Usuario[ id=" + id + " ]";
    }
}
