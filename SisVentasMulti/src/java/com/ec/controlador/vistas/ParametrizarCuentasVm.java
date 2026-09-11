package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.partida.CuParametro;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioContabilidad;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

public class ParametrizarCuentasVm {

    private final ServicioContabilidad servicio = new ServicioContabilidad();
    private final Tipoambiente ambiente;
    private List<CuParametro> lista = new ArrayList<CuParametro>();

    public ParametrizarCuentasVm() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        ambiente = cre != null ? cre.getTipoambiente() : null;
        lista = servicio.findAllParametros(ambiente);
    }

    @Command
    @NotifyChange("lista")
    public void guardar() {
        try {
            if (ambiente == null) {
                Clients.showNotification("No hay empresa activa en la sesión.",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
                return;
            }
            for (int i = 0; i < lista.size(); i++) {
                servicio.modificarParametro(lista.get(i), ambiente);
            }
            lista = servicio.findAllParametros(ambiente);
            Clients.showNotification("Cuentas enlazadas para esta empresa. Las nuevas facturas y compras usarán este plan.",
                    Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 3500, true);
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    public List<CuParametro> getLista() {
        return lista;
    }

    public void setLista(List<CuParametro> lista) {
        this.lista = lista;
    }
}
