/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.NotaCreditoDebito;
import com.ec.servicio.ServicioNotaCredito;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class CambioEstadoNc {

    @Wire
    Window windowEstFact;
    private NotaCreditoDebito notaCreditoDebito;
    private String estado;
    private String descripcionAnula;
    ServicioNotaCredito servicioNotaCredito = new ServicioNotaCredito();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") NotaCreditoDebito valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        this.notaCreditoDebito = valor;
        estado = valor.getEstadosri() != null ? valor.getEstadosri() : "";
    }

    @Command
    public void guardar() {
       
//            facturar.setEstadosri(estado);
//            facturar.setMensajesri(descripcionAnula);
            servicioNotaCredito.modificar(notaCreditoDebito);

            Clients.showNotification("Guardado correctamente",
                    Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 1000, true);
            windowEstFact.detach();
//        } else {
//            Clients.showNotification("Verifique el estado de la factura",
//                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
//            windowEstFact.detach();
//        }

    }

    public NotaCreditoDebito getNotaCreditoDebito() {
        return notaCreditoDebito;
    }

    public void setNotaCreditoDebito(NotaCreditoDebito notaCreditoDebito) {
        this.notaCreditoDebito = notaCreditoDebito;
    }

   
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcionAnula() {
        return descripcionAnula;
    }

    public void setDescripcionAnula(String descripcionAnula) {
        this.descripcionAnula = descripcionAnula;
    }

}
