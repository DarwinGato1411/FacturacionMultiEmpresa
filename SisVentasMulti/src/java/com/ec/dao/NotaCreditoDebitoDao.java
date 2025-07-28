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
public class NotaCreditoDebitoDao {

    private Date facFecha;
    private String tipoNotaCredito;
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
    private String tipoIdentificacionComprador;
    private String identificacionComprador;
    private String razonSocialComprador;
    private String obligadoContabilidad;
    private String codDocModificado;
    private String numDocModificado;
    private Date fechaEmisionDocSustento;
    private BigDecimal totalSinImpuestos;
    private BigDecimal valorModificacion;
    private String moneda;
    private String codigo;
    private String codigoPorcentaje;
    private BigDecimal baseImponible;
    private BigDecimal valor;
    private String motivo;
    private List<DetalleNotaCreditoDebitoDao> detalleNotaCredito;
    private InfoAutorizaDao infoAutoriza;
    private String correo;

    private String facNumeroText;
    private String facCodIva;
    private BigDecimal facTotalBaseCero;
    private BigDecimal facTotalBaseGravada;
    private BigDecimal facIva;
    private BigDecimal facSubt5;
    private BigDecimal facSubt15;
    private BigDecimal facIva5;
    private BigDecimal facIva15;

    private String llevarContabilidad;
    private String direccionMatriz;
    private BigDecimal facSubtotal;
    private BigDecimal facSubsidio;
    private BigDecimal facDescuento;

    private String codigoICE;
    private BigDecimal facValorIce;
    private Boolean grabaICE;
    private BigDecimal facTarifaIce;
    private String facPorcentajeIva;
    private BigDecimal facTotal;

    private String codigoFormaPago;
    private String facMoneda;
    private String facUnidaTiempo;
    private Boolean agenteRetencion;
    private Boolean rimpePolpular;
    private Boolean rimpeEmprendedor;
    private Boolean regimenGeneral;
    private BigDecimal facPlazo;
    private String direccionComprador;
    private String correoComprador;
    private String telefonoComprador;
    private String observacion;
    private String facTipo;

    private String movilComprador;

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }

    public String getAmCodigo() {
        return amCodigo;
    }

    public void setAmCodigo(String amCodigo) {
        this.amCodigo = amCodigo;
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

    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }

    public String getSecuencialText() {
        return secuencialText;
    }

    public void setSecuencialText(String secuencialText) {
        this.secuencialText = secuencialText;
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

    public String getTipoIdentificacionComprador() {
        return tipoIdentificacionComprador;
    }

    public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
        this.tipoIdentificacionComprador = tipoIdentificacionComprador;
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

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getTipoNotaCredito() {
        return tipoNotaCredito;
    }

    public void setTipoNotaCredito(String tipoNotaCredito) {
        this.tipoNotaCredito = tipoNotaCredito;
    }

    public String getCodDocModificado() {
        return codDocModificado;
    }

    public void setCodDocModificado(String codDocModificado) {
        this.codDocModificado = codDocModificado;
    }

    public String getNumDocModificado() {
        return numDocModificado;
    }

    public void setNumDocModificado(String numDocModificado) {
        this.numDocModificado = numDocModificado;
    }

    public Date getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    public void setFechaEmisionDocSustento(Date fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    public BigDecimal getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setTotalSinImpuestos(BigDecimal totalSinImpuestos) {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    public BigDecimal getValorModificacion() {
        return valorModificacion;
    }

    public void setValorModificacion(BigDecimal valorModificacion) {
        this.valorModificacion = valorModificacion;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoPorcentaje() {
        return codigoPorcentaje;
    }

    public void setCodigoPorcentaje(String codigoPorcentaje) {
        this.codigoPorcentaje = codigoPorcentaje;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public List<DetalleNotaCreditoDebitoDao> getDetalleNotaCredito() {
        return detalleNotaCredito;
    }

    public void setDetalleNotaCredito(List<DetalleNotaCreditoDebitoDao> detalleNotaCredito) {
        this.detalleNotaCredito = detalleNotaCredito;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public InfoAutorizaDao getInfoAutoriza() {
        return infoAutoriza;
    }

    public void setInfoAutoriza(InfoAutorizaDao infoAutoriza) {
        this.infoAutoriza = infoAutoriza;
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

    public BigDecimal getFacIva() {
        return facIva;
    }

    public void setFacIva(BigDecimal facIva) {
        this.facIva = facIva;
    }

    public String getLlevarContabilidad() {
        return llevarContabilidad;
    }

    public void setLlevarContabilidad(String llevarContabilidad) {
        this.llevarContabilidad = llevarContabilidad;
    }

    public String getDireccionMatriz() {
        return direccionMatriz;
    }

    public void setDireccionMatriz(String direccionMatriz) {
        this.direccionMatriz = direccionMatriz;
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

    public String getCodigoICE() {
        return codigoICE;
    }

    public void setCodigoICE(String codigoICE) {
        this.codigoICE = codigoICE;
    }

    public BigDecimal getFacValorIce() {
        return facValorIce;
    }

    public void setFacValorIce(BigDecimal facValorIce) {
        this.facValorIce = facValorIce;
    }

    public Boolean getGrabaICE() {
        return grabaICE;
    }

    public void setGrabaICE(Boolean grabaICE) {
        this.grabaICE = grabaICE;
    }

    public BigDecimal getFacTarifaIce() {
        return facTarifaIce;
    }

    public void setFacTarifaIce(BigDecimal facTarifaIce) {
        this.facTarifaIce = facTarifaIce;
    }

    public String getFacPorcentajeIva() {
        return facPorcentajeIva;
    }

    public void setFacPorcentajeIva(String facPorcentajeIva) {
        this.facPorcentajeIva = facPorcentajeIva;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public String getCodigoFormaPago() {
        return codigoFormaPago;
    }

    public void setCodigoFormaPago(String codigoFormaPago) {
        this.codigoFormaPago = codigoFormaPago;
    }

    public String getFacMoneda() {
        return facMoneda;
    }

    public void setFacMoneda(String facMoneda) {
        this.facMoneda = facMoneda;
    }

    public String getFacUnidaTiempo() {
        return facUnidaTiempo;
    }

    public void setFacUnidaTiempo(String facUnidaTiempo) {
        this.facUnidaTiempo = facUnidaTiempo;
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

    public BigDecimal getFacPlazo() {
        return facPlazo;
    }

    public void setFacPlazo(BigDecimal facPlazo) {
        this.facPlazo = facPlazo;
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

    public String getTelefonoComprador() {
        return telefonoComprador;
    }

    public void setTelefonoComprador(String telefonoComprador) {
        this.telefonoComprador = telefonoComprador;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFacTipo() {
        return facTipo;
    }

    public void setFacTipo(String facTipo) {
        this.facTipo = facTipo;
    }

    public String getMovilComprador() {
        return movilComprador;
    }

    public void setMovilComprador(String movilComprador) {
        this.movilComprador = movilComprador;
    }

}
