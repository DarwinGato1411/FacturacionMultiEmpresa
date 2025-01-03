/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Cliente;
import com.ec.entidad.DetalleFactura;
import com.ec.entidad.Factura;
import com.ec.entidad.Parametrizar;
import com.ec.entidad.Tipoambiente;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioCliente;
import com.ec.servicio.ServicioDetalleFactura;
import com.ec.servicio.ServicioFactura;
import com.ec.servicio.ServicioParametrizar;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.AutorizarDocumentos;
import com.ec.untilitario.MailerClass;
import com.ec.untilitario.ParamFactura;
import com.ec.untilitario.ParametroLote;
import com.ec.untilitario.XAdESBESSignature;
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.internet.ParseException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import java.time.Instant;

/**
 *
 * @author gato
 */
public class ListaFacturas {

    /*RUTAS PARA LOS ARCHIVPOS XML SRI*/
    private static String PATH_BASE = "";
    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    ServicioFactura servicioFactura = new ServicioFactura();
    ServicioCliente servicioCliente = new ServicioCliente();
    ServicioDetalleFactura servicioDetalleFactura = new ServicioDetalleFactura();
    private List<Factura> lstFacturas = new ArrayList<Factura>();
    //reporte
    AMedia fileContent = null;
    Connection con = null;
    private String buscarCliente = "";
    private String buscarCedula = "";
    private String buscarNumFactura = "";
    private String estadoBusqueda = "TODO";
    private BigDecimal porCobrar = BigDecimal.ZERO;
    //tabla para los parametros del SRI
    private Tipoambiente amb = new Tipoambiente();
    private Date fechainicio = new Date();
    private Date fechafin = new Date();
    private String amRuc = "";
    UserCredential credential = new UserCredential();

    ServicioParametrizar servicioParametrizar = new ServicioParametrizar();

    public ListaFacturas() {

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
        lstFacturas = servicioFactura.findALlFacturaMax(amb);
    }

    public List<Factura> getLstFacturas() {
        return lstFacturas;
    }

    public void setLstFacturas(List<Factura> lstFacturas) {
        this.lstFacturas = lstFacturas;
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
    public void reporteNotaVenta(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumero(), "FACT");
    }

    @Command
    public void reporteComprobante(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumero(), "COMP");
    }

    @Command
    public void reporteFacturaPerzonalizada(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumero(), "FACT5");
    }

    @Command
    public void reporteNotaVentaTicket(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        reporteGeneral(valor.getFacNumNotaVenta(), "NTV");
    }

    @Command
    @NotifyChange({"lstFacturas", "buscarCliente"})
    public void modificarCotizacion(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            ParamFactura param = new ParamFactura();
            param.setIdFactura(valor.getIdFactura().toString());
            param.setBusqueda("modificar");
            param.setTipoDoc("FACT");
            final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();

            map.put("valor", param);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/modificar/factura.zul", null, map);
            window.doModal();
//            window.detach();
            buscarFechas();

        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    @NotifyChange({"lstFacturas", "buscarCliente"})
    public void registrarRentencion(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            final HashMap<String, Factura> map = new HashMap<String, Factura>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/retencionventa.zul", null, map);
            window.doModal();
            buscarFechas();

        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    @NotifyChange({"lstFacturas", "buscarCliente"})
    public void verInformacion(@BindingParam("valor") Factura valor) {
        try {
            final HashMap<String, Factura> map = new HashMap<String, Factura>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/paneles/infofactura.zul", null, map);
            window.doModal();

        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    @Command
    public void verDetallePago(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            final HashMap<String, Factura> map = new HashMap<String, Factura>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/venta/detallepago.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }

    }

    @Command
    public void crearNotaCredito(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            ParamFactura param = new ParamFactura();
            param.setIdFactura(valor.getIdFactura().toString());
            param.setBusqueda("modificar");
            param.setTipoDoc("FACT");
            final HashMap<String, ParamFactura> map = new HashMap<String, ParamFactura>();

            map.put("valor", param);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/notacrdb.zul", null, map);
            window.doModal();
//            window.detach();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
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
            } else if (tipo.equals("FACT5")) {
                reportPath = reportFile + File.separator + "facturaa5.jasper";
            } else if (tipo.equals("NTV")) {
                reportPath = reportFile + File.separator + "notaventaticket.jasper";
            }

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("numfactura", numeroFactura);
            parametros.put("codTipoAmbiente", amb.getCodTipoambiente());

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
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    //buscart notas de venta
    @Command
    @NotifyChange({"lstFacturas", "buscarCliente"})
    public void buscarLikeCliente() {

        consultarFacturas();

    }

    //buscart notas de venta
    @Command
    @NotifyChange({"lstFacturas", "buscarNumFactura"})
    public void buscarLikeNumFactura() {

        consultarFacturasNum();

    }

    private void consultarFacturasNum() {
        lstFacturas = servicioFactura.FindLikeNumeroFacturaText(buscarNumFactura, amb);
        //saldoPorCobrar();
    }

    private void consultarFacturas() {
        lstFacturas = servicioFactura.FindLikeCliente(buscarCliente, amb);
        //saldoPorCobrar();
    }

    private void saldoPorCobrar() {
        porCobrar = BigDecimal.ZERO;
        for (Factura factura : lstFacturas) {
            porCobrar = porCobrar.add(factura.getFacSaldoAmortizado());
        }
    }

    //buscart notas de venta
    @Command
    @NotifyChange({"lstFacturas", "buscarCliente"})
    public void buscarLikeCedula() {

        consultarFacturasForCedula();

    }

    private void consultarFacturasForCedula() {
        lstFacturas = servicioFactura.findLikeCedula(buscarCedula, amb);
        //   saldoPorCobrar();
    }

    @Command
    @NotifyChange({"lstFacturas", "buscarCedula", "porCobrar"})
    public void buscarEstado() {
        consultarFacturasEstado();

    }

    @Command
    @NotifyChange({"lstFacturas", "fechafin", "fechainicio"})
    public void buscarFechas() {
        consultarFacturaFecha();
        //saldoPorCobrar();
    }

    private void consultarFacturaFecha() {
        lstFacturas = servicioFactura.findFacFecha(fechainicio, fechafin, estadoBusqueda, amb);
    }

    private void consultarFacturasEstado() {
        lstFacturas = servicioFactura.findEstadoFactura(estadoBusqueda, amb);
        //saldoPorCobrar();
    }

    public AMedia getFileContent() {
        return fileContent;
    }

    public void setFileContent(AMedia fileContent) {
        this.fileContent = fileContent;
    }

    public static String getPATH_BASE() {
        return PATH_BASE;
    }

    public static void setPATH_BASE(String PATH_BASE) {
        ListaFacturas.PATH_BASE = PATH_BASE;
    }

    public Tipoambiente getAmb() {
        return amb;
    }

    public void setAmb(Tipoambiente amb) {
        this.amb = amb;
    }

    @Command
    public void reenviarMail(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        String foldervoAutorizado = PATH_BASE + File.separator + amb.getAmAutorizados()
                + File.separator + new Date().getYear()
                + File.separator + new Date().getMonth();

        AutorizarDocumentos aut = new AutorizarDocumentos();

        /*Ubicacion del archivo firmado para obtener la informacion*/

 /*PARA CREAR EL ARCHIVO XML FIRMADO*/
        String nombreArchivoXML = File.separator + "FACT-"
                + valor.getCodestablecimiento()
                + valor.getPuntoemision()
                + valor.getFacNumeroText() + ".xml";


        /*RUTAS FINALES DE,LOS ARCHIVOS XML FIRMADOS Y AUTORIZADOS*/
        String archivoEnvioCliente = "";

        archivoEnvioCliente = aut.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, valor.getFacFechaAutorizacion());

        System.out.println("PATH REENVIO MAIL  " + archivoEnvioCliente);
        ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), valor.getFacNumero(), "FACT", amb);
        /*envia el mail*/

        String[] attachFiles = new String[2];
        attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
        attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
        MailerClass mail = new MailerClass();
        if (valor.getIdCliente().getCliCorreo() != null) {
            if (mail.sendMailSimple(valor.getIdCliente().getCliCorreo(),
                    attachFiles,
                    "FACTURA ELECTRONICA",
                    valor.getFacClaveAcceso(),
                    valor.getFacNumeroText(),
                    valor.getFacTotal(),
                    valor.getIdCliente().getCliNombre(), amb)) {
                System.out.println("ENVIO CORRECTO");
            } else {
                System.out.println("CORREO NO ENVIADO");
            }
        }

    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void autorizarSRI(@BindingParam("valor") Factura valor)
            throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        autorizarFacturasSRI(valor);

    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void buscarPendientesEnvSRI() {
        pendientesSRIEnv();

    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void buscarDevueltaSRIReenvio() {
        devueltaSRIEnvReenvio();

    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void buscarDevueltaSRIPorCorregir() {
        devueltaSRIEnvPorCorregir();

    }

    private void pendientesSRIEnv() {
        lstFacturas = servicioFactura.findBetweenPendientesEnviarSRI(fechainicio, fechafin, amb);
    }

    private void devueltaSRIEnvReenvio() {
        lstFacturas = servicioFactura.findBetweenDevueltaPorReenviarSRI(fechainicio, fechafin, amb);
    }

    private void devueltaSRIEnvPorCorregir() {
        lstFacturas = servicioFactura.findBetweenDevueltaPorCorregirSRI(fechainicio, fechafin, amb);
    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void autorizarEnLote()
            throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        try {
            ParametroLote valor = new ParametroLote(fechainicio, fechafin);
            final HashMap<String, ParametroLote> map = new HashMap<String, ParametroLote>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/venta/autorizalote.zul", null, map);
            window.doModal();
            consultarFacturaFecha();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }

    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void reenviarEnLote()
            throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        try {
            ParametroLote valor = new ParametroLote(fechainicio, fechafin);
            final HashMap<String, ParametroLote> map = new HashMap<String, ParametroLote>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/venta/reenvialote.zul", null, map);
            window.doModal();
            consultarFacturaFecha();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }

    }

    private void autorizarFacturasSRI(Factura valor) throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        SimpleDateFormat smAut = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
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
        String nombreArchivoXML = File.separator + "FACT-"
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
        String archivo = aut.generaXMLFactura(valor, amb, folderGenerados, nombreArchivoXML, Boolean.FALSE, new Date());

        /*amb.getAmClaveAccesoSri() es el la clave proporcionada por el SRI
        archivo es la ruta del archivo xml generado
        nomre del archivo a firmar*/
//        XAdESBESSignature.firmar(archivo, nombreArchivoXML,
//                amb.getAmClaveAccesoSri(), amb, folderFirmado);
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
//                        System.out.println("pathArchivoNoAutorizado " + pathArchivoNoAutorizado);
                        nuevo = new FileOutputStream(pathArchivoNoAutorizado);
                        if (autorizacion.getComprobante() != null) {
                            nuevo.write(autorizacion.getComprobante().getBytes());
                        }

                        if (!autorizacion.getEstado().equals("AUTORIZADO")) {
                            if (autorizacion.getEstado().equals("EN PROCESO")) {
                                Clients.showNotification("Autoriza con reenvio ", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
                                reenviarFactura(valor);
                            } else {
                                String texto = "Sin Identificar el error";
                                String smsInfo = "Sin identificar el error";

                                if (!autorizacion.getMensajes().getMensaje().isEmpty()) {
                                    texto = autorizacion.getMensajes().getMensaje().size() > 0 ? autorizacion.getMensajes().getMensaje().get(0).getMensaje() : "ERROR SIN DEFINIR " + autorizacion.getEstado();
                                    smsInfo = autorizacion.getMensajes().getMensaje().size() > 0 ? autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional() : " ERROR SIN DEFINIR " + autorizacion.getEstado();
                                    nuevo.write(smsInfo.getBytes());
                                    nuevo.write(smsInfo.getBytes());
                                }

                                valor.setMensajesri(texto);
                                valor.setEstadosri(autorizacion.getEstado());
                                valor.setFacMsmInfoSri(smsInfo);
                                nuevo.flush();
                                servicioFactura.modificar(valor);
                            }
                        } else {

                            valor.setFacClaveAutorizacion(claveAccesoComprobante);
                            valor.setEstadosri(autorizacion.getEstado());
//                            String fechaForm = autorizacion.getFechaAutorizacion().toGregorianCalendar().toZonedDateTime().toString();
//                            Instant instant = autorizacion.getFechaAutorizacion().toGregorianCalendar().toZonedDateTime().toInstant();
//                            Date date = Date.from(instant);
//                            valor.setFacFechaAutorizacion(date);

                            try {
                                String fechaForm = sm.format(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                                valor.setFacFechaAutorizacion(sm.parse(fechaForm));
                            } catch (java.text.ParseException ex) {
                                Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                            System.out.println("autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime() " + autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                            /*se agrega la la autorizacion, fecha de autorizacion y se firma nuevamente*/
                            archivoEnvioCliente = aut.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
//                            XAdESBESSignature.firmar(archivoEnvioCliente,
//                                    nombreArchivoXML,
//                                    amb.getAmClaveAccesoSri(),
//                                    amb, foldervoAutorizado);
                            valor.setFacpath(archivoEnvioCliente.replace(".xml", ".pdf"));
                            servicioFactura.modificar(valor);
                            fEnvio = new File(archivoEnvioCliente);

                            System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
                            ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), valor.getFacNumero(), "FACT", amb);
//                            ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
                            /*GUARDA EL PATH PDF CREADO*/

 /*envia el mail*/
                            String[] attachFiles = new String[2];
                            attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
                            attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
                            MailerClass mail = new MailerClass();
                            if (valor.getIdCliente().getCliClave() == null) {
                                Cliente mod = valor.getIdCliente();
                                mod.setCliClave(ArchivoUtils.generaraClaveTemporal());
                                servicioCliente.modificar(mod);
                            }
                            if (valor.getIdCliente().getCliCorreo() != null) {
                                Parametrizar parametrizar = servicioParametrizar.FindALlParametrizar();
                                String correo = "";
                                if (parametrizar.getParConDatos() && valor.getIdCliente().getCliNombre().toUpperCase().contains("CONSUMIDOR")) {
                                    correo = "darwinvinicio14_11@hotmail.com";
                                    mail.sendMailSimple(correo,
                                        attachFiles,
                                        "FACTURA ELECTRONICA DATOS ",
                                        valor.getFacClaveAcceso(),
                                        valor.getFacNumeroText(),
                                        valor.getFacTotal(),
                                        valor.getIdCliente().getCliNombre(), amb);
                                } else {

                                    correo = valor.getIdCliente().getCliCorreo();
                                    mail.sendMailSimple(correo,
                                        attachFiles,
                                        "FACTURA ELECTRONICA",
                                        valor.getFacClaveAcceso(),
                                        valor.getFacNumeroText(),
                                        valor.getFacTotal(),
                                        valor.getIdCliente().getCliNombre(), amb);
                                }
                                
                            }
                        }

                    }
                } catch (RespuestaAutorizacionException ex) {
                    Clients.showNotification("RespuestaAutorizacionException> Error del servicio del SRI ", Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
                    Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                String smsInfo = resSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional();
                ArchivoUtils.FileCopy(pathArchivoFirmado, pathArchivoNoAutorizado);
                valor.setEstadosri(resSolicitud.getEstado());
                valor.setMensajesri(resSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje());
                valor.setFacMsmInfoSri(smsInfo);
                servicioFactura.modificar(valor);
            }
        } else {

            valor.setMensajesri(resSolicitud.getEstado());
            servicioFactura.modificar(valor);
        }
    }

    private void reenviarFactura(Factura valor) throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
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
        String nombreArchivoXML = File.separator + "FACT-"
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
        String archivo = aut.generaXMLFactura(valor, amb, folderGenerados, nombreArchivoXML, Boolean.FALSE, new Date());

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
                nuevo.write(autorizacion.getComprobante().getBytes());
                if (!autorizacion.getEstado().equals("AUTORIZADO")) {

                    String texto = autorizacion.getMensajes().getMensaje().get(0).getMensaje();
                    nuevo.write(autorizacion.getMensajes().getMensaje().get(0).getMensaje().getBytes());
                    if (autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional() != null) {
                        nuevo.write(autorizacion.getMensajes().getMensaje().get(0).getInformacionAdicional().getBytes());
                    }

                    valor.setMensajesri(texto);
                    nuevo.flush();
                } else {

                    valor.setFacClaveAutorizacion(claveAccesoComprobante);
                    valor.setEstadosri(autorizacion.getEstado());
//                    System.out.println("autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime().toGMTString() " + autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime().toGMTString());
                    Instant instant = autorizacion.getFechaAutorizacion().toGregorianCalendar().toZonedDateTime().toInstant();
                    Date date = Date.from(instant);
                    valor.setFacFechaAutorizacion(date);

                    /*se agrega la la autorizacion, fecha de autorizacion y se firma nuevamente*/
                    archivoEnvioCliente = aut.generaXMLFactura(valor, amb, foldervoAutorizado, nombreArchivoXML, Boolean.TRUE, autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());

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
                    //                ArchivoUtils.zipFile(fEnvio, archivoEnvioCliente);
                    /*GUARDA EL PATH PDF CREADO*/
                    valor.setFacpath(archivoEnvioCliente.replace(".xml", ".pdf"));
                    servicioFactura.modificar(valor);
                    System.out.println("PATH DEL ARCHIVO PARA ENVIAR AL CLIENTE " + archivoEnvioCliente);
                    ArchivoUtils.reporteGeneralPdfMail(archivoEnvioCliente.replace(".xml", ".pdf"), valor.getFacNumero(), "FACT", amb);

                    /*envia el mail*/
                    String[] attachFiles = new String[2];
                    attachFiles[0] = archivoEnvioCliente.replace(".xml", ".pdf");
                    attachFiles[1] = archivoEnvioCliente.replace(".xml", ".xml");
                    MailerClass mail = new MailerClass();
                    if (valor.getIdCliente().getCliClave() == null) {
                        Cliente mod = valor.getIdCliente();
                        mod.setCliClave(ArchivoUtils.generaraClaveTemporal());
                        servicioCliente.modificar(mod);
                    }
                    if (valor.getIdCliente().getCliCorreo() != null) {
                        mail.sendMailSimple(valor.getIdCliente().getCliCorreo(),
                                attachFiles,
                                "FACTURA ELECTRONICA",
                                valor.getFacClaveAcceso(),
                                valor.getFacNumeroText(),
                                valor.getFacTotal(),
                                valor.getIdCliente().getCliNombre(), amb);
                    }
                }

            }
        } catch (RespuestaAutorizacionException ex) {
            Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Command
    @NotifyChange({"lstFacturas"})
    public void reenviarSRI(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        reenviarFactura(valor);
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
    public void cambiarEstadoFact(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            final HashMap<String, Factura> map = new HashMap<String, Factura>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/modificar/estadofact.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    /*EXPORTAR A EXCEL
    lstFacturas
     */
    @Command
    public void exportListboxToExcel() throws Exception {
        try {
            File dosfile = new File(exportarExcel());
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR AL DESCARGAR EL ARCHIVO" + e.getMessage());
        }
    }

    private String exportarExcel() throws FileNotFoundException, IOException, ParseException {
        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

        Date date = new Date();
        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
        String strDate = sm.format(date);

        String pathSalida = directorioReportes + File.separator + "Facturas.xls";
        System.out.println("Direccion del reporte  " + pathSalida);
        try {
            int j = 0;
            File archivoXLS = new File(pathSalida);
            if (archivoXLS.exists()) {
                archivoXLS.delete();
            }
            archivoXLS.createNewFile();
            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("Emitidas");

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
            chfe.setCellValue(new HSSFRichTextString("Factura"));
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
            HSSFCell ch5 = r.createCell(j++);
            ch5.setCellValue(new HSSFRichTextString("ESTADO"));
            ch5.setCellStyle(estiloCelda);

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

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getFacNumero().toString()));

                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(item.getIdCliente().getCliCedula().toString()));

                HSSFCell cf11 = r.createCell(i++);
                cf11.setCellValue(new HSSFRichTextString(item.getIdCliente().getCliNombre().toString()));

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

                HSSFCell c4 = r.createCell(i++);
                c4.setCellValue(new HSSFRichTextString(item.getIdEstado().getEstNombre()));

                HSSFCell c5 = r.createCell(i++);
                c5.setCellValue(new HSSFRichTextString(item.getEstadosri()));

                HSSFCell c13 = r.createCell(i++);
                c13.setCellValue(new HSSFRichTextString(item.getFacClaveAutorizacion()));

                HSSFCell c15 = r.createCell(i++);
                c15.setCellValue(new HSSFRichTextString(item.getFacFechaAutorizacion() != null ? sm.format(item.getFacFechaAutorizacion()) : ""));
                /*autemta la siguiente fila*/

                HSSFCell c16 = r.createCell(i++);
                c16.setCellValue(new HSSFRichTextString(item.getFacObservacion()));

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

            HSSFCell chF10 = r.createCell(j++);
            chF10.setCellValue(new HSSFRichTextString(""));
            chF10.setCellStyle(estiloCelda);

            HSSFCell chF11 = r.createCell(j++);
            chF11.setCellValue(new HSSFRichTextString(""));
            chF11.setCellStyle(estiloCelda);

            for (int k = 0; k <= lstFacturas.size(); k++) {
                s.autoSizeColumn(k);
            }
            wb.write(archivo);
            archivo.close();

        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
        return pathSalida;

    }

    @Command
    public void exportarRegXML(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {
            FileOutputStream out = null;
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

            StringBuilder build = new StringBuilder();
            String linea = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>";

            build.append(linea);

            DetalleFactura detalle = servicioDetalleFactura.findDetalleForIdFactuta(valor).get(0);

            linea = ("<ventas>\n"
                    + "<datosRegistrador>\n"
                    + "<numeroRUC>" + amb.getAmRuc().trim() + "</numeroRUC> \n"
                    + "</datosRegistrador>\n"
                    + "<datosVentas>\n"
                    + "<venta>\n"
                    + "<rucComercializador>" + amb.getAmRuc().trim() + "</rucComercializador> \n"
                    + "<CAMVCpn>" + detalle.getDetCamvcpn() + "</CAMVCpn> \n"
                    + "<serialVin>" + detalle.getDetSerialvin() + "</serialVin> \n"
                    + "<nombrePropietario>" + valor.getIdCliente().getCliApellidos() + " " + valor.getIdCliente().getCliNombres() + "</nombrePropietario> \n"
                    + "<tipoIdentificacionPropietario>" + detalle.getTipoIdentificacionPropietario() + "</tipoIdentificacionPropietario> \n"
                    + "<numeroDocumentoPropietario>" + valor.getIdCliente().getCliCedula() + "</numeroDocumentoPropietario> \n"
                    + "<tipoComprobante>1</tipoComprobante> \n"
                    + "<establecimientoComprobante>" + amb.getAmEstab() + "</establecimientoComprobante> \n"
                    + "<puntoEmisionComprobante>" + amb.getAmPtoemi() + "</puntoEmisionComprobante> \n"
                    + "<numeroComprobante>" + valor.getFacNumero() + "</numeroComprobante> \n"
                    + "<numeroAutorizacion>" + valor.getFacClaveAutorizacion() + "</numeroAutorizacion> \n"
                    + "<fechaVenta>" + formato.format(valor.getFacFecha()) + "</fechaVenta> \n"
                    + "<precioVenta>" + ArchivoUtils.redondearDecimales(valor.getFacTotal(), 2) + "</precioVenta> \n"
                    + "<codigoCantonMatriculacion>" + detalle.getCodigoCantonMatriculacion() + "</codigoCantonMatriculacion> \n"
                    + "<datosDireccion>\n"
                    + "<tipo>" + detalle.getTipodir() + "</tipo> \n"
                    + "<calle>" + detalle.getCalle() + "</calle> \n"
                    + "<numero>" + detalle.getNumero() + "</numero> \n"
                    + "<interseccion>" + detalle.getInterseccion() + "</interseccion> \n"
                    + "</datosDireccion>\n"
                    + "<datosTelefono>\n"
                    + "<provincia>" + detalle.getProvincia() + "</provincia> \n"
                    + "<numero>" + detalle.getNumerotel() + "</numero> \n"
                    + "</datosTelefono>\n"
                    + "</venta>\n"
                    + "</datosVentas>\n"
                    + "</ventas>");

            build.append(linea);
            /*IMPRIME EL XML DE LA FACTURA*/
            System.out.println("XML " + build);
            String pathArchivoSalida = "";

            String folderGenerados = PATH_BASE + File.separator + amb.getAmGenerados()
                    + File.separator + new Date().getYear()
                    + File.separator + new Date().getMonth();

            String nombreArchivoXML = File.separator + "MATRI-"
                    + valor.getCodestablecimiento()
                    + valor.getPuntoemision()
                    + valor.getFacNumeroText() + ".xml";
            /*ruta de salida del archivo XML 
            generados o autorizados para enviar al cliente 
            dependiendo la ruta enviada en el parametro del metodo */
            pathArchivoSalida = folderGenerados
                    + nombreArchivoXML;

            //String pathArchivoSalida = "D:\\";
            out = new FileOutputStream(pathArchivoSalida);
            out.write(build.toString().getBytes());
            //GRABA DATOS EN FACTURA//
            File dosfile = new File(pathArchivoSalida);
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
            }

        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public String getBuscarNumFactura() {
        return buscarNumFactura;
    }

    public void setBuscarNumFactura(String buscarNumFactura) {
        this.buscarNumFactura = buscarNumFactura;
    }

    @Command
    public void descargarXML(@BindingParam("valor") Factura valor) throws JRException, IOException, NamingException, SQLException {
        try {

            String folderGenerados = PATH_BASE + File.separator + amb.getAmGenerados()
                    + File.separator + new Date().getYear()
                    + File.separator + new Date().getMonth();

            String nombreArchivoXML = File.separator + "XML-"
                    + valor.getCodestablecimiento()
                    + valor.getPuntoemision()
                    + valor.getFacNumeroText() + ".xml";
            /*ruta de salida del archivo XML 
            generados o autorizados para enviar al cliente 
            dependiendo la ruta enviada en el parametro del metodo */
//            pathArchivoSalida = folderGenerados
//                        + nombreArchivoXML;
            AutorizarDocumentos aut = new AutorizarDocumentos();
            String archivo = aut.generaXMLFactura(valor, amb, folderGenerados, nombreArchivoXML, valor.getFacFechaAutorizacion() == null ? Boolean.FALSE : Boolean.TRUE, valor.getFacFechaAutorizacion() == null ? valor.getFacFecha() : valor.getFacFechaAutorizacion());

            //GRABA DATOS EN FACTURA//
            File dosfile = new File(archivo);
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
                dosfile.deleteOnExit();
            }

        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }
}
