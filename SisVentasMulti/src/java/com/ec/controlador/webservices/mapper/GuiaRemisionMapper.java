/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.webservices.mapper;

import com.ec.dao.GuiaremisionDao;
import com.ec.entidad.Guiaremision;
import java.math.BigDecimal;

/**
 *
 * @author Darwin
 */
public class GuiaRemisionMapper {
    
    public static Guiaremision daoToGuiaremision(GuiaremisionDao valor) {
        Guiaremision respuesta = new Guiaremision();
        respuesta.setFacFecha(valor.getFacFecha());
        respuesta.setMotivoGuia(valor.getMotivoTraslado());
        respuesta.setRazonSocialEmpresa(valor.getRazonSocialEmpresa());
        respuesta.setNombreComercialEmpresa(valor.getNombreComercialEmpresa());
        respuesta.setRucEmpresa(valor.getRucEmpresa());
        respuesta.setEstablecimientoEmpresa(valor.getEstablecimientoEmpresa());
        respuesta.setPuntoEmisionEmpresa(valor.getEstablecimientoEmpresa());
        respuesta.setFacNumero(valor.getSecuencial());
        respuesta.setFacNumeroText(valor.getSecuencialText());
        respuesta.setDirMatrizEmpresa(valor.getDirMatrizEmpresa());
        
        respuesta.setDirEstablecimiento(valor.getDirEstablecimiento());
        respuesta.setPartida(valor.getDirPartida());
        respuesta.setLlegada(valor.getDirLlegada());
        respuesta.setRazonSocialTransportista(valor.getRazonSocialTransportista());
        respuesta.setTipoIdentificacionTransportista(valor.getTipoIdentificacionTransportista());
        respuesta.setRucTransportista(valor.getRucTransportista());
        respuesta.setObligadoContabilidad(valor.getObligadoContabilidad());
        respuesta.setFechainitranspguia(valor.getFechaIniTranspguia());
        respuesta.setFechafintranspguia(valor.getFechaFinTranspguia());
        respuesta.setNumplacaguia(valor.getPlacaGuia());
        
        respuesta.setDirDestinatario(valor.getDirEstablecimiento());
        respuesta.setRazonSocialDestinatario(valor.getRazonSocialDestinatario());
        respuesta.setIdentificacionDestinatario(valor.getIdentificacionDestinatario());
        respuesta.setMotivoTraslado(valor.getMotivoTraslado());
        respuesta.setDocAduaneroUnico(valor.getDocAduaneroUnico());
        respuesta.setRuta(valor.getRuta());
        
        return respuesta;
    }
    
}
