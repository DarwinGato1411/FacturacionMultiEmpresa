/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.webservices;

/**
 *
 * @author Darwin
 */
import com.ec.controlador.procesar.ProcesarDocumentos;
import com.ec.controlador.webservices.mapper.FacturaMapper;
import com.ec.dao.FacturaDao;
import com.ec.entidad.Factura;
import com.ec.servicio.ServicioFactura;
import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.RequestBody;

@Path("/autorizar")
public class ServiciosRest {

    ServicioFactura servicioFactura = new ServicioFactura();

    @GET
    @Path("/modelo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public FacturaDao getFacturas(@PathParam("codigo") Integer codigo) {

        Factura recup = servicioFactura.findFirIdFact(codigo);
        FacturaDao facturaDao = new FacturaDao();
        facturaDao.setFacNumero(recup.getFacNumero());
//        System.out.println("valor "+recup.getEstadosri());
        return facturaDao;

    }

    @GET
    @Path("/factura-enviar/{codigo}/{numero}")
    @Produces(MediaType.APPLICATION_JSON)
    public RespuestaDocumentos getFacturas(@PathParam("codigo") Integer codigo, @PathParam("numero") Integer numero) {
        System.out.println("codigo " + codigo + " numero " + numero);
        RespuestaDocumentos respuesta = new RespuestaDocumentos("PROCESO CORRECTO", "VALIDO");
        ProcesarDocumentos documentos = new ProcesarDocumentos(codigo, numero);
        try {

            System.out.println("INGRESA LA SERVICIO DE FACTURAS");
            respuesta.setDescripcion(documentos.autorizarEnLote());
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException | NamingException | JRException e) {
            respuesta.setDescripcion(e.getMessage());
            respuesta.setEstado("ERROR");
        }

        return respuesta;
    }

    @POST
    @Path("/factura/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaDao getReenviarFacturas(@RequestBody FacturaDao prod) throws Exception {

        Factura recup = servicioFactura.findByNumero(prod.getFacNumero());
//        System.out.println("valor "+recup.getEstadosri());
        FacturaDao dao = FacturaMapper.facturaToDao(recup);
        return dao;
    }

    @GET
    @Path("/factura-reenviar/{codigo}/{numero}")
    public RespuestaDocumentos getReenviarFacturas(@PathParam("codigo") Integer codigo, @PathParam("numero") Integer numero) {

        RespuestaDocumentos respuesta = new RespuestaDocumentos("PROCESO CORRECTO", "VALIDO");
        ProcesarDocumentos documentos = new ProcesarDocumentos(codigo, numero);
        try {

            System.out.println("INGRESA LA SERVICIO DE FACTURAS");
            respuesta.setDescripcion(documentos.reenviarEnLote());
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException | NamingException | JRException e) {
            respuesta.setDescripcion(e.getMessage());
            respuesta.setEstado("ERROR");
        }

        return respuesta;
    }

}
