package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioContabilidad;
import com.ec.untilitario.ExportContabilidad;
import com.ec.untilitario.ModeloEstadoLinea;
import com.ec.untilitario.ModeloLibroMayor;
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

public class EstadosFinancierosVm {

    private final ServicioContabilidad servicio = new ServicioContabilidad();
    private final Tipoambiente ambiente;
    private Date inicio;
    private Date fin;
    private String vista = "COMP";

    private List<ModeloLibroMayor> comprobacion = new ArrayList<ModeloLibroMayor>();
    private List<ModeloEstadoLinea> situacion = new ArrayList<ModeloEstadoLinea>();
    private List<ModeloEstadoLinea> resultados = new ArrayList<ModeloEstadoLinea>();

    private BigDecimal totalDebe = BigDecimal.ZERO;
    private BigDecimal totalHaber = BigDecimal.ZERO;
    private BigDecimal totalDeudor = BigDecimal.ZERO;
    private BigDecimal totalAcreedor = BigDecimal.ZERO;
    private BigDecimal totalActivo = BigDecimal.ZERO;
    private BigDecimal totalPasivoPatrimonio = BigDecimal.ZERO;
    private BigDecimal utilidad = BigDecimal.ZERO;
    private String mensajeCuadre = "";

    public EstadosFinancierosVm() {
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
    @NotifyChange({"comprobacion", "situacion", "resultados", "totalDebe", "totalHaber",
        "totalDeudor", "totalAcreedor", "totalActivo", "totalPasivoPatrimonio", "utilidad",
        "mensajeCuadre", "resumenComprobacion", "resumenSituacion", "resumenResultados"})
    public void buscar() {
        comprobacion = servicio.findMayor(inicio, fin, ambiente, "");
        totalDebe = BigDecimal.ZERO;
        totalHaber = BigDecimal.ZERO;
        totalDeudor = BigDecimal.ZERO;
        totalAcreedor = BigDecimal.ZERO;
        BigDecimal activo = BigDecimal.ZERO;
        BigDecimal pasivo = BigDecimal.ZERO;
        BigDecimal patrimonio = BigDecimal.ZERO;
        BigDecimal ingresos = BigDecimal.ZERO;
        BigDecimal costos = BigDecimal.ZERO;
        BigDecimal gastos = BigDecimal.ZERO;
        situacion = new ArrayList<ModeloEstadoLinea>();
        resultados = new ArrayList<ModeloEstadoLinea>();
        List<ModeloEstadoLinea> linAct = new ArrayList<ModeloEstadoLinea>();
        List<ModeloEstadoLinea> linPas = new ArrayList<ModeloEstadoLinea>();
        List<ModeloEstadoLinea> linPat = new ArrayList<ModeloEstadoLinea>();
        List<ModeloEstadoLinea> linIng = new ArrayList<ModeloEstadoLinea>();
        List<ModeloEstadoLinea> linCos = new ArrayList<ModeloEstadoLinea>();
        List<ModeloEstadoLinea> linGas = new ArrayList<ModeloEstadoLinea>();
        for (int i = 0; i < comprobacion.size(); i++) {
            ModeloLibroMayor m = comprobacion.get(i);
            totalDebe = totalDebe.add(nvl(m.getDebe()));
            totalHaber = totalHaber.add(nvl(m.getHaber()));
            totalDeudor = totalDeudor.add(nvl(m.getSaldoDeudor()));
            totalAcreedor = totalAcreedor.add(nvl(m.getSaldoAcreedor()));
            String codigo = m.getCodigo() != null ? m.getCodigo() : "";
            char clase = codigo.length() > 0 ? codigo.charAt(0) : ' ';
            BigDecimal saldo = nvl(m.getSaldoFinal());
            if (clase == '1') {
                activo = activo.add(saldo);
                linAct.add(new ModeloEstadoLinea(codigo, m.getNombre(), saldo));
            } else if (clase == '2') {
                pasivo = pasivo.add(saldo);
                linPas.add(new ModeloEstadoLinea(codigo, m.getNombre(), saldo));
            } else if (clase == '3') {
                patrimonio = patrimonio.add(saldo);
                linPat.add(new ModeloEstadoLinea(codigo, m.getNombre(), saldo));
            } else if (clase == '4') {
                ingresos = ingresos.add(saldo);
                linIng.add(new ModeloEstadoLinea(codigo, m.getNombre(), saldo));
            } else if (clase == '5') {
                costos = costos.add(saldo);
                linCos.add(new ModeloEstadoLinea(codigo, m.getNombre(), saldo));
            } else if (clase == '6') {
                gastos = gastos.add(saldo);
                linGas.add(new ModeloEstadoLinea(codigo, m.getNombre(), saldo));
            }
        }
        utilidad = ingresos.subtract(costos).subtract(gastos);
        totalActivo = activo;
        totalPasivoPatrimonio = pasivo.add(patrimonio).add(utilidad);

        situacion.add(new ModeloEstadoLinea("ACTIVO", true));
        situacion.addAll(linAct);
        situacion.add(ModeloEstadoLinea.totalDe("Total activo", activo));
        situacion.add(new ModeloEstadoLinea("PASIVO", true));
        situacion.addAll(linPas);
        situacion.add(ModeloEstadoLinea.totalDe("Total pasivo", pasivo));
        situacion.add(new ModeloEstadoLinea("PATRIMONIO", true));
        situacion.addAll(linPat);
        situacion.add(new ModeloEstadoLinea("", "Resultado del ejercicio", utilidad));
        situacion.add(ModeloEstadoLinea.totalDe("Total patrimonio", patrimonio.add(utilidad)));
        situacion.add(ModeloEstadoLinea.totalDe("Total pasivo + patrimonio", totalPasivoPatrimonio));

        resultados.add(new ModeloEstadoLinea("INGRESOS", true));
        resultados.addAll(linIng);
        resultados.add(ModeloEstadoLinea.totalDe("Total ingresos", ingresos));
        resultados.add(new ModeloEstadoLinea("COSTOS", true));
        resultados.addAll(linCos);
        resultados.add(ModeloEstadoLinea.totalDe("Total costos", costos));
        resultados.add(new ModeloEstadoLinea("GASTOS", true));
        resultados.addAll(linGas);
        resultados.add(ModeloEstadoLinea.totalDe("Total gastos", gastos));
        resultados.add(ModeloEstadoLinea.totalDe(utilidad.compareTo(BigDecimal.ZERO) >= 0
                ? "Utilidad del ejercicio" : "Pérdida del ejercicio", utilidad));

        if (totalDebe.compareTo(totalHaber) == 0 && totalActivo.compareTo(totalPasivoPatrimonio) == 0) {
            mensajeCuadre = "Cuadra: sumas del periodo y activo = pasivo + patrimonio (incluye resultado).";
        } else if (totalDebe.compareTo(totalHaber) != 0) {
            mensajeCuadre = "Las sumas debe/haber del periodo no cuadran. Revise asientos incompletos.";
        } else {
            mensajeCuadre = "El activo no iguala pasivo + patrimonio. Faltan asientos o hay cuentas sin naturaleza.";
        }
    }

    @Command
    @NotifyChange({"vista", "comprobacionVisible", "situacionVisible", "resultadosVisible"})
    public void verComprobacion() {
        vista = "COMP";
    }

    @Command
    @NotifyChange({"vista", "comprobacionVisible", "situacionVisible", "resultadosVisible"})
    public void verSituacion() {
        vista = "SIT";
    }

    @Command
    @NotifyChange({"vista", "comprobacionVisible", "situacionVisible", "resultadosVisible"})
    public void verResultados() {
        vista = "RES";
    }

    @Command
    public void exportarExcel() {
        try {
            if ("SIT".equals(vista)) {
                ExportContabilidad.excelEstados("Situacion financiera", inicio, fin, situacion);
            } else if ("RES".equals(vista)) {
                ExportContabilidad.excelEstados("Estado de resultados", inicio, fin, resultados);
            } else {
                ExportContabilidad.excelComprobacion("Balance de comprobacion", inicio, fin, comprobacion,
                        totalDebe, totalHaber, totalDeudor, totalAcreedor);
            }
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    @Command
    public void exportarPdf() {
        try {
            String titulo;
            String tabla;
            if ("SIT".equals(vista)) {
                titulo = "Situacion financiera";
                tabla = ExportContabilidad.tablaEstado(situacion);
            } else if ("RES".equals(vista)) {
                titulo = "Estado de resultados";
                tabla = ExportContabilidad.tablaEstado(resultados);
            } else {
                titulo = "Balance de comprobacion";
                tabla = ExportContabilidad.tablaComprobacion(comprobacion);
            }
            ExportContabilidad.html("estado_financiero.html", titulo, tabla);
        } catch (Exception e) {
            Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 4000, true);
        }
    }

    public boolean isComprobacionVisible() {
        return "COMP".equals(vista);
    }

    public boolean isSituacionVisible() {
        return "SIT".equals(vista);
    }

    public boolean isResultadosVisible() {
        return "RES".equals(vista);
    }

    private BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
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

    public String getVista() {
        return vista;
    }

    public List<ModeloLibroMayor> getComprobacion() {
        return comprobacion;
    }

    public List<ModeloEstadoLinea> getSituacion() {
        return situacion;
    }

    public List<ModeloEstadoLinea> getResultados() {
        return resultados;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public BigDecimal getTotalDeudor() {
        return totalDeudor;
    }

    public BigDecimal getTotalAcreedor() {
        return totalAcreedor;
    }

    public BigDecimal getTotalActivo() {
        return totalActivo;
    }

    public BigDecimal getTotalPasivoPatrimonio() {
        return totalPasivoPatrimonio;
    }

    public BigDecimal getUtilidad() {
        return utilidad;
    }

    public String getMensajeCuadre() {
        return mensajeCuadre;
    }

    public String getResumenComprobacion() {
        return "Totales periodo  Debe " + totalDebe + "   Haber " + totalHaber
                + "  |  Saldos deudor " + totalDeudor + "   acreedor " + totalAcreedor;
    }

    public String getResumenSituacion() {
        return "Activo " + totalActivo + "  |  Pasivo + patrimonio " + totalPasivoPatrimonio;
    }

    public String getResumenResultados() {
        return "Resultado del ejercicio: " + utilidad;
    }
}
