/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Postulaciones;
import com.ec.entidad.Tipoambiente;
import com.ec.entidad.Postulaciones;
import com.ec.entidad.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioPostulaciones {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Postulaciones postulaciones) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(postulaciones);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar postulaciones "+e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Postulaciones postulaciones) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(postulaciones));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  postulaciones " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Postulaciones postulaciones) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(postulaciones);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar postulaciones "+e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Postulaciones> findPostulaciones(String valor,Tipoambiente codTipoambiente) {

        List<Postulaciones> listaPostulacioness = new ArrayList<Postulaciones>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Postulaciones a WHERE a.vacDescripcion LIKE :vacDescripcion AND a.codTipoambiente=:codTipoambiente ORDER by a.vacFechaInicio DESC");
            query.setParameter("vacDescripcion", "%"+valor+"%");
            query.setParameter("codTipoambiente", codTipoambiente);
            listaPostulacioness = (List<Postulaciones>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta postulaciones " + e.getMessage());
        } finally {
            em.close();
        }

        return listaPostulacioness;
    }
    
    
    public List<Postulaciones> findPostulaciones(Usuario idUsuario,String valor) {

        List<Postulaciones> listaPostulacioness = new ArrayList<Postulaciones>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Postulaciones a WHERE a.idUsuario=:idUsuario and a.idVacante.vacNombre LIKE :vacNombre ORDER BY a.idVacante.vacNombre ASC");
            query.setParameter("vacNombre", "%"+valor+"%");
            query.setParameter("idUsuario", idUsuario);
//            query.setParameter("inicio", inicio);
//            query.setParameter("fin", fin);
            
            listaPostulacioness = (List<Postulaciones>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta postulaciones " + e.getMessage());
        } finally {
            em.close();
        }

        return listaPostulacioness;
    }
    public List<Postulaciones> findPostulacionesFecha(Usuario idUsuario,Date inicio , Date fin) {

        List<Postulaciones> listaPostulacioness = new ArrayList<Postulaciones>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Postulaciones a WHERE a.idUsuario=:idUsuario AND a.posFecha BETWEEN :inicio and :fin ORDER BY a.idVacante.vacNombre ASC");
//            query.setParameter("vacNombre", "%"+valor+"%");
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            
            listaPostulacioness = (List<Postulaciones>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta postulaciones " + e.getMessage());
        } finally {
            em.close();
        }

        return listaPostulacioness;
    }

}
