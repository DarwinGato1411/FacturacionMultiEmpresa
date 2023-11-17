/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.webservices.mapper;

import com.ec.dao.DetalleGuiaremisionDao;
import com.ec.dao.GuiaremisionDao;
import com.ec.entidad.DetalleGuiaremision;
import com.ec.entidad.Guiaremision;

/**
 *
 * @author Darwin
 */
public class DetalleGuiaRemisionMapper {
    
    public static DetalleGuiaremision daoToDetalleGuiaremision(DetalleGuiaremisionDao valor) {
        DetalleGuiaremision respuesta = new DetalleGuiaremision();
        respuesta.setDetCantidad(valor.getCantidad());
        respuesta.setDetDescripcion(valor.getDescripcion());
        respuesta.setCodigoInterno(valor.getCodigoInterno());
        
        return respuesta;
    }
    
}
