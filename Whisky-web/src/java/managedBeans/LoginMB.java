package managedBeans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.primefaces.event.CaptureEvent;

@Named(value = "loginMB")
@RequestScoped
public class LoginMB {
    @Inject SessionMB session;
    
    private String email;
    private String password;
    
    public LoginMB() {
    }
    
    public void login(){
        session.login(email, password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
      
    private String getRandomImageName() {  
        int i = (int) (Math.random() * 10000000);  
          
        return String.valueOf(i);  
    }  
  
  
      
    public void oncapture(CaptureEvent captureEvent) {  
        System.out.println("yey saqué una foto");
        String photo = getRandomImageName();  
        byte[] data = captureEvent.getData();  
          
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();  
         String newFileName = "C:\\fotos\\" + photo + ".png";  
          
        FileImageOutputStream imageOutput;  
        try {  
            imageOutput = new FileImageOutputStream(new File(newFileName));  
            System.out.println("pse estoy yey");
            imageOutput.write(data, 0, data.length);  
            System.out.println("pase esto otro yey");
            imageOutput.close();  
            System.out.println("termine de todo yey");
        }  
        catch(Exception e) {  
            throw new FacesException("Error in writing captured image.");  
        }  
    
    }
}
