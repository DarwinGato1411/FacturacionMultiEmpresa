/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Darwin
 */
public class FacturaDao {

    private String amCodigo;
    private Date facFecha;
    private Integer facNumero;
    private String facNumeroText;
    private BigDecimal facTotalBaseGravada;
    private String codigoICE;
    private BigDecimal facTarifaIce;
    private BigDecimal facValorIce;
    private String razonSocialEmpresa;
    private String nombreComercialEmpresa;
    private String rucEmpresa;
    private String establecimientoEmpresa;
    private String puntoEmisionEmpresa;
    private String direccionMatriz;
    private Boolean agenteRetencion;
    private Boolean rimpePolpular;
    private Boolean rimpeEmprendedor;
    private Boolean regimenGeneral;
    private String facTipo;
    private String llevarContabilidad;
    private String tipoIdentificacionComprador;
    private String razonSocialComprador;
    private String identificacionComprador;
    private BigDecimal facSubtotal;
    private BigDecimal facSubsidio;
    private BigDecimal facDescuento;
    private String facCodIva;
    private BigDecimal facTotalBaseCero;
    private Boolean grabaICE;
    private String codigoPorcentaje;
    private BigDecimal facIva;
    private BigDecimal facTotal;
    private String facMoneda;
    private String codigoFormaPago;
    private BigDecimal facPlazo;
    private String facUnidaTiempo;
    private String direccionComprador;
    private String correoComprador;
    private String telefonoComprador;
    private String observacion;
    private String facPorcentajeIva;

    private List<DetFacturaDao> detFacturaDao;
    private InfoAutorizaDao infoAutoriza;
    private String xmlAutorizado;
    private String tipoEmision;

    private String movilComprador;

    private BigDecimal facSubt5;
    private BigDecimal facSubt15;
       private BigDecimal facIva5;
          private BigDecimal facIva15;

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }

    public Integer getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(Integer facNumero) {
        this.facNumero = facNumero;
    }

    public String getFacNumeroText() {
        return facNumeroText;
    }

    public void setFacNumeroText(String facNumeroText) {
        this.facNumeroText = facNumeroText;
    }

    public BigDecimal getFacTotalBaseGravada() {
        return facTotalBaseGravada;
    }

    public void setFacTotalBaseGravada(BigDecimal facTotalBaseGravada) {
        this.facTotalBaseGravada = facTotalBaseGravada;
    }

    public String getCodigoICE() {
        return codigoICE;
    }

    public void setCodigoICE(String codigoICE) {
        this.codigoICE = codigoICE;
    }

    public BigDecimal getFacTarifaIce() {
        return facTarifaIce;
    }

    public void setFacTarifaIce(BigDecimal facTarifaIce) {
        this.facTarifaIce = facTarifaIce;
    }

    public BigDecimal getFacValorIce() {
        return facValorIce;
    }

    public void setFacValorIce(BigDecimal facValorIce) {
        this.facValorIce = facValorIce;
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

    public String getPuntoEmisionEmpresa() {
        return puntoEmisionEmpresa;
    }

    public void setPuntoEmisionEmpresa(String puntoEmisionEmpresa) {
        this.puntoEmisionEmpresa = puntoEmisionEmpresa;
    }

    public String getDireccionMatriz() {
        return direccionMatriz;
    }

    public void setDireccionMatriz(String direccionMatriz) {
        this.direccionMatriz = direccionMatriz;
    }

    public Boolean getAgenteRetencion() {
        return agenteRetencion;
    }

    public void setAgenteRetencion(Boolean agenteRetencion) {
        this.agenteRetencion = agenteRetencion;
    }

    public Boolean getRimpePolpular() {
        return rimpePolpular;
    }

    public void setRimpePolpular(Boolean rimpePolpular) {
        this.rimpePolpular = rimpePolpular;
    }

    public Boolean getRimpeEmprendedor() {
        return rimpeEmprendedor;
    }

    public void setRimpeEmprendedor(Boolean rimpeEmprendedor) {
        this.rimpeEmprendedor = rimpeEmprendedor;
    }

    public Boolean getRegimenGeneral() {
        return regimenGeneral;
    }

    public void setRegimenGeneral(Boolean regimenGeneral) {
        this.regimenGeneral = regimenGeneral;
    }

    public String getTipoIdentificacionComprador() {
        return tipoIdentificacionComprador;
    }

    public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
        this.tipoIdentificacionComprador = tipoIdentificacionComprador;
    }

    public String getRazonSocialComprador() {
        return razonSocialComprador;
    }

    public void setRazonSocialComprador(String razonSocialComprador) {
        this.razonSocialComprador = razonSocialComprador;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public BigDecimal getFacSubtotal() {
        return facSubtotal;
    }

    public void setFacSubtotal(BigDecimal facSubtotal) {
        this.facSubtotal = facSubtotal;
    }

    public BigDecimal getFacSubsidio() {
        return facSubsidio;
    }

    public void setFacSubsidio(BigDecimal facSubsidio) {
        this.facSubsidio = facSubsidio;
    }

    public BigDecimal getFacDescuento() {
        return facDescuento;
    }

    public void setFacDescuento(BigDecimal facDescuento) {
        this.facDescuento = facDescuento;
    }

    public String getFacCodIva() {
        return facCodIva;
    }

    public void setFacCodIva(String facCodIva) {
        this.facCodIva = facCodIva;
    }

    public BigDecimal getFacTotalBaseCero() {
        return facTotalBaseCero;
    }

    public void setFacTotalBaseCero(BigDecimal facTotalBaseCero) {
        this.facTotalBaseCero = facTotalBaseCero;
    }

    public Boolean getGrabaICE() {
        return grabaICE;
    }

    public void setGrabaICE(Boolean grabaICE) {
        this.grabaICE = grabaICE;
    }

    public String getCodigoPorcentaje() {
        return codigoPorcentaje;
    }

    public void setCodigoPorcentaje(String codigoPorcentaje) {
        this.codigoPorcentaje = codigoPorcentaje;
    }

    public BigDecimal getFacIva() {
        return facIva;
    }

    public void setFacIva(BigDecimal facIva) {
        this.facIva = facIva;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public String getFacMoneda() {
        return facMoneda;
    }

    public void setFacMoneda(String facMoneda) {
        this.facMoneda = facMoneda;
    }

    public String getCodigoFormaPago() {
        return codigoFormaPago;
    }

    public void setCodigoFormaPago(String codigoFormaPago) {
        this.codigoFormaPago = codigoFormaPago;
    }

    public BigDecimal getFacPlazo() {
        return facPlazo;
    }

    public void setFacPlazo(BigDecimal facPlazo) {
        this.facPlazo = facPlazo;
    }

    public String getFacUnidaTiempo() {
        return facUnidaTiempo;
    }

    public void setFacUnidaTiempo(String facUnidaTiempo) {
        this.facUnidaTiempo = facUnidaTiempo;
    }

    public String getDireccionComprador() {
        return direccionComprador;
    }

    public void setDireccionComprador(String direccionComprador) {
        this.direccionComprador = direccionComprador;
    }

    public String getCorreoComprador() {
        return correoComprador;
    }

    public void setCorreoComprador(String correoComprador) {
        this.correoComprador = correoComprador;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFacPorcentajeIva() {
        return facPorcentajeIva;
    }

    public void setFacPorcentajeIva(String facPorcentajeIva) {
        this.facPorcentajeIva = facPorcentajeIva;
    }

    public List<DetFacturaDao> getDetFacturaDao() {
        return detFacturaDao;
    }

    public void setDetFacturaDao(List<DetFacturaDao> detFacturaDao) {
        this.detFacturaDao = detFacturaDao;
    }

    public String getAmCodigo() {
        return amCodigo;
    }

    public void setAmCodigo(String amCodigo) {
        this.amCodigo = amCodigo;
    }

    public String getLlevarContabilidad() {
        return llevarContabilidad;
    }

    public void setLlevarContabilidad(String llevarContabilidad) {
        this.llevarContabilidad = llevarContabilidad;
    }

    public InfoAutorizaDao getInfoAutoriza() {
        return infoAutoriza;
    }

    public void setInfoAutoriza(InfoAutorizaDao infoAutoriza) {
        this.infoAutoriza = infoAutoriza;
    }

    public String getXmlAutorizado() {
        return xmlAutorizado;
    }

    public void setXmlAutorizado(String xmlAutorizado) {
        this.xmlAutorizado = xmlAutorizado;
    }

    public String getFacTipo() {
        return facTipo;
    }

    public void setFacTipo(String facTipo) {
        this.facTipo = facTipo;
    }

    public String getTelefonoComprador() {
        return telefonoComprador;
    }

    public void setTelefonoComprador(String telefonoComprador) {
        this.telefonoComprador = telefonoComprador;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public String getMovilComprador() {
        return movilComprador;
    }

    public void setMovilComprador(String movilComprador) {
        this.movilComprador = movilComprador;
    }

    public BigDecimal getFacSubt5() {
        return facSubt5;
    }

    public void setFacSubt5(BigDecimal facSubt5) {
        this.facSubt5 = facSubt5;
    }

    public BigDecimal getFacSubt15() {
        return facSubt15;
    }

    public void setFacSubt15(BigDecimal facSubt15) {
        this.facSubt15 = facSubt15;
    }

    public BigDecimal getFacIva5() {
        return facIva5;
    }

    public void setFacIva5(BigDecimal facIva5) {
        this.facIva5 = facIva5;
    }

    public BigDecimal getFacIva15() {
        return facIva15;
    }

    public void setFacIva15(BigDecimal facIva15) {
        this.facIva15 = facIva15;
    }

}
