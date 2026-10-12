package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioContabilidad;
import com.ec.untilitario.ExportContabilidad;
import com.ec.untilitario.ModeloComparativo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

public class ComparativoContableVm {

    private final ServicioContabilidad servicio = new ServicioContabilidad();
    private final Tipoambiente ambiente;
    private Date inicioA;
    private Date finA;
    private Date inicioB;
    private Date finB;
    private List<ModeloComparativo> lista = new ArrayList<ModeloComparativo>();

    public ComparativoContableVm() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        ambiente = cre != null ? cre.getTipoambiente() : null;
        Calendar cal = Calendar.getInstance();
        finB = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        inicioB = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        finA = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        inicioA = cal.getTime();
        buscar();
    }

    @Command
    @NotifyChange("lista")
    public void buscar() {
        lista = servicio.findComparativo(inicioA, finA, inicioB, finB, ambiente);
    }

    @Command
    public void exportarExcel() {
        try {
            ExportContabilidad.excelComparativo(etiquetaA(), etiquetaB(), lista);
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    @Command
    public void exportarPdf() {
        try {
            ExportContabilidad.html("comparativo.html", "Comparativo de saldos",
                    ExportContabilidad.tablaComparativo(etiquetaA(), etiquetaB(), lista));
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    public String getEtiquetaA() {
        return etiquetaA();
    }

    public String getEtiquetaB() {
        return etiquetaB();
    }

    private String etiquetaA() {
        return "Periodo A";
    }

    private String etiquetaB() {
        return "Periodo B";
    }

    public Date getInicioA() {
        return inicioA;
    }

    public void setInicioA(Date inicioA) {
        this.inicioA = inicioA;
    }

    public Date getFinA() {
        return finA;
    }

    public void setFinA(Date finA) {
        this.finA = finA;
    }

    public Date getInicioB() {
        return inicioB;
    }

    public void setInicioB(Date inicioB) {
        this.inicioB = inicioB;
    }

    public Date getFinB() {
        return finB;
    }

    public void setFinB(Date finB) {
        this.finB = finB;
    }

    public List<ModeloComparativo> getLista() {
        return lista;
    }
}
