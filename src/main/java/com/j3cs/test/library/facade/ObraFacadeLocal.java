/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.facade;

import com.j3cs.test.datalib.Obra;
import com.j3cs.test.library.controller.ObraJpaController;
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
public class ObraFacadeLocal implements ObraLocal, Serializable {

    @PersistenceContext(unitName = "prod")
    EntityManager em;
    @Inject
    ObraJpaController aController;

    @Override
    public boolean save(Obra etity) {
        boolean flag = true;
        try {
            aController.create(etity);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean edit(Obra entity) {
        boolean flag = true;
        try {
            aController.edit(entity);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean delete(Obra entity) {
        boolean flag = true;
        try {
            aController.destroy(entity.getIdObra());
        } catch (NonexistentEntityException e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Obra> findAll() {
        List<Obra> list;
        try {
            list = aController.findObraEntities();
        } catch (Exception e) {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }

}
