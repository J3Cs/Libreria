/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.j3cs.test.datalib.Ejemplar;
import com.j3cs.test.datalib.Prestamo;
import com.j3cs.test.datalib.Usuario;
import com.j3cs.test.library.controller.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 *
 * @author juliocardona
 */
public class PrestamoJpaController implements Serializable {

    @PersistenceContext(unitName = "prod")
    EntityManager em;

    public void create(Prestamo prestamo) {
        
        try {
            Ejemplar idEjemplar = prestamo.getIdEjemplar();
            if (idEjemplar != null) {
                idEjemplar = em.getReference(idEjemplar.getClass(), idEjemplar.getIdEjemplar());
                prestamo.setIdEjemplar(idEjemplar);
            }
            Usuario idUsuario = prestamo.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                prestamo.setIdUsuario(idUsuario);
            }
            em.persist(prestamo);
            if (idEjemplar != null) {
                idEjemplar.getPrestamoList().add(prestamo);
                idEjemplar = em.merge(idEjemplar);
            }
            if (idUsuario != null) {
                idUsuario.getPrestamoList().add(prestamo);
                idUsuario = em.merge(idUsuario);
            }
            
        } finally {
            
        }
    }

    public void edit(Prestamo prestamo) throws NonexistentEntityException, Exception {
        
        try {
            
            Prestamo persistentPrestamo = em.find(Prestamo.class, prestamo.getIdPrestamo());
            Ejemplar idEjemplarOld = persistentPrestamo.getIdEjemplar();
            Ejemplar idEjemplarNew = prestamo.getIdEjemplar();
            Usuario idUsuarioOld = persistentPrestamo.getIdUsuario();
            Usuario idUsuarioNew = prestamo.getIdUsuario();
            if (idEjemplarNew != null) {
                idEjemplarNew = em.getReference(idEjemplarNew.getClass(), idEjemplarNew.getIdEjemplar());
                prestamo.setIdEjemplar(idEjemplarNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                prestamo.setIdUsuario(idUsuarioNew);
            }
            prestamo = em.merge(prestamo);
            if (idEjemplarOld != null && !idEjemplarOld.equals(idEjemplarNew)) {
                idEjemplarOld.getPrestamoList().remove(prestamo);
                idEjemplarOld = em.merge(idEjemplarOld);
            }
            if (idEjemplarNew != null && !idEjemplarNew.equals(idEjemplarOld)) {
                idEjemplarNew.getPrestamoList().add(prestamo);
                idEjemplarNew = em.merge(idEjemplarNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getPrestamoList().remove(prestamo);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getPrestamoList().add(prestamo);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = prestamo.getIdPrestamo();
                if (findPrestamo(id) == null) {
                    throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        
        try {
            
            em.getTransaction().begin();
            Prestamo prestamo;
            try {
                prestamo = em.getReference(Prestamo.class, id);
                prestamo.getIdPrestamo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.", enfe);
            }
            Ejemplar idEjemplar = prestamo.getIdEjemplar();
            if (idEjemplar != null) {
                idEjemplar.getPrestamoList().remove(prestamo);
                idEjemplar = em.merge(idEjemplar);
            }
            Usuario idUsuario = prestamo.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getPrestamoList().remove(prestamo);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(prestamo);
        } finally {
            
        }
    }

    public List<Prestamo> findPrestamoEntities() {
        return findPrestamoEntities(true, -1, -1);
    }

    public List<Prestamo> findPrestamoEntities(int maxResults, int firstResult) {
        return findPrestamoEntities(false, maxResults, firstResult);
    }

    private List<Prestamo> findPrestamoEntities(boolean all, int maxResults, int firstResult) {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prestamo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
          
        }
    }

    public Prestamo findPrestamo(BigDecimal id) {
        
        try {
            return em.find(Prestamo.class, id);
        } finally {
          
        }
    }

    public int getPrestamoCount() {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prestamo> rt = cq.from(Prestamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
        
        }
    }
    
}
