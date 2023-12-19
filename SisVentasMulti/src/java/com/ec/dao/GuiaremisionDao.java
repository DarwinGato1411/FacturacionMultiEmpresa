/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.dao;

import java.util.Date;
import java.util.List;

/**
 *
 * @author jowel
 */
public class GuiaremisionDao {

    private Date facFecha;
    private String amCodigo;
    private Boolean amRimpe;
    private Boolean amGeneral;
    private String razonSocialEmpresa;
    private String nombreComercialEmpresa;
    private String rucEmpresa;
    private String establecimientoEmpresa;
    private String puntoEmisionEmpresa;
    private int secuencial;
    private String secuencialText;
    private String dirMatrizEmpresa;
    private String dirEstablecimiento;
    private String dirPartida;
    private String dirLlegada;
    private String razonSocialTransportista;
    private String tipoIdentificacionTransportista;
    private String rucTransportista;
    private String obligadoContabilidad;
    private Date fechaIniTranspguia;
    private Date fechaFinTranspguia;
    private String placaGuia;
    private String identificacionDestinatario;
    private String razonSocialDestinatario;
    private String dirDestinatario;
    private String motivoTraslado;
    private String docAduaneroUnico;
    private String ruta;
    private InfoAutorizaDao infoAutoriza;
    private List<DetalleGuiaremisionDao> detalleGuiaRemision;

    private String correoDestinatario;

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public String getEstablecimientoEmpresa() {
        return establecimientoEmpresa;
    }

    public void setEstablecimientoEmpresa(String establecimientoEmpresa) {
        this.establecimientoEmpresa = establecimientoEmpresa;
    }

    public String getAmCodigo() {
        return amCodigo;
    }

    public void setAmCodigo(String amCodigo) {
        this.amCodigo = amCodigo;
    }

    public String getPuntoEmisionEmpresa() {
        return puntoEmisionEmpresa;
    }

    public void setPuntoEmisionEmpresa(String puntoEmisionEmpresa) {
        this.puntoEmisionEmpresa = puntoEmisionEmpresa;
    }

    public Boolean getAmRimpe() {
        return amRimpe;
    }

    public void setAmRimpe(Boolean amRimpe) {
        this.amRimpe = amRimpe;
    }

    public Boolean getAmGeneral() {
        return amGeneral;
    }

    public void setAmGeneral(Boolean amGeneral) {
        this.amGeneral = amGeneral;
    }

    public String getSecuencialText() {
        return secuencialText;
    }

    public void setSecuencialText(String secuencialText) {
        this.secuencialText = secuencialText;
    }

    public String getRazonSocialTransportista() {
        return razonSocialTransportista;
    }

    public void setRazonSocialTransportista(String razonSocialTransportista) {
        this.razonSocialTransportista = razonSocialTransportista;
    }

    public String getTipoIdentificacionTransportista() {
        return tipoIdentificacionTransportista;
    }

    public void setTipoIdentificacionTransportista(String tipoIdentificacionTransportista) {
        this.tipoIdentificacionTransportista = tipoIdentificacionTransportista;
    }

    public String getRucTransportista() {
        return rucTransportista;
    }

    public void setRucTransportista(String rucTransportista) {
        this.rucTransportista = rucTransportista;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad == null ? "NO" : obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public Date getFechaIniTranspguia() {
        return fechaIniTranspguia;
    }

    public void setFechaIniTranspguia(Date fechaIniTranspguia) {
        this.fechaIniTranspguia = fechaIniTranspguia;
    }

    public Date getFechaFinTranspguia() {
        return fechaFinTranspguia;
    }

    public void setFechaFinTranspguia(Date fechaFinTranspguia) {
        this.fechaFinTranspguia = fechaFinTranspguia;
    }

    public String getPlacaGuia() {
        return placaGuia;
    }

    public void setPlacaGuia(String placaGuia) {
        this.placaGuia = placaGuia;
    }

    public String getIdentificacionDestinatario() {
        return identificacionDestinatario;
    }

    public void setIdentificacionDestinatario(String identificacionDestinatario) {
        this.identificacionDestinatario = identificacionDestinatario;
    }

    public String getRazonSocialDestinatario() {
        return razonSocialDestinatario;
    }

    public void setRazonSocialDestinatario(String razonSocialDestinatario) {
        this.razonSocialDestinatario = razonSocialDestinatario;
    }

    public String getDirDestinatario() {
        return dirDestinatario;
    }

    public void setDirDestinatario(String dirDestinatario) {
        this.dirDestinatario = dirDestinatario;
    }

    public String getMotivoTraslado() {
        return motivoTraslado;
    }

    public void setMotivoTraslado(String motivoTraslado) {
        this.motivoTraslado = motivoTraslado;
    }

    public String getDocAduaneroUnico() {
        return docAduaneroUnico;
    }

    public void setDocAduaneroUnico(String docAduaneroUnico) {
        this.docAduaneroUnico = docAduaneroUnico;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List<DetalleGuiaremisionDao> getDetalleGuiaRemision() {
        return detalleGuiaRemision;
    }

    public void setDetalleGuiaRemision(List<DetalleGuiaremisionDao> detalleGuiaRemision) {
        this.detalleGuiaRemision = detalleGuiaRemision;
    }

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }


    public String getRazonSocialEmpresa() {
        return razonSocialEmpresa;
    }

    public void setRazonSocialEmpresa(String razonSocialEmpresa) {
        this.razonSocialEmpresa = razonSocialEmpresa;
    }

    public String getNombreComercialEmpresa() {
        return nombreComercialEmpresa;
    }

    public void setNombreComercialEmpresa(String nombreComercialEmpresa) {
        this.nombreComercialEmpresa = nombreComercialEmpresa;
    }

    public String getDirMatrizEmpresa() {
        return dirMatrizEmpresa;
    }

    public void setDirMatrizEmpresa(String dirMatrizEmpresa) {
        this.dirMatrizEmpresa = dirMatrizEmpresa;
    }

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public String getDirPartida() {
        return dirPartida;
    }

    public void setDirPartida(String dirPartida) {
        this.dirPartida = dirPartida;
    }

    public InfoAutorizaDao getInfoAutoriza() {
        return infoAutoriza;
    }

    public void setInfoAutoriza(InfoAutorizaDao infoAutoriza) {
        this.infoAutoriza = infoAutoriza;
    }

    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }

    public String getDirLlegada() {
        return dirLlegada;
    }

    public void setDirLlegada(String dirLlegada) {
        this.dirLlegada = dirLlegada;
    }

  
    public String getCorreoDestinatario() {
        return correoDestinatario;
    }

    public void setCorreoDestinatario(String correoDestinatario) {
        this.correoDestinatario = correoDestinatario;
    }

    
}
