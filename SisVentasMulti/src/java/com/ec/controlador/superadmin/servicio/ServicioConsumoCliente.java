/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.superadmin.servicio;

import com.ec.servicio.*;
import com.ec.controlador.superadmin.entidad.ConsumoClientes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioConsumoCliente {

//    ServicioDetalleGuia servicioDetalleGuia = new ServicioDetalleGuia();
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<ConsumoClientes> findAll() {

        List<ConsumoClientes> listaCosumoClientess = new ArrayList<ConsumoClientes>();

        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a from ConsumoClientes a");
//            query.setParameter("codigo", codigo);
            query.setMaxResults(2);
            listaCosumoClientess = (List<ConsumoClientes>) query.getResultList();
            em.getTransaction().commit();
            return listaCosumoClientess;

        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            System.out.println("Error en lsa consulta cosumoClientes " + e.getMessage());
        } finally {
            em.close();
        }

        return listaCosumoClientess;
    }

}
