package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.partida.CuAsiento;
import com.ec.entidad.partida.CuAsientoDetalle;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioContabilidad;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class LibroDiarioVm {

    private final ServicioContabilidad servicio = new ServicioContabilidad();
    private final Tipoambiente ambiente;
    private Date inicio;
    private Date fin;
    private List<CuAsiento> lista = new ArrayList<CuAsiento>();
    private List<CuAsientoDetalle> detalle = new ArrayList<CuAsientoDetalle>();

    public LibroDiarioVm() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        ambiente = cre != null ? cre.getTipoambiente() : null;
        Calendar cal = Calendar.getInstance();
        fin = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        inicio = cal.getTime();
        buscar();
    }

    @Command
    @NotifyChange({"lista", "detalle"})
    public void buscar() {
        lista = servicio.findAsientos(inicio, fin, ambiente);
        detalle = new ArrayList<CuAsientoDetalle>();
    }

    @Command
    @NotifyChange("detalle")
    public void seleccionar(@BindingParam("valor") CuAsiento valor) {
        detalle = servicio.findDetalle(valor, ambiente);
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

    public List<CuAsiento> getLista() {
        return lista;
    }

    public void setLista(List<CuAsiento> lista) {
        this.lista = lista;
    }

    public List<CuAsientoDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<CuAsientoDetalle> detalle) {
        this.detalle = detalle;
    }
}
