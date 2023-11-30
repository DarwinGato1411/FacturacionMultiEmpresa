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
public class RetencionCompraDao {

    private Date retencionFecha;
    private String amCodigo;
    private Boolean amAgeRet;
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
    private Date fechaEmision;
    private String dirEstablecimiento;
    private String obligadoContabilidad;
    private String tipoIdentificacionSujetoRetenido;
    private String razonSocialSujetoRetenido;
    private String identificacionSujetoRetenido;
    private Date periodoFiscal;
    private List<DetRetencionCompraDao> detRetencion;
    private InfoAutorizaDao infoAutoriza;

    public Date getRetencionFecha() {
        return retencionFecha;
    }

    public void setRetencionFecha(Date retencionFecha) {
        this.retencionFecha = retencionFecha;
    }

    public String getAmCodigo() {
        return amCodigo;
    }

    public void setAmCodigo(String amCodigo) {
        this.amCodigo = amCodigo;
    }

    public Boolean getAmAgeRet() {
        return amAgeRet;
    }

    public void setAmAgeRet(Boolean amAgeRet) {
        this.amAgeRet = amAgeRet;
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

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getTipoIdentificacionSujetoRetenido() {
        return tipoIdentificacionSujetoRetenido;
    }

    public void setTipoIdentificacionSujetoRetenido(String tipoIdentificacionSujetoRetenido) {
        this.tipoIdentificacionSujetoRetenido = tipoIdentificacionSujetoRetenido;
    }

    public String getRazonSocialSujetoRetenido() {
        return razonSocialSujetoRetenido;
    }

    public void setRazonSocialSujetoRetenido(String razonSocialSujetoRetenido) {
        this.razonSocialSujetoRetenido = razonSocialSujetoRetenido;
    }

    public String getIdentificacionSujetoRetenido() {
        return identificacionSujetoRetenido;
    }

    public void setIdentificacionSujetoRetenido(String identificacionSujetoRetenido) {
        this.identificacionSujetoRetenido = identificacionSujetoRetenido;
    }

    public Date getPeriodoFiscal() {
        return periodoFiscal;
    }

    public void setPeriodoFiscal(Date periodoFiscal) {
        this.periodoFiscal = periodoFiscal;
    }

    public List<DetRetencionCompraDao> getDetRetencion() {
        return detRetencion;
    }

    public void setDetRetencion(List<DetRetencionCompraDao> detRetencion) {
        this.detRetencion = detRetencion;
    }

    public InfoAutorizaDao getInfoAutoriza() {
        return infoAutoriza;
    }

    public void setInfoAutoriza(InfoAutorizaDao infoAutoriza) {
        this.infoAutoriza = infoAutoriza;
    }

}
