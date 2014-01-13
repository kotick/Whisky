/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

/**
 *
 * @author Yani
 */
import DTOs.CourseDTO;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

public class CourseDataModel extends ListDataModel<CourseDTO> implements SelectableDataModel<CourseDTO> {

    public CourseDataModel() {
    }

    public CourseDataModel(LinkedList<CourseDTO> data) {
        super(data);
    }

    @Override
    public CourseDTO getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<CourseDTO> courses = (List<CourseDTO>) getWrappedData();

        for (CourseDTO course : courses) {
            if (course.getId().equals(rowKey)) {
                return course;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(CourseDTO course) {
        return course.getId();
    }
}
