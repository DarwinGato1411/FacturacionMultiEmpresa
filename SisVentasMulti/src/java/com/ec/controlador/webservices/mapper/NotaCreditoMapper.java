/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.webservices.mapper;

import com.ec.dao.NotaCreditoDebitoDao;
import com.ec.entidad.NotaCreditoDebito;

/**
 *
 * @author Darwin
 */
public class NotaCreditoMapper {
    
    public static NotaCreditoDebito daoToEntity(NotaCreditoDebitoDao valor) {
        NotaCreditoDebito respuesta = new NotaCreditoDebito();
        respuesta.setFacFecha(valor.getFacFecha());
        respuesta.setRazonSocialComprador(valor.getRazonSocialComprador());
        respuesta.setIdentificacionComprador(valor.getIdentificacionComprador());
        respuesta.setAmCodigo(valor.getAmCodigo());
        respuesta.setAmRimpe(valor.getAmRimpe());
        respuesta.setAmGeneral(valor.getAmGeneral());
        respuesta.setAmRazonSocial(valor.getRazonSocialEmpresa());
        respuesta.setAmNombreComercial(valor.getNombreComercialEmpresa());
        respuesta.setRucEmpresa(valor.getRucEmpresa());
        respuesta.setCodestablecimiento(valor.getEstablecimientoEmpresa());
        respuesta.setPuntoemision(valor.getPuntoEmisionEmpresa());
        respuesta.setFacNumero(valor.getSecuencial());
        respuesta.setFacNumeroText(valor.getSecuencialText());        
        respuesta.setAmDireccionMatriz(valor.getDirMatrizEmpresa());
        respuesta.setDirEstablecimiento(valor.getDirMatrizEmpresa());
        respuesta.setIdentificacionComprador(valor.getIdentificacionComprador());
        respuesta.setRazonSocialComprador(valor.getRazonSocialComprador());
        respuesta.setLlevarContabilidad(valor.getObligadoContabilidad());
        respuesta.setCodDocModificado(valor.getCodDocModificado());
        respuesta.setNumDocModificado(valor.getNumDocModificado());
        respuesta.setFacFechaSustento(valor.getFechaEmisionDocSustento());
        respuesta.setFacSubtotal(valor.getTotalSinImpuestos());
        respuesta.setFacTotal(valor.getValorModificacion());
        respuesta.setFacCodIva(valor.getCodigo());
//        respuesta.setCodigoICE(valor.get());
        respuesta.setCodigoPorcentaje(valor.getCodigoPorcentaje());
        respuesta.setFacTotalBaseGravaba(valor.getBaseImponible());
        respuesta.setFacIva(valor.getValor());
        respuesta.setMotivo(valor.getMotivo());
        respuesta.setCorreo(valor.getCorreo());
        
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
