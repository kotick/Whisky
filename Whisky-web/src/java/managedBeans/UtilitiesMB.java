package managedBeans;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "utilitiesMB")
@RequestScoped
public class UtilitiesMB {

    public UtilitiesMB() {
    }

    public void goToPage(String webpage) {
        ExternalContext extcon = FacesContext.getCurrentInstance().getExternalContext();

        try {
            extcon.redirect(extcon.getRequestContextPath() + webpage);
        } catch (IOException ex) {
            System.out.println("No se ha podido redirigir a la página ".concat(webpage));
        }
    }
}
