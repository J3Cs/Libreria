/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.controller;

import com.j3cs.test.datalib.Autor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.j3cs.test.datalib.Obra;
import com.j3cs.test.library.controller.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author juliocardona
 */
public class AutorJpaController implements Serializable {

    @PersistenceContext(unitName = "prod")
    EntityManager em;

    public void create(Autor autor) {
        if (autor.getObraList() == null) {
            autor.setObraList(new ArrayList<>());
        }
        
        try {
            List<Obra> attachedObraList = new ArrayList<>();
            for (Obra obraListObraToAttach : autor.getObraList()) {
                obraListObraToAttach = em.getReference(obraListObraToAttach.getClass(), obraListObraToAttach.getIdObra());
                attachedObraList.add(obraListObraToAttach);
            }
            autor.setObraList(attachedObraList);
            em.persist(autor);
            
            for (Obra obraListObra : autor.getObraList()) {
                obraListObra.getAutorList().add(autor);
                obraListObra = em.merge(obraListObra);
            }
            
        } finally {
            
        }
    }

    public void edit(Autor autor) throws NonexistentEntityException, Exception {
        try {
            Autor persistentAutor = em.find(Autor.class, autor.getIdAutor());
            List<Obra> obraListOld = persistentAutor.getObraList();
            List<Obra> obraListNew = autor.getObraList();
            List<Obra> attachedObraListNew = new ArrayList<Obra>();
            for (Obra obraListNewObraToAttach : obraListNew) {
                obraListNewObraToAttach = em.getReference(obraListNewObraToAttach.getClass(), obraListNewObraToAttach.getIdObra());
                attachedObraListNew.add(obraListNewObraToAttach);
            }
            obraListNew = attachedObraListNew;
            autor.setObraList(obraListNew);
            autor = em.merge(autor);
            for (Obra obraListOldObra : obraListOld) {
                if (!obraListNew.contains(obraListOldObra)) {
                    obraListOldObra.getAutorList().remove(autor);
                    obraListOldObra = em.merge(obraListOldObra);
                }
            }
            for (Obra obraListNewObra : obraListNew) {
                if (!obraListOld.contains(obraListNewObra)) {
                    obraListNewObra.getAutorList().add(autor);
                    obraListNewObra = em.merge(obraListNewObra);
                }
            }
            
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = autor.getIdAutor();
                if (findAutor(id) == null) {
                    throw new NonexistentEntityException("The autor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        try {
            Autor autor;
            try {
                autor = em.getReference(Autor.class, id);
                autor.getIdAutor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autor with id " + id + " no longer exists.", enfe);
            }
            List<Obra> obraList = autor.getObraList();
            for (Obra obraListObra : obraList) {
                obraListObra.getAutorList().remove(autor);
                obraListObra = em.merge(obraListObra);
            }
            em.remove(autor);
        } finally {
            
        }
    }

    public List<Autor> findAutorEntities() {
        return findAutorEntities(true, -1, -1);
    }

    public List<Autor> findAutorEntities(int maxResults, int firstResult) {
        return findAutorEntities(false, maxResults, firstResult);
    }

    private List<Autor> findAutorEntities(boolean all, int maxResults, int firstResult) {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Autor.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            
        }
    }

    public Autor findAutor(BigDecimal id) {
        
        try {
            return em.find(Autor.class, id);
        } finally {
            
        }
    }

    public int getAutorCount() {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Autor> rt = cq.from(Autor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            
        }
    }
    
}
