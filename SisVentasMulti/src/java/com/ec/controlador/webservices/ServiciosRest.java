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
import com.ec.controlador.webservices.mapper.FacturaMapper;
import com.ec.controlador.webservices.mapper.NotaCreditoMapper;
import com.ec.controlador.webservices.mapper.RetencionCompraMapper;
import com.ec.dao.DetFacturaDao;
import com.ec.dao.DetRetencionCompraDao;
import com.ec.dao.DetalleNotaCreditoDebitoDao;
import com.ec.dao.FacturaDao;
import com.ec.dao.GuiaremisionDao;
import com.ec.dao.InfoAutorizaDao;
import com.ec.dao.NotaCreditoDebitoDao;
import com.ec.dao.RetencionCompraDao;
import com.ec.dao.response.FacturaResponse;
import com.ec.entidad.DetalleFactura;
import com.ec.entidad.DetalleNotaDebitoCredito;
import com.ec.entidad.DetalleRetencionCompra;
import com.ec.entidad.Factura;
import com.ec.entidad.NotaCreditoDebito;
import com.ec.entidad.RetencionCompra;
import com.ec.servicio.ServicioDetalleFactura;
import com.ec.servicio.ServicioDetalleNotaCredito;
import com.ec.servicio.ServicioDetalleRetencionCompra;
import com.ec.servicio.ServicioFactura;
import com.ec.servicio.ServicioNotaCredito;
import com.ec.servicio.ServicioRetencionCompra;
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

import org.springframework.web.bind.annotation.RequestBody;

@Path("/autorizar")
public class ServiciosRest {

    ServicioFactura servicioFactura = new ServicioFactura();
    ServicioDetalleFactura servicioDetalleFactura = new ServicioDetalleFactura();
    ServicioRetencionCompra servicioRetencion = new ServicioRetencionCompra();
    ServicioDetalleRetencionCompra servicioDetalleRetencionCompra = new ServicioDetalleRetencionCompra();
    ServicioTipoRetencion servicioTipoRetencion = new ServicioTipoRetencion();
    ServicioTipoIvaRetencion servicioTipoIvaRetencion = new ServicioTipoIvaRetencion();

    ServicioNotaCredito servicioNotaCredito = new ServicioNotaCredito();
    ServicioDetalleNotaCredito servicioDetalleNotaCredito = new ServicioDetalleNotaCredito();

//
//    @POST
//    @Path("/factura/")
//    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
//    @Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML, javax.ws.rs.core.MediaType.APPLICATION_JSON})
//    public FacturaDao getReenviarFacturas(@RequestBody FacturaDao prod) throws Exception {
//
//        FacturaResponse facturaResponse = new FacturaResponse();
//        Factura recup = servicioFactura.findByNumero(prod.getFacNumero());
////        System.out.println("valor "+recup.getEstadosri());
//        FacturaDao dao = FacturaMapper.facturaToDao(recup);
//        List<DetalleFactura> detalle = servicioDetalleFactura.findDetalleForIdFactuta(recup);
//        List<DetFacturaDao> detalleDao = new ArrayList<>();
//        detalle.forEach((detalleFactura) -> {
//            DetFacturaDao dao1 = DetFacturaMapper.facturaToDao(detalleFactura);
//            dao1.setCodigoProducto("05555");
//            dao1.setTieneSubsidio(Boolean.FALSE);
//            detalleDao.add(dao1);
//        });
//        dao.setDetFacturaDao(detalleDao);
//        InfoAutorizaDao autoriza = new InfoAutorizaDao();
//        autoriza.setRutaArchivo("D:\\\\DOCUMENTOSRI\\XML\\");
//        autoriza.setRutaFirma("D:\\DOCUMENTOSRI\\FIRMA\\DarwinMorocho2022-2023.p12");
//        autoriza.setPasswordFirma("Dereckandre02");
//        dao.setInfoAutoriza(autoriza);
//        dao.setAmCodigo("1");
//        dao.setLlevarContabilidad("NO");
//        dao.setTipoIdentificacionComprador("05");
//        dao.setIdentificacionComprador("1718264839");
//        dao.setRazonSocialComprador("Darwin Morocho");
//        dao.setDireccionComprador("Gonzalez Suarez");
//        dao.setGrabaICE(Boolean.FALSE);
//        dao.setAgenteRetencion(Boolean.FALSE);
//        dao.setRimpePolpular(Boolean.FALSE);
//        dao.setRimpeEmprendedor(Boolean.FALSE);
//        dao.setRegimenGeneral(Boolean.FALSE);
//        dao.setDireccionMatriz("Tabacundo km 27 1/2 via Cayambe");
//        dao.setRazonSocialEmpresa("Darwin Morocho");
//        dao.setNombreComercialEmpresa("DECKXEL");
//        dao.setDireccionMatriz("Tabacundo km 27 1/2 via Cayambe");
//        dao.setRucEmpresa("1718264839001");
//        dao.setFacTarifaIce(BigDecimal.ZERO);
//        return dao;
//    }
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

        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
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

        FacturaResponse facturaResponse = new FacturaResponse();
        facturaResponse.setFacFecha(prod.getRcoFecha());
        facturaResponse.setFacNumeroText(prod.getRcoSecuencialText());
        facturaResponse.setIdentificacionComprador(prod.getAmRuc());
        facturaResponse.setRazonSocialComprador(prod.getAmRazonSocial());

        //Rellenar de 0 el numero de factura
        prod.setRcoSecuencialText(rellenarConCeros(prod.getRcoSecuencial(), 9));

        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        SimpleDateFormat smAut = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        /*RUTA DE LOS ARCHIVOS*/
        String folderArchivos = prod.getInfoAutoriza().getRutaArchivo();
        String folderFirmado = folderArchivos + File.separator + "FIRMADO" + File.separator;
        String nombreArchivo = prod.getAmRuc() + "-" + prod.getRcoSecuencial() + ".xml";
        String pathArchivoFirmado = folderFirmado + nombreArchivo;
        String pathArchivoNoAutorizado = folderArchivos + File.separator + "NOAUTORIZADO" + File.separator;
        String archivoEnvioCliente = prod.getInfoAutoriza().getRutaArchivo() + File.separator + "ENVIARCLIENTE" + File.separator;
        final String secretKey = "AFSOTEC2023";
        AutorizarDocumentosApi api = new AutorizarDocumentosApi();
        String archivo = api.generaXMLComprobanteRetencionApi(prod, prod.getInfoAutoriza().getRutaArchivo(), nombreArchivo);
        XAdESBESSignatureApi.firmar(archivo, prod.getAmRuc() + "-" + prod.getRcoSecuencial() + ".xml",
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
        String nombreArchivo = prod.getRucEmpresa() + "-" + prod.getSecuencial() + ".xml";
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

                            } catch (java.text.ParseException ex) {
                                Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                     
//                            archivoEnvioCliente = api.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                            archivoEnvioCliente = api.generaXMLGuiaRemisionApi(prod, archivoEnvioCliente, nombreArchivo);
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
//                            Factura factura = FacturaMapper.daoToFactura(prod);
//                            servicioFactura.crear(factura);
//                            DetalleFactura detalleFactura = new DetalleFactura();
//                            for (DetFacturaDao detFacturaDao : prod.getDetFacturaDao()) {
//                                detalleFactura = new DetalleFactura();
//                                detalleFactura = DetFacturaMapper.daoToFactura(detFacturaDao);
//                                detalleFactura.setIdFactura(factura);
//                                servicioDetalleFactura.crear(detalleFactura);
//                            }
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

}
