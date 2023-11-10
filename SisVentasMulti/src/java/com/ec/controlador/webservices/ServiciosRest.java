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
import com.ec.controlador.ListaFacturas;
import com.ec.controlador.procesar.ProcesarDocumentos;
import com.ec.controlador.webservices.mapper.DetFacturaMapper;
import com.ec.controlador.webservices.mapper.FacturaMapper;
import com.ec.dao.DetFacturaDao;
import com.ec.dao.FacturaDao;
import com.ec.dao.InfoAutoriza;
import com.ec.dao.response.FacturaResponse;
import com.ec.entidad.DetalleFactura;
import com.ec.entidad.Factura;
import com.ec.entidad.Tipoambiente;
import com.ec.servicio.ServicioDetalleFactura;
import com.ec.servicio.ServicioFactura;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.AutorizarDocumentosApi;
import com.ec.untilitario.MailerClass;
import com.ec.untilitario.XAdESBESSignatureApi;
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    ServicioDetalleFactura servicioDetalleFactura = new ServicioDetalleFactura();
    
    @GET
    @Path("/modelo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public FacturaDao getFacturas(@PathParam("codigo") Integer codigo) {
        
        Factura recup = servicioFactura.findFirIdFact(codigo);
        FacturaDao facturaDao = new FacturaDao();
        facturaDao.setFacNumero(recup.getFacNumero());
        List<DetalleFactura> detalle = servicioDetalleFactura.findDetalleForIdFactuta(recup);
        List<DetFacturaDao> detalleDao = new ArrayList<>();
        detalle.forEach((detalleFactura) -> {
            detalleDao.add(DetFacturaMapper.facturaToDao(detalleFactura));
        });
        facturaDao.setDetFacturaDao(detalleDao);
        InfoAutoriza autoriza = new InfoAutoriza();
        autoriza.setRutaArchivo("D:\\\\DOCUMENTOSRI\\XML\\");
        autoriza.setRutaFirma("D:\\DOCUMENTOSRI\\FIRMA\\DarwinMorocho2022-2023.p12");
        autoriza.setPasswordFirma("Dereckandre02");
        facturaDao.setInfoAutoriza(autoriza);
        facturaDao.setAmCodigo("1");
        facturaDao.setLlevarContabilidad("NO");
        facturaDao.setTipoIdentificacionComprador("05");
        facturaDao.setIdentificacionComprador("1718264839");
        facturaDao.setRazonSocialComprador("Darwin Morocho");
        facturaDao.setDireccionComprador("Gonzalez Suarez");
        facturaDao.setGrabaICE(Boolean.FALSE);
        facturaDao.setAgenteRetencion(Boolean.FALSE);
        facturaDao.setRimpePolpular(Boolean.FALSE);
        facturaDao.setRimpeEmprendedor(Boolean.FALSE);
        facturaDao.setRegimenGeneral(Boolean.FALSE);
        facturaDao.setDireccionMatriz("Tabacundo km 27 1/2 via Cayambe");
        facturaDao.setRazonSocialEmpresa("Darwin Morocho");
        facturaDao.setNombreComercialEmpresa("DECKXEL");
        facturaDao.setDireccionMatriz("Tabacundo km 27 1/2 via Cayambe");
        facturaDao.setRucEmpresa("1718264839001");
        facturaDao.setFacTarifaIce(BigDecimal.ZERO);
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
        
        FacturaResponse facturaResponse = new FacturaResponse();
        Factura recup = servicioFactura.findByNumero(prod.getFacNumero());
//        System.out.println("valor "+recup.getEstadosri());
        FacturaDao dao = FacturaMapper.facturaToDao(recup);
        List<DetalleFactura> detalle = servicioDetalleFactura.findDetalleForIdFactuta(recup);
        List<DetFacturaDao> detalleDao = new ArrayList<>();
        detalle.forEach((detalleFactura) -> {
            DetFacturaDao dao1 = DetFacturaMapper.facturaToDao(detalleFactura);
            dao1.setCodigoProducto("05555");
            dao1.setTieneSubsidio(Boolean.FALSE);
            detalleDao.add(dao1);
        });
        dao.setDetFacturaDao(detalleDao);
        InfoAutoriza autoriza = new InfoAutoriza();
        autoriza.setRutaArchivo("D:\\\\DOCUMENTOSRI\\XML\\");
        autoriza.setRutaFirma("D:\\DOCUMENTOSRI\\FIRMA\\DarwinMorocho2022-2023.p12");
        autoriza.setPasswordFirma("Dereckandre02");
        dao.setInfoAutoriza(autoriza);
        dao.setAmCodigo("1");
        dao.setLlevarContabilidad("NO");
        dao.setTipoIdentificacionComprador("05");
        dao.setIdentificacionComprador("1718264839");
        dao.setRazonSocialComprador("Darwin Morocho");
        dao.setDireccionComprador("Gonzalez Suarez");
        dao.setGrabaICE(Boolean.FALSE);
        dao.setAgenteRetencion(Boolean.FALSE);
        dao.setRimpePolpular(Boolean.FALSE);
        dao.setRimpeEmprendedor(Boolean.FALSE);
        dao.setRegimenGeneral(Boolean.FALSE);
        dao.setDireccionMatriz("Tabacundo km 27 1/2 via Cayambe");
        dao.setRazonSocialEmpresa("Darwin Morocho");
        dao.setNombreComercialEmpresa("DECKXEL");
        dao.setDireccionMatriz("Tabacundo km 27 1/2 via Cayambe");
        dao.setRucEmpresa("1718264839001");
        dao.setFacTarifaIce(BigDecimal.ZERO);
        return dao;
    }
    
    @POST
    @Path("/factura-enviar/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaResponse getEnviarFacturas(@RequestBody FacturaDao prod) throws Exception {
        
        FacturaResponse facturaResponse = new FacturaResponse();
        facturaResponse.setFacFecha(prod.getFacFecha());
        facturaResponse.setFacNumeroText(prod.getFacNumeroText());
        facturaResponse.setIdentificacionComprador(prod.getIdentificacionComprador());
        facturaResponse.setRazonSocialComprador(prod.getRazonSocialComprador());
        
        //Rellenar de 0 el numero de factura
        prod.setFacNumeroText(rellenarConCeros(prod.getFacNumero(), 9));
        
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        SimpleDateFormat smAut = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        /*RUTA DE LOS ARCHIVOS*/
        String folderArchivos = prod.getInfoAutoriza().getRutaArchivo();
        String folderFirmado = folderArchivos + File.separator + "FIRMADO" + File.separator;
        String nombreArchivo = prod.getIdentificacionComprador() + "-" + prod.getFacNumero() + ".xml";
        String pathArchivoFirmado = folderFirmado + nombreArchivo;
        String pathArchivoNoAutorizado = folderArchivos + File.separator + "NOAUTORIZADO" + File.separator;
        String archivoEnvioCliente = prod.getInfoAutoriza().getRutaArchivo() + File.separator + "ENVIARCLIENTE" + File.separator;
        final String secretKey = "AFSOTEC2023";
        AutorizarDocumentosApi api = new AutorizarDocumentosApi();
        String archivo = api.generaXMLFactura(prod, prod.getInfoAutoriza().getRutaArchivo(), nombreArchivo, Boolean.FALSE, new Date());
        XAdESBESSignatureApi.firmar(archivo, prod.getIdentificacionComprador() + "-" + prod.getFacNumero() + ".xml",
                ArchivoUtils.decrypt(prod.getInfoAutoriza().getPasswordFirma(), secretKey), prod.getInfoAutoriza().getRutaFirma(), folderFirmado);
        File f = null;
        File fEnvio = null;
        byte[] datos = null;
        f = new File(folderFirmado + File.separator + nombreArchivo);
//
        datos = ArchivoUtils.ConvertirBytes(pathArchivoFirmado);
        
        String claveAccesoComprobante = ArchivoUtils.obtenerValorXML(f, "/*/infoTributaria/claveAcceso");
        facturaResponse.setClaveAutorizacion(claveAccesoComprobante);
        /*PRUEBAS  
         PRODUCCION */
        RespuestaSolicitud resSolicitud = api.validar(datos, prod.getInfoAutoriza().getAmbiente());
        
        if (resSolicitud != null && resSolicitud.getComprobantes() != null) {
            // Autorizacion autorizacion = null;
            facturaResponse.setEstadoSri(resSolicitud.getEstado());
            if (resSolicitud.getEstado().equals("RECIBIDA")) {
                try {
                    System.out.println("RECIBIDA");
                    RespuestaComprobante resComprobante = api.autorizarComprobante(claveAccesoComprobante, prod.getInfoAutoriza().getAmbiente());
                    for (Autorizacion autorizacion : resComprobante.getAutorizaciones().getAutorizacion()) {
                        FileOutputStream nuevo = null;

                        /*CREA EL ARCHIVO XML AUTORIZADO*/
//                        System.out.println("pathArchivoNoAutorizado " + pathArchivoNoAutorizado);
                        nuevo = new FileOutputStream(pathArchivoNoAutorizado);
                        if (autorizacion.getComprobante() != null) {
                            nuevo.write(autorizacion.getComprobante().getBytes());
                        }
                        
                        if (!autorizacion.getEstado().equals("AUTORIZADO")) {
                            if (autorizacion.getEstado().equals("EN PROCESO")) {
//                                Clients.showNotification("Autoriza con reenvio ", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
//                                reenviarFactura(valor);
                            } else {
                                String texto = "Sin Identificar el error";
                                String smsInfo = "Sin identificar el error";
                                
                                if (!autorizacion.getMensajes().getMensaje().isEmpty()) {
                                    texto = autorizacion.getMensajes().getMensaje().size() > 0 ? autorizacion.getMensajes().getMensaje().get(0).getMensaje() : "ERROR SIN DEFINIR " + autorizacion.getEstado();
                                    smsInfo = autorizacion.getMensajes().getMensaje().size() > 0 ? autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional() : " ERROR SIN DEFINIR " + autorizacion.getEstado();
                                    nuevo.write(smsInfo.getBytes());
                                    nuevo.write(smsInfo.getBytes());
                                }
                                
                                facturaResponse.setEstadoSri(autorizacion.getEstado());
                                facturaResponse.setMensajeError(texto);
                                facturaResponse.setDetalleError(smsInfo);
                                
                            }
                        } else {
                            facturaResponse.setEstadoSri(autorizacion.getEstado());
                            
                            try {
                                String fechaForm = sm.format(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                                facturaResponse.setFechaAtorizacion(sm.parse(fechaForm));
                                
                            } catch (java.text.ParseException ex) {
                                Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                     
//                            archivoEnvioCliente = api.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                            archivoEnvioCliente = api.generaXMLFactura(prod, archivoEnvioCliente, nombreArchivo, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
//                            XAdESBESSignature.firmar(archivoEnvioCliente,
//                                    nombreArchivoXML,
//                                    amb.getAmClaveAccesoSri(),
//                                    amb, foldervoAutorizado);
//                            valor.setFacpath(archivoEnvioCliente.replace(".xml", ".pdf"));
//                            servicioFactura.modificar(valor);

                            fEnvio = new File(archivoEnvioCliente);
                            
                            System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
//                            ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), valor.getFacNumero(), "FACT", amb);
//                            ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
                            /*GUARDA EL PATH PDF CREADO*/
                            Factura factura = FacturaMapper.daoToFactura(prod);
                            servicioFactura.crear(factura);
                            DetalleFactura detalleFactura = new DetalleFactura();
                            for (DetFacturaDao detFacturaDao : prod.getDetFacturaDao()) {
                                detalleFactura = new DetalleFactura();
                                detalleFactura = DetFacturaMapper.daoToFactura(detFacturaDao);
                                detalleFactura.setIdFactura(factura);
                                servicioDetalleFactura.crear(detalleFactura);
                            }
                            /*envia el mail*/
                            String[] attachFiles = new String[2];
                            attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
                            attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
                            MailerClass mail = new MailerClass();
//                            Tipoambiente amb= servicioTipoAmbiente.finActivo();
//                            if (prod.getCorreoComprador() != null) {
//                                mail.sendMailSimple(prod.getCorreoComprador(),
//                                            attachFiles,
//                                            "FACTURA ELECTRONICA",
//                                           claveAccesoComprobante,
//                                            prod.getFacNumeroText(),
//                                            prod.getFacTotal(),
//                                            prod.getRazonSocialComprador(), amb);
//                            }

                            /*INCLUIMOS EL XML PARA LA RESPUESTA*/
                            facturaResponse.setXmlAutorizado(autorizacion.getComprobante());
                        }
                        
                    }
                } catch (RespuestaAutorizacionException ex) {
                    Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                String smsInfo = resSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getTipo();
                String detalle = resSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje();
                System.out.println("NO RECIBIDA " + resSolicitud.getEstado());
                System.out.println("Mensaje " + smsInfo);
                
                facturaResponse.setMensajeError(smsInfo);
                facturaResponse.setDetalleError(detalle);
                
            }
        }
        return facturaResponse;
    }
    
<<<<<<< HEAD
=======
    
    
     @POST
    @Path("/factura-reenviar/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaResponse getReenvioFacturas(@RequestBody FacturaDao prod) throws Exception {

        FacturaResponse facturaResponse = new FacturaResponse();
        facturaResponse.setFacFecha(prod.getFacFecha());
        facturaResponse.setFacNumeroText(prod.getFacNumeroText());
        facturaResponse.setIdentificacionComprador(prod.getIdentificacionComprador());
        facturaResponse.setRazonSocialComprador(prod.getRazonSocialComprador());

        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        SimpleDateFormat smAut = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        /*RUTA DE LOS ARCHIVOS*/
        String folderArchivos = prod.getInfoAutoriza().getRutaArchivo();
        String folderFirmado = folderArchivos + File.separator + "FIRMADO" + File.separator;
        String nombreArchivo = prod.getIdentificacionComprador() + "-" + prod.getFacNumero() + ".xml";
        String pathArchivoFirmado = folderFirmado + nombreArchivo;
        String pathArchivoNoAutorizado = folderArchivos + File.separator + "NOAUTORIZADO" + File.separator;
        String archivoEnvioCliente = prod.getInfoAutoriza().getRutaArchivo() + File.separator + "ENVIARCLIENTE" + File.separator;
        final String secretKey = "AFSOTEC2023";
        AutorizarDocumentosApi api = new AutorizarDocumentosApi();
        String archivo = api.generaXMLFactura(prod, prod.getInfoAutoriza().getRutaArchivo(), nombreArchivo, Boolean.FALSE, new Date());
        XAdESBESSignatureApi.firmar(archivo, prod.getIdentificacionComprador() + "-" + prod.getFacNumero() + ".xml",
                    ArchivoUtils.decrypt(prod.getInfoAutoriza().getPasswordFirma(), secretKey), prod.getInfoAutoriza().getRutaFirma(), folderFirmado);
        File f = null;
        File fEnvio = null;
        byte[] datos = null;
        f = new File(folderFirmado + File.separator + nombreArchivo);
//
        datos = ArchivoUtils.ConvertirBytes(pathArchivoFirmado);

        String claveAccesoComprobante = ArchivoUtils.obtenerValorXML(f, "/*/infoTributaria/claveAcceso");
        facturaResponse.setClaveAutorizacion(claveAccesoComprobante);
   
                try {
                    RespuestaComprobante resComprobante = api.autorizarComprobante(claveAccesoComprobante, prod.getInfoAutoriza().getAmbiente());
                    for (Autorizacion autorizacion : resComprobante.getAutorizaciones().getAutorizacion()) {
                        FileOutputStream nuevo = null;

                        /*CREA EL ARCHIVO XML AUTORIZADO*/
//                        System.out.println("pathArchivoNoAutorizado " + pathArchivoNoAutorizado);
                        nuevo = new FileOutputStream(pathArchivoNoAutorizado);
                        if (autorizacion.getComprobante() != null) {
                            nuevo.write(autorizacion.getComprobante().getBytes());
                        }

                        if (!autorizacion.getEstado().equals("AUTORIZADO")) {
                            if (autorizacion.getEstado().equals("EN PROCESO")) {
//                                Clients.showNotification("Autoriza con reenvio ", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
//                                reenviarFactura(valor);
                            } else {
                                String texto = "Sin Identificar el error";
                                String smsInfo = "Sin identificar el error";

                                if (!autorizacion.getMensajes().getMensaje().isEmpty()) {
                                    texto = autorizacion.getMensajes().getMensaje().size() > 0 ? autorizacion.getMensajes().getMensaje().get(0).getMensaje() : "ERROR SIN DEFINIR " + autorizacion.getEstado();
                                    smsInfo = autorizacion.getMensajes().getMensaje().size() > 0 ? autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional() : " ERROR SIN DEFINIR " + autorizacion.getEstado();
                                    nuevo.write(smsInfo.getBytes());
                                    nuevo.write(smsInfo.getBytes());
                                }

                                facturaResponse.setEstadoSri(autorizacion.getEstado());
                                facturaResponse.setMensajeError(texto);
                                facturaResponse.setDetalleError(smsInfo);

                            }
                        } else {
                            facturaResponse.setEstadoSri(autorizacion.getEstado());

                            try {
                                String fechaForm = sm.format(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                                facturaResponse.setFechaAtorizacion(sm.parse(fechaForm));

                            } catch (java.text.ParseException ex) {
                                Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                     
//                            archivoEnvioCliente = api.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                            archivoEnvioCliente = api.generaXMLFactura(prod, archivoEnvioCliente, nombreArchivo, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());

                            fEnvio = new File(archivoEnvioCliente);

                            System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
//                            ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), valor.getFacNumero(), "FACT", amb);
//                            ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
                            /*GUARDA EL PATH PDF CREADO*/
                            Factura factura = FacturaMapper.daoToFactura(prod);
                            servicioFactura.crear(factura);
                            DetalleFactura detalleFactura = new DetalleFactura();
                            for (DetFacturaDao detFacturaDao : prod.getDetFacturaDao()) {
                                detalleFactura = new DetalleFactura();
                                detalleFactura = DetFacturaMapper.daoToFactura(detFacturaDao);
                                detalleFactura.setIdFactura(factura);
                                servicioDetalleFactura.crear(detalleFactura);
                            }
                              /*envia el mail*/
                            String[] attachFiles = new String[2];
                            attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
                            attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
                            MailerClass mail = new MailerClass();
//                            Tipoambiente amb= servicioTipoAmbiente.finActivo();
//                            if (prod.getCorreoComprador() != null) {
//                                mail.sendMailSimple(prod.getCorreoComprador(),
//                                            attachFiles,
//                                            "FACTURA ELECTRONICA",
//                                           claveAccesoComprobante,
//                                            prod.getFacNumeroText(),
//                                            prod.getFacTotal(),
//                                            prod.getRazonSocialComprador(), amb);
//                            }

                            facturaResponse.setXmlAutorizado(autorizacion.getComprobante());
                        }

                    }
                } catch (RespuestaAutorizacionException ex) {
                    Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
//            
        return facturaResponse;
    }
    

>>>>>>> af37fc2459bf8dd85a3040edde105fc35985ca61
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

    /*encriptar desencriptar*/
    @POST
    @Path("/encrypted/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public String encrypted(@RequestBody InfoAutoriza info) throws Exception {
        final String secretKey = "AFSOTEC2023";
        
        String originalString = info.getPasswordFirma();
        String encryptedString = ArchivoUtils.encrypt(originalString, secretKey);
        String decryptedString = ArchivoUtils.decrypt(encryptedString, secretKey);
        
        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
        return encryptedString;
    }
    
    @POST
    @Path("/decrypted/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public String decrypted(@RequestBody InfoAutoriza info) throws Exception {
        final String secretKey = "AFSOTEC2023";
        
        String originalString = info.getPasswordFirma();
//        String encryptedString = ArchivoUtils.encrypt(originalString, secretKey);
        String decryptedString = ArchivoUtils.decrypt(originalString, secretKey);
        
        System.out.println(originalString);
//        System.out.println(encryptedString);
        System.out.println(decryptedString);
        return decryptedString;
    }
    
    @POST
    @Path("/notaventa-enviar/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaResponse getNotaVenta(@RequestBody FacturaDao prod) throws Exception {
        
        FacturaResponse facturaResponse = new FacturaResponse();
        prod.setFacNumeroText(rellenarConCeros(prod.getFacNumero(), 9));
        Factura factura = FacturaMapper.daoToFactura(prod);
        servicioFactura.crear(factura);
        
        DetalleFactura detalleFactura = new DetalleFactura();
        
        for (DetFacturaDao detFacturaDao : prod.getDetFacturaDao()) {
            detalleFactura = new DetalleFactura();
            detalleFactura = DetFacturaMapper.daoToFactura(detFacturaDao);
            detalleFactura.setIdFactura(factura);
            servicioDetalleFactura.crear(detalleFactura);
        }
        return facturaResponse;
    }
    
    public static String rellenarConCeros(int numero, int longitudDeseada) {
        String numeroStr = String.valueOf(numero);
        
        if (numeroStr.length() >= longitudDeseada) {
            return numeroStr;
        }
        
        int cerosPorRellenar = longitudDeseada - numeroStr.length();
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < cerosPorRellenar; i++) {
            builder.append("0");
        }
        
        builder.append(numeroStr);
        return builder.toString();
    }
}
