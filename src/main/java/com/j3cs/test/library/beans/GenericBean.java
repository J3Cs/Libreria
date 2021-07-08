/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.beans;

import com.j3cs.test.library.facade.GenericInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author juliocardona
 */
public abstract class GenericBean<T> implements Serializable{
    T e = getEntity();
    List<T> lista = new ArrayList<>();

    public GenericBean() {
    }

    public T getE() {
        return e;
    }

    public void setE(T e) {
        this.e = e;
    }

    public List<T> getLista() {
        return lista;
    }

    public void setLista(List<T> lista) {
        this.lista = lista;
    }

    public void create() {
        if (getfacadelocal() != null) {
            try {
                getfacadelocal().save(getEntity());
                llenar();
                setE(getEntity());
            } catch (Exception ex) {
                System.out.println("Error" + ex.getMessage());
            }
        }
    }
    
    public void edit(){
        if (getfacadelocal() != null) {
            try {
                getfacadelocal().edit(getEntity());
                llenar();
                setE(getEntity());
            } catch (Exception ex) {
                System.out.println("Error" + ex.getMessage());
            }
        }
    }
    
    public void delete(){
        if (getfacadelocal() != null) {
            try {
                getfacadelocal().delete(getEntity());
                llenar();
                setE(getEntity());
            } catch (Exception ex) {
                System.out.println("Error" + ex.getMessage());
            }
        }
    }
    
    public void findById(){
        
    }
    public void llenar() {
        if (getfacadelocal() != null) {
            setLista(getfacadelocal().findAll());
        } else {
            this.lista = Collections.EMPTY_LIST;
        }
    }

    public abstract GenericInterface<T> getfacadelocal();

    public abstract T getEntity();

}
