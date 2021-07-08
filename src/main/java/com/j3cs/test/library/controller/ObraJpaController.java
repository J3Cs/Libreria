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
import com.j3cs.test.datalib.Autor;
import java.util.ArrayList;
import java.util.List;
import com.j3cs.test.datalib.Ejemplar;
import com.j3cs.test.datalib.Obra;
import com.j3cs.test.library.controller.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 *
 * @author juliocardona
 */
public class ObraJpaController implements Serializable {

    @PersistenceContext(unitName = "prod")
    EntityManager em;

    public void create(Obra obra) {
        if (obra.getAutorList() == null) {
            obra.setAutorList(new ArrayList<Autor>());
        }
        if (obra.getEjemplarList() == null) {
            obra.setEjemplarList(new ArrayList<Ejemplar>());
        }
        
        try {
            List<Autor> attachedAutorList = new ArrayList<Autor>();
            for (Autor autorListAutorToAttach : obra.getAutorList()) {
                autorListAutorToAttach = em.getReference(autorListAutorToAttach.getClass(), autorListAutorToAttach.getIdAutor());
                attachedAutorList.add(autorListAutorToAttach);
            }
            obra.setAutorList(attachedAutorList);
            List<Ejemplar> attachedEjemplarList = new ArrayList<Ejemplar>();
            for (Ejemplar ejemplarListEjemplarToAttach : obra.getEjemplarList()) {
                ejemplarListEjemplarToAttach = em.getReference(ejemplarListEjemplarToAttach.getClass(), ejemplarListEjemplarToAttach.getIdEjemplar());
                attachedEjemplarList.add(ejemplarListEjemplarToAttach);
            }
            obra.setEjemplarList(attachedEjemplarList);
            em.persist(obra);
            for (Autor autorListAutor : obra.getAutorList()) {
                autorListAutor.getObraList().add(obra);
                autorListAutor = em.merge(autorListAutor);
            }
            for (Ejemplar ejemplarListEjemplar : obra.getEjemplarList()) {
                ejemplarListEjemplar.getObraList().add(obra);
                ejemplarListEjemplar = em.merge(ejemplarListEjemplar);
            }
            
        } finally {
            
        }
    }

    public void edit(Obra obra) throws NonexistentEntityException, Exception {
        
        try {
            
            Obra persistentObra = em.find(Obra.class, obra.getIdObra());
            List<Autor> autorListOld = persistentObra.getAutorList();
            List<Autor> autorListNew = obra.getAutorList();
            List<Ejemplar> ejemplarListOld = persistentObra.getEjemplarList();
            List<Ejemplar> ejemplarListNew = obra.getEjemplarList();
            List<Autor> attachedAutorListNew = new ArrayList<Autor>();
            for (Autor autorListNewAutorToAttach : autorListNew) {
                autorListNewAutorToAttach = em.getReference(autorListNewAutorToAttach.getClass(), autorListNewAutorToAttach.getIdAutor());
                attachedAutorListNew.add(autorListNewAutorToAttach);
            }
            autorListNew = attachedAutorListNew;
            obra.setAutorList(autorListNew);
            List<Ejemplar> attachedEjemplarListNew = new ArrayList<Ejemplar>();
            for (Ejemplar ejemplarListNewEjemplarToAttach : ejemplarListNew) {
                ejemplarListNewEjemplarToAttach = em.getReference(ejemplarListNewEjemplarToAttach.getClass(), ejemplarListNewEjemplarToAttach.getIdEjemplar());
                attachedEjemplarListNew.add(ejemplarListNewEjemplarToAttach);
            }
            ejemplarListNew = attachedEjemplarListNew;
            obra.setEjemplarList(ejemplarListNew);
            obra = em.merge(obra);
            for (Autor autorListOldAutor : autorListOld) {
                if (!autorListNew.contains(autorListOldAutor)) {
                    autorListOldAutor.getObraList().remove(obra);
                    autorListOldAutor = em.merge(autorListOldAutor);
                }
            }
            for (Autor autorListNewAutor : autorListNew) {
                if (!autorListOld.contains(autorListNewAutor)) {
                    autorListNewAutor.getObraList().add(obra);
                    autorListNewAutor = em.merge(autorListNewAutor);
                }
            }
            for (Ejemplar ejemplarListOldEjemplar : ejemplarListOld) {
                if (!ejemplarListNew.contains(ejemplarListOldEjemplar)) {
                    ejemplarListOldEjemplar.getObraList().remove(obra);
                    ejemplarListOldEjemplar = em.merge(ejemplarListOldEjemplar);
                }
            }
            for (Ejemplar ejemplarListNewEjemplar : ejemplarListNew) {
                if (!ejemplarListOld.contains(ejemplarListNewEjemplar)) {
                    ejemplarListNewEjemplar.getObraList().add(obra);
                    ejemplarListNewEjemplar = em.merge(ejemplarListNewEjemplar);
                }
            }
            
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = obra.getIdObra();
                if (findObra(id) == null) {
                    throw new NonexistentEntityException("The obra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        
        try {
            
            
            Obra obra;
            try {
                obra = em.getReference(Obra.class, id);
                obra.getIdObra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The obra with id " + id + " no longer exists.", enfe);
            }
            List<Autor> autorList = obra.getAutorList();
            for (Autor autorListAutor : autorList) {
                autorListAutor.getObraList().remove(obra);
                autorListAutor = em.merge(autorListAutor);
            }
            List<Ejemplar> ejemplarList = obra.getEjemplarList();
            for (Ejemplar ejemplarListEjemplar : ejemplarList) {
                ejemplarListEjemplar.getObraList().remove(obra);
                ejemplarListEjemplar = em.merge(ejemplarListEjemplar);
            }
            em.remove(obra);
            
        } finally {
            
        }
    }

    public List<Obra> findObraEntities() {
        return findObraEntities(true, -1, -1);
    }

    public List<Obra> findObraEntities(int maxResults, int firstResult) {
        return findObraEntities(false, maxResults, firstResult);
    }

    private List<Obra> findObraEntities(boolean all, int maxResults, int firstResult) {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Obra.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            
        }
    }

    public Obra findObra(BigDecimal id) {
        
        try {
            return em.find(Obra.class, id);
        } finally {
            
        }
    }

    public int getObraCount() {
        
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Obra> rt = cq.from(Obra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            
        }
    }

}
