/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.primefaces.event.CaptureEvent;
import sessionBeans.PhotoManagementSBLocal;

/**
 *
 * @author Kay
 */
@Named(value = "photoMB")
@RequestScoped
public class PhotoMB {
    @Inject PhotoConversationMB photoConversationMB;
    @Inject SessionMB session;
    @EJB
    private PhotoManagementSBLocal photoManagementSB;

    private long id;
    public PhotoMB() {
    }
    @PostConstruct
    void init(){
        id = photoConversationMB.getId();
        
    }
     public void oncapture(CaptureEvent captureEvent) {  
        
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
