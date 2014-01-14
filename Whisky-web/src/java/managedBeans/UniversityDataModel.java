/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entity.University;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Yani
 */
class UniversityDataModel extends ListDataModel<University> implements SelectableDataModel<University>{
    public UniversityDataModel() {
    }

    public UniversityDataModel(List<University> data) {
        super(data);
    }

    @Override
    public University getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<University> participants = (List<University>) getWrappedData();

        for (University participant : participants) {
            if (participant.getId().equals(rowKey)) {
                return participant;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(University participant) {
        return participant.getId();
    }
    
}
