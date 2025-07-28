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
import com.ec.controlador.webservices.mapper.DetFacturaMapper;
import com.ec.controlador.webservices.mapper.DetNotaCreditoMapper;
import com.ec.controlador.webservices.mapper.DetRetencionMapper;
import com.ec.controlador.webservices.mapper.DetalleGuiaRemisionMapper;
import com.ec.controlador.webservices.mapper.FacturaMapper;
import com.ec.controlador.webservices.mapper.GuiaRemisionMapper;
import com.ec.controlador.webservices.mapper.NotaCreditoMapper;
import com.ec.controlador.webservices.mapper.RetencionCompraMapper;
import com.ec.dao.DetFacturaDao;
import com.ec.dao.DetRetencionCompraDao;
import com.ec.dao.DetalleGuiaremisionDao;
import com.ec.dao.DetalleNotaCreditoDebitoDao;
import com.ec.dao.ExcelRequest;
import com.ec.dao.FacturaDao;
import com.ec.dao.GuiaremisionDao;
import com.ec.dao.InfoAutorizaDao;
import com.ec.dao.NotaCreditoDebitoDao;
import com.ec.dao.PdfRequest;
import com.ec.dao.RequestAnulaDoc;
import com.ec.dao.RetencionCompraDao;
import com.ec.dao.response.DatosFirmaResponse;
import com.ec.dao.response.FacturaResponse;
import com.ec.dao.response.PdfResponse;
import com.ec.entidad.DetalleFactura;
import com.ec.entidad.DetalleGuiaremision;
import com.ec.entidad.DetalleNotaDebitoCredito;
import com.ec.entidad.DetalleRetencionCompra;
import com.ec.entidad.Factura;
import com.ec.entidad.Guiaremision;
import com.ec.entidad.NotaCreditoDebito;
import com.ec.entidad.RetencionCompra;
import com.ec.entidad.Tipoambiente;
import com.ec.servicio.ServicioDetalleFactura;
import com.ec.servicio.ServicioDetalleGuia;
import com.ec.servicio.ServicioDetalleNotaCredito;
import com.ec.servicio.ServicioDetalleRetencionCompra;
import com.ec.servicio.ServicioFactura;
import com.ec.servicio.ServicioGuia;
import com.ec.servicio.ServicioNotaCredito;
import com.ec.servicio.ServicioRetencionCompra;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.servicio.ServicioTipoIvaRetencion;
import com.ec.servicio.ServicioTipoRetencion;
import com.ec.untilitario.ArchivoUtils;

import com.ec.untilitario.MailerClass;
import com.ec.untilitario.XAdESBESSignatureApi;
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.ec.untilitario.AutorizarDocumentosApi;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

@Path("/autorizar")
public class ServiciosRest {
    
    ServicioFactura servicioFactura = new ServicioFactura();
    
    ServicioDetalleFactura servicioDetalleFactura = new ServicioDetalleFactura();
    ServicioRetencionCompra servicioRetencion = new ServicioRetencionCompra();
    ServicioDetalleRetencionCompra servicioDetalleRetencionCompra = new ServicioDetalleRetencionCompra();
    ServicioTipoRetencion servicioTipoRetencion = new ServicioTipoRetencion();
    ServicioTipoIvaRetencion servicioTipoIvaRetencion = new ServicioTipoIvaRetencion();
    ServicioGuia servicioGuiaRemision = new ServicioGuia();
    ServicioDetalleGuia servicioDetalleGuiaRemision = new ServicioDetalleGuia();
    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    ServicioNotaCredito servicioNotaCredito = new ServicioNotaCredito();
    ServicioDetalleNotaCredito servicioDetalleNotaCredito = new ServicioDetalleNotaCredito();
    
    @POST
    @Path("/cargar-firma/")
    @Consumes({javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA})
    public String cargarFirma(@RequestPart("logo") MultipartFile firma) {
        System.out.println("logo = contentType: {}, fileName: {}, formName: {}, size: {}" + firma.getContentType()
                + "   " + firma.getOriginalFilename() + "   " + firma.getName() + "   " + firma.getSize());
        return "";
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
        String archivoGenerado = prod.getInfoAutoriza().getRutaArchivo() + File.separator + "GENERADO" + File.separator;
        
        File folderNuevo = new File(folderFirmado);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        
        folderNuevo = new File(folderArchivos);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        
        folderNuevo = new File(pathArchivoNoAutorizado);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        
        folderNuevo = new File(archivoEnvioCliente);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        folderNuevo = new File(archivoGenerado);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        
        final String secretKey = "AFSOTEC2023";
        AutorizarDocumentosApi api = new AutorizarDocumentosApi();
        String archivo = api.generaXMLFactura(prod, archivoGenerado, nombreArchivo, Boolean.FALSE, new Date());
        try {
            XAdESBESSignatureApi.firmar(archivo, prod.getIdentificacionComprador() + "-" + prod.getFacNumero() + ".xml",
                    ArchivoUtils.decrypt(prod.getInfoAutoriza().getPasswordFirma(), secretKey), prod.getInfoAutoriza().getRutaFirma(), folderFirmado);
        } catch (Exception e) {
            e.printStackTrace();
            facturaResponse.setMensajeError("FIRMA");
            facturaResponse.setDetalleError("Firma o clave incorrecta");
            return facturaResponse;
        }
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
//                        FileOutputStream nuevo = null;

                        /*CREA EL ARCHIVO XML AUTORIZADO*/
//                        System.out.println("pathArchivoNoAutorizado " + pathArchivoNoAutorizado);
//                        nuevo = new FileOutputStream(pathArchivoNoAutorizado);
//                        if (autorizacion.getComprobante() != null) {
//                            nuevo.write(autorizacion.getComprobante().getBytes());
//                        }
                        if (!autorizacion.getEstado().equals("AUTORIZADO")) {
                            if (autorizacion.getEstado().equals("EN PROCESO")) {
//                                Clients.showNotification("Autoriza con reenvio ", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
//                                reenviarFactura(valor);
                            } else {
                                String texto = "Sin Identificar el error";
                                String smsInfo = "Sin identificar el error";
                                
                                if (!autorizacion.getMensajes().getMensaje().isEmpty()) {
                                    texto = autorizacion.getMensajes().getMensaje().size() > 0 ? autorizacion.getMensajes().getMensaje().get(0).getMensaje() : "ERROR SIN DEFINIR " + autorizacion.getEstado();
                                    smsInfo = autorizacion.getMensajes().getMensaje().size() > 0 ? (autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional() != null ? autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional() : "") : " ERROR SIN DEFINIR " + autorizacion.getEstado();
//                                    if (smsInfo != null) {
//                                        nuevo.write(smsInfo.getBytes());
//                                    }
//                                    nuevo.write(smsInfo.getBytes());
                                }
                                
                                facturaResponse.setEstadoSri(autorizacion.getEstado());
                                facturaResponse.setMensajeError(texto);
                                if (smsInfo != null) {
                                    facturaResponse.setDetalleError(smsInfo);
                                }
                                
                            }
                        } else {
                            facturaResponse.setEstadoSri(autorizacion.getEstado());
                            facturaResponse.setClaveAutorizacion(claveAccesoComprobante);
                            Date fechaAutoriza = null;
                            try {
                                String fechaForm = sm.format(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                                fechaAutoriza = sm.parse(fechaForm);
                                facturaResponse.setFechaAtorizacion(fechaAutoriza);
                                
                            } catch (java.text.ParseException ex) {
                                Logger.getLogger(ListaFacturas.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
//                     
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

//                            ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
                            /*GUARDA EL PATH PDF CREADO*/
                            Factura factura = FacturaMapper.daoToFactura(prod);
                            factura.setFacClaveAcceso(claveAccesoComprobante);
                            factura.setFacClaveAutorizacion(claveAccesoComprobante);
                            factura.setEstadosri(autorizacion.getEstado());
                            factura.setFacFechaAutorizacion(fechaAutoriza);
                            servicioFactura.crear(factura);
                            DetalleFactura detalleFactura = new DetalleFactura();
                            
                            for (DetFacturaDao detFacturaDao : prod.getDetFacturaDao()) {
                                detalleFactura = new DetalleFactura();
                                detalleFactura = DetFacturaMapper.daoToFactura(detFacturaDao);
                                detalleFactura.setIdFactura(factura);
                                servicioDetalleFactura.crear(detalleFactura);
                            }
                            /*GENERAR EL PDF PARA ENVIAR AL CLIENTE*/
                            
                            System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
                            ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), prod.getFacNumero(), "FACT", prod.getRucEmpresa(), prod.getEstablecimientoEmpresa(), prod.getPuntoEmisionEmpresa(), folderArchivos, prod.getInfoAutoriza().getRutaLogo());

                            /*envia el mail*/
                            String[] attachFiles = new String[2];
                            attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
                            attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
                            MailerClass mail = new MailerClass();
                            Tipoambiente amb = servicioTipoAmbiente.finActivo();
                            if (prod.getCorreoComprador() != null) {
                                mail.sendMailSimple(prod.getCorreoComprador(),
                                        attachFiles,
                                        "FACTURA ELECTRONICA",
                                        claveAccesoComprobante,
                                        prod.getFacNumeroText(),
                                        prod.getFacTotal(),
                                        prod.getRazonSocialComprador(), amb, prod.getNombreComercialEmpresa());
                            }

                            /*INCLUIMOS EL XML PARA LA RESPUESTA*/
                            facturaResponse.setXmlAutorizado(autorizacion.getComprobante());
                            
                        }
                        
                    }
                } catch (RespuestaAutorizacionException ex) {
                    Logger.getLogger(ListaFacturas.class
                            .getName()).log(Level.SEVERE, null, ex);
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
    
    @POST
    @Path("/factura-reenviar/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaResponse getReenvioFacturas(@RequestBody FacturaDao prod) throws Exception {
        
        prod.setFacNumeroText(rellenarConCeros(prod.getFacNumero(), 9));
        
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
        
        File folderNuevo = new File(folderFirmado);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        
        folderNuevo = new File(folderArchivos);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        
        folderNuevo = new File(pathArchivoNoAutorizado);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        
        folderNuevo = new File(archivoEnvioCliente);
        if (!folderNuevo.exists()) {
            folderNuevo.mkdirs();
        }
        
        final String secretKey = "AFSOTEC2023";
        AutorizarDocumentosApi api = new AutorizarDocumentosApi();
        String archivo = api.generaXMLFactura(prod, prod.getInfoAutoriza().getRutaArchivo(), nombreArchivo, Boolean.FALSE, new Date());
        try {
            XAdESBESSignatureApi.firmar(archivo, prod.getIdentificacionComprador() + "-" + prod.getFacNumero() + ".xml",
                    ArchivoUtils.decrypt(prod.getInfoAutoriza().getPasswordFirma(), secretKey), prod.getInfoAutoriza().getRutaFirma(), folderFirmado);
        } catch (Exception e) {
            e.printStackTrace();
            facturaResponse.setMensajeError("FIRMA");
            facturaResponse.setDetalleError("Firma o clave incorrecta");
            return facturaResponse;
        }
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
//                FileOutputStream nuevo = null;

                /*CREA EL ARCHIVO XML AUTORIZADO*/
//                        System.out.println("pathArchivoNoAutorizado " + pathArchivoNoAutorizado);
//                nuevo = new FileOutputStream(pathArchivoNoAutorizado+ nombreArchivo);
//                if (autorizacion.getComprobante() != null) {
//                    nuevo.write(autorizacion.getComprobante().getBytes());
//                }
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
//                            nuevo.write(smsInfo.getBytes());
//                            nuevo.write(smsInfo.getBytes());
                        }
                        
                        facturaResponse.setEstadoSri(autorizacion.getEstado());
                        facturaResponse.setMensajeError(texto);
                        facturaResponse.setDetalleError(smsInfo);
                        
                    }
                } else {
                    facturaResponse.setEstadoSri(autorizacion.getEstado());
                    Date fechaAutoriza = null;
                    try {
                        String fechaForm = sm.format(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                        fechaAutoriza = sm.parse(fechaForm);
                        facturaResponse.setFechaAtorizacion(fechaAutoriza);
                        
                    } catch (java.text.ParseException ex) {
                        Logger.getLogger(ListaFacturas.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
//                     
//                            archivoEnvioCliente = api.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                    archivoEnvioCliente = api.generaXMLFactura(prod, archivoEnvioCliente, nombreArchivo, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                    
                    fEnvio = new File(archivoEnvioCliente);
                    servicioFactura.eliminarFactura(prod.getFacNumero(), prod.getRucEmpresa(), prod.getEstablecimientoEmpresa(), prod.getPuntoEmisionEmpresa());
                    /*GUARDA EL PATH PDF CREADO*/
                    Factura factura = FacturaMapper.daoToFactura(prod);
                    factura.setFacClaveAcceso(claveAccesoComprobante);
                    factura.setFacFechaAutorizacion(fechaAutoriza);
                    factura.setFacClaveAutorizacion(claveAccesoComprobante);
                    
                    factura.setEstadosri(autorizacion.getEstado());
                    servicioFactura.crear(factura);
                    DetalleFactura detalleFactura = new DetalleFactura();
                    
                    for (DetFacturaDao detFacturaDao : prod.getDetFacturaDao()) {
                        detalleFactura = new DetalleFactura();
                        detalleFactura = DetFacturaMapper.daoToFactura(detFacturaDao);
                        detalleFactura.setIdFactura(factura);
                        servicioDetalleFactura.crear(detalleFactura);
                    }
                    /*GENERAR EL PDF PARA ENVIAR AL CLIENTE*/
                    
                    System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
                    ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), prod.getFacNumero(), "FACT", prod.getRucEmpresa(), prod.getEstablecimientoEmpresa(), prod.getPuntoEmisionEmpresa(), folderArchivos, prod.getInfoAutoriza().getRutaLogo());

                    /*envia el mail*/
                    String[] attachFiles = new String[2];
                    attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
                    attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
                    MailerClass mail = new MailerClass();
                    Tipoambiente amb = servicioTipoAmbiente.finActivo();
                    if (prod.getCorreoComprador() != null) {
                        mail.sendMailSimple(prod.getCorreoComprador(),
                                attachFiles,
                                "FACTURA ELECTRONICA",
                                claveAccesoComprobante,
                                prod.getFacNumeroText(),
                                prod.getFacTotal(),
                                prod.getRazonSocialComprador(), amb,
                                prod.getNombreComercialEmpresa());
                    }
                    
                    facturaResponse.setXmlAutorizado(autorizacion.getComprobante());
                    
                }
                
            }
        } catch (RespuestaAutorizacionException ex) {
            Logger.getLogger(ListaFacturas.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
//            
        return facturaResponse;
    }


    /*encriptar desencriptar*/
    @POST
    @Path("/encrypted/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public String encrypted(@RequestBody InfoAutorizaDao info) throws Exception {
        final String secretKey = "AFSOTEC2023";
        
        String originalString = info.getPasswordFirma();
        String encryptedString = ArchivoUtils.encrypt(originalString, secretKey);
        String decryptedString = ArchivoUtils.decrypt(encryptedString, secretKey);

//        System.out.println(originalString);
//        System.out.println(encryptedString);
//        System.out.println(decryptedString);
        return encryptedString;
    }
    
    @POST
    @Path("/decrypted/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public String decrypted(@RequestBody InfoAutorizaDao info) throws Exception {
        final String secretKey = "AFSOTEC2023";
        
        String originalString = info.getPasswordFirma();
//        String encryptedString = ArchivoUtils.encrypt(originalString, secretKey);
        String decryptedString = ArchivoUtils.decrypt(originalString, secretKey);
        
        System.out.println(originalString);
//        System.out.println(encryptedString);
        System.out.println(decryptedString);
        return decryptedString;
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
    
    @POST
    @Path("/retencion-enviar/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaResponse getEnviarRetencion(@RequestBody RetencionCompraDao prod) throws Exception {
        
        prod.setSecuencialText(rellenarConCeros(prod.getSecuencial(), 9));
        
        FacturaResponse facturaResponse = new FacturaResponse();
        facturaResponse.setFacFecha(prod.getRetencionFecha());
        facturaResponse.setFacNumeroText(prod.getSecuencialText());
        facturaResponse.setIdentificacionComprador(prod.getRucEmpresa());
        facturaResponse.setRazonSocialComprador(prod.getRazonSocialEmpresa());

        //Rellenar de 0 el numero de factura
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        SimpleDateFormat smAut = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        /*RUTA DE LOS ARCHIVOS*/
        String folderArchivos = prod.getInfoAutoriza().getRutaArchivo();
        String folderFirmado = folderArchivos + File.separator + "FIRMADO" + File.separator;
        String nombreArchivo = prod.getRucEmpresa() + "-" + prod.getSecuencial() + ".xml";
        String pathArchivoFirmado = folderFirmado + nombreArchivo;
        String pathArchivoNoAutorizado = folderArchivos + File.separator + "NOAUTORIZADO" + File.separator;
        String archivoEnvioCliente = prod.getInfoAutoriza().getRutaArchivo() + File.separator + "ENVIARCLIENTE" + File.separator;
        final String secretKey = "AFSOTEC2023";
        AutorizarDocumentosApi api = new AutorizarDocumentosApi();
        String archivo = api.generaXMLComprobanteRetencionApi(prod, prod.getInfoAutoriza().getRutaArchivo(), nombreArchivo);
        XAdESBESSignatureApi.firmar(archivo, prod.getRucEmpresa() + "-" + prod.getSecuencial() + ".xml",
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
                                Logger.getLogger(ListaFacturas.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
////                     
////                            archivoEnvioCliente = api.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                            archivoEnvioCliente = api.generaXMLComprobanteRetencionApi(prod, archivoEnvioCliente, nombreArchivo);
////                            XAdESBESSignature.firmar(archivoEnvioCliente,
////                                    nombreArchivoXML,
////                                    amb.getAmClaveAccesoSri(),
////                                    amb, foldervoAutorizado);
////                            valor.setFacpath(archivoEnvioCliente.replace(".xml", ".pdf"));
////                            servicioFactura.modificar(valor);
//
//                            fEnvio = new File(archivoEnvioCliente);
//
//                            System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
////                            ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), valor.getFacNumero(), "FACT", amb);
////                            ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
//                            /*GUARDA EL PATH PDF CREADO*/
                            RetencionCompra retencinon = RetencionCompraMapper.daoToRetencion(prod);
//                            //RetencionCompraDao retencion=
//
                            servicioRetencion.crear(retencinon);
                            DetalleRetencionCompra detalleRetencion;
                            for (DetRetencionCompraDao detRetencionDao : prod.getDetRetencion()) {
                                detalleRetencion = new DetalleRetencionCompra();
                                if (detRetencionDao.getTireCodigo() != null) {
                                    detalleRetencion.setTireCodigo(servicioTipoRetencion.findOneTireCodigo(detRetencionDao.getTireCodigo()));
                                }
                                
                                if (detRetencionDao.getTipivaretValor() != null) {
                                    detalleRetencion.setIdTipoivaretencion(servicioTipoIvaRetencion.finTipoivaretencion(detRetencionDao.getTipivaretValor()));
                                }
                                detalleRetencion = DetRetencionMapper.daoToRetencion(detRetencionDao);
                                detalleRetencion.setRcoCodigo(retencinon);
                                servicioDetalleRetencionCompra.crear(detalleRetencion);
                                
                            }
//                            /*envia el mail*/
//                            String[] attachFiles = new String[2];
//                            attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
//                            attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
//                            MailerClass mail = new MailerClass();
////                            Tipoambiente amb= servicioTipoAmbiente.finActivo();
////                            if (prod.getCorreoComprador() != null) {
////                                mail.sendMailSimple(prod.getCorreoComprador(),
////                                            attachFiles,
////                                            "FACTURA ELECTRONICA",
////                                           claveAccesoComprobante,
////                                            prod.getFacNumeroText(),
////                                            prod.getFacTotal(),
////                                            prod.getRazonSocialComprador(), amb);
////                            }
//
//                            /*INCLUIMOS EL XML PARA LA RESPUESTA*/
//                            facturaResponse.setXmlAutorizado(autorizacion.getComprobante());
                        }
                        
                    }
                } catch (RespuestaAutorizacionException ex) {
                    Logger.getLogger(ListaFacturas.class
                            .getName()).log(Level.SEVERE, null, ex);
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
    
    @POST
    @Path("/nota-credito-debito-enviar/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaResponse getEnviarNotaCreditoDebito(@RequestBody NotaCreditoDebitoDao prod) throws Exception {
        
        FacturaResponse facturaResponse = new FacturaResponse();
        facturaResponse.setFacFecha(prod.getFacFecha());
        facturaResponse.setFacNumeroText(prod.getSecuencialText());
        facturaResponse.setIdentificacionComprador(prod.getRucEmpresa());
        facturaResponse.setRazonSocialComprador(prod.getRazonSocialEmpresa());

        //Rellenar de 0 el numero de factura
        prod.setSecuencialText(rellenarConCeros(prod.getSecuencial(), 9));
        
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        SimpleDateFormat smAut = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        /*RUTA DE LOS ARCHIVOS*/
        String folderArchivos = prod.getInfoAutoriza().getRutaArchivo();
        String folderFirmado = folderArchivos + File.separator + "FIRMADO" + File.separator;
        String nombreArchivo = prod.getRucEmpresa() + "-" + prod.getSecuencialText() + ".xml";
        String pathArchivoFirmado = folderFirmado + nombreArchivo;
        String pathArchivoNoAutorizado = folderArchivos + File.separator + "NOAUTORIZADO" + File.separator;
        String archivoEnvioCliente = prod.getInfoAutoriza().getRutaArchivo() + File.separator + "ENVIARCLIENTE" + File.separator;
        final String secretKey = "AFSOTEC2023";
        AutorizarDocumentosApi api = new AutorizarDocumentosApi();
        String archivo = api.generaXMLNotaCreditoDebitoApi(prod, prod.getInfoAutoriza().getRutaArchivo(), nombreArchivo);
        XAdESBESSignatureApi.firmar(archivo, prod.getRucEmpresa() + "-" + prod.getSecuencialText() + ".xml",
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
                                facturaResponse.setClaveAutorizacion(claveAccesoComprobante);
                            } catch (java.text.ParseException ex) {
                                Logger.getLogger(ListaFacturas.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
//                     
//                            archivoEnvioCliente = api.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                            archivoEnvioCliente = api.generaXMLNotaCreditoDebitoApi(prod, archivoEnvioCliente, nombreArchivo);
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
                            NotaCreditoDebito notacr = NotaCreditoMapper.daoToEntity(prod);
                            servicioNotaCredito.crear(notacr);
                            DetalleNotaDebitoCredito detalleNC = new DetalleNotaDebitoCredito();
                            for (DetalleNotaCreditoDebitoDao item : prod.getDetalleNotaCredito()) {
                                detalleNC = new DetalleNotaDebitoCredito();
                                detalleNC = DetNotaCreditoMapper.daoToEntity(item);
                                detalleNC.setIdNota(notacr);
                                servicioDetalleNotaCredito.crear(detalleNC);
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
                    Logger.getLogger(ListaFacturas.class
                            .getName()).log(Level.SEVERE, null, ex);
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
    
    @POST
    @Path("/guia-remision-enviar/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaResponse getEnviarGuiaRemision(@RequestBody GuiaremisionDao prod) throws Exception {
        
        FacturaResponse facturaResponse = new FacturaResponse();
        facturaResponse.setFacFecha(prod.getFacFecha());
        facturaResponse.setFacNumeroText(prod.getSecuencialText());
        facturaResponse.setIdentificacionComprador(prod.getRucEmpresa());
        facturaResponse.setRazonSocialComprador(prod.getRazonSocialEmpresa());

        //Rellenar de 0 el numero de factura
        prod.setSecuencialText(rellenarConCeros(prod.getSecuencial(), 9));
        
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        SimpleDateFormat smAut = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        /*RUTA DE LOS ARCHIVOS*/
        String folderArchivos = prod.getInfoAutoriza().getRutaArchivo();
        String folderFirmado = folderArchivos + File.separator + "FIRMADO" + File.separator;
        String nombreArchivo = prod.getRucEmpresa() + "-" + prod.getSecuencial() + ".xml";
        String pathArchivoFirmado = folderFirmado + nombreArchivo;
        String pathArchivoNoAutorizado = folderArchivos + File.separator + "NOAUTORIZADO" + File.separator;
        String archivoEnvioCliente = prod.getInfoAutoriza().getRutaArchivo() + File.separator + "ENVIARCLIENTE" + File.separator;
        final String secretKey = "AFSOTEC2023";
        AutorizarDocumentosApi api = new AutorizarDocumentosApi();
        String archivo = api.generaXMLGuiaRemisionApi(prod, prod.getInfoAutoriza().getRutaArchivo(), nombreArchivo);
        XAdESBESSignatureApi.firmar(archivo, prod.getRucEmpresa() + "-" + prod.getSecuencial() + ".xml",
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
                                Logger.getLogger(ListaFacturas.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            archivoEnvioCliente = api.generaXMLGuiaRemisionApi(prod, archivoEnvioCliente, nombreArchivo);
                            
                            fEnvio = new File(archivoEnvioCliente);
                            
                            System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
//                            ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), prod.getSecuencialText(), "FACT", amb);
//                            ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
                            /*GUARDA EL PATH PDF CREADO*/
                            Guiaremision guia = GuiaRemisionMapper.daoToGuiaremision(prod);
                            
                            servicioGuiaRemision.crear(guia);
                            DetalleGuiaremision detalleGuia;
                            for (DetalleGuiaremisionDao detFacturaDao : prod.getDetalleGuiaRemision()) {
                                detalleGuia = new DetalleGuiaremision();
                                detalleGuia = DetalleGuiaRemisionMapper.daoToDetalleGuiaremision(detFacturaDao);
                                detalleGuia.setIdGuiaremision(guia);
                                servicioDetalleGuiaRemision.crear(detalleGuia);
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
                    Logger.getLogger(ListaFacturas.class
                            .getName()).log(Level.SEVERE, null, ex);
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

    /*generar PDF*/
    @POST
    @Path("/pdf-factura/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public PdfResponse getPdfFactura(@RequestBody PdfRequest prod) throws Exception {
        PdfResponse response = new PdfResponse();
        
        try {
            String folderCliente = prod.getRutaArchivo() + File.separator + "ENVIARCLIENTE" + File.separator;
            String folderArchivos = prod.getRutaArchivo();
            
            String nombreArchivo = prod.getIdentificacionComprador() + "-" + prod.getFacNumero() + ".pdf";
            String pathEnvio = folderCliente + File.separator + nombreArchivo;
            String directoryName = System.getProperty("user.dir");
            String[] valores = directoryName.split("config");
            String rutaGeneraPdf = valores[0] + File.separator + "applications" + File.separator + "recursos" + File.separator + "recursos" + File.separator + "pdf";
            pathEnvio = rutaGeneraPdf + File.separator + nombreArchivo;
            
            System.out.println("pathEnvio " + pathEnvio);
            ArchivoUtils.reporteGeneralPdfMail(pathEnvio.replace(".xml", ".pdf"), prod.getFacNumero(), "FACT", prod.getRucEmpresa(), prod.getEstablecimientoEmpresa(), prod.getPuntoEmisionEmpresa(), folderArchivos, prod.getRutaLogo());
            response.setDocumento("FACTURA");
            response.setUrlReporte("https://api.clouget.net/recursos/recursos/pdf/" + nombreArchivo);
//            response.setUrlReporte("http://148.113.182.154:8080/recursos/recursos/pdf/" + nombreArchivo);
//            response.setUrlReporte("https://consultoriointegradodecardiologia.com/recursos/recursos/pdf/" + nombreArchivo);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException | NamingException | JRException e) {
            response.setDocumento("FACTURA");
            response.setUrlReporte("Error al generar el pdf " + e.getMessage());
        }
        return response;
    }
    
    @POST
    @Path("/pdf-ticket/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public PdfResponse getPdfTicket(@RequestBody PdfRequest prod) throws Exception {
        PdfResponse response = new PdfResponse();
        
        try {
            String folderCliente = prod.getRutaArchivo() + File.separator + "ENVIARCLIENTE" + File.separator;
            String folderArchivos = prod.getRutaArchivo();
            
            String nombreArchivo = "TK-" + prod.getIdentificacionComprador() + "-" + prod.getFacNumero() + ".pdf";
            String pathEnvio = folderCliente + File.separator + nombreArchivo;
            String directoryName = System.getProperty("user.dir");
            String[] valores = directoryName.split("config");
            String rutaGeneraPdf = valores[0] + File.separator + "applications" + File.separator + "recursos" + File.separator + "recursos" + File.separator + "pdf";
            pathEnvio = rutaGeneraPdf + File.separator + nombreArchivo;
            System.out.println("pathEnvio " + pathEnvio);
            ArchivoUtils.reporteGeneralPdfMail(pathEnvio.replace(".xml", ".pdf"), prod.getFacNumero(), "TICKET", prod.getRucEmpresa(), prod.getEstablecimientoEmpresa(), prod.getPuntoEmisionEmpresa(), folderArchivos, prod.getRutaLogo());
            response.setDocumento("TICKET");
            response.setUrlReporte("https://api.clouget.net/recursos/recursos/pdf/" + nombreArchivo);
//            response.setUrlReporte("http://148.113.182.154:8080/recursos/recursos/pdf/" + nombreArchivo);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException | NamingException | JRException e) {
            response.setDocumento("TICKET");
            response.setUrlReporte("Error al generar el pdf " + e.getMessage());
        }
        return response;
    }
    
    @POST
    @Path("/excel-factura/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public PdfResponse exportarExcel(@RequestBody ExcelRequest prod) {
        PdfResponse response = new PdfResponse();
        List<Factura> lstFacturas = servicioFactura.findFacFechaEmpresa(prod.getFechainicio(), prod.getFechafin(), prod.getRucEmpresa(), prod.getEstablecimiento(), prod.getPuntoEmision());
        Date date = new Date();
        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
        String strDate = sm.format(date);
        
        String nombreArchivo = "Facturas" + date.getTime() + ".xls";
        
        String directoryName = System.getProperty("user.dir");
        String[] valores = directoryName.split("config");
        String rutaGeneraPdf = valores[0] + File.separator + "applications" + File.separator + "recursos" + File.separator + "recursos" + File.separator + "excel";
        String pathEnvio = rutaGeneraPdf + File.separator + nombreArchivo;
        
        String pathSalida = pathEnvio;
        System.out.println("Direccion del reporte  " + pathSalida);
        try {
            int j = 0;
            File archivoXLS = new File(pathSalida);
//              folderNuevo = new File(archivoGenerado);
//        if (!folderNuevo.exists()) {
//            folderNuevo.mkdirs();
//        }
            BufferedWriter bw;
            if (!archivoXLS.exists()) {
                bw = new BufferedWriter(new FileWriter(pathSalida));
                bw.close(); // Debe cerrarse la escritura del ficher
            }
            System.out.println("PASA 1 ");
            archivoXLS.createNewFile();
            System.out.println("PASA 2 ");
            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("Facturas emitidas");
            System.out.println("PASA 3 ");
            HSSFFont fuente = wb.createFont();
            fuente.setBoldweight((short) 700);
            HSSFCellStyle estiloCelda = wb.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment((short) 2);
            estiloCelda.setFont(fuente);
            
            HSSFCellStyle estiloCeldaInterna = wb.createCellStyle();
            estiloCeldaInterna.setWrapText(true);
            estiloCeldaInterna.setAlignment((short) 5);
            estiloCeldaInterna.setFont(fuente);
            
            HSSFCellStyle estiloCelda1 = wb.createCellStyle();
            estiloCelda1.setWrapText(true);
            estiloCelda1.setFont(fuente);
            
            HSSFRow r = null;
            
            HSSFCell c = null;
            r = s.createRow(0);
            
            HSSFCell chfe = r.createCell(j++);
            chfe.setCellValue(new HSSFRichTextString("Nº Factura"));
            chfe.setCellStyle(estiloCelda);
            
            HSSFCell chfe1 = r.createCell(j++);
            chfe1.setCellValue(new HSSFRichTextString("CI/RUC"));
            chfe1.setCellStyle(estiloCelda);
            
            HSSFCell chfe11 = r.createCell(j++);
            chfe11.setCellValue(new HSSFRichTextString("Cliente"));
            chfe11.setCellStyle(estiloCelda);
            
            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("F Emision"));
            ch1.setCellStyle(estiloCelda);
            
            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("Subtotal"));
            ch2.setCellStyle(estiloCelda);
            
            HSSFCell ch23 = r.createCell(j++);
            ch23.setCellValue(new HSSFRichTextString("Subtotal 0%"));
            ch23.setCellStyle(estiloCelda);
            HSSFCell ch22 = r.createCell(j++);
            ch22.setCellValue(new HSSFRichTextString("Subtotal 5%"));
            ch22.setCellStyle(estiloCelda);
            HSSFCell ch222 = r.createCell(j++);
            ch222.setCellValue(new HSSFRichTextString("Subtotal 15%"));
            ch222.setCellStyle(estiloCelda);
            
            HSSFCell ch3 = r.createCell(j++);
            ch3.setCellValue(new HSSFRichTextString("Iva 5%"));
            ch3.setCellStyle(estiloCelda);
            HSSFCell ch33 = r.createCell(j++);
            ch33.setCellValue(new HSSFRichTextString("Iva 15%"));
            ch33.setCellStyle(estiloCelda);
            
            HSSFCell ch4 = r.createCell(j++);
            ch4.setCellValue(new HSSFRichTextString("Total"));
            ch4.setCellStyle(estiloCelda);
//            HSSFCell ch5 = r.createCell(j++);
//            ch5.setCellValue(new HSSFRichTextString("ESTADO"));
//            ch5.setCellStyle(estiloCelda);

            HSSFCell ch6 = r.createCell(j++);
            ch6.setCellValue(new HSSFRichTextString("ESTADO SRI"));
            ch6.setCellStyle(estiloCelda);
            
            HSSFCell ch7 = r.createCell(j++);
            ch7.setCellValue(new HSSFRichTextString("CLAVE AUTORIZA"));
            ch7.setCellStyle(estiloCelda);
            
            HSSFCell ch8 = r.createCell(j++);
            ch8.setCellValue(new HSSFRichTextString("FECHA AUTORIZA"));
            ch8.setCellStyle(estiloCelda);
            
            HSSFCell ch9 = r.createCell(j++);
            ch9.setCellValue(new HSSFRichTextString("OBSERVACIÓN"));
            ch9.setCellStyle(estiloCelda);
            
            int rownum = 1;
            int i = 0;
            BigDecimal subTotal = BigDecimal.ZERO;
            BigDecimal subTotal12 = BigDecimal.ZERO;
            BigDecimal subTotal5 = BigDecimal.ZERO;
            BigDecimal subTotal15 = BigDecimal.ZERO;
            BigDecimal subTotal0 = BigDecimal.ZERO;
            BigDecimal IVATotal = BigDecimal.ZERO;
            BigDecimal IVATotal5 = BigDecimal.ZERO;
            BigDecimal IVATotal15 = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;
            
            for (Factura item : lstFacturas) {
                i = 0;
                System.out.println("PASA 4 ");
                r = s.createRow(rownum);
                
                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getCodestablecimiento() + "-" + item.getPuntoemision() + "-" + item.getFacNumero().toString()));
                
                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(item.getIdentificacionComprador().toString()));
                
                HSSFCell cf11 = r.createCell(i++);
                cf11.setCellValue(new HSSFRichTextString(item.getRazonSocialComprador().toString()));
                
                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(sm.format(item.getFacFecha())));
                
                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacSubtotal(), 2)).toString()));
                
                subTotal = subTotal.add(ArchivoUtils.redondearDecimales(item.getFacSubtotal(), 2));
                
                HSSFCell c12 = r.createCell(i++);
                c12.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacTotalBaseCero(), 2)).toString()));
                subTotal0 = subTotal0.add(ArchivoUtils.redondearDecimales(item.getFacTotalBaseCero(), 2));
                
                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacSubt5(), 2)).toString()));
                subTotal5 = subTotal5.add(ArchivoUtils.redondearDecimales(item.getFacSubt5(), 2));
                
                HSSFCell c111 = r.createCell(i++);
                c111.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacSubt15(), 2)).toString()));
                subTotal15 = subTotal15.add(ArchivoUtils.redondearDecimales(item.getFacSubt15(), 2));
                
                HSSFCell c2 = r.createCell(i++);
                c2.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacIva5(), 2)).toString()));
                IVATotal5 = IVATotal5.add(ArchivoUtils.redondearDecimales(item.getFacIva5(), 2));
                
                HSSFCell c22 = r.createCell(i++);
                c22.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacIva15(), 2)).toString()));
                IVATotal15 = IVATotal15.add(ArchivoUtils.redondearDecimales(item.getFacIva15(), 2));
                HSSFCell c3 = r.createCell(i++);
                c3.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(item.getFacTotal(), 2)).toString()));
                
                total = total.add(ArchivoUtils.redondearDecimales(item.getFacTotal(), 2));
//
//                HSSFCell c4 = r.createCell(i++);
//                c4.setCellValue(new HSSFRichTextString(item.getIdEstado().getEstNombre()));

                HSSFCell c5 = r.createCell(i++);
                c5.setCellValue(new HSSFRichTextString(item.getEstadosri() != null ? item.getEstadosri() : ""));
                
                HSSFCell c13 = r.createCell(i++);
                c13.setCellValue(new HSSFRichTextString(item.getFacClaveAcceso() != null ? item.getFacClaveAcceso() : "Sin Autorizar"));
                
                HSSFCell c15 = r.createCell(i++);
                c15.setCellValue(new HSSFRichTextString(item.getFacFechaAutorizacion() != null ? sm.format(item.getFacFechaAutorizacion()) : ""));
                /*autemta la siguiente fila*/
                
                HSSFCell c16 = r.createCell(i++);
                c16.setCellValue(new HSSFRichTextString(item.getFacObservacion() != null ? item.getFacObservacion() : ""));
                
                rownum += 1;
                
            }
            
            j = 0;
            r = s.createRow(rownum);
            HSSFCell chfeF1 = r.createCell(j++);
            chfeF1.setCellValue(new HSSFRichTextString(""));
            chfeF1.setCellStyle(estiloCelda);
            
            HSSFCell chfeF2 = r.createCell(j++);
            chfeF2.setCellValue(new HSSFRichTextString(""));
            chfeF2.setCellStyle(estiloCelda);
            
            HSSFCell chfeF3 = r.createCell(j++);
            chfeF3.setCellValue(new HSSFRichTextString(""));
            chfeF3.setCellStyle(estiloCelda);
            
            HSSFCell chF4 = r.createCell(j++);
            chF4.setCellValue(new HSSFRichTextString(""));
            chF4.setCellStyle(estiloCelda);
            
            HSSFCell chF5 = r.createCell(j++);
            chF5.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(subTotal, 2)).toString()));
            chF5.setCellStyle(estiloCelda);
            
            HSSFCell chF6 = r.createCell(j++);
            chF6.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(subTotal0, 2)).toString()));
            chF6.setCellStyle(estiloCelda);
            
            HSSFCell chF7 = r.createCell(j++);
            chF7.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(subTotal5, 2)).toString()));
            chF7.setCellStyle(estiloCelda);
            
            HSSFCell chF77 = r.createCell(j++);
            chF77.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(subTotal15, 2)).toString()));
            chF77.setCellStyle(estiloCelda);
            
            HSSFCell chF8 = r.createCell(j++);
            chF8.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(IVATotal5, 2)).toString()));
            chF8.setCellStyle(estiloCelda);
            
            HSSFCell chF88 = r.createCell(j++);
            chF88.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(IVATotal15, 2)).toString()));
            chF88.setCellStyle(estiloCelda);
            
            HSSFCell chF9 = r.createCell(j++);
            chF9.setCellValue(new HSSFRichTextString((ArchivoUtils.redondearDecimales(total, 2)).toString()));
            chF9.setCellStyle(estiloCelda);

//            HSSFCell chF10 = r.createCell(j++);
//            chF10.setCellValue(new HSSFRichTextString(""));
//            chF10.setCellStyle(estiloCelda);
            HSSFCell chF11 = r.createCell(j++);
            chF11.setCellValue(new HSSFRichTextString(""));
            chF11.setCellStyle(estiloCelda);
            
            for (int k = 0; k <= lstFacturas.size(); k++) {
                s.autoSizeColumn(k);
            }
            wb.write(archivo);
            archivo.close();
            response.setDocumento("EXCEL");
            response.setUrlReporte("https://api.clouget.net/recursos/recursos/excel/" + nombreArchivo);
//            response.setUrlReporte("http://148.113.182.154:8080/recursos/recursos/excel/" + nombreArchivo);
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
            response.setDocumento("ERROR");
            response.setUrlReporte("Error al crear el reporte " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
        
    }
    
    @POST
    @Path("/anular-factura/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public FacturaResponse getAnularFactura(@RequestBody RequestAnulaDoc prod) throws Exception {
        FacturaResponse facturaResponse = new FacturaResponse();
        
        try {
            Factura recup = servicioFactura.findNumeroEmpresa(prod.getEstablecimientoEmpresa(), prod.getPuntoEmisionEmpresa(), prod.getFacNumero(), prod.getRucEmpresa());
            if (recup != null) {
                recup.setEstadosri("ANULADA");
                servicioFactura.modificar(recup);
                facturaResponse.setMensajeError("FACTURA OK");
                facturaResponse.setEstadoSri("ANULADA");
            } else {
                facturaResponse.setMensajeError("FACTURA OK");
                facturaResponse.setEstadoSri("NO SE ENCONTRO LA FACTURA ");
            }
        } catch (Exception e) {
            facturaResponse.setMensajeError("ERROR");
            facturaResponse.setEstadoSri(e.getMessage());
        }
        return facturaResponse;
    }
    
    @POST
    @Path("/validar-firma/")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public DatosFirmaResponse validarFirma(@RequestBody InfoAutorizaDao prod) throws Exception {
        DatosFirmaResponse datosFirma = new DatosFirmaResponse();
        
        try {
            // Ruta al archivo P12 o PFX

            final String secretKey = "AFSOTEC2023";
            
            String rutaArchivo = prod.getRutaFirma();
            String contrasena = ArchivoUtils.decrypt(prod.getPasswordFirma(), secretKey); // Contraseña del archivo

            // Cargar el almacén de claves (KeyStore)
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(rutaArchivo);
            keyStore.load(fis, contrasena.toCharArray());

            // Obtener el alias del certificado
            String alias = keyStore.aliases().nextElement();

            // Obtener el certificado X509
            X509Certificate certificado = (X509Certificate) keyStore.getCertificate(alias);

            // Mostrar información del certificado
            System.out.println("Titular: " + certificado.getSubjectDN());
            System.out.println("Emisor: " + certificado.getIssuerDN());
            System.out.println("Válido desde: " + certificado.getNotBefore());
            System.out.println("Válido hasta: " + certificado.getNotAfter());
            datosFirma.setTitular(String.valueOf(certificado.getSubjectDN()));
            datosFirma.setEmisor(String.valueOf(certificado.getIssuerDN()));
            SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
            String fechaDesde = sm.format(certificado.getNotBefore());
//            Date fechaDesdeFor = sm.parse(fechaDesde);
            String fechaHasta = sm.format(certificado.getNotAfter());
//            Date fechaHataFor = sm.parse(fechaHasta);

            datosFirma.setDesde(fechaDesde);
            datosFirma.setHasta(fechaHasta);
            // Cerrar el flujo de entrada
            fis.close();
            
        } catch (Exception e) {
            datosFirma.setTitular("ERROR " + e.getMessage());
            datosFirma.setEmisor("ERROR");
            
        }
        return datosFirma;
    }
    
}
