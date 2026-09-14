package com.ec.untilitario;

import com.ec.entidad.sri.SriTipoDocumento;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Cliente HTTP de SRI en línea para comprobantes recibidos.
 * El SRI no publica un contrato estable; si hay captcha, la pantalla permite TXT.
 */
public class ClienteSriComprobantesRecibidos {

    private static final Logger LOG = Logger.getLogger(ClienteSriComprobantesRecibidos.class.getName());
    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    private final Map<String, String> cookies = new LinkedHashMap<String, String>();
    private String bearer;

    public ResultadoDescargaSri descargar(String usuario, String clave, SriTipoDocumento tipo,
            Date inicio, Date fin) {
        ResultadoDescargaSri r = new ResultadoDescargaSri();
        if (usuario == null || usuario.trim().isEmpty() || clave == null || clave.isEmpty()) {
            r.setOk(false);
            r.setMensaje("Configure usuario y clave de SRI en línea en Configuración SRI.");
            return r;
        }
        try {
            autenticar(usuario.trim(), clave, r);
            if (bearer == null && cookies.isEmpty()) {
                r.setOk(false);
                if (r.getMensaje() == null) {
                    r.setMensaje("No se pudo iniciar sesión en SRI en línea. Revise usuario/clave o si el portal pide captcha.");
                }
                return r;
            }
            List<SriTxtRecibidosParser.Linea> acumulado = new ArrayList<SriTxtRecibidosParser.Linea>();
            SriTxtRecibidosParser parser = new SriTxtRecibidosParser();
            List<int[]> periodos = diasEntre(inicio, fin);
            for (int[] ymd : periodos) {
                String bloque = consultarPeriodo(tipo, ymd[0], ymd[1], ymd[2], r);
                if (bloque == null || bloque.trim().isEmpty()) {
                    continue;
                }
                if (pareceJson(bloque)) {
                    acumulado.addAll(desdeJson(bloque, tipo));
                } else {
                    acumulado.addAll(parser.parsear(bloque));
                }
            }
            filtrarRango(acumulado, inicio, fin);
            if (tipo != null && tipo.getStdCodigo() != null) {
                acumulado = parser.filtrarPorTipo(acumulado, tipo.getStdCodigo());
            }
            if (acumulado.isEmpty()) {
                r.setOk(false);
                r.setOrigen("PORTAL");
                r.setMensaje(r.getMensaje() != null
                        ? r.getMensaje()
                        : "La sesión se abrió pero el portal no devolvió comprobantes para el período. "
                        + "Puede cargar el TXT descargado a mano desde SRI en línea.");
                return r;
            }
            r.setOk(true);
            r.setOrigen("PORTAL");
            r.setContenidoTxt(parser.aTxt(acumulado));
            r.setMensaje("Se obtuvieron " + acumulado.size() + " comprobantes desde SRI en línea.");
            return r;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Descarga SRI", e);
            r.setOk(false);
            r.setMensaje("Error al consultar SRI en línea: " + e.getMessage());
            return r;
        }
    }

    private void autenticar(String usuario, String clave, ResultadoDescargaSri r) throws Exception {
        if (loginToken("sri-en-linea", usuario, clave)
                || loginToken("sri-en-linea-publico", usuario, clave)
                || loginJson(usuario, clave)
                || loginForm(usuario, clave)) {
            r.addDetalle("Sesión SRI en línea iniciada.");
            return;
        }
        r.addDetalle("Falló autenticación automática (token/formulario).");
    }

    private boolean loginToken(String clientId, String usuario, String clave) throws Exception {
        String body = "grant_type=password&client_id=" + enc(clientId)
                + "&username=" + enc(usuario) + "&password=" + enc(clave);
        HttpResp resp = post("https://srienlinea.sri.gob.ec/auth/realms/general/protocol/openid-connect/token",
                "application/x-www-form-urlencoded; charset=UTF-8", body);
        if (!resp.ok || resp.body == null) {
            return false;
        }
        try {
            JSONObject json = new JSONObject(resp.body);
            if (json.has("access_token")) {
                bearer = json.getString("access_token");
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean loginJson(String usuario, String clave) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("usuario", usuario);
        payload.put("password", clave);
        String[] urls = {
            "https://srienlinea.sri.gob.ec/sri-en-linea/rest/ReceptorGeneral/validar",
            "https://srienlinea.sri.gob.ec/sri-en-linea/SriAutenticacionWeb/iniciarSesionCaptcha"
        };
        for (int i = 0; i < urls.length; i++) {
            HttpResp resp = post(urls[i], "application/json; charset=UTF-8", payload.toString());
            if (resp.ok && resp.body != null && !resp.body.toLowerCase().contains("captcha")
                    && !resp.body.toLowerCase().contains("incorrect")) {
                try {
                    JSONObject json = new JSONObject(resp.body);
                    if (json.has("access_token")) {
                        bearer = json.getString("access_token");
                    }
                } catch (Exception ignored) {
                }
                return true;
            }
        }
        return false;
    }

    private boolean loginForm(String usuario, String clave) throws Exception {
        String encoded = "usuario=" + enc(usuario) + "&password=" + enc(clave);
        HttpResp resp = post("https://srienlinea.sri.gob.ec/sri-en-linea/SriAutenticacionWeb/iniciarSesion",
                "application/x-www-form-urlencoded; charset=UTF-8", encoded);
        return resp.ok && !cookies.isEmpty();
    }

    private String consultarPeriodo(SriTipoDocumento tipo, int anio, int mes, int dia, ResultadoDescargaSri r)
            throws Exception {
        String mesTxt = mes < 10 ? "0" + mes : String.valueOf(mes);
        String diaTxt = dia < 10 ? "0" + dia : String.valueOf(dia);
        Integer codPortal = tipo.getStdCodigoPortal();
        String[] urls = {
            "https://srienlinea.sri.gob.ec/comprobantes-electronicos-internet/rest/ReporteComprobantesRecibidos/obtenerCompRecibidos"
            + "?anio=" + anio + "&mes=" + mes + "&dia=" + dia + "&tipoComprobanteCodigo=" + codPortal,
            "https://srienlinea.sri.gob.ec/comprobantes-electronicos-internet/rest/ComprobanteRecibido/obtenerComprobantes"
            + "?anio=" + anio + "&mes=" + mes + "&dia=" + dia + "&tipoComprobante=" + tipo.getStdCodigo(),
            "https://srienlinea.sri.gob.ec/comprobantes-electronicos-internet/rest/ReporteComprobantesRecibidos/generarTxt"
            + "?anio=" + anio + "&mes=" + mes + "&dia=" + dia + "&tipoComprobanteCodigo=" + codPortal
        };
        for (int i = 0; i < urls.length; i++) {
            HttpResp resp = get(urls[i]);
            if (resp.ok && resp.body != null && resp.body.trim().length() > 20
                    && !resp.body.toLowerCase().contains("login")
                    && !resp.body.toLowerCase().contains("captcha")) {
                r.addDetalle("Día " + diaTxt + "/" + mesTxt + "/" + anio + " ok (" + resp.code + ").");
                return resp.body;
            }
        }
        r.addDetalle("Sin datos el " + diaTxt + "/" + mesTxt + "/" + anio + ".");
        return null;
    }

    private List<SriTxtRecibidosParser.Linea> desdeJson(String raw, SriTipoDocumento tipo) {
        List<SriTxtRecibidosParser.Linea> lista = new ArrayList<SriTxtRecibidosParser.Linea>();
        JSONArray arr = extraerArreglo(raw);
        SriTxtRecibidosParser parser = new SriTxtRecibidosParser();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) {
                continue;
            }
            SriTxtRecibidosParser.Linea l = new SriTxtRecibidosParser.Linea();
            l.tipoComprobante = primerTexto(o, "comprobante", "tipoComprobante", "tipoComprobanteDescripcion");
            if (l.tipoComprobante == null || l.tipoComprobante.isEmpty()) {
                l.tipoComprobante = tipo.getStdNombrePortal();
            }
            l.serie = primerTexto(o, "serieComprobante", "serie", "numeroComprobante");
            l.rucEmisor = primerTexto(o, "rucEmisor", "identificacionEmisor");
            l.razonSocial = primerTexto(o, "razonSocialEmisor", "razonSocial");
            l.fechaEmision = parser.parseFecha(primerTexto(o, "fechaEmision", "fecha"));
            l.fechaAutorizacion = parser.parseFecha(primerTexto(o, "fechaAutorizacion"));
            l.tipoEmision = primerTexto(o, "tipoEmision");
            l.identificacionReceptor = primerTexto(o, "identificacionReceptor", "ruc");
            l.claveAcceso = primerTexto(o, "claveAcceso", "claveDeAcceso");
            l.autorizacion = primerTexto(o, "numeroAutorizacion", "autorizacion", "numeroAutorizacionComprobante");
            l.total = primerTexto(o, "importeTotal", "total", "valor");
            if (l.claveAcceso != null && l.claveAcceso.length() >= 20) {
                lista.add(l);
            }
        }
        return lista;
    }

    private JSONArray extraerArreglo(String raw) {
        String t = raw.trim();
        try {
            if (t.startsWith("[")) {
                return new JSONArray(t);
            }
            JSONObject o = new JSONObject(t);
            String[] keys = {"comprobantes", "data", "lista", "resultado", "content", "items"};
            for (int i = 0; i < keys.length; i++) {
                if (o.has(keys[i]) && o.get(keys[i]) instanceof JSONArray) {
                    return o.getJSONArray(keys[i]);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.FINE, raw, e);
        }
        return new JSONArray();
    }

    private String primerTexto(JSONObject o, String... keys) {
        for (int i = 0; i < keys.length; i++) {
            if (o.has(keys[i]) && !o.isNull(keys[i])) {
                try {
                    return String.valueOf(o.get(keys[i])).trim();
                } catch (JSONException ex) {
                    LOG.log(Level.FINE, keys[i], ex);
                }
            }
        }
        return "";
    }

    private void filtrarRango(List<SriTxtRecibidosParser.Linea> lista, Date inicio, Date fin) {
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
        Iterator<SriTxtRecibidosParser.Linea> it = lista.iterator();
        while (it.hasNext()) {
            SriTxtRecibidosParser.Linea l = it.next();
            if (l.fechaEmision != null && (l.fechaEmision.before(di) || l.fechaEmision.after(df))) {
                it.remove();
            }
        }
    }

    private List<int[]> diasEntre(Date inicio, Date fin) {
        List<int[]> lista = new ArrayList<int[]>();
        Calendar c = Calendar.getInstance();
        c.setTime(inicio);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(fin);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        int guard = 0;
        while (!c.after(end) && guard < 62) {
            lista.add(new int[]{c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)});
            c.add(Calendar.DAY_OF_MONTH, 1);
            guard++;
        }
        return lista;
    }

    private boolean pareceJson(String s) {
        String t = s.trim();
        return t.startsWith("{") || t.startsWith("[");
    }

    private HttpResp get(String url) throws Exception {
        return ejecutar("GET", url, null, null);
    }

    private HttpResp post(String url, String contentType, String body) throws Exception {
        return ejecutar("POST", url, contentType, body);
    }

    private HttpResp ejecutar(String metodo, String urlTxt, String contentType, String body) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlTxt).openConnection();
        conn.setRequestMethod(metodo);
        conn.setConnectTimeout(25000);
        conn.setReadTimeout(60000);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("User-Agent", UA);
        conn.setRequestProperty("Accept", "application/json, text/plain, */*");
        if (bearer != null) {
            conn.setRequestProperty("Authorization", "Bearer " + bearer);
        }
        if (!cookies.isEmpty()) {
            conn.setRequestProperty("Cookie", cookieHeader());
        }
        if (body != null) {
            byte[] data = body.getBytes(Charset.forName("UTF-8"));
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream os = conn.getOutputStream();
            os.write(data);
            os.close();
        }
        guardarCookies(conn);
        int code = conn.getResponseCode();
        InputStream in = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
        String txt = leer(in);
        conn.disconnect();
        HttpResp r = new HttpResp();
        r.code = code;
        r.body = txt;
        r.ok = code >= 200 && code < 300;
        return r;
    }

    private void guardarCookies(HttpURLConnection conn) {
        List<String> set = conn.getHeaderFields().get("Set-Cookie");
        if (set == null) {
            set = conn.getHeaderFields().get("set-cookie");
        }
        if (set == null) {
            return;
        }
        for (String raw : set) {
            if (raw == null || raw.trim().isEmpty()) {
                continue;
            }
            String parte = raw.split(";", 2)[0];
            int eq = parte.indexOf('=');
            if (eq > 0) {
                cookies.put(parte.substring(0, eq).trim(), parte.substring(eq + 1).trim());
            }
        }
    }

    private String cookieHeader() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : cookies.entrySet()) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(e.getKey()).append('=').append(e.getValue());
        }
        return sb.toString();
    }

    private String leer(InputStream in) throws Exception {
        if (in == null) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        br.close();
        return sb.toString();
    }

    private String enc(String v) throws Exception {
        return URLEncoder.encode(v, "UTF-8");
    }

    private static class HttpResp {
        int code;
        String body;
        boolean ok;
    }
}
