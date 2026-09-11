package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.partida.CuAsientoDetalle;
import com.ec.entidad.partida.CuSubCuenta;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioContabilidad;
import com.ec.untilitario.ModeloLineaAsiento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

public class AsientoManualVm {

    private final ServicioContabilidad servicio = new ServicioContabilidad();
    private final Tipoambiente ambiente;
    private Date fecha = new Date();
    private String glosa = "";
    private List<ModeloLineaAsiento> lineas = new ArrayList<ModeloLineaAsiento>();
    private BigDecimal totalDebe = BigDecimal.ZERO;
    private BigDecimal totalHaber = BigDecimal.ZERO;

    public AsientoManualVm() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        ambiente = cre != null ? cre.getTipoambiente() : null;
        for (int i = 0; i < 4; i++) {
            lineas.add(new ModeloLineaAsiento());
        }
    }

    @Command
    @NotifyChange("lineas")
    public void agregarLinea() {
        lineas.add(new ModeloLineaAsiento());
    }

    @Command
    @NotifyChange({"totalDebe", "totalHaber", "resumenTotales"})
    public void recalcular() {
        sumar();
    }

    @Command
    @NotifyChange({"lineas", "glosa", "fecha", "totalDebe", "totalHaber", "resumenTotales"})
    public void guardar() {
        try {
            sumar();
            if (totalDebe.compareTo(totalHaber) != 0) {
                Clients.showNotification("El debe y el haber no cuadran.",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3500, true);
                return;
            }
            List<CuAsientoDetalle> det = new ArrayList<CuAsientoDetalle>();
            for (int i = 0; i < lineas.size(); i++) {
                ModeloLineaAsiento m = lineas.get(i);
                if (m.getCodigo() == null || m.getCodigo().trim().length() == 0) {
                    continue;
                }
                CuSubCuenta cuenta = servicio.buscarSubcuenta(m.getCodigo().trim(), ambiente);
                if (cuenta == null) {
                    throw new RuntimeException("No existe la cuenta " + m.getCodigo() + " en el plan de esta empresa.");
                }
                m.setNombre(cuenta.getSubcNombre());
                BigDecimal debe = m.getDebe() != null ? m.getDebe() : BigDecimal.ZERO;
                BigDecimal haber = m.getHaber() != null ? m.getHaber() : BigDecimal.ZERO;
                if (debe.compareTo(BigDecimal.ZERO) == 0 && haber.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                CuAsientoDetalle d = new CuAsientoDetalle();
                d.setIdSubCuenta(cuenta);
                d.setDetDebe(debe);
                d.setDetHaber(haber);
                d.setDetGlosa(glosa);
                det.add(d);
            }
            servicio.guardarAsientoManual(ambiente, fecha, glosa, det);
            lineas = new ArrayList<ModeloLineaAsiento>();
            for (int i = 0; i < 4; i++) {
                lineas.add(new ModeloLineaAsiento());
            }
            glosa = "";
            fecha = new Date();
            totalDebe = BigDecimal.ZERO;
            totalHaber = BigDecimal.ZERO;
            Clients.showNotification("Asiento registrado en el diario de esta empresa.",
                    Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 3000, true);
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    private void sumar() {
        totalDebe = BigDecimal.ZERO;
        totalHaber = BigDecimal.ZERO;
        for (int i = 0; i < lineas.size(); i++) {
            ModeloLineaAsiento m = lineas.get(i);
            if (m.getDebe() != null) {
                totalDebe = totalDebe.add(m.getDebe());
            }
            if (m.getHaber() != null) {
                totalHaber = totalHaber.add(m.getHaber());
            }
        }
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

    public List<ModeloLineaAsiento> getLineas() {
        return lineas;
    }

    public void setLineas(List<ModeloLineaAsiento> lineas) {
        this.lineas = lineas;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public String getResumenTotales() {
        return "Debe " + totalDebe + "   Haber " + totalHaber;
    }
}
