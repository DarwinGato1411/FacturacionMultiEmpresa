package com.ec.untilitario;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SriTxtRecibidosParser {

    public static class Linea {

        public String tipoComprobante;
        public String serie;
        public String rucEmisor;
        public String razonSocial;
        public Date fechaEmision;
        public Date fechaAutorizacion;
        public String tipoEmision;
        public String identificacionReceptor;
        public String claveAcceso;
        public String autorizacion;
        public String total;
    }

    public List<Linea> parsear(String contenido) {
        List<Linea> lista = new ArrayList<Linea>();
        if (contenido == null || contenido.trim().isEmpty()) {
            return lista;
        }
        String normalizado = contenido.replace("\r\n", "\n").replace('\r', '\n');
        if (normalizado.length() > 0 && normalizado.charAt(0) == '\uFEFF') {
            normalizado = normalizado.substring(1);
        }
        String[] lineas = normalizado.split("\n");
        Map<String, Integer> idx = null;
        for (String cruda : lineas) {
            if (cruda == null || cruda.trim().isEmpty()) {
                continue;
            }
            String[] cols = partir(cruda);
            if (cols.length < 4) {
                continue;
            }
            if (idx == null && esCabecera(cols)) {
                idx = indices(cols);
                continue;
            }
            Linea linea = idx != null ? desdeMapa(cols, idx) : desdePosicionAntigua(cols);
            if (linea != null && linea.claveAcceso != null && linea.claveAcceso.length() >= 20) {
                lista.add(linea);
            }
        }
        return lista;
    }

    public List<Linea> filtrarPorTipo(List<Linea> lineas, String codigoTipo) {
        List<Linea> out = new ArrayList<Linea>();
        if (lineas == null) {
            return out;
        }
        for (int i = 0; i < lineas.size(); i++) {
            Linea l = lineas.get(i);
            if (esDelTipo(l, codigoTipo)) {
                out.add(l);
            }
        }
        return out;
    }

    public static boolean esDelTipo(Linea l, String codigoTipo) {
        if (l == null || codigoTipo == null || codigoTipo.trim().isEmpty()) {
            return false;
        }
        String codigo = codigoTipo.trim();
        String desdeClave = codigoDesdeClave(l.claveAcceso);
        if (desdeClave.length() == 2) {
            return codigo.equals(desdeClave);
        }
        return coincideNombreTipo(l.tipoComprobante, codigo);
    }

    public static String codigoDesdeClave(String clave) {
        if (clave == null) {
            return "";
        }
        String t = clave.trim();
        if (t.length() < 10 || !t.matches("\\d+")) {
            return "";
        }
        return t.substring(8, 10);
    }

    public static boolean coincideNombreTipo(String nombre, String codigo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        String n = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toUpperCase(Locale.ROOT);
        if (n.contains(codigo)) {
            return true;
        }
        if ("01".equals(codigo)) {
            return n.contains("FACTURA") && !n.contains("LIQUID");
        }
        if ("03".equals(codigo)) {
            return n.contains("LIQUID");
        }
        if ("04".equals(codigo)) {
            return n.contains("CREDITO") || n.contains("CREDITO");
        }
        if ("05".equals(codigo)) {
            return n.contains("DEBITO");
        }
        if ("06".equals(codigo)) {
            return n.contains("GUIA");
        }
        if ("07".equals(codigo)) {
            return n.contains("RETENC");
        }
        return false;
    }

    public String aTxt(List<Linea> lineas) {
        StringBuilder sb = new StringBuilder();
        sb.append("COMPROBANTE\tSERIE COMPROBANTE\tRUC EMISOR\tRAZÓN SOCIAL EMISOR\tFECHA EMISIÓN\tFECHA AUTORIZACIÓN\tTIPO EMISIÓN\tIDENTIFICACIÓN RECEPTOR\tCLAVE DE ACCESO\tNÚMERO AUTORIZACIÓN\tIMPORTE TOTAL\n");
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        for (Linea l : lineas) {
            sb.append(nvl(l.tipoComprobante)).append('\t')
                    .append(nvl(l.serie)).append('\t')
                    .append(nvl(l.rucEmisor)).append('\t')
                    .append(nvl(l.razonSocial)).append('\t')
                    .append(l.fechaEmision != null ? fmt.format(l.fechaEmision) : "").append('\t')
                    .append(l.fechaAutorizacion != null ? fmt.format(l.fechaAutorizacion) : "").append('\t')
                    .append(nvl(l.tipoEmision)).append('\t')
                    .append(nvl(l.identificacionReceptor)).append('\t')
                    .append(nvl(l.claveAcceso)).append('\t')
                    .append(nvl(l.autorizacion)).append('\t')
                    .append(nvl(l.total)).append('\n');
        }
        return sb.toString();
    }

    private Linea desdeMapa(String[] cols, Map<String, Integer> idx) {
        Linea linea = new Linea();
        linea.rucEmisor = campo(cols, idx, "RUC_EMISOR", "IDENTIFICACION_EMISOR");
        linea.razonSocial = campo(cols, idx, "RAZON_SOCIAL_EMISOR", "RAZON_SOCIAL");
        linea.tipoComprobante = campo(cols, idx, "TIPO_COMPROBANTE", "COMPROBANTE");
        linea.serie = campo(cols, idx, "SERIE_COMPROBANTE", "SERIE");
        linea.claveAcceso = campo(cols, idx, "CLAVE_ACCESO", "CLAVE_DE_ACCESO", "CLAVEACCESO");
        linea.fechaAutorizacion = parseFecha(campo(cols, idx, "FECHA_AUTORIZACION"));
        linea.fechaEmision = parseFecha(campo(cols, idx, "FECHA_EMISION"));
        linea.identificacionReceptor = campo(cols, idx, "IDENTIFICACION_RECEPTOR", "RUC");
        linea.total = campo(cols, idx, "IMPORTE_TOTAL", "TOTAL", "VALOR");
        linea.autorizacion = campo(cols, idx, "NUMERO_AUTORIZACION", "AUTORIZACION");
        if (linea.autorizacion == null || linea.autorizacion.isEmpty()) {
            linea.autorizacion = linea.claveAcceso;
        }
        linea.tipoEmision = campo(cols, idx, "TIPO_EMISION");
        return linea;
    }

    private Linea desdePosicionAntigua(String[] cols) {
        if (cols.length < 9) {
            return null;
        }
        Linea linea = new Linea();
        linea.tipoComprobante = limpio(cols, 0);
        linea.serie = limpio(cols, 1);
        linea.rucEmisor = limpio(cols, 2);
        linea.razonSocial = limpio(cols, 3);
        linea.fechaEmision = parseFecha(limpio(cols, 4));
        linea.fechaAutorizacion = parseFecha(limpio(cols, 5));
        linea.tipoEmision = limpio(cols, 6);
        linea.identificacionReceptor = limpio(cols, 7);
        linea.claveAcceso = limpio(cols, 8);
        linea.autorizacion = cols.length > 9 ? limpio(cols, 9) : linea.claveAcceso;
        linea.total = cols.length > 10 ? limpio(cols, 10) : "";
        if (pareceClave(linea.rucEmisor) && !pareceClave(linea.claveAcceso)) {
            linea.rucEmisor = limpio(cols, 0);
            linea.razonSocial = limpio(cols, 1);
            linea.tipoComprobante = limpio(cols, 2);
            linea.serie = limpio(cols, 3);
            linea.claveAcceso = limpio(cols, 4);
            linea.fechaAutorizacion = parseFecha(limpio(cols, 5));
            linea.fechaEmision = parseFecha(limpio(cols, 6));
            linea.identificacionReceptor = limpio(cols, 7);
            linea.total = cols.length > 10 ? limpio(cols, 10) : limpio(cols, 8);
            linea.autorizacion = linea.claveAcceso;
        }
        return linea;
    }

    private String[] partir(String cruda) {
        String[] cols = cruda.split("\t", -1);
        if (cols.length < 4) {
            cols = cruda.split(";", -1);
        }
        if (cols.length < 4) {
            cols = cruda.trim().split("\\s{2,}");
        }
        return cols;
    }

    private Map<String, Integer> indices(String[] cols) {
        Map<String, Integer> idx = new HashMap<String, Integer>();
        for (int i = 0; i < cols.length; i++) {
            String k = claveHeader(cols[i]);
            if (!k.isEmpty() && !idx.containsKey(k)) {
                idx.put(k, Integer.valueOf(i));
            }
        }
        return idx;
    }

    private boolean esCabecera(String[] cols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            sb.append(claveHeader(cols[i])).append('|');
        }
        String h = sb.toString();
        return h.contains("CLAVE_ACCESO") || h.contains("RUC_EMISOR") || h.contains("TIPO_COMPROBANTE")
                || h.contains("COMPROBANTE");
    }

    private String campo(String[] cols, Map<String, Integer> idx, String... nombres) {
        for (int i = 0; i < nombres.length; i++) {
            Integer pos = idx.get(nombres[i]);
            if (pos != null) {
                return limpio(cols, pos.intValue());
            }
        }
        return "";
    }

    private String claveHeader(String raw) {
        if (raw == null) {
            return "";
        }
        String s = Normalizer.normalize(raw.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toUpperCase(Locale.ROOT)
                .replace(' ', '_')
                .replace('-', '_');
        s = s.replace("CLAVE_DE_ACCESO", "CLAVE_ACCESO");
        s = s.replace("RAZON_SOCIAL_EMISOR", "RAZON_SOCIAL_EMISOR");
        return s;
    }

    private boolean pareceClave(String v) {
        if (v == null) {
            return false;
        }
        String t = v.trim();
        return t.length() >= 49 && t.matches("\\d+");
    }

    private String limpio(String[] cols, int i) {
        if (i >= cols.length || cols[i] == null) {
            return "";
        }
        return cols[i].trim().replace("\"", "");
    }

    private String nvl(String v) {
        return v == null ? "" : v;
    }

    public Date parseFecha(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        String v = valor.trim();
        String[] formatos = {
            "dd/MM/yyyy HH:mm:ss",
            "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "dd/MM/yy"
        };
        for (int i = 0; i < formatos.length; i++) {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat(formatos[i]);
                fmt.setLenient(false);
                return fmt.parse(v);
            } catch (ParseException ignored) {
            }
        }
        if (v.length() > 10) {
            String corto = v.substring(0, 10).replace('.', '/').replace('-', '/');
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(corto);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }
}
