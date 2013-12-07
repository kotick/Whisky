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

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import sessionBeans.FaceRecognizerSBLocal;

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
        //numero=faceRecognizerSB.prueba();
        System.out.println(numero);
        
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
    
    /*    public void FaceRecognizer(){
     * System.out.println("entre al face recog =D");
     * int i=  faceRecognizerSB.prueba();
     * System.out.println("Respuesta "+ i);
     * //return i;
     * 
     * }*/
}
