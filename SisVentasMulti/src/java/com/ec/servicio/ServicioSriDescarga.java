package com.ec.servicio;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.sri.SriComprobanteRecibido;
import com.ec.entidad.sri.SriDescargaLote;
import com.ec.entidad.sri.SriTipoDocumento;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ServicioSriDescarga {

    public SriTipoDocumento findTipo(String codigo) {
        EntityManager em = HelperPersistencia.getEMF();
        try {
            Query q = em.createQuery("SELECT t FROM SriTipoDocumento t WHERE t.stdCodigo = :c");
            q.setParameter("c", codigo);
            List<SriTipoDocumento> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    public List<SriComprobanteRecibido> findPorTipoYFecha(String codigoTipo, Date inicio, Date fin, Tipoambiente ambiente) {
        if (ambiente == null) {
            return new ArrayList<SriComprobanteRecibido>();
        }
        EntityManager em = HelperPersistencia.getEMF();
        try {
            Query q = em.createQuery(
                    "SELECT c FROM SriComprobanteRecibido c "
                    + "WHERE c.stdCodigo = :tipo "
                    + "AND c.codTipoambiente = :amb "
                    + "AND c.scrFechaEmision BETWEEN :ini AND :fin "
                    + "ORDER BY c.scrFechaEmision DESC, c.scrId DESC");
            q.setParameter("tipo", codigoTipo);
            q.setParameter("amb", ambiente);
            q.setParameter("ini", inicio);
            q.setParameter("fin", fin);
            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<SriComprobanteRecibido>();
        } finally {
            em.close();
        }
    }

    public SriComprobanteRecibido findByClave(String clave, Tipoambiente ambiente) {
        if (clave == null || clave.trim().isEmpty() || ambiente == null) {
            return null;
        }
        EntityManager em = HelperPersistencia.getEMF();
        try {
            Query q = em.createQuery(
                    "SELECT c FROM SriComprobanteRecibido c "
                    + "WHERE c.scrClaveAcceso = :c AND c.codTipoambiente = :amb");
            q.setParameter("c", clave.trim());
            q.setParameter("amb", ambiente);
            List<SriComprobanteRecibido> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    public SriDescargaLote crearLote(SriDescargaLote lote) {
        EntityManager em = HelperPersistencia.getEMF();
        try {
            em.getTransaction().begin();
            em.persist(lote);
            em.getTransaction().commit();
            return lote;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void modificarLote(SriDescargaLote lote) {
        EntityManager em = HelperPersistencia.getEMF();
        try {
            em.getTransaction().begin();
            em.merge(lote);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void crearComprobante(SriComprobanteRecibido item) {
        EntityManager em = HelperPersistencia.getEMF();
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void modificarComprobante(SriComprobanteRecibido item) {
        EntityManager em = HelperPersistencia.getEMF();
        try {
            em.getTransaction().begin();
            em.merge(item);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public int eliminarPorTipoYFecha(String codigoTipo, Date inicio, Date fin, Tipoambiente ambiente) {
        if (ambiente == null || codigoTipo == null || inicio == null || fin == null) {
            return 0;
        }
        List<SriComprobanteRecibido> items = findPorTipoYFecha(codigoTipo, inicio, fin, ambiente);
        EntityManager em = HelperPersistencia.getEMF();
        try {
            em.getTransaction().begin();
            int n = em.createQuery(
                    "DELETE FROM SriComprobanteRecibido c "
                    + "WHERE c.stdCodigo = :tipo AND c.codTipoambiente = :amb "
                    + "AND c.scrFechaEmision BETWEEN :ini AND :fin")
                    .setParameter("tipo", codigoTipo)
                    .setParameter("amb", ambiente)
                    .setParameter("ini", inicio)
                    .setParameter("fin", fin)
                    .executeUpdate();
            em.getTransaction().commit();
            for (SriComprobanteRecibido item : items) {
                borrarArchivo(item.getScrRutaXml());
                if (item.getScrClaveAcceso() != null && item.getScrRutaXml() != null) {
                    File xml = new File(item.getScrRutaXml());
                    File pdf = new File(xml.getParentFile(), item.getScrClaveAcceso() + ".pdf");
                    if (pdf.isFile()) {
                        pdf.delete();
                    }
                }
            }
            return n;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private void borrarArchivo(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return;
        }
        File f = new File(ruta);
        if (f.isFile()) {
            f.delete();
        }
    }
}
