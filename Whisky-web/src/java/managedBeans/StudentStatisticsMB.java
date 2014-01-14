/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.CourseDTO;
import DTOs.LectureDTO;
import JpaControllers.CourseJpaController;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import sessionBeans.CourseStatisticsSBLocal;
import sessionBeans.studentStatisticsSBLocal;

/**
 *
 * @author Kay
 */
@Named(value = "studentStatisticsMB")
@RequestScoped
public class StudentStatisticsMB {
    @EJB
    private studentStatisticsSBLocal studentStatisticsSB;
    @EJB
    private CourseStatisticsSBLocal courseStatisticsSB;
    @Inject
    StudentStatisticsConversation  studentStatisticsConversation;
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Whisky-ejbPU");
    @Resource
    UserTransaction utx;
    
    private CourseJpaController cjc;

    private String nombreAlumno;
    private CartesianChartModel graficoEstudiante;
    private Long idAlumno;
    
    
    public StudentStatisticsMB() {
    }
    
    @PostConstruct
    void init(){
    idAlumno=studentStatisticsConversation.getId();
    nombreAlumno = studentStatisticsConversation.getNombreAlumno() + " " + studentStatisticsConversation.getApellidoAlumno();
    createGraficoEstudiante();
    
    }
    private void createGraficoEstudiante() {  
        graficoEstudiante = new CartesianChartModel();  
        cjc=new CourseJpaController(utx, emf);
        String nombreCurso;
        Collection<CourseDTO> listaCursos = cjc.getCourseForParticipant(idAlumno);
        System.out.println("el id del alumno es");
        System.out.println(idAlumno);
        System.out.println("oli hice algo ");
        for (CourseDTO iter:listaCursos){
            
            nombreCurso = iter.getName();
            Long idCurso = iter.getId();
            double asistencia =studentStatisticsSB.getPercetageAssistance(idAlumno, idCurso);
            System.out.println("La asistencia es");
            System.out.println(asistencia);
            ChartSeries curso = new ChartSeries();
            curso.setLabel(nombreCurso);
            curso.set(nombreCurso, asistencia);
            graficoEstudiante.addSeries(curso);            
        }
        
        
        
       
    }  

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public CartesianChartModel getGraficoEstudiante() {
        return graficoEstudiante;
    }

    public void setGraficoEstudiante(CartesianChartModel graficoEstudiante) {
        this.graficoEstudiante = graficoEstudiante;
    }

    public Long getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Long idAlumno) {
        this.idAlumno = idAlumno;
    }
    
    
    
}
