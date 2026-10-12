package com.ec.untilitario;

import com.ec.entidad.Tipoambiente;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 * Genera QR Deuna (Payvalida Ecuador) con el monto de la venta.
 * Si hay comercio y hash, llama a la API; si no, arma un QR local con el total.
 */
public class ClienteDeuna {

    private static final Logger LOG = Logger.getLogger(ClienteDeuna.class.getName());
    private static final String UA = "Defact-Deuna/1.0";
    private static final String TEST = "https://api-test.payvalida.com";
    private static final String PROD = "https://api.payvalida.com";

    public static ResultadoQrDeuna generarQr(Tipoambiente amb, BigDecimal total, String referencia) {
        ResultadoQrDeuna r = new ResultadoQrDeuna();
        BigDecimal monto = total == null ? BigDecimal.ZERO : total.setScale(2, RoundingMode.HALF_UP);
        r.setMonto(monto);
        String poId = sanitizarPoId(referencia);
        r.setPoId(poId);

        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            r.setMensaje("El total debe ser mayor a cero.");
            return r;
        }

        String merchant = texto(amb == null ? null : amb.getAmDeunaMerchant());
        String hash = texto(amb == null ? null : amb.getAmDeunaHash());
        String email = texto(amb == null ? null : amb.getAmDeunaEmail());
        if (email.isEmpty() && amb != null) {
            email = texto(amb.getAmUsuarioSmpt());
        }
        if (email.isEmpty()) {
            email = "notificaciones@defact.local";
        }

        if (!merchant.isEmpty() && !hash.isEmpty()) {
            try {
                consultarApi(amb, merchant, hash, email, poId, monto, r);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Deuna API", e);
                r.setMensaje("Payvalida no respondio (" + e.getMessage() + "). Se genera QR local con el total.");
            }
        } else {
            r.setMensaje("Configure comercio y hash Deuna en Configuracion SRI para cobrar en linea. Se muestra QR con el total.");
        }

        if (r.getImagen() == null) {
            String payload = r.getDeepLink() != null && !r.getDeepLink().isEmpty()
                    ? r.getDeepLink()
                    : payloadLocal(amb, monto, poId);
            r.setImagen(qrLocal(payload, poId));
        }
        return r;
    }

    private static void consultarApi(Tipoambiente amb, String merchant, String hash, String email,
            String poId, BigDecimal monto, ResultadoQrDeuna r) throws Exception {
        String checksum = sha512Hex(merchant + poId + email + hash);
        String base = "PRODUCCION".equalsIgnoreCase(amb.getAmDeunaAmbiente()) ? PROD : TEST;
        String amount = monto.toPlainString();

        JSONObject body = new JSONObject();
        body.put("merchant", merchant);
        body.put("checksum", checksum);
        body.put("poId", poId);
        body.put("notificationEmail", email);
        body.put("amount", amount);
        body.put("money", "USD");
        body.put("currency", "USD");
        if (amb.getAmDeunaCaja() != null && !amb.getAmDeunaCaja().trim().isEmpty()) {
            body.put("caja", amb.getAmDeunaCaja().trim());
        }

        String postUrl = base + "/api/v4/merchants/qr/deuna";
        HttpResp post = http("POST", postUrl, "application/json; charset=UTF-8", body.toString());
        if (aplicarRespuesta(post, r)) {
            return;
        }

        String q = postUrl
                + "?merchant=" + enc(merchant)
                + "&checksum=" + enc(checksum)
                + "&poId=" + enc(poId)
                + "&notificationEmail=" + enc(email)
                + "&amount=" + enc(amount)
                + "&money=USD";
        HttpResp get = http("GET", q, null, null);
        aplicarRespuesta(get, r);
        if (r.getImagen() == null && r.getMensaje() == null && post.body != null) {
            r.setMensaje(resumenError(post) + " " + resumenError(get));
        }
    }

    private static boolean aplicarRespuesta(HttpResp resp, ResultadoQrDeuna r) {
        if (resp == null) {
            return false;
        }
        if (resp.binario != null && resp.binario.length > 80 && pareceImagen(resp.binario)) {
            r.setImagen(resp.binario);
            r.setDesdeApi(true);
            return true;
        }
        if (resp.body == null || resp.body.trim().isEmpty()) {
            return false;
        }
        String t = resp.body.trim();
        if (t.startsWith("{")) {
            try {
                JSONObject json = new JSONObject(t);
                extraerCampos(json, r);
                if (r.getImagen() != null) {
                    r.setDesdeApi(true);
                    return true;
                }
            } catch (Exception e) {
                LOG.log(Level.FINE, t, e);
            }
        }
        return false;
    }

    private static void extraerCampos(JSONObject json, ResultadoQrDeuna r) {
        String deep = primerTexto(json, "deepLink", "deeplink", "url", "link", "qrUrl");
        if (deep != null) {
            r.setDeepLink(deep);
        }
        String b64 = primerTexto(json, "qr", "qrCode", "qrBase64", "image", "base64", "codigoQr");
        if (b64 != null) {
            byte[] img = decodificarBase64(b64);
            if (img != null) {
                r.setImagen(img);
            }
        }
        Iterator<?> keys = json.keys();
        while (keys.hasNext() && r.getImagen() == null) {
            Object val = json.opt(String.valueOf(keys.next()));
            if (val instanceof JSONObject) {
                extraerCampos((JSONObject) val, r);
            }
        }
    }

    private static String primerTexto(JSONObject o, String... keys) {
        for (int i = 0; i < keys.length; i++) {
            if (o.has(keys[i]) && !o.isNull(keys[i])) {
                String v = String.valueOf(o.opt(keys[i])).trim();
                if (!v.isEmpty() && !"null".equalsIgnoreCase(v)) {
                    return v;
                }
            }
        }
        return null;
    }

    private static byte[] decodificarBase64(String raw) {
        String s = raw.trim();
        int coma = s.indexOf("base64,");
        if (coma >= 0) {
            s = s.substring(coma + 7);
        }
        s = s.replaceAll("\\s+", "");
        try {
            return java.util.Base64.getDecoder().decode(s);
        } catch (Exception e) {
            try {
                return java.util.Base64.getMimeDecoder().decode(s);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    private static boolean pareceImagen(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return (b[0] == (byte) 0x89 && b[1] == 0x50)
                || (b[0] == (byte) 0xFF && b[1] == (byte) 0xD8)
                || (b[0] == 0x47 && b[1] == 0x49);
    }

    private static String payloadLocal(Tipoambiente amb, BigDecimal monto, String poId) {
        String ruc = amb != null && amb.getAmRuc() != null ? amb.getAmRuc() : "";
        String mer = amb != null ? texto(amb.getAmDeunaMerchant()) : "";
        String caja = amb != null ? texto(amb.getAmDeunaCaja()) : "";
        return "DEUNA|RUC:" + ruc
                + "|COMERCIO:" + mer
                + "|CAJA:" + caja
                + "|MONTO:" + monto.toPlainString()
                + "|ORDEN:" + poId
                + "|MONEDA:USD";
    }

    private static byte[] qrLocal(String payload, String poId) {
        File tmp = new File(System.getProperty("java.io.tmpdir"), "deuna-" + poId + ".png");
        return CodigoQR.generarCodigoQR(payload, tmp.getAbsolutePath());
    }

    private static String sanitizarPoId(String referencia) {
        String base = referencia == null ? "" : referencia.replaceAll("[^A-Za-z0-9]", "");
        if (base.isEmpty()) {
            base = "DU" + System.currentTimeMillis();
        }
        if (base.length() > 20) {
            base = base.substring(base.length() - 20);
        }
        return base;
    }

    private static String texto(String s) {
        return s == null ? "" : s.trim();
    }

    private static String sha512Hex(String raw) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] d = md.digest(raw.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder(d.length * 2);
        for (int i = 0; i < d.length; i++) {
            sb.append(String.format("%02X", d[i] & 0xff));
        }
        return sb.toString();
    }

    private static String enc(String s) throws Exception {
        return URLEncoder.encode(s, "UTF-8");
    }

    private static String resumenError(HttpResp resp) {
        if (resp == null) {
            return "";
        }
        String b = resp.body == null ? "" : resp.body;
        if (b.length() > 180) {
            b = b.substring(0, 180);
        }
        return "HTTP " + resp.code + " " + b;
    }

    private static HttpResp http(String metodo, String urlTxt, String contentType, String body) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlTxt).openConnection();
        conn.setRequestMethod(metodo);
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(30000);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("User-Agent", UA);
        conn.setRequestProperty("Accept", "application/json, image/png, image/*, */*");
        if (body != null) {
            byte[] data = body.getBytes(Charset.forName("UTF-8"));
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream os = conn.getOutputStream();
            os.write(data);
            os.close();
        }
        int code = conn.getResponseCode();
        InputStream in = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
        byte[] raw = leer(in);
        HttpResp r = new HttpResp();
        r.code = code;
        String ct = conn.getContentType() == null ? "" : conn.getContentType().toLowerCase();
        if (ct.contains("image") || pareceImagen(raw)) {
            r.binario = raw;
        } else {
            r.body = new String(raw, Charset.forName("UTF-8"));
        }
        conn.disconnect();
        return r;
    }

    private static byte[] leer(InputStream in) throws Exception {
        if (in == null) {
            return new byte[0];
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf)) >= 0) {
            bos.write(buf, 0, n);
        }
        in.close();
        return bos.toByteArray();
    }

    private static class HttpResp {
        int code;
        String body;
        byte[] binario;
    }
}
