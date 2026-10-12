package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioContabilidad;
import com.ec.untilitario.ExportContabilidad;
import com.ec.untilitario.ModeloFlujoLinea;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

public class FlujoEfectivoVm {

    private final ServicioContabilidad servicio = new ServicioContabilidad();
    private final Tipoambiente ambiente;
    private Date inicio;
    private Date fin;
    private List<ModeloFlujoLinea> lista = new ArrayList<ModeloFlujoLinea>();
    private BigDecimal totalIngreso = BigDecimal.ZERO;
    private BigDecimal totalEgreso = BigDecimal.ZERO;

    public FlujoEfectivoVm() {
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
    @NotifyChange({"lista", "totalIngreso", "totalEgreso", "resumen"})
    public void buscar() {
        lista = servicio.findFlujoEfectivo(inicio, fin, ambiente);
        totalIngreso = BigDecimal.ZERO;
        totalEgreso = BigDecimal.ZERO;
        for (int i = 0; i < lista.size(); i++) {
            totalIngreso = totalIngreso.add(nvl(lista.get(i).getIngreso()));
            totalEgreso = totalEgreso.add(nvl(lista.get(i).getEgreso()));
        }
    }

    @Command
    public void exportarExcel() {
        try {
            ExportContabilidad.excelFlujo(inicio, fin, lista, totalIngreso, totalEgreso);
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    @Command
    public void exportarPdf() {
        try {
            ExportContabilidad.html("flujo_efectivo.html",
                    "Flujo de efectivo (directo) — movimientos de CAJA",
                    ExportContabilidad.tablaFlujo(lista));
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    private BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    public String getResumen() {
        return "Ingresos de caja " + totalIngreso + "  |  Egresos " + totalEgreso
                + "  |  Neto " + totalIngreso.subtract(totalEgreso);
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

    public List<ModeloFlujoLinea> getLista() {
        return lista;
    }

    public BigDecimal getTotalIngreso() {
        return totalIngreso;
    }

    public BigDecimal getTotalEgreso() {
        return totalEgreso;
    }
}
