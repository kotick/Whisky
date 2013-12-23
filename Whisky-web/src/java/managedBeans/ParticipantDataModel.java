/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

/**
 *
 * @author Yani
 */
import DTOs.ParticipantDTO;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;  
import javax.faces.model.ListDataModel;  
import org.primefaces.model.SelectableDataModel;  
  
public class ParticipantDataModel extends ListDataModel<ParticipantDTO> implements SelectableDataModel<ParticipantDTO> {    
  
    public ParticipantDataModel() {  
    }  
  
    public ParticipantDataModel(LinkedList<ParticipantDTO> data) {  
        super(data);  
    }  
      
    @Override  
    public ParticipantDTO getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<ParticipantDTO> participants = (List<ParticipantDTO>) getWrappedData();  
          
        for(ParticipantDTO participant : participants) {  
            if(participant.getId().equals(rowKey))  
                return participant;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(ParticipantDTO participant) {  
        return participant.getId();  
    }  
} 