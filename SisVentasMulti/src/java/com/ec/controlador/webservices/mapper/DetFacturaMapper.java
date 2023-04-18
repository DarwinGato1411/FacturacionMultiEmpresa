/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.webservices.mapper;

import com.ec.dao.DetFacturaDao;
import com.ec.entidad.DetalleFactura;

/**
 *
 * @author Darwin
 */
public class DetFacturaMapper {

    public static DetalleFactura daoToFactura(DetFacturaDao valor) {
        DetalleFactura respuesta = new DetalleFactura();
        respuesta.setTieneSubsidio(valor.getTieneSubsidio());
        respuesta.setCodigoProducto(valor.getCodigoProducto());
        respuesta.setDetDescripcion(valor.getDescripcionProducto());
        respuesta.setPrecioProductoSinSubsidio(valor.getPrecioProductoSinSubsidio());
        respuesta.setDetSubtotaldescuento(valor.getDetSubtotaldescuento());
        respuesta.setDetCantidad(valor.getDetCantidad());
        respuesta.setDetSubtotal(valor.getDetSubtotal());
        respuesta.setDetCantpordescuento(valor.getDetCantpordescuento());
        respuesta.setDetCodIva(valor.getDetCodIva());
        respuesta.setDetCodPorcentaje(valor.getDetCodPorcentaje());
        respuesta.setDetTarifa(valor.getDetTarifa());
        respuesta.setDetValorIce(valor.getDetValorIce());
        respuesta.setDetIva(valor.getDetIva());
        return respuesta;

    }

    public static DetFacturaDao facturaToDao(DetalleFactura valor) {
        DetFacturaDao respuesta = new DetFacturaDao();
        respuesta.setTieneSubsidio(valor.getTieneSubsidio());
        respuesta.setCodigoProducto(valor.getCodigoProducto());
        respuesta.setDescripcionProducto(valor.getDetDescripcion());
        respuesta.setPrecioProductoSinSubsidio(valor.getPrecioProductoSinSubsidio());
        respuesta.setDetSubtotaldescuento(valor.getDetSubtotaldescuento());
        respuesta.setDetCantidad(valor.getDetCantidad());
        respuesta.setDetSubtotal(valor.getDetSubtotal());
        respuesta.setDetCantpordescuento(valor.getDetCantpordescuento());
        respuesta.setDetCodIva(valor.getDetCodIva());
        respuesta.setDetCodPorcentaje(valor.getDetCodPorcentaje());
        respuesta.setDetTarifa(valor.getDetTarifa());
        respuesta.setDetValorIce(valor.getDetValorIce());
        respuesta.setDetIva(valor.getDetIva());
        return respuesta;
    }
}
