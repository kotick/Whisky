/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import java.io.File;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;

/**
 *
 * @author Kay
 */
@Stateless
public class PhotoManagementSB implements PhotoManagementSBLocal {
    @EJB
    private FaceRecognizerSBLocal faceRecognizerSB;
    
    
    
    @Override
    public void save_predict(byte [] foto, long id, boolean reconocido, String direccion_foto) {
        
        
       DateFormat df = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
       Date today = Calendar.getInstance().getTime();        
       String date_name = df.format(today);
        
            
            //String newFileName = "./"+ date_name + ".jpg";  
        String newFileName = "./photos/test/"+ date_name + ".jpg"; 
        FileImageOutputStream imageOutput;  
        try {  
            imageOutput = new FileImageOutputStream(new File(newFileName));  
            imageOutput.write(foto, 0, foto.length);
            imageOutput.close();
        }  
        catch(Exception e) {  
            throw new FacesException("Error en escribir la fotografia");  
        }
        
    reconocido = faceRecognizerSB.predict(newFileName, id);
    direccion_foto= newFileName;
    }
}
