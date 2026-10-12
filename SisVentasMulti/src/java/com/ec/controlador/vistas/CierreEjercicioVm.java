package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.partida.CuEjercicio;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioContabilidad;
import com.ec.untilitario.ModeloLibroMayor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

public class CierreEjercicioVm {

    private final ServicioContabilidad servicio = new ServicioContabilidad();
    private final Tipoambiente ambiente;
    private Integer anio;
    private String estado = "ABIERTO";
    private String mensaje = "";
    private List<ModeloLibroMayor> preview = new ArrayList<ModeloLibroMayor>();

    public CierreEjercicioVm() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        ambiente = cre != null ? cre.getTipoambiente() : null;
        anio = Calendar.getInstance().get(Calendar.YEAR) - 1;
        refrescar();
    }

    @Command
    @NotifyChange({"estado", "mensaje", "preview", "cerrado"})
    public void buscar() {
        refrescar();
    }

    @Command
    @NotifyChange({"estado", "mensaje", "preview", "cerrado"})
    public void cerrar() {
        try {
            mensaje = servicio.cerrarEjercicio(ambiente, anio);
            Clients.showNotification(mensaje, Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 5000, true);
            refrescar();
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 5000, true);
        }
    }

    @Command
    @NotifyChange({"estado", "mensaje", "preview", "cerrado"})
    public void reabrir() {
        try {
            servicio.reabrirEjercicio(ambiente, anio);
            mensaje = "Ejercicio " + anio + " reabierto. Se eliminó el asiento de cierre.";
            Clients.showNotification(mensaje, Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 4000, true);
            refrescar();
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 5000, true);
        }
    }

    private void refrescar() {
        preview = new ArrayList<ModeloLibroMayor>();
        List<ModeloLibroMayor> mayor = servicio.previewCierre(ambiente, anio);
        for (int i = 0; i < mayor.size(); i++) {
            String c = mayor.get(i).getCodigo();
            if (c != null && c.length() > 0) {
                char ch = c.charAt(0);
                if (ch == '4' || ch == '5' || ch == '6') {
                    preview.add(mayor.get(i));
                }
            }
        }
        CuEjercicio eje = servicio.findEjercicio(ambiente, anio);
        estado = eje != null && eje.isCerrado() ? "CERRADO" : "ABIERTO";
        if ("ABIERTO".equals(estado)) {
            mensaje = "El año está abierto. Cerrar genera el asiento CIERRE (origen CIERRE) y bloquea nuevos asientos de " + anio + ".";
        }
    }

    public boolean isCerrado() {
        return "CERRADO".equals(estado);
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<ModeloLibroMayor> getPreview() {
        return preview;
    }
}
