/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.beans;

import com.j3cs.test.datalib.Usuario;
import com.j3cs.test.library.facade.GenericInterface;
import com.j3cs.test.library.facade.UsuarioLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author juliocardona
 */
@Named("UsuarioBean")
@SessionScoped
public class UsuarioBean extends GenericBean<Usuario> implements Serializable {
    @EJB
    private UsuarioLocal uLocal;
    
    private Usuario nuevo;
    private boolean edit;

    
    @PostConstruct
    public void init(){
        nuevo = new Usuario();
        edit = false;
        llenar();
    }
    
    public void onRowSelect(SelectEvent evt){
        setNuevo((Usuario) evt.getObject());
        edit = true;
    }
    
    public void limpiar(){
        setNuevo(new Usuario());
        edit = false;
    }
    
    public Usuario getNuevo() {
        return nuevo;
    }

    public void setNuevo(Usuario nuevo) {
        this.nuevo = nuevo;
    }
    
    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    
    @Override
    public GenericInterface<Usuario> getfacadelocal() {
        return uLocal;
    }

    @Override
    public Usuario getEntity() {
        return nuevo;
    }
    
}
