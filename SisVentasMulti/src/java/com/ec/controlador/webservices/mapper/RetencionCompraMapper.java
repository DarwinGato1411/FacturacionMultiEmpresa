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
        
        respuesta.setRazonSocialEmpresa(valor.getRazonSocialEmpresa());
        respuesta.setNombreComercialEmpresa(valor.getNombreComercialEmpresa());
        respuesta.setRucEmpresa(valor.getRucEmpresa());
        respuesta.setEstablecimientoEmpresa(valor.getEstablecimientoEmpresa());
        respuesta.setPuntoEmisionEmpresa(valor.getPuntoEmisionEmpresa());
        respuesta.setRcoSecuencial(valor.getSecuencial());
        respuesta.setRcoSecuencialText(valor.getSecuencialText());
        respuesta.setDirMatriz(valor.getDirMatrizEmpresa());
        respuesta.setCabFechaEmision(valor.getRetencionFecha());
        respuesta.setDirEstablecimiento(valor.getDirEstablecimiento());
        respuesta.setObligadoContabilidad(valor.getObligadoContabilidad());
        respuesta.setTipoIdentificacionSujetoRetenido(valor.getTipoIdentificacionSujetoRetenido());
        respuesta.setRazonSocialSujetoRetenido(valor.getRazonSocialSujetoRetenido());
        respuesta.setIdentificacionSujetoRetenido(valor.getIdentificacionSujetoRetenido());
        respuesta.setPeriodoFiscal(valor.getPeriodoFiscal());
        respuesta.setAmAgeRet(valor.getAmAgeRet());
        respuesta.setAmGeneral(valor.getAmGeneral());
        respuesta.setAmRimpe(valor.getAmRimpe());
        
        respuesta.setRcoDetalle("COMPRA DE MERCADERIA");
        respuesta.setRcoIva(Boolean.FALSE);
        respuesta.setRcoPorcentajeIva(12);
        respuesta.setRcoPuntoEmision(valor.getPuntoEmisionEmpresa());
        
        respuesta.setRcoSerie("1");
        respuesta.setRcoValorRetencionIva(BigDecimal.ZERO);
        
        respuesta.setDrcEstadosri("PENDIENTE");
        respuesta.setRcoFecha(valor.getRetencionFecha());
        
        respuesta.setProvNombre(valor.getRazonSocialSujetoRetenido());
        respuesta.setProvCedula(valor.getIdentificacionSujetoRetenido());
        
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