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
public class DetalleNotaCreditoDebitoDao {

    private String codigoProducto;
    private String descripcionProducto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal precioTotalSinImpuesto;
    private String codigo;
    private Boolean grabaIva;
    private BigDecimal baseImponible;
    private BigDecimal valor;

    private String detCodIva;
    private String detCodPorcentaje;
    private BigDecimal detTarifa;
    private BigDecimal detValorIce;
    private BigDecimal detIva;

    private BigDecimal precioProductoSinSubsidio;
    private BigDecimal detSubtotaldescuento;

    private Boolean tieneSubsidio;
    private BigDecimal detCantpordescuento;
    private BigDecimal detSubtotal;

    private BigDecimal facSubt5;
    private BigDecimal facSubt15;
    private BigDecimal facIva5;
    private BigDecimal facIva15;

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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecioTotalSinImpuesto() {
        return precioTotalSinImpuesto;
    }

    public void setPrecioTotalSinImpuesto(BigDecimal precioTotalSinImpuesto) {
        this.precioTotalSinImpuesto = precioTotalSinImpuesto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getGrabaIva() {
        return grabaIva;
    }

    public void setGrabaIva(Boolean grabaIva) {
        this.grabaIva = grabaIva;
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

    public Boolean getTieneSubsidio() {
        return tieneSubsidio;
    }

    public void setTieneSubsidio(Boolean tieneSubsidio) {
        this.tieneSubsidio = tieneSubsidio;
    }

    public BigDecimal getDetCantpordescuento() {
        return detCantpordescuento;
    }

    public void setDetCantpordescuento(BigDecimal detCantpordescuento) {
        this.detCantpordescuento = detCantpordescuento;
    }

    public BigDecimal getDetSubtotal() {
        return detSubtotal;
    }

    public void setDetSubtotal(BigDecimal detSubtotal) {
        this.detSubtotal = detSubtotal;
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
