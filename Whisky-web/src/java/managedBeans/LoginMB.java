package managedBeans;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import sessionBeans.FaceRecognizerSBLocal;

@Named(value = "loginMB")
@RequestScoped
public class LoginMB {
    @EJB
    private FaceRecognizerSBLocal faceRecognizerSB;
    @Inject SessionMB session;
    
    private String email;
    private String password;
    private static boolean  entrenado = false;
    
    public LoginMB() {
    }
    
    @PostConstruct
    void init(){
        
        if (!entrenado){
            faceRecognizerSB.train();
            entrenado=true;
        }
    
    }
    public void login(){
        session.login(email, password);
    }
    
    
    public void logout() throws IOException{
        session.logout();
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
  
}
