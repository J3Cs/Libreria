package com.j3cs.test.library.beans;

import com.j3cs.test.datalib.Ejemplar;
import com.j3cs.test.library.facade.EjemplarLocal;
import com.j3cs.test.library.facade.GenericInterface;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import org.primefaces.event.SelectEvent;

@Named("EjemplarBean")
@SessionScoped
public class EjemplarBean extends GenericBean<Ejemplar>implements Serializable {

    @EJB
    EjemplarLocal eController;
    
    private Ejemplar nuevo;
    private boolean edit;
    private List<Ejemplar> selectedEjemplar;

    public List<Ejemplar> getSelectedEjemplar() {
        return selectedEjemplar;
    }

    public void setSelectedEjemplar(List<Ejemplar> selectedEjemplar) {
        this.selectedEjemplar = selectedEjemplar;
    }
    
    public EjemplarBean() {
    }
    
    @PostConstruct
    public void init(){
        nuevo = new Ejemplar();
        edit = false;
        llenar();
    }
    
    public void limpiar(){
        setNuevo(new Ejemplar());
        edit = false;
    }
    
    public void onRowSelect(SelectEvent evt){
        setNuevo((Ejemplar) evt.getObject());
        edit = true;
    }
    
    public Ejemplar getNuevo() {
        return nuevo;
    }

    public void setNuevo(Ejemplar nuevo) {
        this.nuevo = nuevo;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    
    @Override
    public GenericInterface<Ejemplar> getfacadelocal() {
        return eController;
    }

    @Override
    public Ejemplar getEntity() {
        return nuevo;
    }

    
}
