package DTOs;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

@Stateless
@LocalBean
public class ParticipantDTO {
    /* TODO agregar correo y rut para mostrarlos en la lista */
    private String firstName;
    private String lastName;
<<<<<<< HEAD
<<<<<<< HEAD
    private String photo;
    
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
=======
=======
>>>>>>> 21c8e5a8b5881f85e0006d3f1335317c06e875cd
    
    private String rut;
    
    
    private Long id;

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
<<<<<<< HEAD
>>>>>>> 21c8e5a8b5881f85e0006d3f1335317c06e875cd
=======
>>>>>>> 21c8e5a8b5881f85e0006d3f1335317c06e875cd
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


}
