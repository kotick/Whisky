package DTOs;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

@Stateless
@LocalBean
public class ParticipantDTO {
    /* TODO agregar correo y rut para mostrarlos en la lista */
    private String firstName;
    private String lastName;
    private String photo;
    private boolean present;

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
    
    private Long id;
    private String rut;
    
    
   

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
    
    }

    
    

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
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
