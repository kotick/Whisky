package managedBeans;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named(value = "sessionMB")
@SessionScoped
public class SessionMB implements Serializable {
    @Inject LoginConversationMB loginConversation;
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
                    request.getRemoteUser();
                    this.loginConversation.beginConversation();
                    this.loginConversation.setUsername(email);
        
                    redirect("/faces/teacher/course.xhtml?cid=".concat(this.loginConversation.getConversation().getId().toString()));
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
        if (request.getUserPrincipal()!= null) {
            try {
                request.logout();
                
                redirectLogin(request);
            } catch (ServletException e) {
                System.out.println("Ha ocurrido un error, no se ha podido desloguear" + e.getMessage());
                return "";
            }
        
        }
        System.out.println("mal el logout");
        return "";
    }
    public boolean isLogin(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();      
        if (request.getRemoteUser() == null) {
            return false;
        }
        else{
            return true;
        }
    }
    
    public void redirect(String page){
        ExternalContext extcon = FacesContext.getCurrentInstance().getExternalContext();
        try{
            extcon.redirect(extcon.getRequestContextPath() + page);
        }
        catch(IOException ex){
            System.out.println("No se ha podido redirigir a la página ".concat(page));            
        }
    }
    
    private void redirectLogin(HttpServletRequest request){
        ExternalContext extcon = FacesContext.getCurrentInstance().getExternalContext();
        String page = "";
        try{
            page = page.concat("/faces/login.xhtml");
            extcon.redirect(extcon.getRequestContextPath() + page);
        }
        catch(IOException ex){
            System.out.println("No se ha podido redirigir a la página ".concat(page));            
        }
    }
}
