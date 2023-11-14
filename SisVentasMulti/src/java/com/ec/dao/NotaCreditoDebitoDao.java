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
    
    
}
