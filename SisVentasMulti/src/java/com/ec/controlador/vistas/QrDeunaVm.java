package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.untilitario.ResultadoQrDeuna;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

public class QrDeunaVm {

    private ResultadoQrDeuna resultado;
    private Tipoambiente ambiente;
    private AImage qrImagen;
    private String empresa = "";
    private String ruc = "";
    private String caja = "";
    private String orden = "";
    private String montoTexto = "0.00";
    private String aviso = "";

    @Init
    public void init(@ExecutionArgParam("resultado") ResultadoQrDeuna resultado,
            @ExecutionArgParam("ambiente") Tipoambiente ambiente) {
        this.resultado = resultado;
        this.ambiente = ambiente;
        if (ambiente != null) {
            empresa = ambiente.getAmNombreComercial() != null && !ambiente.getAmNombreComercial().trim().isEmpty()
                    ? ambiente.getAmNombreComercial() : nvl(ambiente.getAmRazonSocial());
            ruc = nvl(ambiente.getAmRuc());
            caja = nvl(ambiente.getAmDeunaCaja());
        }
        if (resultado != null) {
            orden = nvl(resultado.getPoId());
            aviso = nvl(resultado.getMensaje());
            BigDecimal m = resultado.getMonto() == null ? BigDecimal.ZERO : resultado.getMonto().setScale(2, RoundingMode.HALF_UP);
            montoTexto = m.toPlainString();
            if (resultado.getImagen() != null) {
                try {
                    qrImagen = new AImage("deuna.png", resultado.getImagen());
                } catch (Exception e) {
                    Messagebox.show("No se pudo mostrar la imagen del QR: " + e.getMessage(),
                            "Deuna", Messagebox.OK, Messagebox.ERROR);
                }
            }
        }
    }

    @Command
    public void imprimirTicket() {
        if (resultado == null || resultado.getImagen() == null) {
            return;
        }
        String b64 = java.util.Base64.getEncoder().encodeToString(resultado.getImagen());
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        String html = "<!DOCTYPE html><html><head><meta charset='utf-8'><title>QR Deuna</title>"
                + "<style>"
                + "@page{size:80mm auto;margin:3mm;}"
                + "body{width:74mm;margin:0 auto;font-family:Arial,Helvetica,sans-serif;color:#122536;text-align:center;}"
                + "h1{font-size:13px;margin:0 0 4px;text-transform:uppercase;}"
                + ".r{font-size:11px;margin:2px 0;}"
                + ".t{font-size:20px;font-weight:700;margin:8px 0;}"
                + "img{width:58mm;height:58mm;}"
                + "</style></head><body>"
                + "<h1>" + esc(empresa) + "</h1>"
                + "<div class='r'>RUC " + esc(ruc) + "</div>"
                + "<div class='r'>Cobro Deuna</div>"
                + "<div class='t'>$ " + esc(montoTexto) + "</div>"
                + "<img src='data:image/png;base64," + b64 + "'/>"
                + "<div class='r'>Orden " + esc(orden) + "</div>"
                + (caja.isEmpty() ? "" : "<div class='r'>Caja " + esc(caja) + "</div>")
                + "<div class='r'>" + esc(fecha) + "</div>"
                + "<div class='r'>Escanee con la app Deuna</div>"
                + "</body></html>";
        String js = "var w=window.open('','_blank','width=340,height=640');"
                + "w.document.open();w.document.write(" + JSONObject.quote(html)
                + ");w.document.close();setTimeout(function(){w.focus();w.print();},350);";
        Clients.evalJavaScript(js);
    }

    private static String nvl(String s) {
        return s == null ? "" : s.trim();
    }

    private static String esc(String s) {
        return nvl(s).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public AImage getQrImagen() {
        return qrImagen;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getRuc() {
        return ruc;
    }

    public String getCaja() {
        return caja;
    }

    public String getOrden() {
        return orden;
    }

    public String getMontoTexto() {
        return montoTexto;
    }

    public String getAviso() {
        return aviso;
    }

    public String getLineaRuc() {
        return "RUC " + ruc;
    }

    public String getLineaMonto() {
        return "$ " + montoTexto;
    }

    public String getLineaOrden() {
        return "Orden " + orden;
    }

    public String getLineaCaja() {
        return caja.isEmpty() ? "" : "Caja " + caja;
    }

    public boolean isCajaVisible() {
        return !caja.isEmpty();
    }

    public boolean isAvisoVisible() {
        return !aviso.isEmpty();
    }
}
