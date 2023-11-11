/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.webservices.mapper;

import com.ec.dao.FacturaDao;
import com.ec.dao.RetencionCompraDao;
import com.ec.entidad.Factura;
import com.ec.entidad.RetencionCompra;

/**
 *
 * @author Darwin
 */
public class RetencionCompraMapper {

    public static RetencionCompra daoToFactura(RetencionCompraDao valor) {
        RetencionCompra respuesta = new RetencionCompra();
        respuesta.setRcoFecha(valor.getRcoFecha());
        respuesta.setRcoSecuencialText(valor.getRcoSecuencialText());
        respuesta.setRcoSecuencial(valor.getRcoSecuencial());
        
        
        return respuesta;
    }

//    public static FacturaDao facturaToDao(Factura valor) {
//        FacturaDao respuesta = new FacturaDao();
//        respuesta.setFacFecha(valor.getFacFecha());
//        respuesta.setFacNumero(valor.getFacNumero());
//        respuesta.setFacNumeroText(valor.getFacNumeroText());
//        respuesta.setFacTotalBaseGravada(valor.getFacTotalBaseGravaba());
//        respuesta.setCodigoICE(valor.getCodigoICE());
//        respuesta.setFacTarifaIce(valor.getFacTarifaIce());
//        respuesta.setFacValorIce(valor.getFacValorIce());
//        respuesta.setEstablecimientoEmpresa(valor.getCodestablecimiento());
//        respuesta.setPuntoEmisionEmpresa(valor.getPuntoemision());
//        respuesta.setDireccionMatriz(valor.getDireccionMatriz());
//        respuesta.setAgenteRetencion(valor.getAgenteRetencion());
//        respuesta.setRimpePolpular(valor.getRimpePolpular());
//        respuesta.setRimpeEmprendedor(valor.getRimpeEmprendedor());
//        respuesta.setRegimenGeneral(valor.getRegimenGeneral());
//        respuesta.setObligadoLlevarContabilidad(valor.getObligadoLlevarContabilidad());
//        respuesta.setTipoIdentificacionComprador(valor.getTipoIdentificacionComprador());
//        respuesta.setRazonSocialComprador(valor.getRazonSocialComprador());
//        respuesta.setIdentificacionComprador(valor.getIdentificacionComprador());
//        respuesta.setFacSubtotal(valor.getFacSubtotal());
//        respuesta.setFacSubsidio(valor.getFacSubsidio());
//        respuesta.setFacDescuento(valor.getFacDescuento());
//        respuesta.setFacCodIva(valor.getFacCodIva());
//        respuesta.setFacTotalBaseCero(valor.getFacTotalBaseCero());
//        respuesta.setGrabaICE(valor.getGrabaICE());
//        respuesta.setCodigoPorcentaje(valor.getCodigoPorcentaje());
//        respuesta.setFacIva(valor.getFacIva());
//        respuesta.setFacTotal(valor.getFacTotal());
//        respuesta.setFacMoneda(valor.getFacMoneda());
//        respuesta.setCodigoFormaPago(valor.getCodigoFormaPago());
//        respuesta.setFacPlazo(valor.getFacPlazo());
//        respuesta.setFacUnidaTiempo(valor.getFacUnidadTiempo());
//        respuesta.setDireccionComprador(valor.getDireccionComprador());
//        respuesta.setCorreoComprador(valor.getCorreoComprador());
//        respuesta.setObservacion(valor.getFacObservacion());
//        respuesta.setFacPorcentajeIva(valor.getFacPorcentajeIva());
//
//        return respuesta;
//    }
}
