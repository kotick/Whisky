/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author kotick
 */
@Named(value = "sessionMB")
@SessionScoped
public class SessionMB implements Serializable {

    public SessionMB() {
    }
    public void login(String email, String password){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            if (request.getRemoteUser() == null) {
                System.out.println("email: "+email+"   password: "+password);
                request.login(email, password);
                
            }
            else {
                System.out.println("Error, Usuario ya conectado");
            }
        }
        catch (Exception e) {
            System.out.println("error(loginMB-login): "+e.getMessage());
        }
    }
}
