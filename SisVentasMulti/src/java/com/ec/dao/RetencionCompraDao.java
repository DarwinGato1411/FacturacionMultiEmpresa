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

    private String amRuc;
    private String amEstab;
    private String amPtoemi;
    private String amCodigo;
    private Date rcoFecha;
    private String rcoSecuencialText;
    private int rcoSecuencial;
    private String razonSocialEmpresa;
    private String amNombreComercial;
    private String amDireccionMatriz;
    private String llevarContabilidad;
    private String ticCodigo;
    private String provNombre;
    private String provCedula;
    private boolean amAgeRet;
    private boolean amRimpe;
    private boolean amGeneral;
    private List<DetRetencionCompraDao> detRetencion;
    private InfoAutorizaDao infoAutoriza;

    public Date getRcoFecha() {
        return rcoFecha;
    }

    public void setRcoFecha(Date rcoFecha) {
        this.rcoFecha = rcoFecha;
    }

    public String getAmCodigo() {
        return amCodigo;
    }

    public void setAmCodigo(String amCodigo) {
        this.amCodigo = amCodigo;
    }

    public String getAmRuc() {
        return amRuc;
    }

    public void setAmRuc(String amRuc) {
        this.amRuc = amRuc;
    }

    public String getAmEstab() {
        return amEstab;
    }

    public void setAmEstab(String amEstab) {
        this.amEstab = amEstab;
    }

    public String getAmPtoemi() {
        return amPtoemi;
    }

    public void setAmPtoemi(String amPtoemi) {
        this.amPtoemi = amPtoemi;
    }

    public String getRcoSecuencialText() {
        return rcoSecuencialText;
    }

    public void setRcoSecuencialText(String rcoSecuencialText) {
        this.rcoSecuencialText = rcoSecuencialText;
    }

    public String getAmRazonSocial() {
        return razonSocialEmpresa;
    }

    public void setAmRazonSocial(String amRazonSocial) {
        this.razonSocialEmpresa = amRazonSocial;
    }

    public String getAmNombreComercial() {
        return amNombreComercial;
    }

    public void setAmNombreComercial(String amNombreComercial) {
        this.amNombreComercial = amNombreComercial;
    }

    public String getAmDireccionMatriz() {
        return amDireccionMatriz;
    }

    public void setAmDireccionMatriz(String amDireccionMatriz) {
        this.amDireccionMatriz = amDireccionMatriz;
    }

    public String getLlevarContabilidad() {
        return llevarContabilidad;
    }

    public void setLlevarContabilidad(String llevarContabilidad) {
        this.llevarContabilidad = llevarContabilidad;
    }

    public String getTicCodigo() {
        return ticCodigo;
    }

    public void setTicCodigo(String ticCodigo) {
        this.ticCodigo = ticCodigo;
    }

    public String getProvNombre() {
        return provNombre;
    }

    public void setProvNombre(String provNombre) {
        this.provNombre = provNombre;
    }

    public String getProvCedula() {
        return provCedula;
    }

    public void setProvCedula(String provCedula) {
        this.provCedula = provCedula;
    }

    public List<DetRetencionCompraDao> getDetRetencion() {
        return detRetencion;
    }

    public void setDetRetencion(List<DetRetencionCompraDao> detRetencionDao) {
        this.detRetencion = detRetencionDao;
    }

    public boolean isAmAgeRet() {
        return amAgeRet;
    }

    public void setAmAgeRet(boolean amAgeRet) {
        this.amAgeRet = amAgeRet;
    }

    public boolean isAmRimpe() {
        return amRimpe;
    }

    public void setAmRimpe(boolean amRimpe) {
        this.amRimpe = amRimpe;
    }

    public boolean isAmGeneral() {
        return amGeneral;
    }

    public void setAmGeneral(boolean amGeneral) {
        this.amGeneral = amGeneral;
    }

    public int getRcoSecuencial() {
        return rcoSecuencial;
    }

    public void setRcoSecuencial(int rcoSecuencial) {
        this.rcoSecuencial = rcoSecuencial;
    }

    public InfoAutorizaDao getInfoAutoriza() {
        return infoAutoriza;
    }

    public void setInfoAutoriza(InfoAutorizaDao infoAutoriza) {
        this.infoAutoriza = infoAutoriza;
    }

}
