package com.ec.untilitario;

import com.ec.entidad.Tipoambiente;
import java.math.BigDecimal;
import java.util.HashMap;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class CobroDeunaUi {

    public static void abrir(Tipoambiente amb, BigDecimal total, String referencia) {
        if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
            Clients.showNotification("Ingrese un total mayor a cero para generar el QR Deuna",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3500, true);
            return;
        }
        ResultadoQrDeuna r = ClienteDeuna.generarQr(amb, total, referencia);
        if (r.getImagen() == null) {
            Messagebox.show(r.getMensaje() != null ? r.getMensaje() : "No se pudo generar el QR Deuna.",
                    "Deuna", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        try {
            byte[] pdf = TicketDeunaPdf.generar(amb, r);
            AMedia amedia = new AMedia("qr-deuna", "pdf", "application/pdf", pdf);
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
            map.put("pdf", amedia);
            Window window = (Window) Executions.createComponents("/venta/contenedorReporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("No se pudo generar el PDF del QR Deuna: " + e.getMessage(),
                    "Deuna", Messagebox.OK, Messagebox.ERROR);
        }
    }
}
