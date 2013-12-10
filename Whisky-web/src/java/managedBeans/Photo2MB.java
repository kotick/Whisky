/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.event.CaptureEvent;
import sessionBeans.PhotoManagementSBLocal;

/**
 *
 * @author kotick
 */
@Named(value = "photo2MB")
@RequestScoped
public class Photo2MB {
    @EJB
    private PhotoManagementSBLocal photoManagementSB;
    private long id;
    /**
     * Creates a new instance of Photo2MB
     */
    public Photo2MB() {
    }
    
    public void oncapture(CaptureEvent captureEvent) {  
        
        id =1;
        System.out.println("entro a oncapture");
         byte[] foto = captureEvent.getData();
         
        if (photoManagementSB.save_predict(foto, id)){
        
            System.out.println("yey te reconozco");
            //Crear la clase con la foto, y notificar que el wn está logeado
        
        }
        else{
            System.out.println("No te reconozco");
        }
    }

}