/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Cliente;
import com.ec.entidad.Guiaremision;
import com.ec.entidad.NotaCreditoDebito;
import com.ec.entidad.Tipoambiente;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioCliente;
import com.ec.servicio.ServicioGuia;
import com.ec.servicio.ServicioNotaCredito;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.AutorizarDocumentos;
import com.ec.untilitario.MailerClass;
import com.ec.untilitario.XAdESBESSignature;
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.jfree.chart.JFreeChart;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author gato
 */
public class ListaNC1 {

    /*RUTAS PARA LOS ARCHIVPOS XML SRI*/
    private static String PATH_BASE = "";
    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    ServicioNotaCredito servicioNotaCredito = new ServicioNotaCredito();
    ServicioCliente servicioCliente = new ServicioCliente();
    private List<NotaCreditoDebito> lstCreditoDebitos = new ArrayList<NotaCreditoDebito>();
    //reporte
    AMedia fileContent = null;
    Connection con = null;
    private String buscarCliente = "";
    private String buscarCedula = "";
    private String estadoBusqueda = "";
    private BigDecimal porCobrar = BigDecimal.ZERO;
    //tabla para los parametros del SRI
    private Tipoambiente amb = new Tipoambiente();
    private Date fechainicio = new Date();
    private Date fechafin = new Date();
    private String amRuc = "";
    UserCredential credential = new UserCredential();

    public ListaNC1() {

        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
//        amRuc = credential.getUsuarioSistema().getUsuRuc();
        amb = servicioTipoAmbiente.findALlTipoambientePorUsuario(credential.getUsuarioSistema());

        //OBTIENE LAS RUTAS DE ACCESO A LOS DIRECTORIOS DE LA TABLA TIPOAMBIENTE
        PATH_BASE = amb.getAmDirBaseArchivos() + File.separator
                + amb.getAmDirXml();

        consultarFactura();
    }

    private void consultarFactura() {
        lstCreditoDebitos = servicioNotaCredito.findBetweenFecha(fechainicio, fechafin, amb);
    }

    public List<NotaCreditoDebito> getLstCreditoDebitos() {
        return lstCreditoDebitos;
    }

    public void setLstCreditoDebitos(List<NotaCreditoDebito> lstCreditoDebitos) {
        this.lstCreditoDebitos = lstCreditoDebitos;
    }

    public String getBuscarCliente() {
        return buscarCliente;
    }

    public void setBuscarCliente(String buscarCliente) {
        this.buscarCliente = buscarCliente;
    }

    public String getEstadoBusqueda() {
        return estadoBusqueda;
    }

    public void setEstadoBusqueda(String estadoBusqueda) {
        this.estadoBusqueda = estadoBusqueda;
    }

    public BigDecimal getPorCobrar() {
        return porCobrar;
    }

    public void setPorCobrar(BigDecimal porCobrar) {
        this.porCobrar = porCobrar;
    }

    @Command
    public void reporteNotaVenta(@BindingParam("valor") Guiaremision valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumero(), "GUIA");
    }

    public void reporteGeneral(Integer numeroFactura, String tipo) throws JRException, IOException, NamingException, SQLException {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            String reportPath = "";
            if (tipo.equals("COMP")) {
                reportPath = reportFile + File.separator + "puntoventa.jasper";
            } else if (tipo.equals("FACT")) {
                reportPath = reportFile + File.separator + "factura.jasper";
            } else if (tipo.equals("GUIA")) {
                reportPath = reportFile + File.separator + "guia.jasper";
            }

            Map<String, Object> parametros = new HashMap<String, Object>();

            parametros.put("tipoambiente", amb.getCodTipoambiente());
            parametros.put("numfactura", numeroFactura);

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/venta/contenedorReporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    //buscart notas de venta
    @Command
    @NotifyChange({"lstCreditoDebitos", "buscarCliente"})
    public void buscarLikeCliente() {

        consultarFacturas();

    }

    private void consultarFacturas() {
        lstCreditoDebitos = servicioNotaCredito.findLikeCliente(buscarCliente, amb);

    }

    //buscart notas de venta
    @Command
    @NotifyChange({"lstCreditoDebitos", "buscarCliente"})
    public void buscarLikeCedula() {

        consultarFacturasForCedula();

    }

    private void consultarFacturasForCedula() {
        lstCreditoDebitos = servicioNotaCredito.findLikeCedula(buscarCedula, amb);

    }

    @Command
    @NotifyChange({"lstCreditoDebitos", "fechafin", "fechainicio"})
    public void buscarFechas() {
        consultarFacturaFecha();

    }

    private void consultarFacturaFecha() {
        lstCreditoDebitos = servicioNotaCredito.findBetweenFecha(fechainicio, fechafin, amb);
    }
    //GRAFICA POR UBICACION
    JFreeChart jfreechartMes;
    private byte[] graficoBarrasMes;
    String pathSalidaMes = "";
    private AImage reporteMes;

    public AMedia getFileContent() {
        return fileContent;
    }

    public void setFileContent(AMedia fileContent) {
        this.fileContent = fileContent;
    }

    public JFreeChart getJfreechartMes() {
        return jfreechartMes;
    }

    public void setJfreechartMes(JFreeChart jfreechartMes) {
        this.jfreechartMes = jfreechartMes;
    }

    public byte[] getGraficoBarrasMes() {
        return graficoBarrasMes;
    }

    public void setGraficoBarrasMes(byte[] graficoBarrasMes) {
        this.graficoBarrasMes = graficoBarrasMes;
    }

    public String getPathSalidaMes() {
        return pathSalidaMes;
    }

    public void setPathSalidaMes(String pathSalidaMes) {
        this.pathSalidaMes = pathSalidaMes;
    }

    public AImage getReporteMes() {
        return reporteMes;
    }

    public void setReporteMes(AImage reporteMes) {
        this.reporteMes = reporteMes;
    }

    public static String getPATH_BASE() {
        return PATH_BASE;
    }

    public static void setPATH_BASE(String PATH_BASE) {
        ListaNC1.PATH_BASE = PATH_BASE;
    }

    public Tipoambiente getAmb() {
        return amb;
    }

    public void setAmb(Tipoambiente amb) {
        this.amb = amb;
    }

    @Command
    @NotifyChange({"lstCreditoDebitos"})
    public void autorizarSRI(@BindingParam("valor") NotaCreditoDebito valor)
            throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        String folderGenerados = PATH_BASE + File.separator + amb.getAmGenerados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();
        String folderEnviarCliente = PATH_BASE + File.separator + amb.getAmEnviocliente()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();
        String folderFirmado = PATH_BASE + File.separator + amb.getAmFirmados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();

        String foldervoAutorizado = PATH_BASE + File.separator + amb.getAmAutorizados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();

        String folderNoAutorizados = PATH_BASE + File.separator + amb.getAmNoAutorizados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();

        /*EN EL CASO DE NO EXISTIR LOS DIRECTORIOS LOS CREA*/
        File folderGen = new File(folderGenerados);
        if (!folderGen.exists()) {
            folderGen.mkdirs();
        }
        File folderFirm = new File(folderFirmado);
        if (!folderFirm.exists()) {
            folderFirm.mkdirs();
        }

        File folderAu = new File(foldervoAutorizado);
        if (!folderAu.exists()) {
            folderAu.mkdirs();
        }

        File folderCliente = new File(folderEnviarCliente);
        if (!folderCliente.exists()) {
            folderCliente.mkdirs();
        }
        File folderNoAut = new File(folderNoAutorizados);
        if (!folderNoAut.exists()) {
            folderNoAut.mkdirs();
        }
        /*Ubicacion del archivo firmado para obtener la informacion*/

 /*PARA CREAR EL ARCHIVO XML FIRMADO*/
        String nombreArchivoXML = File.separator + "NCRE-"
                + valor.getCodestablecimiento()
                + valor.getPuntoemision()
                + valor.getFacNumeroText() + ".xml";


        /*RUTAS FINALES DE,LOS ARCHIVOS XML FIRMADOS Y AUTORIZADOS*/
        String pathArchivoFirmado = folderFirmado + nombreArchivoXML;
        String pathArchivoAutorizado = foldervoAutorizado + nombreArchivoXML;
        String pathArchivoNoAutorizado = folderNoAutorizados + nombreArchivoXML;
        String archivoEnvioCliente = "";

        File f = null;
        File fEnvio = null;
        byte[] datos = null;
        //tipoambiente tiene los parameteos para los directorios y la firma digital
        AutorizarDocumentos aut = new AutorizarDocumentos();
        /*Generamos el archivo XML de la factura*/
        String archivo = aut.generaXMLNotaCreditoDebito(valor, amb, folderGenerados, nombreArchivoXML, "04");

        /*amb.getAmClaveAccesoSri() es el la clave proporcionada por el SRI
        archivo es la ruta del archivo xml generado
        nomre del archivo a firmar*/
        try {
            XAdESBESSignature.firmar(archivo, nombreArchivoXML,
                    amb.getAmClaveAccesoSri(), amb, folderFirmado);
        } catch (Exception e) {
            Clients.showNotification("Verifique su firma electronica y su contraseña ", Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
            return;
        }
        f = new File(pathArchivoFirmado);

        datos = ArchivoUtils.ConvertirBytes(pathArchivoFirmado);
        //obtener la clave de acceso desde el archivo xml
        String claveAccesoComprobante = ArchivoUtils.obtenerValorXML(f, "/*/infoTributaria/claveAcceso");
        /*GUARDAMOS LA CLAVE DE ACCESO ANTES DE ENVIAR A AUTORIZAR*/
        valor.setFacClaveAcceso(claveAccesoComprobante);
        AutorizarDocumentos autorizarDocumentos = new AutorizarDocumentos();
        RespuestaSolicitud resSolicitud = autorizarDocumentos.validar(datos, amb);
        if (resSolicitud != null && resSolicitud.getComprobantes() != null) {
            // Autorizacion autorizacion = null;

            if (resSolicitud.getEstado().equals("RECIBIDA")) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Tipoambiente.class.getName()).log(Level.SEVERE, null, ex);
//                }
                try {

                    RespuestaComprobante resComprobante = autorizarDocumentos.autorizarComprobante(claveAccesoComprobante, amb);
                    for (Autorizacion autorizacion : resComprobante.getAutorizaciones().getAutorizacion()) {
                        FileOutputStream nuevo = null;

                        /*CREA EL ARCHIVO XML AUTORIZADO*/
                        System.out.println("pathArchivoNoAutorizado " + pathArchivoNoAutorizado);
                        nuevo = new FileOutputStream(pathArchivoNoAutorizado);
                        nuevo.write(autorizacion.getComprobante().getBytes());
                        if (!autorizacion.getEstado().equals("AUTORIZADO")) {

                            String texto = autorizacion.getMensajes().getMensaje().get(0).getMensaje();
                            String smsInfo = autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional();
                            nuevo.write(autorizacion.getMensajes().getMensaje().get(0).getMensaje().getBytes());
                            if (autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional() != null) {
                                nuevo.write(autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional().getBytes());
                            }

                            valor.setMensajesri(texto);
                            valor.setEstadosri(autorizacion.getEstado());

                            nuevo.flush();
                            servicioNotaCredito.modificar(valor);
                        } else {

                            valor.setFacClaveAutorizacion(claveAccesoComprobante);
                            valor.setEstadosri(autorizacion.getEstado());
                            valor.setFacFechaAutorizacion(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());

                            /*se agrega la la autorizacion, fecha de autorizacion y se firma nuevamente*/
                            archivoEnvioCliente = aut.generaXMLNotaCreditoDebito(valor, amb, folderGenerados, nombreArchivoXML, "04");
//                            XAdESBESSignature.firmar(archivoEnvioCliente,
//                                    nombreArchivoXML,
//                                    amb.getAmClaveAccesoSri(),
//                                    amb, foldervoAutorizado);

                            fEnvio = new File(archivoEnvioCliente);

                            System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
                            ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), valor.getFacNumero(), "FACT", amb);
//                            ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
                            /*GUARDA EL PATH PDF CREADO*/

                            servicioNotaCredito.modificar(valor);
                            /*envia el mail*/

                            String[] attachFiles = new String[2];
                            attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
                            attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
                            MailerClass mail = new MailerClass();
                            if (valor.getIdFactura().getIdCliente().getCliClave() == null) {
                                Cliente mod = valor.getIdFactura().getIdCliente();
                                mod.setCliClave(ArchivoUtils.generaraClaveTemporal());
                                servicioCliente.modificar(mod);
                            }
                            if (valor.getIdFactura().getIdCliente().getCliCorreo() != null) {
                                mail.sendMailSimple(valor.getIdFactura().getIdCliente().getCliCorreo(),
                                        attachFiles,
                                        "NOTA DE CREDITO ELECTRONICA",
                                        valor.getFacClaveAcceso(),
                                        valor.getFacNumeroText(),
                                        valor.getFacTotal(),
                                        valor.getIdFactura().getIdCliente().getCliNombre(), amb);

                            }
                        }

                    }
                } catch (RespuestaAutorizacionException ex) {
                    Logger.getLogger(ListaNC1.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                String smsInfo = resSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional();
                ArchivoUtils.FileCopy(pathArchivoFirmado, pathArchivoNoAutorizado);
                valor.setEstadosri(resSolicitud.getEstado());
                valor.setMensajesri(resSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje());

                servicioNotaCredito.modificar(valor);
            }
        } else {

            valor.setMensajesri(resSolicitud.getEstado());
            servicioNotaCredito.modificar(valor);
        }

    }

    @Command
    @NotifyChange({"lstCreditoDebitos"})
    public void reenviarSRI(@BindingParam("valor") NotaCreditoDebito valor)
            throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        String folderGenerados = PATH_BASE + File.separator + amb.getAmGenerados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();
        String folderEnviarCliente = PATH_BASE + File.separator + amb.getAmEnviocliente()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();
        String folderFirmado = PATH_BASE + File.separator + amb.getAmFirmados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();

        String foldervoAutorizado = PATH_BASE + File.separator + amb.getAmAutorizados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();

        String folderNoAutorizados = PATH_BASE + File.separator + amb.getAmNoAutorizados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();

        /*EN EL CASO DE NO EXISTIR LOS DIRECTORIOS LOS CREA*/
        File folderGen = new File(folderGenerados);
        if (!folderGen.exists()) {
            folderGen.mkdirs();
        }
        File folderFirm = new File(folderFirmado);
        if (!folderFirm.exists()) {
            folderFirm.mkdirs();
        }

        File folderAu = new File(foldervoAutorizado);
        if (!folderAu.exists()) {
            folderAu.mkdirs();
        }

        File folderCliente = new File(folderEnviarCliente);
        if (!folderCliente.exists()) {
            folderCliente.mkdirs();
        }
        File folderNoAut = new File(folderNoAutorizados);
        if (!folderNoAut.exists()) {
            folderNoAut.mkdirs();
        }
        /*Ubicacion del archivo firmado para obtener la informacion*/

 /*PARA CREAR EL ARCHIVO XML FIRMADO*/
        String nombreArchivoXML = File.separator + "GUIA-"
                + valor.getCodestablecimiento()
                + valor.getPuntoemision()
                + valor.getFacNumeroText() + ".xml";


        /*RUTAS FINALES DE,LOS ARCHIVOS XML FIRMADOS Y AUTORIZADOS*/
        String pathArchivoFirmado = folderFirmado + nombreArchivoXML;
        String pathArchivoAutorizado = foldervoAutorizado + nombreArchivoXML;
        String pathArchivoNoAutorizado = folderNoAutorizados + nombreArchivoXML;
        String archivoEnvioCliente = "";

        File f = null;
        File fEnvio = null;
        byte[] datos = null;
        //tipoambiente tiene los parameteos para los directorios y la firma digital
        AutorizarDocumentos aut = new AutorizarDocumentos();
        /*Generamos el archivo XML de la factura*/
        String archivo = aut.generaXMLNotaCreditoDebito(valor, amb, folderGenerados, nombreArchivoXML, "04");

        /*amb.getAmClaveAccesoSri() es el la clave proporcionada por el SRI
        archivo es la ruta del archivo xml generado
        nomre del archivo a firmar*/
        try {
            XAdESBESSignature.firmar(archivo, nombreArchivoXML,
                    amb.getAmClaveAccesoSri(), amb, folderFirmado);
        } catch (Exception e) {
            Clients.showNotification("Verifique su firma electronica y su contraseña ", Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
            return;
        }
        f = new File(pathArchivoFirmado);

        datos = ArchivoUtils.ConvertirBytes(pathArchivoFirmado);
        //obtener la clave de acceso desde el archivo xml
        String claveAccesoComprobante = ArchivoUtils.obtenerValorXML(f, "/*/infoTributaria/claveAcceso");
        /*GUARDAMOS LA CLAVE DE ACCESO ANTES DE ENVIAR A AUTORIZAR*/
        valor.setFacClaveAcceso(claveAccesoComprobante);
        AutorizarDocumentos autorizarDocumentos = new AutorizarDocumentos();
//        RespuestaSolicitud resSolicitud = autorizarDocumentos.validar(datos);
//        if (resSolicitud != null && resSolicitud.getComprobantes() != null) {
//            // Autorizacion autorizacion = null;
//
//            if (resSolicitud.getEstado().equals("RECIBIDA")) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tipoambiente.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            RespuestaComprobante resComprobante = autorizarDocumentos.autorizarComprobante(claveAccesoComprobante, amb);
            for (Autorizacion autorizacion : resComprobante.getAutorizaciones().getAutorizacion()) {
                FileOutputStream nuevo = null;

                /*CREA EL ARCHIVO XML AUTORIZADO*/
                System.out.println("pathArchivoNoAutorizado " + pathArchivoNoAutorizado);
                nuevo = new FileOutputStream(pathArchivoNoAutorizado);
                if (autorizacion.getComprobante() != null) {
                    nuevo.write(autorizacion.getComprobante().getBytes());
                }

                if (!autorizacion.getEstado().equals("AUTORIZADO")) {

                    String texto = autorizacion.getMensajes().getMensaje().get(0).getMensaje();
                    String smsInfo = autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional();
                    nuevo.write(autorizacion.getMensajes().getMensaje().get(0).getMensaje().getBytes());
                    if (autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional() != null) {
                        nuevo.write(autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional().getBytes());
                    }

                    valor.setMensajesri(texto);
                    valor.setMensajeInf(smsInfo);
                    nuevo.flush();
                } else {

                    valor.setFacClaveAutorizacion(claveAccesoComprobante);
                    valor.setEstadosri(autorizacion.getEstado());
                    valor.setFacFechaAutorizacion(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());

                    /*se agrega la la autorizacion, fecha de autorizacion y se firma nuevamente*/
                    archivoEnvioCliente = aut.generaXMLNotaCreditoDebito(valor, amb, folderGenerados, nombreArchivoXML, "04");

                    try {
                        XAdESBESSignature.firmar(archivoEnvioCliente,
                                nombreArchivoXML,
                                amb.getAmClaveAccesoSri(),
                                amb, foldervoAutorizado);
                    } catch (Exception e) {
                        Clients.showNotification("Verifique su firma electronica y su contraseña ", Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
                        return;
                    }
                    fEnvio = new File(archivoEnvioCliente);
                }

                System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
                ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), valor.getFacNumero(), "FACT", amb);
//                ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
                /*GUARDA EL PATH PDF CREADO*/

                servicioNotaCredito.modificar(valor);
                /*envia el mail*/

                String[] attachFiles = new String[2];
                attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
                attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
                MailerClass mail = new MailerClass();
                if (valor.getIdFactura().getIdCliente().getCliClave() == null) {
                    Cliente mod = valor.getIdFactura().getIdCliente();
                    mod.setCliClave(ArchivoUtils.generaraClaveTemporal());
                    servicioCliente.modificar(mod);
                }
                if (valor.getIdFactura().getIdCliente().getCliCorreo() != null) {
                    mail.sendMailSimple(valor.getIdFactura().getIdCliente().getCliCorreo(),
                            attachFiles,
                            "NOTA DE CREDITO ELECTRONICA",
                            valor.getFacClaveAcceso(),
                            valor.getFacNumeroText(),
                            valor.getFacTotal(),
                            valor.getIdFactura().getIdCliente().getCliNombre(), amb);

                }

            }
            consultarFacturaFecha();
        } catch (RespuestaAutorizacionException ex) {
            Logger.getLogger(ListaNC1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public String getBuscarCedula() {
        return buscarCedula;
    }

    public void setBuscarCedula(String buscarCedula) {
        this.buscarCedula = buscarCedula;
    }

    @Command
    public void reporteGeneral(@BindingParam("valor") NotaCreditoDebito valor) throws JRException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, NamingException {
        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            //  con = emf.unwrap(Connection.class);
            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            String reportPath = "";
//                con = ConexionReportes.Conexion.conexion();

//                    reportPath = reportFile + File.separator + "puntoventa.jasper";
            reportPath = reportFile + File.separator + "notacr.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("numfactura", valor.getFacNumero());
            parametros.put("tipoambiente", amb.getCodTipoambiente());
            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/venta/contenedorReporte.zul", null, map);
            window.doModal();
            con.close();

        } catch (Exception e) {
            System.out.println("Error en generar el reporte " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.close();
                System.out.println("cerro entity");
            }
        }

    }

    @Command
    public void cambiarEstadoFact(@BindingParam("valor") NotaCreditoDebito valor) throws JRException, IOException, NamingException, SQLException {
        try {
            final HashMap<String, NotaCreditoDebito> map = new HashMap<String, NotaCreditoDebito>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/modificar/estadonc.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }
}
