/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.beans;

import com.j3cs.test.datalib.Autor;
import com.j3cs.test.datalib.Ejemplar;
import com.j3cs.test.datalib.Obra;
import com.j3cs.test.library.beans.util.JsfUtil;
import com.j3cs.test.library.facade.GenericInterface;
import com.j3cs.test.library.facade.ObraLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author juliocardona
 */
@Named("ObraBean")
@ViewScoped
public class ObrasBean extends GenericBean<Obra> implements Serializable{
    
    @EJB
    ObraLocal oController;
    @Inject
    AutorBean autorBean;
    @Inject
    EjemplarBean ejemplarBean;

    Obra nuevo;
    boolean edit;
    List<Autor> aList;
    List<Ejemplar> eList;
    List<Obra> SelectedObras;

    
    public EjemplarBean getEjemplarBean() {
        return ejemplarBean;
    }

    public void setEjemplarBean(EjemplarBean ejemplarBean) {
        this.ejemplarBean = ejemplarBean;
    }
    
    public List<Obra> getSelectedObras() {
        return SelectedObras;
    }

    public void setSelectedObras(List<Obra> SelectedObras) {
        this.SelectedObras = SelectedObras;
    }

    public List<Ejemplar> geteList() {
        return eList;
    }

    public void seteList(List<Ejemplar> eList) {
        this.eList = eList;
    }
    
    
    @PostConstruct
    public void init(){
        nuevo = new Obra();
        edit = false;
        aList = new ArrayList<>();
        llenar();
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    
    public void onCreate(){
        nuevo.setAutorList(autorBean.getSelectedAutors());
        nuevo.setEjemplarList(ejemplarBean.getSelectedEjemplar());
        create();
    }
    
    public void onEdit(){
        nuevo.setAutorList(autorBean.getSelectedAutors());
        nuevo.setEjemplarList(ejemplarBean.getSelectedEjemplar());
        edit();
    }
    
    public void onDelete(){
        nuevo.setAutorList(autorBean.getSelectedAutors());
        nuevo.setEjemplarList(ejemplarBean.getSelectedEjemplar());
        delete();
    }
    
    public void onRowSelect(SelectEvent evt){
        edit = true;
        nuevo = (Obra) evt.getObject();
        setaList(nuevo.getAutorList());
        seteList(nuevo.getEjemplarList());
        autorBean.setSelectedAutors(aList);
        ejemplarBean.setSelectedEjemplar(eList);
    }

    public List<Autor> getaList() {
        return aList;
    }

    public void setaList(List<Autor> aList) {
        this.aList = aList;
    }
    
    public void limpiar(){
        setNuevo(new Obra());
        setaList(new ArrayList<>());
        seteList(new ArrayList<>());
        autorBean.setSelectedAutors(new ArrayList<>());
        ejemplarBean.setSelectedEjemplar(new ArrayList<>());
        edit = false;
    }
    
    public ObraLocal getoController() {
        return oController;
    }

    public void setoController(ObraLocal oController) {
        this.oController = oController;
    }

    public AutorBean getAutorBean() {
        return autorBean;
    }

    public void setAutorBean(AutorBean autorBean) {
        this.autorBean = autorBean;
    }

    public Obra getNuevo() {
        return nuevo;
    }

    public void setNuevo(Obra nuevo) {
        this.nuevo = nuevo;
    }
    
    
    @Override
    public GenericInterface<Obra> getfacadelocal() {
        return oController;
    }

    @Override
    public Obra getEntity() {
        return nuevo;
    }
    
}
