/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.dao.response;

import java.util.Date;

/**
 *
 * @author Darwin
 */
public class FacturaResponse {

    private Date facFecha;
    private String facNumeroText;
    private String identificacionComprador;
    private String razonSocialComprador;
    private String xmlAutorizado;
    private Date fechaAtorizacion;
    private String estadoSri;
    private String mensajeError;
    private String detalleError;
    private String claveAutorizacion;

    public FacturaResponse() {
    }

    public FacturaResponse(Date facFecha, String facNumeroText, String identificacionComprador, String razonSocialComprador, String xmlAutorizado, Date fechaAtorizacion, String estadoSri) {
        this.facFecha = facFecha;
        this.facNumeroText = facNumeroText;
        this.identificacionComprador = identificacionComprador;
        this.razonSocialComprador = razonSocialComprador;
        this.xmlAutorizado = xmlAutorizado;
        this.fechaAtorizacion = fechaAtorizacion;
        this.estadoSri = estadoSri;
    }

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }

    public String getFacNumeroText() {
        return facNumeroText;
    }

    public void setFacNumeroText(String facNumeroText) {
        this.facNumeroText = facNumeroText;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public String getRazonSocialComprador() {
        return razonSocialComprador;
    }

    public void setRazonSocialComprador(String razonSocialComprador) {
        this.razonSocialComprador = razonSocialComprador;
    }

    public String getXmlAutorizado() {
        return xmlAutorizado;
    }

    public void setXmlAutorizado(String xmlAutorizado) {
        this.xmlAutorizado = xmlAutorizado;
    }

    public Date getFechaAtorizacion() {
        return fechaAtorizacion;
    }

    public void setFechaAtorizacion(Date fechaAtorizacion) {
        this.fechaAtorizacion = fechaAtorizacion;
    }

    public String getEstadoSri() {
        return estadoSri;
    }

    public void setEstadoSri(String estadoSri) {
        this.estadoSri = estadoSri;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public String getDetalleError() {
        return detalleError;
    }

    public void setDetalleError(String detalleError) {
        this.detalleError = detalleError;
    }

    public String getClaveAutorizacion() {
        return claveAutorizacion;
    }

    public void setClaveAutorizacion(String claveAutorizacion) {
        this.claveAutorizacion = claveAutorizacion;
    }
}
