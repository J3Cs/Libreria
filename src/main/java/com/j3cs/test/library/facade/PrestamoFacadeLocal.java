/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.facade;

import com.j3cs.test.datalib.Prestamo;
import com.j3cs.test.library.controller.PrestamoJpaController;
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
public class PrestamoFacadeLocal implements PrestamoLocal, Serializable{

    @PersistenceContext(unitName = "prod")
    EntityManager em;
@Inject
    PrestamoJpaController aController;
    
    @Override
    public boolean save(Prestamo etity) {
        boolean flag = true;
        try {
            aController.create(etity);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean edit(Prestamo entity) {
        boolean flag = true;
        try {
            aController.edit(entity);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean delete(Prestamo entity) {
        boolean flag = true;
        try {
            aController.destroy(entity.getIdPrestamo());
        } catch (NonexistentEntityException e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Prestamo> findAll() {
        List<Prestamo> list;
        try {
            list = aController.findPrestamoEntities();
        } catch (Exception e) {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }
    
}
