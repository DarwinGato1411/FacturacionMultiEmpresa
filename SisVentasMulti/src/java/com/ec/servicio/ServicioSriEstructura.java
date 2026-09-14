package com.ec.servicio;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ServicioSriEstructura {

    private static final Logger LOG = Logger.getLogger(ServicioSriEstructura.class.getName());
    private static volatile boolean inicializado = false;
    private static volatile boolean ambienteEnlazado = false;

    public void asegurarTablas() {
        if (inicializado) {
            enlazarTipoambiente();
            return;
        }
        synchronized (ServicioSriEstructura.class) {
            if (inicializado) {
                enlazarTipoambiente();
                return;
            }
            EntityManager em = HelperPersistencia.getEMF();
            try {
                em.getTransaction().begin();
                ejecutarSilencioso(em, "ALTER TABLE tipoambiente ADD COLUMN am_clave_sri_linea character varying(255)");
                em.createNativeQuery(
                        "CREATE TABLE IF NOT EXISTS sri_tipo_documento ("
                        + " std_codigo character varying(2) NOT NULL,"
                        + " std_nombre character varying(120) NOT NULL,"
                        + " std_nombre_portal character varying(200),"
                        + " std_codigo_portal integer,"
                        + " std_orden integer,"
                        + " std_activo boolean DEFAULT true,"
                        + " CONSTRAINT sri_tipo_documento_pkey PRIMARY KEY (std_codigo))")
                        .executeUpdate();
                em.createNativeQuery(
                        "CREATE TABLE IF NOT EXISTS sri_descarga_lote ("
                        + " sdl_id serial NOT NULL,"
                        + " std_codigo character varying(2) NOT NULL,"
                        + " sdl_fecha_inicio date,"
                        + " sdl_fecha_fin date,"
                        + " sdl_fecha_ejecucion timestamp without time zone,"
                        + " sdl_origen character varying(20),"
                        + " sdl_estado character varying(20),"
                        + " sdl_mensaje text,"
                        + " sdl_ruta_archivo text,"
                        + " sdl_cantidad integer DEFAULT 0,"
                        + " sdl_nuevos integer DEFAULT 0,"
                        + " sdl_duplicados integer DEFAULT 0,"
                        + " sdl_usuario character varying(80),"
                        + " cod_tipoambiente integer,"
                        + " CONSTRAINT sri_descarga_lote_pkey PRIMARY KEY (sdl_id))")
                        .executeUpdate();
                em.createNativeQuery(
                        "CREATE TABLE IF NOT EXISTS sri_comprobante_recibido ("
                        + " scr_id serial NOT NULL,"
                        + " std_codigo character varying(2) NOT NULL,"
                        + " sdl_id integer,"
                        + " scr_tipo_comprobante character varying(120),"
                        + " scr_serie character varying(40),"
                        + " scr_ruc_emisor character varying(20),"
                        + " scr_razon_social character varying(300),"
                        + " scr_fecha_emision date,"
                        + " scr_fecha_autorizacion date,"
                        + " scr_tipo_emision character varying(40),"
                        + " scr_identificacion_receptor character varying(20),"
                        + " scr_clave_acceso character varying(60),"
                        + " scr_autorizacion character varying(60),"
                        + " scr_total character varying(30),"
                        + " scr_clasificacion character varying(80),"
                        + " scr_estado_clasificacion character varying(30) DEFAULT 'PENDIENTE',"
                        + " scr_xml_descargado boolean DEFAULT false,"
                        + " scr_ruta_xml text,"
                        + " scr_fecha_registro timestamp without time zone,"
                        + " cod_tipoambiente integer,"
                        + " CONSTRAINT sri_comprobante_recibido_pkey PRIMARY KEY (scr_id))")
                        .executeUpdate();
                ejecutarSilencioso(em, "CREATE INDEX IF NOT EXISTS sri_comprobante_recibido_tipo_fecha_idx "
                        + "ON sri_comprobante_recibido (std_codigo, scr_fecha_emision)");
                insertarTipo(em, "01", "Facturas", "Factura", 1, 1);
                insertarTipo(em, "03", "Liquidaciones de compra",
                        "Liquidación de compra de bienes y prestación de servicios", 2, 2);
                insertarTipo(em, "04", "Notas de crédito", "Notas de crédito", 3, 3);
                insertarTipo(em, "05", "Notas de débito", "Notas de débito", 4, 4);
                insertarTipo(em, "06", "Guías de remisión", "Guías de remisión", 6, 5);
                insertarTipo(em, "07", "Comprobantes de retención", "Comprobante de retención", 7, 6);
                em.getTransaction().commit();
                inicializado = true;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                LOG.log(Level.SEVERE, "No se pudo crear la estructura de descarga SRI", e);
            } finally {
                em.close();
            }
            enlazarTipoambiente();
        }
    }

    private void enlazarTipoambiente() {
        if (ambienteEnlazado) {
            return;
        }
        synchronized (ServicioSriEstructura.class) {
            if (ambienteEnlazado) {
                return;
            }
            EntityManager em = HelperPersistencia.getEMF();
            try {
                em.getTransaction().begin();
                ejecutarSilencioso(em, "ALTER TABLE sri_descarga_lote ADD COLUMN cod_tipoambiente integer");
                ejecutarSilencioso(em, "ALTER TABLE sri_comprobante_recibido ADD COLUMN cod_tipoambiente integer");
                ejecutarSilencioso(em,
                        "ALTER TABLE sri_descarga_lote ADD CONSTRAINT sri_descarga_lote_ambiente_fk "
                        + "FOREIGN KEY (cod_tipoambiente) REFERENCES tipoambiente (cod_tipoambiente)");
                ejecutarSilencioso(em,
                        "ALTER TABLE sri_comprobante_recibido ADD CONSTRAINT sri_comprobante_recibido_ambiente_fk "
                        + "FOREIGN KEY (cod_tipoambiente) REFERENCES tipoambiente (cod_tipoambiente)");
                ejecutarSilencioso(em, "DROP INDEX IF EXISTS sri_comprobante_recibido_clave_uk");
                ejecutarSilencioso(em,
                        "CREATE UNIQUE INDEX IF NOT EXISTS sri_comprobante_recibido_amb_clave_uk "
                        + "ON sri_comprobante_recibido (cod_tipoambiente, scr_clave_acceso) "
                        + "WHERE scr_clave_acceso IS NOT NULL AND scr_clave_acceso <> ''");
                ejecutarSilencioso(em,
                        "CREATE INDEX IF NOT EXISTS sri_comprobante_recibido_ambiente_idx "
                        + "ON sri_comprobante_recibido (cod_tipoambiente, std_codigo, scr_fecha_emision)");
                em.getTransaction().commit();
                ambienteEnlazado = true;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                LOG.log(Level.WARNING, "No se pudo enlazar descarga SRI con tipoambiente", e);
            } finally {
                em.close();
            }
        }
    }

    private void insertarTipo(EntityManager em, String codigo, String nombre, String portal, int codigoPortal, int orden) {
        em.createNativeQuery(
                "INSERT INTO sri_tipo_documento (std_codigo, std_nombre, std_nombre_portal, std_codigo_portal, std_orden, std_activo) "
                + "SELECT ?1, ?2, ?3, ?4, ?5, true "
                + "WHERE NOT EXISTS (SELECT 1 FROM sri_tipo_documento WHERE std_codigo = ?1)")
                .setParameter(1, codigo)
                .setParameter(2, nombre)
                .setParameter(3, portal)
                .setParameter(4, codigoPortal)
                .setParameter(5, orden)
                .executeUpdate();
    }

    private void ejecutarSilencioso(EntityManager em, String sql) {
        try {
            em.createNativeQuery(sql).executeUpdate();
        } catch (Exception e) {
            LOG.log(Level.FINE, sql, e);
        }
    }
}
