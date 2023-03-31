/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.dao;

import java.math.BigDecimal;

/**
 *
 * @author Darwin
 */
public class DetFacturaDao {

    private Boolean tieneSubsidio;
    private String codigoProducto;
    private String descripcionProducto;
    private BigDecimal precioProductoSinSubsidio;
    private BigDecimal detSubtotaldescuento;
    private BigDecimal detCantidad;
    private BigDecimal detSubtotal;
    private BigDecimal detCantpordescuento;
    private String detCodIva;
    private String detCodPorcentaje;
    private BigDecimal detTarifa;
    private BigDecimal detValorIce;

    private BigDecimal detIva;

    public Boolean getTieneSubsidio() {
        return tieneSubsidio;
    }

    public void setTieneSubsidio(Boolean tieneSubsidio) {
        this.tieneSubsidio = tieneSubsidio;
    }

    public BigDecimal getDetValorIce() {
        return detValorIce;
    }

    public void setDetValorIce(BigDecimal detValorIce) {
        this.detValorIce = detValorIce;
    }

    public BigDecimal getDetIva() {
        return detIva;
    }

    public void setDetIva(BigDecimal detIva) {
        this.detIva = detIva;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public BigDecimal getPrecioProductoSinSubsidio() {
        return precioProductoSinSubsidio;
    }

    public void setPrecioProductoSinSubsidio(BigDecimal precioProductoSinSubsidio) {
        this.precioProductoSinSubsidio = precioProductoSinSubsidio;
    }

    public BigDecimal getDetSubtotaldescuento() {
        return detSubtotaldescuento;
    }

    public void setDetSubtotaldescuento(BigDecimal detSubtotaldescuento) {
        this.detSubtotaldescuento = detSubtotaldescuento;
    }

    public BigDecimal getDetCantidad() {
        return detCantidad;
    }

    public void setDetCantidad(BigDecimal detCantidad) {
        this.detCantidad = detCantidad;
    }

    public BigDecimal getDetSubtotal() {
        return detSubtotal;
    }

    public void setDetSubtotal(BigDecimal detSubtotal) {
        this.detSubtotal = detSubtotal;
    }

    public BigDecimal getDetCantpordescuento() {
        return detCantpordescuento;
    }

    public void setDetCantpordescuento(BigDecimal detCantpordescuento) {
        this.detCantpordescuento = detCantpordescuento;
    }

    public String getDetCodIva() {
        return detCodIva;
    }

    public void setDetCodIva(String detCodIva) {
        this.detCodIva = detCodIva;
    }

    public String getDetCodPorcentaje() {
        return detCodPorcentaje;
    }

    public void setDetCodPorcentaje(String detCodPorcentaje) {
        this.detCodPorcentaje = detCodPorcentaje;
    }

    public BigDecimal getDetTarifa() {
        return detTarifa;
    }

    public void setDetTarifa(BigDecimal detTarifa) {
        this.detTarifa = detTarifa;
    }

}
