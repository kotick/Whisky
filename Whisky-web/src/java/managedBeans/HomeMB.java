/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.ParticipantDTO;
import java.io.IOException;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
<<<<<<< HEAD
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
=======
import sessionBeans.FaceRecognizerSBLocal;
>>>>>>> Lógica de Reconocer rostros incluida en prueba
import sessionBeans.ParticipantManagementSBLocal;
import sessionBeans.oliSD;
import sessionBeans.oliSDLocal;

/**
 *
 * @author kotick
 */
@Named(value = "homeMB")
@RequestScoped
public class HomeMB {
    @EJB
    private FaceRecognizerSBLocal faceRecognizerSB;
  
   
    
    @EJB
    private ParticipantManagementSBLocal participantManagementSB;
    private LinkedList<ParticipantDTO> participantsList;
    

    
    
    private int numero=0;
   
    
    public HomeMB() {
    }
    
    @PostConstruct
    void init(){
        participantsList= participantManagementSB.selectAllUser();
       numero=faceRecognizerSB.prueba();
        System.out.println(numero);
        
    }
    
    public void redirect(){
        System.out.println("casa");
        try {  
            HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();  
            FacesContext.getCurrentInstance().responseComplete();  
            response.sendRedirect("home.xhtml");
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
    }
    public LinkedList<ParticipantDTO> getParticipantsList() {
        System.out.println("oli");
        return participantsList;
    }

    public void setParticipantsList(LinkedList<ParticipantDTO> participantsList) {
        this.participantsList = participantsList;
    }
    
    /*    public void FaceRecognizer(){
     * System.out.println("entre al face recog =D");
     * int i=  faceRecognizerSB.prueba();
     * System.out.println("Respuesta "+ i);
     * //return i;
     * 
     * }*/
}
