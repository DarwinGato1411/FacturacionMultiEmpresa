/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.webservices.mapper;

import com.ec.dao.RetencionCompraDao;
import com.ec.entidad.RetencionCompra;
import java.math.BigDecimal;

/**
 *
 * @author Darwin
 */
public class RetencionCompraMapper {

    public static RetencionCompra daoToRetencion(RetencionCompraDao valor) {
        RetencionCompra respuesta = new RetencionCompra();
        respuesta.setRcoDetalle("COMPRA DE MERCADERIA");
        respuesta.setRcoIva(Boolean.FALSE);
        respuesta.setRcoPorcentajeIva(12);
        respuesta.setRcoPuntoEmision(valor.getAmPtoemi());
        respuesta.setRcoSecuencial(valor.getRcoSecuencial());
        respuesta.setRcoSerie("1");
        respuesta.setRcoValorRetencionIva(BigDecimal.ZERO);
        respuesta.setCabFechaEmision(valor.getRcoFecha());
        respuesta.setDrcEstadosri("PENDIENTE");        
        respuesta.setRcoFecha(valor.getRcoFecha());
        respuesta.setRcoSecuencialText(valor.getRcoSecuencialText());
        respuesta.setRcoSecuencial(valor.getRcoSecuencial());
        respuesta.setProvNombre(valor.getProvNombre());
        respuesta.setProvCedula(valor.getProvCedula());
        
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