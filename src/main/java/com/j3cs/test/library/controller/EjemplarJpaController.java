/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.controller;

import com.j3cs.test.datalib.Ejemplar;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.j3cs.test.datalib.Obra;
import java.util.ArrayList;
import java.util.List;
import com.j3cs.test.datalib.Prestamo;
import com.j3cs.test.library.controller.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 *
 * @author juliocardona
 */
public class EjemplarJpaController implements Serializable {

    @PersistenceContext(unitName = "prod")
    EntityManager em;

    public void create(Ejemplar ejemplar) {
        if (ejemplar.getObraList() == null) {
            ejemplar.setObraList(new ArrayList<Obra>());
        }
        if (ejemplar.getPrestamoList() == null) {
            ejemplar.setPrestamoList(new ArrayList<Prestamo>());
        }
        
        try {
            List<Obra> attachedObraList = new ArrayList<Obra>();
            for (Obra obraListObraToAttach : ejemplar.getObraList()) {
                obraListObraToAttach = em.getReference(obraListObraToAttach.getClass(), obraListObraToAttach.getIdObra());
                attachedObraList.add(obraListObraToAttach);
            }
            ejemplar.setObraList(attachedObraList);
            List<Prestamo> attachedPrestamoList = new ArrayList<Prestamo>();
            for (Prestamo prestamoListPrestamoToAttach : ejemplar.getPrestamoList()) {
                prestamoListPrestamoToAttach = em.getReference(prestamoListPrestamoToAttach.getClass(), prestamoListPrestamoToAttach.getIdPrestamo());
                attachedPrestamoList.add(prestamoListPrestamoToAttach);
            }
            ejemplar.setPrestamoList(attachedPrestamoList);
            em.persist(ejemplar);
            for (Obra obraListObra : ejemplar.getObraList()) {
                obraListObra.getEjemplarList().add(ejemplar);
                obraListObra = em.merge(obraListObra);
            }
            for (Prestamo prestamoListPrestamo : ejemplar.getPrestamoList()) {
                Ejemplar oldIdEjemplarOfPrestamoListPrestamo = prestamoListPrestamo.getIdEjemplar();
                prestamoListPrestamo.setIdEjemplar(ejemplar);
                prestamoListPrestamo = em.merge(prestamoListPrestamo);
                if (oldIdEjemplarOfPrestamoListPrestamo != null) {
                    oldIdEjemplarOfPrestamoListPrestamo.getPrestamoList().remove(prestamoListPrestamo);
                    oldIdEjemplarOfPrestamoListPrestamo = em.merge(oldIdEjemplarOfPrestamoListPrestamo);
                }
            }
            
        } finally {
            
        }
    }

    public void edit(Ejemplar ejemplar) throws NonexistentEntityException, Exception {
        
        try {
            Ejemplar persistentEjemplar = em.find(Ejemplar.class, ejemplar.getIdEjemplar());
            List<Obra> obraListOld = persistentEjemplar.getObraList();
            List<Obra> obraListNew = ejemplar.getObraList();
            List<Prestamo> prestamoListOld = persistentEjemplar.getPrestamoList();
            List<Prestamo> prestamoListNew = ejemplar.getPrestamoList();
            List<Obra> attachedObraListNew = new ArrayList<Obra>();
            for (Obra obraListNewObraToAttach : obraListNew) {
                obraListNewObraToAttach = em.getReference(obraListNewObraToAttach.getClass(), obraListNewObraToAttach.getIdObra());
                attachedObraListNew.add(obraListNewObraToAttach);
            }
            obraListNew = attachedObraListNew;
            ejemplar.setObraList(obraListNew);
            List<Prestamo> attachedPrestamoListNew = new ArrayList<Prestamo>();
            for (Prestamo prestamoListNewPrestamoToAttach : prestamoListNew) {
                prestamoListNewPrestamoToAttach = em.getReference(prestamoListNewPrestamoToAttach.getClass(), prestamoListNewPrestamoToAttach.getIdPrestamo());
                attachedPrestamoListNew.add(prestamoListNewPrestamoToAttach);
            }
            prestamoListNew = attachedPrestamoListNew;
            ejemplar.setPrestamoList(prestamoListNew);
            ejemplar = em.merge(ejemplar);
            for (Obra obraListOldObra : obraListOld) {
                if (!obraListNew.contains(obraListOldObra)) {
                    obraListOldObra.getEjemplarList().remove(ejemplar);
                    obraListOldObra = em.merge(obraListOldObra);
                }
            }
            for (Obra obraListNewObra : obraListNew) {
                if (!obraListOld.contains(obraListNewObra)) {
                    obraListNewObra.getEjemplarList().add(ejemplar);
                    obraListNewObra = em.merge(obraListNewObra);
                }
            }
            for (Prestamo prestamoListOldPrestamo : prestamoListOld) {
                if (!prestamoListNew.contains(prestamoListOldPrestamo)) {
                    prestamoListOldPrestamo.setIdEjemplar(null);
                    prestamoListOldPrestamo = em.merge(prestamoListOldPrestamo);
                }
            }
            for (Prestamo prestamoListNewPrestamo : prestamoListNew) {
                if (!prestamoListOld.contains(prestamoListNewPrestamo)) {
                    Ejemplar oldIdEjemplarOfPrestamoListNewPrestamo = prestamoListNewPrestamo.getIdEjemplar();
                    prestamoListNewPrestamo.setIdEjemplar(ejemplar);
                    prestamoListNewPrestamo = em.merge(prestamoListNewPrestamo);
                    if (oldIdEjemplarOfPrestamoListNewPrestamo != null && !oldIdEjemplarOfPrestamoListNewPrestamo.equals(ejemplar)) {
                        oldIdEjemplarOfPrestamoListNewPrestamo.getPrestamoList().remove(prestamoListNewPrestamo);
                        oldIdEjemplarOfPrestamoListNewPrestamo = em.merge(oldIdEjemplarOfPrestamoListNewPrestamo);
                    }
                }
            }
            
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = ejemplar.getIdEjemplar();
                if (findEjemplar(id) == null) {
                    throw new NonexistentEntityException("The ejemplar with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        
        try {
            
            Ejemplar ejemplar;
            try {
                ejemplar = em.getReference(Ejemplar.class, id);
                ejemplar.getIdEjemplar();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ejemplar with id " + id + " no longer exists.", enfe);
            }
            List<Obra> obraList = ejemplar.getObraList();
            for (Obra obraListObra : obraList) {
                obraListObra.getEjemplarList().remove(ejemplar);
                obraListObra = em.merge(obraListObra);
            }
            List<Prestamo> prestamoList = ejemplar.getPrestamoList();
            for (Prestamo prestamoListPrestamo : prestamoList) {
                prestamoListPrestamo.setIdEjemplar(null);
                prestamoListPrestamo = em.merge(prestamoListPrestamo);
            }
            em.remove(ejemplar);
        } finally {
            
        }
    }

    public List<Ejemplar> findEjemplarEntities() {
        return findEjemplarEntities(true, -1, -1);
    }

    public List<Ejemplar> findEjemplarEntities(int maxResults, int firstResult) {
        return findEjemplarEntities(false, maxResults, firstResult);
    }

    private List<Ejemplar> findEjemplarEntities(boolean all, int maxResults, int firstResult) {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ejemplar.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
         
        }
    }

    public Ejemplar findEjemplar(BigDecimal id) {
        
        try {
            return em.find(Ejemplar.class, id);
        } finally {
         
        }
    }

    public int getEjemplarCount() {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ejemplar> rt = cq.from(Ejemplar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            
        }
    }

}
