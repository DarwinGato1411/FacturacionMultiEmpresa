/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.dao;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Darwin
 */
public class DetRetencionDao {

    private BigDecimal drcValorRetenido;
    private String drcCodImpuestoAsignado;
    private String drcDescripcion;
    private BigDecimal drcBaseImponible;
    private BigDecimal drcPorcentaje;
    private String drcCodigoSustento;
    private String cabEstablecimiento;
    private String cabPuntoEmi;
    private String cabNumFactura;
    private String tipivaretValor;
    private String tireCodigo;
    private Date cabFechaEmision;

    public BigDecimal getDrcValorRetenido() {
        return drcValorRetenido;
    }

    public void setDrcValorRetenido(BigDecimal drcValorRetenido) {
        this.drcValorRetenido = drcValorRetenido;
    }

    public String getDrcCodImpuestoAsignado() {
        return drcCodImpuestoAsignado;
    }

    public void setDrcCodImpuestoAsignado(String drcCodImpuestoAsignado) {
        this.drcCodImpuestoAsignado = drcCodImpuestoAsignado;
    }

    public String getDrcDescripcion() {
        return drcDescripcion;
    }

    public void setDrcDescripcion(String drcDescripcion) {
        this.drcDescripcion = drcDescripcion;
    }

    public BigDecimal getDrcBaseImponible() {
        return drcBaseImponible;
    }

    public void setDrcBaseImponible(BigDecimal drcBaseImponible) {
        this.drcBaseImponible = drcBaseImponible;
    }

    public BigDecimal getDrcPorcentaje() {
        return drcPorcentaje;
    }

    public void setDrcPorcentaje(BigDecimal drcPorcentaje) {
        this.drcPorcentaje = drcPorcentaje;
    }

    public String getDrcCodigoSustento() {
        return drcCodigoSustento;
    }

    public void setDrcCodigoSustento(String drcCodigoSustento) {
        this.drcCodigoSustento = drcCodigoSustento;
    }

    public String getCabEstablecimiento() {
        return cabEstablecimiento;
    }

    public void setCabEstablecimiento(String cabEstablecimiento) {
        this.cabEstablecimiento = cabEstablecimiento;
    }

    public String getCabPuntoEmi() {
        return cabPuntoEmi;
    }

    public void setCabPuntoEmi(String cabPuntoEmi) {
        this.cabPuntoEmi = cabPuntoEmi;
    }

    public String getCabNumFactura() {
        return cabNumFactura;
    }

    public void setCabNumFactura(String cabNumFactura) {
        this.cabNumFactura = cabNumFactura;
    }

    public String getTipivaretValor() {
        return tipivaretValor;
    }

    public void setTipivaretValor(String tipivaretValor) {
        this.tipivaretValor = tipivaretValor;
    }

    public String getTireCodigo() {
        return tireCodigo;
    }

    public void setTireCodigo(String tireCodigo) {
        this.tireCodigo = tireCodigo;
    }

    public Date getCabFechaEmision() {
        return cabFechaEmision;
    }

    public void setCabFechaEmision(Date cabFechaEmision) {
        this.cabFechaEmision = cabFechaEmision;
    }

}
