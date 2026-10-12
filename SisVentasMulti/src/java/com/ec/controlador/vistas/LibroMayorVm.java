package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioContabilidad;
import com.ec.untilitario.ExportContabilidad;
import com.ec.untilitario.ModeloLibroMayor;
import com.ec.untilitario.ModeloLibroMayorMov;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

public class LibroMayorVm {

    private final ServicioContabilidad servicio = new ServicioContabilidad();
    private final Tipoambiente ambiente;
    private Date inicio;
    private Date fin;
    private String filtroCuenta = "";
    private List<ModeloLibroMayor> lista = new ArrayList<ModeloLibroMayor>();
    private List<ModeloLibroMayorMov> detalle = new ArrayList<ModeloLibroMayorMov>();
    private String cuentaDetalle = "";

    public LibroMayorVm() {
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
    @NotifyChange({"lista", "detalle", "cuentaDetalle"})
    public void buscar() {
        lista = servicio.findMayor(inicio, fin, ambiente, filtroCuenta);
        detalle = new ArrayList<ModeloLibroMayorMov>();
        cuentaDetalle = "";
    }

    @Command
    @NotifyChange({"detalle", "cuentaDetalle"})
    public void seleccionar(@BindingParam("valor") ModeloLibroMayor valor) {
        if (valor == null) {
            detalle = new ArrayList<ModeloLibroMayorMov>();
            cuentaDetalle = "";
            return;
        }
        cuentaDetalle = valor.getCodigo() + " — " + valor.getNombre();
        detalle = valor.getMovimientos();
    }

    @Command
    public void exportarExcel() {
        try {
            ExportContabilidad.excelMayor(inicio, fin, lista);
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    @Command
    public void exportarPdf() {
        try {
            ExportContabilidad.html("mayor_auxiliar.html",
                    "Mayor auxiliar por cuenta (la glosa identifica el documento)",
                    ExportContabilidad.tablaMayor(lista));
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
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

    public String getFiltroCuenta() {
        return filtroCuenta;
    }

    public void setFiltroCuenta(String filtroCuenta) {
        this.filtroCuenta = filtroCuenta;
    }

    public List<ModeloLibroMayor> getLista() {
        return lista;
    }

    public void setLista(List<ModeloLibroMayor> lista) {
        this.lista = lista;
    }

    public List<ModeloLibroMayorMov> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<ModeloLibroMayorMov> detalle) {
        this.detalle = detalle;
    }

    public String getCuentaDetalle() {
        return cuentaDetalle;
    }

    public void setCuentaDetalle(String cuentaDetalle) {
        this.cuentaDetalle = cuentaDetalle;
    }
}
