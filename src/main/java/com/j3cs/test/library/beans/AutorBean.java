/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.beans;

import com.j3cs.test.datalib.Autor;
import com.j3cs.test.library.facade.AutorLocal;
import com.j3cs.test.library.facade.GenericInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author juliocardona
 */
@Named("AutorBean")
@ViewScoped
public class AutorBean extends GenericBean<Autor> implements Serializable{ 

    @EJB
    AutorLocal autorLocal;
    
    Autor nuevo = new Autor();
    List<Autor> SelectedAutors = new ArrayList<>();

    public List<Autor> getSelectedAutors() {
        return SelectedAutors;
    }

    public void setSelectedAutors(List<Autor> SelectedAutors) {
        this.SelectedAutors = SelectedAutors;
    }

    public Autor getNuevo() {
        return nuevo;
    }

    public void setNuevo(Autor nuevo) {
        this.nuevo = nuevo;
    }

    public AutorLocal getAutorLocal() {
        return autorLocal;
    }

    public void setAutorLocal(AutorLocal autorLocal) {
        this.autorLocal = autorLocal;
    }
    
    @PostConstruct
    public void init(){
        llenar();
    }

    public void limpiar(){
        nuevo=null;
    }
    @Override
    public GenericInterface<Autor> getfacadelocal() {
        return autorLocal;
    }

    @Override
    public Autor getEntity() {
        return nuevo;
    }
    
}
