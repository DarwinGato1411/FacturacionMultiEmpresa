/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.webservices.mapper;

import com.ec.dao.FacturaDao;
import com.ec.entidad.Factura;

/**
 *
 * @author Darwin
 */
public class FacturaMapper {

    public static Factura daoToFactura(FacturaDao valor) {
        Factura respuesta = new Factura();

        respuesta.setRazonSocialEmpresa(valor.getRazonSocialEmpresa());
        respuesta.setNombreComercialEmpresa(valor.getNombreComercialEmpresa());
        respuesta.setRucEmpresa(valor.getRucEmpresa());
        respuesta.setCodestablecimiento(valor.getEstablecimientoEmpresa());
        respuesta.setPuntoemision(valor.getPuntoEmisionEmpresa());
        respuesta.setFacNumero(valor.getFacNumero());
        respuesta.setFacNumeroText(valor.getFacNumeroText());
        respuesta.setDireccionMatriz(valor.getDireccionMatriz());
        respuesta.setFacFecha(valor.getFacFecha());
        respuesta.setObligadoLlevarContabilidad(valor.getObligadoLlevarContabilidad());
        respuesta.setTipoIdentificacionComprador(valor.getTipoIdentificacionComprador());
        respuesta.setRazonSocialComprador(valor.getRazonSocialComprador());
        respuesta.setIdentificacionComprador(valor.getIdentificacionComprador());
        respuesta.setFacSubtotal(valor.getFacSubtotal());
        respuesta.setFacSubsidio(valor.getFacSubsidio());
        respuesta.setFacDescuento(valor.getFacDescuento());
        respuesta.setFacCodIva(valor.getFacCodIva());
        respuesta.setFacTotalBaseCero(valor.getFacTotalBaseCero());
        respuesta.setGrabaICE(valor.getGrabaICE());
        respuesta.setFacValorIce(valor.getFacValorIce());
        respuesta.setCodigoPorcentaje(valor.getCodigoPorcentaje());
        respuesta.setFacTotalBaseGravaba(valor.getFacTotalBaseGravada());
        respuesta.setFacPorcentajeIva(valor.getFacPorcentajeIva());
        respuesta.setFacIva(valor.getFacIva());
        respuesta.setFacTotal(valor.getFacTotal());
        respuesta.setCodigoFormaPago(valor.getCodigoFormaPago());
        respuesta.setFacMoneda(valor.getFacMoneda());
        respuesta.setFacUnidadTiempo(valor.getFacUnidaTiempo());

        respuesta.setCodigoICE(valor.getCodigoICE());
        respuesta.setFacTarifaIce(valor.getFacTarifaIce());
        respuesta.setCodestablecimiento(valor.getEstablecimientoEmpresa());
        respuesta.setAgenteRetencion(valor.getAgenteRetencion());
        respuesta.setRimpePolpular(valor.getRimpePolpular());
        respuesta.setRimpeEmprendedor(valor.getRimpeEmprendedor());
        respuesta.setRegimenGeneral(valor.getRegimenGeneral());
        respuesta.setFacPlazo(valor.getFacPlazo());
        respuesta.setDireccionComprador(valor.getDireccionComprador());
        respuesta.setCorreoComprador(valor.getCorreoComprador());
        respuesta.setFacObservacion(valor.getObservacion());
        respuesta.setDireccionMatriz(valor.getDireccionMatriz());
        respuesta.setFacTipo(valor.getFacTipo());

        return respuesta;

    }

    public static FacturaDao facturaToDao(Factura valor) {
        FacturaDao respuesta = new FacturaDao();
        respuesta.setFacFecha(valor.getFacFecha());
        respuesta.setFacNumero(valor.getFacNumero());
        respuesta.setFacNumeroText(valor.getFacNumeroText());
        respuesta.setFacTotalBaseGravada(valor.getFacTotalBaseGravaba());
        respuesta.setCodigoICE(valor.getCodigoICE());
        respuesta.setFacTarifaIce(valor.getFacTarifaIce());
        respuesta.setFacValorIce(valor.getFacValorIce());
        respuesta.setEstablecimientoEmpresa(valor.getCodestablecimiento());
        respuesta.setPuntoEmisionEmpresa(valor.getPuntoemision());
        respuesta.setDireccionMatriz(valor.getDireccionMatriz());
        respuesta.setAgenteRetencion(valor.getAgenteRetencion());
        respuesta.setRimpePolpular(valor.getRimpePolpular());
        respuesta.setRimpeEmprendedor(valor.getRimpeEmprendedor());
        respuesta.setRegimenGeneral(valor.getRegimenGeneral());
        respuesta.setObligadoLlevarContabilidad(valor.getObligadoLlevarContabilidad());
        respuesta.setTipoIdentificacionComprador(valor.getTipoIdentificacionComprador());
        respuesta.setRazonSocialComprador(valor.getRazonSocialComprador());
        respuesta.setIdentificacionComprador(valor.getIdentificacionComprador());
        respuesta.setFacSubtotal(valor.getFacSubtotal());
        respuesta.setFacSubsidio(valor.getFacSubsidio());
        respuesta.setFacDescuento(valor.getFacDescuento());
        respuesta.setFacCodIva(valor.getFacCodIva());
        respuesta.setFacTotalBaseCero(valor.getFacTotalBaseCero());
        respuesta.setGrabaICE(valor.getGrabaICE());
        respuesta.setCodigoPorcentaje(valor.getCodigoPorcentaje());
        respuesta.setFacIva(valor.getFacIva());
        respuesta.setFacTotal(valor.getFacTotal());
        respuesta.setFacMoneda(valor.getFacMoneda());
        respuesta.setCodigoFormaPago(valor.getCodigoFormaPago());
        respuesta.setFacPlazo(valor.getFacPlazo());
        respuesta.setFacUnidaTiempo(valor.getFacUnidadTiempo());
        respuesta.setDireccionComprador(valor.getDireccionComprador());
        respuesta.setCorreoComprador(valor.getCorreoComprador());
        respuesta.setObservacion(valor.getFacObservacion());
        respuesta.setFacPorcentajeIva(valor.getFacPorcentajeIva());

        return respuesta;
    }
}
