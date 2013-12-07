/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
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
                try {
                    request.login(email, password);

                } catch (ServletException e) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o contraseña incorrecta", "Login inválido"));
                }
            }
            else {
                System.out.println("Error, Usuario ya conectado");
            }
        }
        catch (Exception e) {
            System.out.println("error(loginMB-login): "+e.getMessage());
        }
    }
    public String logout() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.getUserPrincipal() != null) {
            try {
                request.logout();
                ExternalContext ext = context.getExternalContext();
                context.getExternalContext().redirect(ext.getRequestContextPath());
            } catch (ServletException e) {
                System.out.println("Ha ocurrido un erro, no se ha podido desloguear" + e.getMessage());
                return "";
            } catch (IOException ex) {
                Logger.getLogger(SessionMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }
}
