package managedBeans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

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
    
}
