/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.facade;

import com.j3cs.test.datalib.Ejemplar;
import com.j3cs.test.library.controller.EjemplarJpaController;
import com.j3cs.test.library.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author juliocardona
 */
@Stateless
public class EjemplarFacadeLocal implements EjemplarLocal, Serializable{

    @PersistenceContext(unitName = "prod")
    EntityManager em;

    @Inject
    EjemplarJpaController aController;
    
    @Override
    public boolean save(Ejemplar etity) {
        boolean flag = true;
        try {
            aController.create(etity);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean edit(Ejemplar entity) {
        boolean flag = true;
        try {
            aController.edit(entity);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean delete(Ejemplar entity) {
        boolean flag = true;
        try {
            aController.destroy(entity.getIdEjemplar());
        } catch (NonexistentEntityException e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Ejemplar> findAll() {
        List<Ejemplar> list;
        try {
            list = aController.findEjemplarEntities();
        } catch (Exception e) {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }

    
}
