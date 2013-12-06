/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.ParticipantDTO;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
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

    public LinkedList<ParticipantDTO> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(LinkedList<ParticipantDTO> participantsList) {
        this.participantsList = participantsList;
    }

}
