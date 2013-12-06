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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import sessionBeans.ParticipantManagementSBLocal;

/**
 *
 * @author kotick
 */
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
        return participantsList;
    }

    public void setParticipantsList(LinkedList<ParticipantDTO> participantsList) {
        this.participantsList = participantsList;
    }

}
