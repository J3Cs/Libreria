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
import com.j3cs.test.datalib.Prestamo;
import com.j3cs.test.datalib.Usuario;
import com.j3cs.test.library.controller.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 *
 * @author juliocardona
 */
public class UsuarioJpaController implements Serializable {

    @PersistenceContext(unitName = "prod")
    EntityManager em;

    public void create(Usuario usuario) {
        if (usuario.getPrestamoList() == null) {
            usuario.setPrestamoList(new ArrayList<Prestamo>());
        }
        
        try {
            List<Prestamo> attachedPrestamoList = new ArrayList<Prestamo>();
            for (Prestamo prestamoListPrestamoToAttach : usuario.getPrestamoList()) {
                prestamoListPrestamoToAttach = em.getReference(prestamoListPrestamoToAttach.getClass(), prestamoListPrestamoToAttach.getIdPrestamo());
                attachedPrestamoList.add(prestamoListPrestamoToAttach);
            }
            usuario.setPrestamoList(attachedPrestamoList);
            em.persist(usuario);
            for (Prestamo prestamoListPrestamo : usuario.getPrestamoList()) {
                Usuario oldIdUsuarioOfPrestamoListPrestamo = prestamoListPrestamo.getIdUsuario();
                prestamoListPrestamo.setIdUsuario(usuario);
                prestamoListPrestamo = em.merge(prestamoListPrestamo);
                if (oldIdUsuarioOfPrestamoListPrestamo != null) {
                    oldIdUsuarioOfPrestamoListPrestamo.getPrestamoList().remove(prestamoListPrestamo);
                    oldIdUsuarioOfPrestamoListPrestamo = em.merge(oldIdUsuarioOfPrestamoListPrestamo);
                }
            }
            
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        
        try {
            
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            List<Prestamo> prestamoListOld = persistentUsuario.getPrestamoList();
            List<Prestamo> prestamoListNew = usuario.getPrestamoList();
            List<Prestamo> attachedPrestamoListNew = new ArrayList<Prestamo>();
            for (Prestamo prestamoListNewPrestamoToAttach : prestamoListNew) {
                prestamoListNewPrestamoToAttach = em.getReference(prestamoListNewPrestamoToAttach.getClass(), prestamoListNewPrestamoToAttach.getIdPrestamo());
                attachedPrestamoListNew.add(prestamoListNewPrestamoToAttach);
            }
            prestamoListNew = attachedPrestamoListNew;
            usuario.setPrestamoList(prestamoListNew);
            usuario = em.merge(usuario);
            for (Prestamo prestamoListOldPrestamo : prestamoListOld) {
                if (!prestamoListNew.contains(prestamoListOldPrestamo)) {
                    prestamoListOldPrestamo.setIdUsuario(null);
                    prestamoListOldPrestamo = em.merge(prestamoListOldPrestamo);
                }
            }
            for (Prestamo prestamoListNewPrestamo : prestamoListNew) {
                if (!prestamoListOld.contains(prestamoListNewPrestamo)) {
                    Usuario oldIdUsuarioOfPrestamoListNewPrestamo = prestamoListNewPrestamo.getIdUsuario();
                    prestamoListNewPrestamo.setIdUsuario(usuario);
                    prestamoListNewPrestamo = em.merge(prestamoListNewPrestamo);
                    if (oldIdUsuarioOfPrestamoListNewPrestamo != null && !oldIdUsuarioOfPrestamoListNewPrestamo.equals(usuario)) {
                        oldIdUsuarioOfPrestamoListNewPrestamo.getPrestamoList().remove(prestamoListNewPrestamo);
                        oldIdUsuarioOfPrestamoListNewPrestamo = em.merge(oldIdUsuarioOfPrestamoListNewPrestamo);
                    }
                }
            }
            
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        
        try {
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<Prestamo> prestamoList = usuario.getPrestamoList();
            for (Prestamo prestamoListPrestamo : prestamoList) {
                prestamoListPrestamo.setIdUsuario(null);
                prestamoListPrestamo = em.merge(prestamoListPrestamo);
            }
            em.remove(usuario);
            
        } finally {
            
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            
        }
    }

    public Usuario findUsuario(BigDecimal id) {
        
        try {
            return em.find(Usuario.class, id);
        } finally {
        
        }
    }

    public int getUsuarioCount() {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
        
        }
    }
    
}
