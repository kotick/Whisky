/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.LectureDTO;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import sessionBeans.CourseManagementSBLocal;
import sessionBeans.CourseStatisticsSBLocal;

/**
 *
 * @author Kay
 */
@Named(value = "courseStatisticsMB")
@RequestScoped
public class CourseStatisticsMB implements Serializable {
    @EJB
    private CourseManagementSBLocal courseManagementSB;
    @EJB
    private CourseStatisticsSBLocal courseStatisticsSB;
    
    
    @Inject
    CourseStatisticsConversation courseStatisticsConversation;
    
    @Inject
    SessionMB session;
    
    private CartesianChartModel graficoCurso;
    private String nombreCurso;
    private int maximoCurso;
    private String fechaClase;
    private Long idCurso;
    
    
    public CourseStatisticsMB() {
        
    }
    
     @PostConstruct
    void init() {
     
     idCurso= courseStatisticsConversation.getIdCourse();
     nombreCurso = courseManagementSB.getCourse(idCurso).getName();
     maximoCurso = courseStatisticsSB.getTotalQuantityBy(idCurso);
     createGraficoCurso();
     }
    
    

    private void createGraficoCurso() {
        System.out.println("El Id del curso es");
        System.out.println(courseStatisticsConversation.getIdCourse());
        
        graficoCurso = new CartesianChartModel();
        System.out.println("El nombre del curso es");
        System.out.println(nombreCurso);
        System.out.println("Maximo de alumnos es");
        System.out.println(maximoCurso);
        ChartSeries curso = new ChartSeries(nombreCurso);
        
        //Sacar primero todos los lecture de un Curso dado
        Collection<LectureDTO> lectures = courseStatisticsSB.getLectureByCourseId(idCurso);
        
        //Luego en la tabla attendance ver cada elemento que cruce con el id de la lecture y del curso
        //Y sumar uno al contador cuando en esa attendance está presente
        boolean total =true;
        
        for(LectureDTO iter:lectures){
            
            curso.set(iter.getDate(),courseStatisticsSB.getPresentQuantityBy(iter.getId()));
        }
        
        
        graficoCurso.addSeries(curso);
        
        
    }
    


    public CartesianChartModel getGraficoCurso() {
        return graficoCurso;
    }

    public void setGraficoCurso(CartesianChartModel graficoCurso) {
        this.graficoCurso = graficoCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public int getMaximoCurso() {
        return maximoCurso;
    }

    public void setMaximoCurso(int maximoCurso) {
        this.maximoCurso = maximoCurso;
    }
    
    
}
