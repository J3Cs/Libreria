/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.facade;

import com.j3cs.test.datalib.Autor;
import com.j3cs.test.library.controller.AutorJpaController;
import com.j3cs.test.library.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author juliocardona
 */
@Stateless
public class AutorFacadeLocal  implements AutorLocal, Serializable{

    @Inject
    AutorJpaController aController;
    
    @Override
    public boolean save(Autor etity) {
        boolean flag = true;
        try {
            aController.create(etity);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean edit(Autor entity) {
        boolean flag = true;
        try {
            aController.edit(entity);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean delete(Autor entity) {
        boolean flag = true;
        try {
            aController.destroy(entity.getIdAutor());
        } catch (NonexistentEntityException e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Autor> findAll() {
        List<Autor> list;
        try {
            list = aController.findAutorEntities();
        } catch (Exception e) {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }

    /*@Override
    public Autor findById(Autor Entity) {
        Autor autor = Entity;
        try {
            aController.findAutor(autor.getIdAutor());
        } catch (Exception e) {
        }
        return autor;
    }*/
    

    
}
