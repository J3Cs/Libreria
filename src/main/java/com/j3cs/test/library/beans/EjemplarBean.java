package com.j3cs.test.library.beans;

import com.j3cs.test.datalib.Ejemplar;
import com.j3cs.test.library.facade.EjemplarLocal;
import com.j3cs.test.library.facade.GenericInterface;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named("EjemplarBean")
@SessionScoped
public class EjemplarBean extends GenericBean<Ejemplar>implements Serializable {

    @EJB
    EjemplarLocal eController;
    
    private Ejemplar selected;

    public EjemplarBean() {
    }

    public Ejemplar getSelected() {
        return selected;
    }

    public void setSelected(Ejemplar selected) {
        this.selected = selected;
    }

    @Override
    public GenericInterface<Ejemplar> getfacadelocal() {
        return eController;
    }

    @Override
    public Ejemplar getEntity() {
        return selected;
    }

    
}
