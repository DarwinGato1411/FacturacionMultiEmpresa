package com.ec.untilitario;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Filedownload;

public class ExportContabilidad {

    private static final SimpleDateFormat FMT = new SimpleDateFormat("dd/MM/yyyy");

    public static void excelComprobacion(String titulo, Date inicio, Date fin, List<ModeloLibroMayor> filas,
            BigDecimal totalDebe, BigDecimal totalHaber, BigDecimal totalDeudor, BigDecimal totalAcreedor) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh = wb.createSheet("Comprobacion");
        HSSFCellStyle cab = cabecera(wb);
        int r = 0;
        fila(sh, r++, new String[]{titulo}, null);
        fila(sh, r++, new String[]{"Periodo", texto(inicio) + " al " + texto(fin)}, null);
        r++;
        fila(sh, r++, new String[]{"Codigo", "Cuenta", "Debe periodo", "Haber periodo", "Saldo deudor", "Saldo acreedor"}, cab);
        if (filas != null) {
            for (int i = 0; i < filas.size(); i++) {
                ModeloLibroMayor m = filas.get(i);
                fila(sh, r++, new String[]{m.getCodigo(), m.getNombre(), num(m.getDebe()), num(m.getHaber()),
                    num(m.getSaldoDeudor()), num(m.getSaldoAcreedor())}, null);
            }
        }
        fila(sh, r++, new String[]{"", "TOTALES", num(totalDebe), num(totalHaber), num(totalDeudor), num(totalAcreedor)}, cab);
        auto(sh, 6);
        descargarXls(wb, "balance_comprobacion.xls");
    }

    public static void excelEstados(String titulo, Date inicio, Date fin, List<ModeloEstadoLinea> filas) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh = wb.createSheet("Estado");
        HSSFCellStyle cab = cabecera(wb);
        int r = 0;
        fila(sh, r++, new String[]{titulo}, null);
        fila(sh, r++, new String[]{"Periodo", texto(inicio) + " al " + texto(fin)}, null);
        r++;
        fila(sh, r++, new String[]{"Codigo", "Cuenta", "Saldo"}, cab);
        if (filas != null) {
            for (int i = 0; i < filas.size(); i++) {
                ModeloEstadoLinea l = filas.get(i);
                fila(sh, r++, new String[]{nz(l.getCodigo()), nz(l.getNombre()),
                    l.isTitulo() ? "" : num(l.getValor())}, null);
            }
        }
        auto(sh, 3);
        descargarXls(wb, "estado_financiero.xls");
    }

    public static void excelMayor(Date inicio, Date fin, List<ModeloLibroMayor> cuentas) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet res = wb.createSheet("Resumen");
        HSSFCellStyle cab = cabecera(wb);
        int r = 0;
        fila(res, r++, new String[]{"Libro mayor " + texto(inicio) + " al " + texto(fin)}, null);
        r++;
        fila(res, r++, new String[]{"Codigo", "Cuenta", "Saldo ant.", "Debe", "Haber", "Saldo"}, cab);
        if (cuentas != null) {
            for (int i = 0; i < cuentas.size(); i++) {
                ModeloLibroMayor m = cuentas.get(i);
                fila(res, r++, new String[]{m.getCodigo(), m.getNombre(), num(m.getSaldoInicial()),
                    num(m.getDebe()), num(m.getHaber()), num(m.getSaldoFinal())}, null);
            }
        }
        auto(res, 6);

        HSSFSheet aux = wb.createSheet("Mayor auxiliar");
        int a = 0;
        fila(aux, a++, new String[]{"Mayor auxiliar por cuenta (la glosa identifica el documento / tercero)"}, null);
        a++;
        fila(aux, a++, new String[]{"Cuenta", "Nombre", "Fecha", "Asiento", "Origen", "Glosa / tercero", "Debe", "Haber", "Saldo"}, cab);
        if (cuentas != null) {
            for (int i = 0; i < cuentas.size(); i++) {
                ModeloLibroMayor m = cuentas.get(i);
                List<ModeloLibroMayorMov> movs = m.getMovimientos();
                if (movs == null) {
                    continue;
                }
                for (int j = 0; j < movs.size(); j++) {
                    ModeloLibroMayorMov mv = movs.get(j);
                    fila(aux, a++, new String[]{m.getCodigo(), m.getNombre(), texto(mv.getFecha()),
                        mv.getNumero() == null ? "" : String.valueOf(mv.getNumero()),
                        nz(mv.getOrigen()), nz(mv.getGlosa()), num(mv.getDebe()), num(mv.getHaber()), num(mv.getSaldo())}, null);
                }
            }
        }
        auto(aux, 9);
        descargarXls(wb, "libro_mayor.xls");
    }

    public static void excelFlujo(Date inicio, Date fin, List<ModeloFlujoLinea> filas,
            BigDecimal totalIng, BigDecimal totalEgr) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh = wb.createSheet("Flujo");
        HSSFCellStyle cab = cabecera(wb);
        int r = 0;
        fila(sh, r++, new String[]{"Flujo de efectivo (directo) " + texto(inicio) + " al " + texto(fin)}, null);
        fila(sh, r++, new String[]{"Movimientos de la cuenta parametrizada CAJA"}, null);
        r++;
        fila(sh, r++, new String[]{"Fecha", "Origen", "Clasificacion", "Glosa", "Ingreso", "Egreso", "Saldo"}, cab);
        if (filas != null) {
            for (int i = 0; i < filas.size(); i++) {
                ModeloFlujoLinea f = filas.get(i);
                fila(sh, r++, new String[]{texto(f.getFecha()), nz(f.getOrigen()), nz(f.getClasificacion()),
                    nz(f.getGlosa()), num(f.getIngreso()), num(f.getEgreso()), num(f.getSaldo())}, null);
            }
        }
        fila(sh, r++, new String[]{"", "", "", "TOTALES", num(totalIng), num(totalEgr), ""}, cab);
        auto(sh, 7);
        descargarXls(wb, "flujo_efectivo.xls");
    }

    public static void excelComparativo(String etiquetaA, String etiquetaB, List<ModeloComparativo> filas) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh = wb.createSheet("Comparativo");
        HSSFCellStyle cab = cabecera(wb);
        int r = 0;
        fila(sh, r++, new String[]{"Comparativo de saldos"}, null);
        r++;
        fila(sh, r++, new String[]{"Codigo", "Cuenta", etiquetaA, etiquetaB, "Diferencia (B-A)"}, cab);
        if (filas != null) {
            for (int i = 0; i < filas.size(); i++) {
                ModeloComparativo c = filas.get(i);
                fila(sh, r++, new String[]{c.getCodigo(), c.getNombre(), num(c.getSaldoA()),
                    num(c.getSaldoB()), num(c.getDiferencia())}, null);
            }
        }
        auto(sh, 5);
        descargarXls(wb, "comparativo.xls");
    }

    public static void html(String nombreArchivo, String titulo, String cuerpoTabla) throws Exception {
        String html = "<html><head><meta charset='UTF-8'><title>" + esc(titulo) + "</title>"
                + "<style>body{font-family:Arial,sans-serif;font-size:12px;color:#122536}"
                + "h1{font-size:16px}table{border-collapse:collapse;width:100%}"
                + "th,td{border:1px solid #ccc;padding:4px 6px;text-align:left}"
                + "th{background:#122536;color:#fff}td.num{text-align:right}"
                + "@media print{button{display:none}}</style></head><body>"
                + "<button onclick='window.print()'>Imprimir / guardar PDF</button>"
                + "<h1>" + esc(titulo) + "</h1>"
                + cuerpoTabla + "</body></html>";
        Filedownload.save(new AMedia(nombreArchivo, "html", "text/html", html.getBytes("UTF-8")));
    }

    public static String tablaComprobacion(List<ModeloLibroMayor> filas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table><tr><th>Codigo</th><th>Cuenta</th><th>Debe</th><th>Haber</th>"
                + "<th>Saldo deudor</th><th>Saldo acreedor</th></tr>");
        if (filas != null) {
            for (int i = 0; i < filas.size(); i++) {
                ModeloLibroMayor m = filas.get(i);
                sb.append("<tr><td>").append(esc(m.getCodigo())).append("</td><td>")
                        .append(esc(m.getNombre())).append("</td><td class='num'>").append(num(m.getDebe()))
                        .append("</td><td class='num'>").append(num(m.getHaber()))
                        .append("</td><td class='num'>").append(num(m.getSaldoDeudor()))
                        .append("</td><td class='num'>").append(num(m.getSaldoAcreedor()))
                        .append("</td></tr>");
            }
        }
        sb.append("</table>");
        return sb.toString();
    }

    public static String tablaEstado(List<ModeloEstadoLinea> filas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table><tr><th>Codigo</th><th>Cuenta</th><th>Saldo</th></tr>");
        if (filas != null) {
            for (int i = 0; i < filas.size(); i++) {
                ModeloEstadoLinea l = filas.get(i);
                sb.append("<tr><td>").append(esc(l.getCodigo())).append("</td><td>")
                        .append(esc(l.getNombre())).append("</td><td class='num'>")
                        .append(l.isTitulo() ? "" : num(l.getValor())).append("</td></tr>");
            }
        }
        sb.append("</table>");
        return sb.toString();
    }

    public static String tablaMayor(List<ModeloLibroMayor> cuentas) {
        StringBuilder sb = new StringBuilder();
        if (cuentas == null) {
            return "<p>Sin datos</p>";
        }
        for (int i = 0; i < cuentas.size(); i++) {
            ModeloLibroMayor m = cuentas.get(i);
            sb.append("<h2>").append(esc(m.getCodigo())).append(" ").append(esc(m.getNombre())).append("</h2>");
            sb.append("<table><tr><th>Fecha</th><th>Asiento</th><th>Origen</th><th>Glosa / tercero</th>"
                    + "<th>Debe</th><th>Haber</th><th>Saldo</th></tr>");
            List<ModeloLibroMayorMov> movs = m.getMovimientos();
            if (movs != null) {
                for (int j = 0; j < movs.size(); j++) {
                    ModeloLibroMayorMov mv = movs.get(j);
                    sb.append("<tr><td>").append(esc(texto(mv.getFecha()))).append("</td><td>")
                            .append(mv.getNumero() == null ? "" : mv.getNumero())
                            .append("</td><td>").append(esc(mv.getOrigen())).append("</td><td>")
                            .append(esc(mv.getGlosa())).append("</td><td class='num'>").append(num(mv.getDebe()))
                            .append("</td><td class='num'>").append(num(mv.getHaber()))
                            .append("</td><td class='num'>").append(num(mv.getSaldo()))
                            .append("</td></tr>");
                }
            }
            sb.append("</table>");
        }
        return sb.toString();
    }

    public static String tablaFlujo(List<ModeloFlujoLinea> filas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table><tr><th>Fecha</th><th>Origen</th><th>Clasificacion</th><th>Glosa</th>"
                + "<th>Ingreso</th><th>Egreso</th><th>Saldo</th></tr>");
        if (filas != null) {
            for (int i = 0; i < filas.size(); i++) {
                ModeloFlujoLinea f = filas.get(i);
                sb.append("<tr><td>").append(esc(texto(f.getFecha()))).append("</td><td>")
                        .append(esc(f.getOrigen())).append("</td><td>").append(esc(f.getClasificacion()))
                        .append("</td><td>").append(esc(f.getGlosa()))
                        .append("</td><td class='num'>").append(num(f.getIngreso()))
                        .append("</td><td class='num'>").append(num(f.getEgreso()))
                        .append("</td><td class='num'>").append(num(f.getSaldo()))
                        .append("</td></tr>");
            }
        }
        sb.append("</table>");
        return sb.toString();
    }

    public static String tablaComparativo(String a, String b, List<ModeloComparativo> filas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table><tr><th>Codigo</th><th>Cuenta</th><th>").append(esc(a)).append("</th><th>")
                .append(esc(b)).append("</th><th>Diferencia</th></tr>");
        if (filas != null) {
            for (int i = 0; i < filas.size(); i++) {
                ModeloComparativo c = filas.get(i);
                sb.append("<tr><td>").append(esc(c.getCodigo())).append("</td><td>")
                        .append(esc(c.getNombre())).append("</td><td class='num'>").append(num(c.getSaldoA()))
                        .append("</td><td class='num'>").append(num(c.getSaldoB()))
                        .append("</td><td class='num'>").append(num(c.getDiferencia()))
                        .append("</td></tr>");
            }
        }
        sb.append("</table>");
        return sb.toString();
    }

    private static void descargarXls(HSSFWorkbook wb, String nombre) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wb.write(baos);
        Filedownload.save(new AMedia(nombre, "xls", "application/vnd.ms-excel", baos.toByteArray()));
    }

    private static HSSFCellStyle cabecera(HSSFWorkbook wb) {
        HSSFCellStyle estilo = wb.createCellStyle();
        HSSFFont fuente = wb.createFont();
        fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        estilo.setFont(fuente);
        return estilo;
    }

    private static void fila(HSSFSheet sh, int idx, String[] vals, HSSFCellStyle estilo) {
        HSSFRow row = sh.createRow(idx);
        for (int i = 0; i < vals.length; i++) {
            HSSFCell c = row.createCell(i);
            c.setCellValue(new HSSFRichTextString(vals[i] == null ? "" : vals[i]));
            if (estilo != null) {
                c.setCellStyle(estilo);
            }
        }
    }

    private static void auto(HSSFSheet sh, int cols) {
        for (int i = 0; i < cols; i++) {
            sh.autoSizeColumn(i);
        }
    }

    private static String num(BigDecimal v) {
        return v == null ? "0.00" : v.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private static String texto(Date d) {
        return d == null ? "" : FMT.format(d);
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    private static String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
