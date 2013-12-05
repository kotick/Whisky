/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import DTOs.UsuarioDTO;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import sessionBeans.UserManagementSBLocal;

/**
 *
 * @author kotick
 */
@Named(value = "homeMB")
@RequestScoped
public class HomeMB {
    
    @EJB
    private UserManagementSBLocal userManagementSB;
    private LinkedList<UsuarioDTO> listausuarios;
    
    public HomeMB() {
    }
    
    @PostConstruct
    void init(){
        listausuarios= userManagementSB.selectAllUser();
        
    }

    public LinkedList<UsuarioDTO> getListausuarios() {
        return listausuarios;
    }

    public void setListausuarios(LinkedList<UsuarioDTO> listausuarios) {
        this.listausuarios = listausuarios;
    }
    
}
