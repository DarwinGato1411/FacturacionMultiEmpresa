/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.Vacante;
import com.ec.entidad.Vacante;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioVacante {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Vacante vacante) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(vacante);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar vacante "+e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Vacante vacante) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(vacante));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  vacante " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Vacante vacante) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(vacante);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar vacante "+e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Vacante> findVacante(String valor,Tipoambiente codTipoambiente) {

        List<Vacante> listaVacantes = new ArrayList<Vacante>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Vacante a WHERE a.vacDescripcion LIKE :vacDescripcion AND a.codTipoambiente=:codTipoambiente ORDER by a.vacFechaInicio DESC");
            query.setParameter("vacDescripcion", "%"+valor+"%");
            query.setParameter("codTipoambiente", codTipoambiente);
            listaVacantes = (List<Vacante>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta vacante " + e.getMessage());
        } finally {
            em.close();
        }

        return listaVacantes;
    }
    
    
    public List<Vacante> findVacntes(String valor,Tipoambiente codTipoambiente) {

        List<Vacante> listaVacantes = new ArrayList<Vacante>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Vacante a WHERE a.trpRazonSocial LIKE :trpRazonSocial AND a.codTipoambiente=:codTipoambiente");
            query.setParameter("trpRazonSocial", "%"+valor+"%");
            query.setParameter("codTipoambiente", codTipoambiente);
            listaVacantes = (List<Vacante>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta vacante " + e.getMessage());
        } finally {
            em.close();
        }

        return listaVacantes;
    }

}
