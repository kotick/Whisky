/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import classes.photoConfirmation;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.FacesException;
import javax.imageio.stream.FileImageOutputStream;

/**
 *
 * @author Kay
 */
@Stateless
public class PhotoManagementSB implements PhotoManagementSBLocal {
    @EJB
    private FaceRecognizerSBLocal faceRecognizerSB;
    
    
    
    @Override
    
      public photoConfirmation save_predict(byte[] foto, Long id){    
        
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
        
    photoConfirmation confirmacion= new photoConfirmation();
    confirmacion.setValidado(faceRecognizerSB.predict(newFileName, id));
    confirmacion.setDireccionFoto(newFileName);
    return confirmacion;
    }
}
