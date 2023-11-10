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
public class RetencionDao {

    private String rcoFecha;
    private Date facFecha;
    private String facNumeroText;
    private String identificacionComprador;
    private String razonSocialComprador;
    private Integer facNumero;
    private InfoAutoriza infoAutoriza;
    private String amCodigo;
    private String rucEmpresa;
    private String establecimientoEmpresa;
    private String puntoEmisionEmpresa;
    private String razonSocialEmpresa;
    private String nombreComercialEmpresa;
    private String direccionMatriz;
    private String llevarContabilidad;
    private String ticCodigo;
    private String provNombre;
    private String provCedula;
    private List<DetRetencionDao> detRetencionDao;
    private Boolean amAgeRet;
    private Boolean amRimpe;
    private Boolean amGeneral;

    public String getProvCedula() {
        return provCedula;
    }

    public void setProvCedula(String provCedula) {
        this.provCedula = provCedula;
    }

    public String getProvNombre() {
        return provNombre;
    }

    public void setProvNombre(String provNombre) {
        this.provNombre = provNombre;
    }

    public String getTicCodigo() {
        return ticCodigo;
    }

    public void setTicCodigo(String ticCodigo) {
        this.ticCodigo = ticCodigo;
    }

    public String getRcoFecha() {
        return rcoFecha;
    }

    public void setRcoFecha(String rcoFecha) {
        this.rcoFecha = rcoFecha;
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

    public Integer getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(Integer facNumero) {
        this.facNumero = facNumero;
    }

    public InfoAutoriza getInfoAutoriza() {
        return infoAutoriza;
    }

    public void setInfoAutoriza(InfoAutoriza infoAutoriza) {
        this.infoAutoriza = infoAutoriza;
    }

    public String getAmCodigo() {
        return amCodigo;
    }

    public void setAmCodigo(String amCodigo) {
        this.amCodigo = amCodigo;
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

    public String getDireccionMatriz() {
        return direccionMatriz;
    }

    public void setDireccionMatriz(String direccionMatriz) {
        this.direccionMatriz = direccionMatriz;
    }

    public String getLlevarContabilidad() {
        return llevarContabilidad;
    }

    public void setLlevarContabilidad(String llevarContabilidad) {
        this.llevarContabilidad = llevarContabilidad;
    }

    public List<DetRetencionDao> getDetRetencionDao() {
        return detRetencionDao;
    }

    public void setDetRetencionDao(List<DetRetencionDao> detRetencionDao) {
        this.detRetencionDao = detRetencionDao;
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

    
}
