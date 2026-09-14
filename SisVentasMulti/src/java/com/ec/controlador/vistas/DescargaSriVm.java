package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.sri.SriComprobanteRecibido;
import com.ec.entidad.sri.SriDescargaLote;
import com.ec.entidad.sri.SriTipoDocumento;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioSriDescarga;
import com.ec.servicio.ServicioSriEstructura;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.AutorizarDocumentos;
import com.ec.untilitario.ClienteSriComprobantesRecibidos;
import com.ec.untilitario.ResultadoDescargaSri;
import com.ec.untilitario.RidePdfDesdeXml;
import com.ec.untilitario.SriTxtRecibidosParser;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class DescargaSriVm {

    private static final Logger LOG = Logger.getLogger(DescargaSriVm.class.getName());
    private final ServicioSriEstructura servicioEstructura = new ServicioSriEstructura();
    private final ServicioSriDescarga servicio = new ServicioSriDescarga();
    private final ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    private final SriTxtRecibidosParser parser = new SriTxtRecibidosParser();
    private final AutorizarDocumentos autorizarDocumentos = new AutorizarDocumentos();

    private UserCredential credential;
    private String codigoTipo = "01";
    private SriTipoDocumento tipoDocumento;
    private Date inicio;
    private Date fin;
    private String mensajeEstado = "";
    private List<SriComprobanteRecibido> lista = new ArrayList<SriComprobanteRecibido>();
    private Tipoambiente ambiente;

    @Init
    public void init(@QueryParam("tipo") String tipo) {
        servicioEstructura.asegurarTablas();
        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        if (tipo != null && tipo.trim().length() == 2) {
            codigoTipo = tipo.trim();
        }
        tipoDocumento = servicio.findTipo(codigoTipo);
        if (tipoDocumento == null) {
            codigoTipo = "01";
            tipoDocumento = servicio.findTipo(codigoTipo);
        }
        ambiente = ambienteDelUsuario();
        Calendar cal = Calendar.getInstance();
        fin = ArchivoUtils.recuperarFecha(cal.getTime(), "fin");
        cal.set(Calendar.DAY_OF_MONTH, 1);
        inicio = ArchivoUtils.recuperarFecha(cal.getTime(), "inicio");
        buscar();
    }

    @Command
    @NotifyChange({"lista", "mensajeEstado"})
    public void buscar() {
        lista = servicio.findPorTipoYFecha(codigoTipo, inicio, fin, ambiente);
        mensajeEstado = lista.size() + " comprobantes de " + nombreTipo() + " en el período.";
    }

    @Command
    @NotifyChange({"lista", "mensajeEstado"})
    public void descargarSri() {
        ambiente = ambienteDelUsuario();
        if (ambiente == null) {
            Clients.showNotification("No hay ambiente SRI para el usuario logueado.",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000, true);
            return;
        }
        String usuario = ambiente.getAmUsuariosri();
        if (usuario == null || usuario.trim().isEmpty()) {
            usuario = ambiente.getAmRuc();
        }
        if (tipoDocumento == null) {
            Clients.showNotification("No está definido el tipo de documento.",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000, true);
            return;
        }
        if (inicio == null || fin == null) {
            Clients.showNotification("Indique las fechas Desde y Hasta.",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000, true);
            return;
        }
        if (fin.before(inicio)) {
            Clients.showNotification("La fecha Hasta no puede ser menor que Desde.",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000, true);
            return;
        }
        String clave = ambiente.getAmClaveSriLinea();
        ClienteSriComprobantesRecibidos cliente = new ClienteSriComprobantesRecibidos();
        ResultadoDescargaSri resultado = cliente.descargar(usuario, clave, tipoDocumento, inicio, fin);
        if (!resultado.isOk()) {
            mensajeEstado = resultado.getMensaje();
            Clients.showNotification(resultado.getMensaje(),
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 7000, true);
            return;
        }
        persistirTxt(resultado.getContenidoTxt(), "PORTAL", resultado.getMensaje());
    }

    @Command
    @NotifyChange({"lista", "mensajeEstado", "inicio", "fin"})
    public void cargarTxt() {
        org.zkoss.util.media.Media media = Fileupload.get();
        if (media == null) {
            return;
        }
        String nombre = media.getName() == null ? "" : media.getName().toLowerCase();
        String formato = media.getFormat() == null ? "" : media.getFormat().toLowerCase();
        boolean esTxt = nombre.endsWith(".txt") || nombre.contains("recibidos")
                || "txt".equals(formato) || formato.contains("text") || formato.contains("plain");
        if (!esTxt && nombre.contains(".")) {
            Clients.showNotification("Seleccione un archivo .txt del SRI (Recibidos).",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000, true);
            return;
        }
        String contenido = leerTxt(media);
        persistirTxt(contenido, "TXT", "Archivo " + media.getName() + " importado.");
    }

    private String leerTxt(org.zkoss.util.media.Media media) {
        byte[] datos = null;
        try {
            if (media.inMemory()) {
                datos = media.getByteData();
            }
        } catch (Exception ignored) {
        }
        if (datos == null) {
            try {
                return media.getStringData();
            } catch (Exception e) {
                try {
                    datos = media.getByteData();
                } catch (Exception e2) {
                    return "";
                }
            }
        }
        if (datos == null) {
            return "";
        }
        String utf8 = new String(datos, Charset.forName("UTF-8"));
        if (utf8.indexOf('\ufffd') >= 0) {
            return new String(datos, Charset.forName("ISO-8859-1"));
        }
        return utf8;
    }

    @Command
    public void excelTxt() {
        if (lista.isEmpty()) {
            Clients.showNotification("No hay datos para exportar. Consulte el rango primero.",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 4000, true);
            return;
        }
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("TXT");
            HSSFCellStyle head = estiloCabecera(wb);
            String[] cols = {
                "Emisión", "Serie", "RUC emisor", "Razón social", "Total",
                "Clave de acceso", "Autorización", "Ident. receptor", "Tipo",
                "Clasificación", "XML", "Ruta XML"
            };
            HSSFRow r0 = s.createRow(0);
            for (int i = 0; i < cols.length; i++) {
                celda(r0, i, cols[i], head);
            }
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            int row = 1;
            for (SriComprobanteRecibido item : lista) {
                HSSFRow r = s.createRow(row++);
                int c = 0;
                celda(r, c++, item.getScrFechaEmision() == null ? "" : fmt.format(item.getScrFechaEmision()), null);
                celda(r, c++, nvl(item.getScrSerie()), null);
                celda(r, c++, nvl(item.getScrRucEmisor()), null);
                celda(r, c++, nvl(item.getScrRazonSocial()), null);
                celda(r, c++, nvl(item.getScrTotal()), null);
                celda(r, c++, nvl(item.getScrClaveAcceso()), null);
                celda(r, c++, nvl(item.getScrAutorizacion()), null);
                celda(r, c++, nvl(item.getScrIdentificacionReceptor()), null);
                celda(r, c++, nvl(item.getScrTipoComprobante()), null);
                celda(r, c++, nvl(item.getScrEstadoClasificacion()), null);
                celda(r, c++, Boolean.TRUE.equals(item.getScrXmlDescargado()) ? "SI" : "NO", null);
                celda(r, c++, nvl(item.getScrRutaXml()), null);
            }
            for (int i = 0; i < cols.length; i++) {
                s.autoSizeColumn(i);
            }
            descargarExcel(wb, "sri_" + nombreTipoCorto() + "_txt.xls");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Excel TXT", e);
            Clients.showNotification("No se pudo generar el Excel: " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 5000, true);
        }
    }

    @Command
    public void excelXml() {
        if (lista.isEmpty()) {
            Clients.showNotification("No hay datos para exportar. Consulte el rango primero.",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 4000, true);
            return;
        }
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("Detalle XML");
            HSSFCellStyle head = estiloCabecera(wb);
            String[] cols = RidePdfDesdeXml.encabezadoExcelDetallado();
            HSSFRow r0 = s.createRow(0);
            for (int i = 0; i < cols.length; i++) {
                celda(r0, i, cols[i], head);
            }
            int row = 1;
            int conXml = 0;
            int sinXml = 0;
            for (SriComprobanteRecibido item : lista) {
                File f = archivoXml(item);
                if (f == null) {
                    sinXml++;
                    continue;
                }
                List<String[]> filas = RidePdfDesdeXml.filasExcelDetallado(RidePdfDesdeXml.leerXml(f));
                for (int i = 0; i < filas.size(); i++) {
                    HSSFRow r = s.createRow(row++);
                    String[] vals = filas.get(i);
                    for (int c = 0; c < vals.length; c++) {
                        celda(r, c, vals[c], null);
                    }
                }
                conXml++;
            }
            for (int i = 0; i < cols.length; i++) {
                s.autoSizeColumn(i);
            }
            if (conXml == 0) {
                Clients.showNotification("Ningún comprobante de la lista tiene XML. Use Traer XML primero.",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000, true);
                return;
            }
            descargarExcel(wb, "sri_" + nombreTipoCorto() + "_xml.xls");
            if (sinXml > 0) {
                Clients.showNotification("Excel detallado generado. Sin XML: " + sinXml + " (no se incluyeron).",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000, true);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Excel XML", e);
            Clients.showNotification("No se pudo generar el Excel detallado: " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 5000, true);
        }
    }

    @Command
    public void borrarListado() {
        if (lista.isEmpty()) {
            Clients.showNotification("No hay comprobantes listados para borrar.",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
            return;
        }
        final int n = lista.size();
        Messagebox.show("Se eliminarán " + n + " comprobantes de " + nombreTipo()
                + " en el rango Desde-Hasta. También se borrarán XML/PDF guardados. ¿Continuar?",
                "Confirmar borrado",
                Messagebox.YES | Messagebox.NO,
                Messagebox.EXCLAMATION,
                new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        if (!Messagebox.ON_YES.equals(event.getName())) {
                            return;
                        }
                        int bor = servicio.eliminarPorTipoYFecha(codigoTipo, inicio, fin, ambiente);
                        buscar();
                        mensajeEstado = "Se eliminaron " + bor + " comprobantes de " + nombreTipo() + ".";
                        BindUtils.postNotifyChange(null, null, DescargaSriVm.this, "lista");
                        BindUtils.postNotifyChange(null, null, DescargaSriVm.this, "mensajeEstado");
                        Clients.showNotification(mensajeEstado,
                                Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 4000, true);
                    }
                });
    }

    private HSSFCellStyle estiloCabecera(HSSFWorkbook wb) {
        HSSFFont fuente = wb.createFont();
        fuente.setBoldweight((short) 700);
        HSSFCellStyle estilo = wb.createCellStyle();
        estilo.setFont(fuente);
        estilo.setWrapText(true);
        return estilo;
    }

    private void celda(HSSFRow r, int col, String valor, HSSFCellStyle estilo) {
        HSSFCell c = r.createCell(col);
        c.setCellValue(new HSSFRichTextString(valor == null ? "" : valor));
        if (estilo != null) {
            c.setCellStyle(estilo);
        }
    }

    private void descargarExcel(HSSFWorkbook wb, String nombre) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wb.write(baos);
        Filedownload.save(new AMedia(nombre, "xls", "application/vnd.ms-excel", baos.toByteArray()));
    }

    @Command
    @NotifyChange({"lista", "mensajeEstado"})
    public void traerXml() {
        if (lista.isEmpty()) {
            Clients.showNotification("No hay comprobantes en la lista.",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000, true);
            return;
        }
        ambiente = ambienteDelUsuario();
        if (ambiente == null) {
            LOG.warning("Traer XML: no hay tipoambiente para el usuario logueado.");
            Clients.showNotification("No hay ambiente SRI para el usuario logueado.",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000, true);
            return;
        }
        LOG.log(Level.INFO, "Traer XML: ruc={0} urlSri={1} codigoAmbiente={2} tipo={3} lista={4}",
                new Object[]{
                    ambiente.getAmRuc(),
                    ambiente.getAmUrlsri(),
                    ambiente.getAmCodigo(),
                    codigoTipo,
                    Integer.valueOf(lista.size())
                });
        if (ambiente.getAmUrlsri() == null || ambiente.getAmUrlsri().trim().isEmpty()) {
            mensajeEstado = "El tipoambiente no tiene URL del SRI (am_urlsri). Revise Configuración SRI.";
            LOG.warning(mensajeEstado);
            Clients.showNotification(mensajeEstado,
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 6000, true);
            return;
        }
        String base = ambiente.getAmDirBaseArchivos() != null
                ? ambiente.getAmDirBaseArchivos() : System.getProperty("java.io.tmpdir");
        String folder = base + File.separator + "COMPRASDESCARGADAS" + File.separator + codigoTipo;
        boolean dirOk = new File(folder).mkdirs() || new File(folder).isDirectory();
        LOG.log(Level.INFO, "Traer XML: carpeta={0} existe={1}", new Object[]{folder, Boolean.valueOf(dirOk)});
        int ok = 0;
        int fail = 0;
        int omitidos = 0;
        String ultimoError = "";
        for (SriComprobanteRecibido item : lista) {
            if (Boolean.TRUE.equals(item.getScrXmlDescargado())) {
                omitidos++;
                continue;
            }
            String clave = item.getScrClaveAcceso();
            try {
                LOG.log(Level.INFO, "Traer XML: consultando clave={0} serie={1}",
                        new Object[]{clave, item.getScrSerie()});
                RespuestaComprobante resp = autorizarDocumentos.autorizarComprobante(clave, ambiente);
                if (resp == null) {
                    fail++;
                    ultimoError = "SRI devolvió respuesta nula para " + clave;
                    LOG.warning(ultimoError);
                    continue;
                }
                if (resp.getNumeroComprobantes() != null
                        && resp.getNumeroComprobantes().toUpperCase().contains("ERROR")) {
                    fail++;
                    ultimoError = resp.getNumeroComprobantes();
                    LOG.log(Level.WARNING, "Traer XML: error WS clave={0} msg={1}",
                            new Object[]{clave, ultimoError});
                    continue;
                }
                if (resp.getAutorizaciones() == null
                        || resp.getAutorizaciones().getAutorizacion() == null
                        || resp.getAutorizaciones().getAutorizacion().isEmpty()) {
                    fail++;
                    ultimoError = "Sin autorizaciones para " + clave
                            + " numeroComprobantes=" + resp.getNumeroComprobantes();
                    LOG.log(Level.WARNING, "Traer XML: {0}", ultimoError);
                    continue;
                }
                boolean escribio = false;
                for (Autorizacion aut : resp.getAutorizaciones().getAutorizacion()) {
                    String estado = aut.getEstado();
                    int tamXml = aut.getComprobante() == null ? 0 : aut.getComprobante().length();
                    LOG.log(Level.INFO, "Traer XML: clave={0} estado={1} xmlChars={2}",
                            new Object[]{clave, estado, Integer.valueOf(tamXml)});
                    if (aut.getComprobante() == null || aut.getComprobante().trim().isEmpty()) {
                        ultimoError = "Autorización sin XML. estado=" + estado + " clave=" + clave;
                        continue;
                    }
                    String path = folder + File.separator + clave + ".xml";
                    FileOutputStream out = new FileOutputStream(path);
                    out.write(aut.getComprobante().getBytes("UTF-8"));
                    out.close();
                    item.setScrXmlDescargado(Boolean.TRUE);
                    item.setScrRutaXml(path);
                    servicio.modificarComprobante(item);
                    ok++;
                    escribio = true;
                    LOG.log(Level.INFO, "Traer XML: guardado {0}", path);
                }
                if (!escribio) {
                    fail++;
                    LOG.log(Level.WARNING, "Traer XML: no se escribió XML para {0}. {1}",
                            new Object[]{clave, ultimoError});
                }
            } catch (Exception e) {
                fail++;
                ultimoError = e.getClass().getSimpleName() + ": " + e.getMessage();
                LOG.log(Level.SEVERE, "Traer XML: excepción clave=" + clave, e);
            }
        }
        buscar();
        mensajeEstado = "XML descargados: " + ok + ". Sin XML: " + fail
                + ". Ya tenían XML: " + omitidos + ".";
        if (fail > 0 && ultimoError != null && !ultimoError.isEmpty()) {
            mensajeEstado = mensajeEstado + " Último error: " + ultimoError;
        }
        LOG.log(Level.INFO, "Traer XML: fin {0}", mensajeEstado);
        Clients.showNotification(mensajeEstado,
                fail > 0 ? Clients.NOTIFICATION_TYPE_WARNING : Clients.NOTIFICATION_TYPE_INFO,
                null, "middle_center", 8000, true);
    }

    @Command
    public void verXml(@BindingParam("valor") SriComprobanteRecibido item) {
        try {
            File f = archivoXml(item);
            if (f == null) {
                Clients.showNotification("Primero use Traer XML para este comprobante.",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 4000, true);
                return;
            }
            String xml = RidePdfDesdeXml.prettyXml(RidePdfDesdeXml.leerXml(f));
            Window w = new Window("XML " + nvl(item.getScrClaveAcceso()), "normal", true);
            w.setWidth("92%");
            w.setHeight("90%");
            w.setClosable(true);
            w.setSizable(true);
            w.setMaximizable(true);
            Textbox tb = new Textbox(xml);
            tb.setMultiline(true);
            tb.setReadonly(true);
            tb.setWidth("100%");
            tb.setHeight("100%");
            tb.setStyle("font-family:Consolas,monospace;font-size:12px;height:100%;");
            w.appendChild(tb);
            w.setPage(Executions.getCurrent().getDesktop().getFirstPage());
            w.doModal();
        } catch (Exception e) {
            Clients.showNotification("No se pudo leer el XML: " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 5000, true);
        }
    }

    @Command
    public void verRide(@BindingParam("valor") SriComprobanteRecibido item) {
        try {
            File f = archivoXml(item);
            if (f == null) {
                Clients.showNotification("Primero use Traer XML para este comprobante.",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 4000, true);
                return;
            }
            byte[] pdf = RidePdfDesdeXml.generar(RidePdfDesdeXml.leerXml(f));
            File pdfFile = new File(f.getParentFile(), nvl(item.getScrClaveAcceso()) + ".pdf");
            FileOutputStream out = new FileOutputStream(pdfFile);
            out.write(pdf);
            out.close();
            AMedia media = new AMedia(pdfFile.getName(), "pdf", "application/pdf", pdf);
            Window w = new Window("RIDE " + nvl(item.getScrSerie()), "normal", true);
            w.setWidth("92%");
            w.setHeight("92%");
            w.setClosable(true);
            w.setSizable(true);
            w.setMaximizable(true);
            Iframe iframe = new Iframe();
            iframe.setWidth("100%");
            iframe.setHeight("100%");
            iframe.setContent(media);
            w.appendChild(iframe);
            w.setPage(Executions.getCurrent().getDesktop().getFirstPage());
            w.doModal();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "RIDE PDF", e);
            Clients.showNotification("No se pudo armar el RIDE: " + e.getClass().getSimpleName()
                    + (e.getMessage() == null ? "" : ": " + e.getMessage()),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 6000, true);
        }
    }

    private File archivoXml(SriComprobanteRecibido item) {
        if (item == null) {
            return null;
        }
        File f = RidePdfDesdeXml.archivoXml(item.getScrRutaXml());
        if (f != null) {
            return f;
        }
        if (item.getScrClaveAcceso() == null || ambiente == null) {
            return null;
        }
        String base = ambiente.getAmDirBaseArchivos() != null
                ? ambiente.getAmDirBaseArchivos() : System.getProperty("java.io.tmpdir");
        return RidePdfDesdeXml.archivoXml(base + File.separator + "COMPRASDESCARGADAS"
                + File.separator + codigoTipo + File.separator + item.getScrClaveAcceso() + ".xml");
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    private Tipoambiente ambienteDelUsuario() {
        if (credential == null || credential.getUsuarioSistema() == null) {
            return null;
        }
        return servicioTipoAmbiente.findALlTipoambientePorUsuario(credential.getUsuarioSistema());
    }

    private void persistirTxt(String contenido, String origen, String mensajeOk) {
        ambiente = ambienteDelUsuario();
        if (ambiente == null) {
            Clients.showNotification("No hay ambiente SRI activo para asociar los documentos.",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000, true);
            return;
        }
        List<SriTxtRecibidosParser.Linea> lineas = parser.parsear(contenido);
        int leidas = lineas.size();
        lineas = parser.filtrarPorTipo(lineas, codigoTipo);
        int delTipo = lineas.size();
        lineas = filtrarLineasPorRango(lineas);
        if (lineas.isEmpty()) {
            if (leidas == 0) {
                mensajeEstado = "No se leyeron filas del TXT. Debe ser el archivo Recibidos del SRI (RUC_EMISOR, CLAVE_ACCESO, FECHA_EMISION).";
            } else if (delTipo == 0) {
                mensajeEstado = "El TXT tiene " + leidas + " filas, pero ninguna es de " + nombreTipo()
                        + ". Abra el menú del tipo correcto o descargue el TXT de ese comprobante.";
            } else {
                mensajeEstado = "El TXT no tiene " + nombreTipo() + " en el rango Desde-Hasta indicado.";
            }
            Clients.showNotification(mensajeEstado,
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 7000, true);
            return;
        }
        SriDescargaLote lote = new SriDescargaLote();
        lote.setStdCodigo(codigoTipo);
        lote.setCodTipoambiente(ambiente);
        lote.setSdlFechaInicio(inicio);
        lote.setSdlFechaFin(fin);
        lote.setSdlFechaEjecucion(new Date());
        lote.setSdlOrigen(origen);
        lote.setSdlEstado("OK");
        lote.setSdlMensaje(mensajeOk);
        lote.setSdlUsuario(usuarioSesion());
        lote.setSdlCantidad(lineas.size());
        lote.setSdlRutaArchivo(guardarArchivo(contenido));
        int nuevos = 0;
        int duplicados = 0;
        lote = servicio.crearLote(lote);
        for (SriTxtRecibidosParser.Linea linea : lineas) {
            if (servicio.findByClave(linea.claveAcceso, ambiente) != null) {
                duplicados++;
                continue;
            }
            SriComprobanteRecibido item = new SriComprobanteRecibido();
            item.setStdCodigo(codigoTipo);
            item.setCodTipoambiente(ambiente);
            item.setSdlId(lote.getSdlId());
            item.setScrTipoComprobante(linea.tipoComprobante);
            item.setScrSerie(linea.serie);
            item.setScrRucEmisor(linea.rucEmisor);
            item.setScrRazonSocial(linea.razonSocial);
            item.setScrFechaEmision(linea.fechaEmision);
            item.setScrFechaAutorizacion(linea.fechaAutorizacion);
            item.setScrTipoEmision(linea.tipoEmision);
            item.setScrIdentificacionReceptor(linea.identificacionReceptor);
            item.setScrClaveAcceso(linea.claveAcceso);
            item.setScrAutorizacion(linea.autorizacion);
            item.setScrTotal(linea.total);
            item.setScrEstadoClasificacion("PENDIENTE");
            item.setScrXmlDescargado(Boolean.FALSE);
            item.setScrFechaRegistro(new Date());
            servicio.crearComprobante(item);
            nuevos++;
        }
        lote.setSdlNuevos(nuevos);
        lote.setSdlDuplicados(duplicados);
        lote.setSdlMensaje(mensajeOk + " Nuevos: " + nuevos + ". Ya existían: " + duplicados + "."
                + (leidas > lineas.size()
                ? " Omitidos de otro tipo: " + (leidas - lineas.size()) + "." : ""));
        servicio.modificarLote(lote);
        buscar();
        mensajeEstado = lote.getSdlMensaje();
        Clients.showNotification(mensajeEstado,
                Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000, true);
    }

    private List<SriTxtRecibidosParser.Linea> filtrarLineasPorRango(List<SriTxtRecibidosParser.Linea> lineas) {
        if (inicio == null || fin == null) {
            return lineas;
        }
        Calendar a = Calendar.getInstance();
        a.setTime(inicio);
        a.set(Calendar.HOUR_OF_DAY, 0);
        a.set(Calendar.MINUTE, 0);
        a.set(Calendar.SECOND, 0);
        a.set(Calendar.MILLISECOND, 0);
        Calendar b = Calendar.getInstance();
        b.setTime(fin);
        b.set(Calendar.HOUR_OF_DAY, 23);
        b.set(Calendar.MINUTE, 59);
        b.set(Calendar.SECOND, 59);
        Date di = a.getTime();
        Date df = b.getTime();
        List<SriTxtRecibidosParser.Linea> filtradas = new ArrayList<SriTxtRecibidosParser.Linea>();
        for (SriTxtRecibidosParser.Linea l : lineas) {
            if (l.fechaEmision == null || (!l.fechaEmision.before(di) && !l.fechaEmision.after(df))) {
                filtradas.add(l);
            }
        }
        return filtradas;
    }

    private void ajustarRangoConLineas(List<SriTxtRecibidosParser.Linea> lineas) {
        Date min = null;
        Date max = null;
        for (SriTxtRecibidosParser.Linea l : lineas) {
            if (l.fechaEmision == null) {
                continue;
            }
            if (min == null || l.fechaEmision.before(min)) {
                min = l.fechaEmision;
            }
            if (max == null || l.fechaEmision.after(max)) {
                max = l.fechaEmision;
            }
        }
        if (min != null) {
            inicio = ArchivoUtils.recuperarFecha(min, "inicio");
            fin = ArchivoUtils.recuperarFecha(max, "fin");
        }
    }

    private String guardarArchivo(String contenido) {
        try {
            String base = ambiente != null && ambiente.getAmDirBaseArchivos() != null
                    ? ambiente.getAmDirBaseArchivos() : System.getProperty("java.io.tmpdir");
            File dir = new File(base + File.separator + "SRIRECIBIDOS" + File.separator + codigoTipo);
            dir.mkdirs();
            File f = new File(dir, "recibidos_" + codigoTipo + "_" + System.currentTimeMillis() + ".txt");
            FileOutputStream out = new FileOutputStream(f);
            out.write(contenido.getBytes("UTF-8"));
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    private String usuarioSesion() {
        try {
            if (credential != null && credential.getUsuarioSistema() != null) {
                return credential.getUsuarioSistema().getUsuLogin();
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public String getTitulo() {
        return "SRI · " + nombreTipoCorto();
    }

    public String getAyudaTipo() {
        return "Indique Desde y Hasta, luego Descargar del SRI o Cargar TXT. Después Traer XML, Excel o RIDE.";
    }

    public String getEtiquetaTotal() {
        if ("07".equals(codigoTipo)) {
            return "Retenido";
        }
        if ("06".equals(codigoTipo)) {
            return "Bultos";
        }
        if ("04".equals(codigoTipo) || "05".equals(codigoTipo)) {
            return "Valor";
        }
        return "Total";
    }

    public String nombreTipo() {
        return tipoDocumento != null ? tipoDocumento.getStdNombre() : codigoTipo;
    }

    public String nombreTipoCorto() {
        if ("01".equals(codigoTipo)) {
            return "Facturas";
        }
        if ("03".equals(codigoTipo)) {
            return "Liquidaciones";
        }
        if ("04".equals(codigoTipo)) {
            return "Notas de crédito";
        }
        if ("05".equals(codigoTipo)) {
            return "Notas de débito";
        }
        if ("06".equals(codigoTipo)) {
            return "Guías";
        }
        if ("07".equals(codigoTipo)) {
            return "Retenciones";
        }
        return nombreTipo();
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getMensajeEstado() {
        return mensajeEstado;
    }

    public List<SriComprobanteRecibido> getLista() {
        return lista;
    }

    public String getCodigoTipo() {
        return codigoTipo;
    }
}
