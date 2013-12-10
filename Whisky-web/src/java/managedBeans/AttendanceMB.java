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
    
    
    public void oncapture(CaptureEvent captureEvent) {  
        
        System.out.println("entro a oncapture");
        
       if (participantManagementSB.checkEmailPassword(this.getEmail(), this.getPassword())){
    
           byte[] foto = captureEvent.getData();
           
            long id = utilitiesSB.selectFirstIdByEmail(email);
            if (photoManagementSB.save_predict(foto, id)){
            
                System.out.println("Te reconozco");
               
                //if (este alumno pertenece al curso)
                    //agregar al tipo en la base de datos de la clase con la foto
                
                // else decir, tu no perteneces a esa clase
            }
            
            else{
                System.out.println("No te reconozco");}
    
    }
       else{
           System.out.println("Error en el usuario y contraseñá");
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

