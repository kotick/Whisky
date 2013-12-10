/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.AttendanceDTO;
import java.io.File;
import java.util.LinkedList;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import sessionBeans.PhotoManagementSBLocal;
import org.primefaces.event.CaptureEvent;
import sessionBeans.ParticipantManagementSBLocal;
import sessionBeans.UtilitiesSBLocal;

/**
 *
 * @author Kay
 */
@Named(value = "attendanceMB")
@RequestScoped
public class AttendanceMB {
    @EJB
    private UtilitiesSBLocal utilitiesSB;
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    @EJB
    private PhotoManagementSBLocal photoManagementSB;

    private String email;
    private String password;
    
    /**
     * Creates a new instance of AttendanceMB
     */
    public AttendanceMB() {
    }
    
    public void checkEmailPassword(){
    
        System.out.println(email);
        System.out.println(password);
        
        if(participantManagementSB.checkEmailPassword(email, password)){
            long id = utilitiesSB.selectFirstIdByEmail(email);
            
                
                //Comprobar si el wn que se logea pertenece al curso
                // Si pertenece al curso, que se vaya a la otra vista de la foto
                //sino que te diga que no pertenece al curso
               
               
            }
            
            
       else{
           System.out.println("Usuario o contraseña incorrectos");
       }
        
        
    }
    
    
   

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    }

