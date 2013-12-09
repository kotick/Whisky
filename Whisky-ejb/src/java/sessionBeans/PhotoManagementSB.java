/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import java.io.File;
import java.security.Timestamp;
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

    
    public int save_predict(byte [] foto, int id) {
        
        Date date = new Date();
        String fecha = date.toString();
        
        String photo = id +"-"+fecha;
        System.out.println(photo);
        int i = (int) (Math.random() * 10000000);  
          
     
      
            String newFileName = "C:\\fotos\\" + i + ".jpg";  

            FileImageOutputStream imageOutput;  
            try {  
                imageOutput = new FileImageOutputStream(new File(newFileName));  
                imageOutput.write(foto, 0, foto.length);
                imageOutput.close();
            }  
            catch(Exception e) {  
                throw new FacesException("Error en escribir la fotografia");  
            }
        
    return faceRecognizerSB.predict(newFileName);
    }
}
