package managedBeans;

import DTOs.ParticipantDTO;
import java.io.IOException;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import sessionBeans.FaceRecognizerSBLocal;
import sessionBeans.ParticipantManagementSBLocal;

@Named(value = "homeMB")
@RequestScoped
public class HomeMB {
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    private LinkedList<ParticipantDTO> participantsList;
   
    public HomeMB() {
    }
    
    @PostConstruct
    void init(){
        participantsList= participantManagementSB.selectAllUser();
<<<<<<< HEAD
=======
        //numero=faceRecognizerSB.prueba();
        System.out.println(numero);

      // numero=faceRecognizerSB.prueba();
        //System.out.println(numero);
        
>>>>>>> c1dc782c7b22d6bc3b236f7b852170e8d55ea080
    }
    
    public void redirectToPage(String toUrl) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            System.out.println(ctx);
            ExternalContext extContext = ctx.getExternalContext();
            System.out.println(extContext);
            String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, toUrl));
            System.out.println(url);
            extContext.redirect(url);
            
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }
    
    public LinkedList<ParticipantDTO> getParticipantsList() {
        System.out.println("oli");
        return participantsList;
    }

    public void setParticipantsList(LinkedList<ParticipantDTO> participantsList) {
        this.participantsList = participantsList;
    }
}
