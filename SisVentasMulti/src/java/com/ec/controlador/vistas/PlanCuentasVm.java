package com.ec.controlador.vistas;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.partida.CuClase;
import com.ec.entidad.partida.CuCuenta;
import com.ec.entidad.partida.CuGrupo;
import com.ec.entidad.partida.CuSubCuenta;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioPlanCuentas;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

public class PlanCuentasVm {

    private final ServicioPlanCuentas servicio = new ServicioPlanCuentas();
    private final Tipoambiente ambiente;

    private List<CuClase> listaClases = new ArrayList<CuClase>();
    private List<CuGrupo> listaGrupos = new ArrayList<CuGrupo>();
    private List<CuCuenta> listaCuentas = new ArrayList<CuCuenta>();
    private List<CuSubCuenta> listaSubcuentas = new ArrayList<CuSubCuenta>();

    private CuClase claseSelected;
    private CuGrupo grupoSelected;
    private CuCuenta cuentaSelected;
    private CuSubCuenta subSelected;

    private CuClase claseForm = new CuClase();
    private CuGrupo grupoForm = new CuGrupo();
    private CuCuenta cuentaForm = new CuCuenta();
    private CuSubCuenta subForm = new CuSubCuenta();

    public PlanCuentasVm() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        ambiente = cre != null ? cre.getTipoambiente() : null;
        recargarClases();
        listaGrupos = servicio.findAllGrupos(ambiente);
        listaCuentas = servicio.findAllCuentas(ambiente);
    }

    private void recargarClases() {
        listaClases = servicio.findAllClases(ambiente);
    }

    private void cero(CuClase c) {
        if (c.getClasTotal() == null) {
            c.setClasTotal(BigDecimal.ZERO);
        }
        if (c.getClasSaldo() == null) {
            c.setClasSaldo(BigDecimal.ZERO);
        }
    }

    private void cero(CuGrupo c) {
        if (c.getGrupTotal() == null) {
            c.setGrupTotal(BigDecimal.ZERO);
        }
        if (c.getGrupSaldo() == null) {
            c.setGrupSaldo(BigDecimal.ZERO);
        }
    }

    private void cero(CuCuenta c) {
        if (c.getGrupTotal() == null) {
            c.setGrupTotal(BigDecimal.ZERO);
        }
        if (c.getGrupSaldo() == null) {
            c.setGrupSaldo(BigDecimal.ZERO);
        }
    }

    private void cero(CuSubCuenta c) {
        if (c.getSubcTotal() == null) {
            c.setSubcTotal(BigDecimal.ZERO);
        }
        if (c.getSubcSaldo() == null) {
            c.setSubcSaldo(BigDecimal.ZERO);
        }
    }

    @Command
    @NotifyChange({"listaGrupos", "listaCuentas", "listaSubcuentas", "claseForm", "grupoForm"})
    public void seleccionarClase(@BindingParam("valor") CuClase valor) {
        claseSelected = valor;
        claseForm = valor;
        listaGrupos = servicio.findGruposPorClase(valor.getIdClase());
        listaCuentas = new ArrayList<CuCuenta>();
        listaSubcuentas = new ArrayList<CuSubCuenta>();
        grupoForm = new CuGrupo();
        grupoForm.setIdClase(valor.getIdClase());
        cuentaSelected = null;
        subSelected = null;
    }

    @Command
    @NotifyChange({"listaCuentas", "listaSubcuentas", "grupoForm", "cuentaForm"})
    public void seleccionarGrupo(@BindingParam("valor") CuGrupo valor) {
        grupoSelected = valor;
        grupoForm = valor;
        if (valor.getIdClase() != null) {
            for (CuClase c : listaClases) {
                if (c.getIdClase().equals(valor.getIdClase())) {
                    claseSelected = c;
                    break;
                }
            }
        }
        listaCuentas = servicio.findCuentasPorGrupo(valor);
        listaSubcuentas = new ArrayList<CuSubCuenta>();
        cuentaForm = new CuCuenta();
        cuentaForm.setIdGrupo(valor);
        cuentaSelected = null;
        subSelected = null;
    }

    @Command
    @NotifyChange({"listaSubcuentas", "cuentaForm", "subForm"})
    public void seleccionarCuenta(@BindingParam("valor") CuCuenta valor) {
        cuentaSelected = valor;
        cuentaForm = valor;
        grupoSelected = valor.getIdGrupo();
        listaSubcuentas = servicio.findSubcuentasPorCuenta(valor);
        subForm = new CuSubCuenta();
        subForm.setIdCuenta(valor);
        subSelected = null;
    }

    @Command
    @NotifyChange("subForm")
    public void seleccionarSubcuenta(@BindingParam("valor") CuSubCuenta valor) {
        subSelected = valor;
        subForm = valor;
    }

    @Command
    @NotifyChange("claseForm")
    public void nuevaClase() {
        claseForm = new CuClase();
        claseSelected = null;
    }

    @Command
    @NotifyChange({"listaClases", "claseForm"})
    public void guardarClase() {
        if (vacio(claseForm.getClasNumero()) || vacio(claseForm.getClasNombre())) {
            Clients.showNotification("Ingrese código y nombre de la clase",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2500, true);
            return;
        }
        cero(claseForm);
        if (claseForm.getIdClase() == null) {
            servicio.crearClase(claseForm, ambiente);
            Clients.showNotification("Clase registrada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
        } else {
            servicio.modificarClase(claseForm, ambiente);
            Clients.showNotification("Clase actualizada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
        }
        recargarClases();
        claseForm = new CuClase();
    }

    @Command
    @NotifyChange({"listaClases", "listaGrupos", "claseForm"})
    public void eliminarClase() {
        if (claseForm.getIdClase() == null) {
            return;
        }
        try {
            servicio.eliminarClase(claseForm);
            recargarClases();
            claseForm = new CuClase();
            listaGrupos = new ArrayList<CuGrupo>();
            Clients.showNotification("Clase eliminada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
        } catch (Exception e) {
            Clients.showNotification("No se puede eliminar: tiene grupos asociados",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    @Command
    @NotifyChange("grupoForm")
    public void nuevoGrupo() {
        grupoForm = new CuGrupo();
        if (claseSelected != null) {
            grupoForm.setIdClase(claseSelected.getIdClase());
        }
        grupoSelected = null;
    }

    @Command
    @NotifyChange({"listaGrupos", "grupoForm"})
    public void guardarGrupo() {
        if (grupoForm.getIdClase() == null) {
            Clients.showNotification("Seleccione la clase del grupo",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2500, true);
            return;
        }
        if (vacio(grupoForm.getGrupNumero()) || vacio(grupoForm.getGrupNombre())) {
            Clients.showNotification("Ingrese código y nombre del grupo",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2500, true);
            return;
        }
        cero(grupoForm);
        try {
            if (grupoForm.getIdGrupo() == null) {
                servicio.crearGrupo(grupoForm, ambiente);
                Clients.showNotification("Grupo registrado", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
            } else {
                servicio.modificarGrupo(grupoForm, ambiente);
                Clients.showNotification("Grupo actualizado", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
            }
            listaGrupos = servicio.findGruposPorClase(grupoForm.getIdClase());
            grupoForm = new CuGrupo();
            grupoForm.setIdClase(claseSelected != null ? claseSelected.getIdClase() : null);
        } catch (Exception e) {
            Clients.showNotification("No se pudo guardar el grupo",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    @Command
    @NotifyChange({"listaGrupos", "listaCuentas", "grupoForm"})
    public void eliminarGrupo() {
        if (grupoForm.getIdGrupo() == null) {
            return;
        }
        Integer idClase = grupoForm.getIdClase();
        try {
            servicio.eliminarGrupo(grupoForm);
            listaGrupos = idClase != null ? servicio.findGruposPorClase(idClase) : servicio.findAllGrupos(ambiente);
            grupoForm = new CuGrupo();
            listaCuentas = new ArrayList<CuCuenta>();
            Clients.showNotification("Grupo eliminado", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
        } catch (Exception e) {
            Clients.showNotification("No se puede eliminar: tiene cuentas asociadas",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    @Command
    @NotifyChange("cuentaForm")
    public void nuevaCuenta() {
        cuentaForm = new CuCuenta();
        if (grupoSelected != null) {
            cuentaForm.setIdGrupo(grupoSelected);
        }
        cuentaSelected = null;
    }

    @Command
    @NotifyChange({"listaCuentas", "cuentaForm"})
    public void guardarCuenta() {
        if (cuentaForm.getIdGrupo() == null) {
            Clients.showNotification("Seleccione el grupo de la cuenta",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2500, true);
            return;
        }
        if (vacio(cuentaForm.getGrupNumero()) || vacio(cuentaForm.getGrupNombre())) {
            Clients.showNotification("Ingrese código y nombre de la cuenta",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2500, true);
            return;
        }
        cero(cuentaForm);
        try {
            if (cuentaForm.getIdCuenta() == null) {
                servicio.crearCuenta(cuentaForm, ambiente);
                Clients.showNotification("Cuenta registrada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
            } else {
                servicio.modificarCuenta(cuentaForm, ambiente);
                Clients.showNotification("Cuenta actualizada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
            }
            listaCuentas = servicio.findCuentasPorGrupo(cuentaForm.getIdGrupo());
            cuentaForm = new CuCuenta();
            cuentaForm.setIdGrupo(grupoSelected);
        } catch (Exception e) {
            Clients.showNotification("No se pudo guardar la cuenta",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    @Command
    @NotifyChange({"listaCuentas", "listaSubcuentas", "cuentaForm"})
    public void eliminarCuenta() {
        if (cuentaForm.getIdCuenta() == null) {
            return;
        }
        CuGrupo g = cuentaForm.getIdGrupo();
        try {
            servicio.eliminarCuenta(cuentaForm);
            listaCuentas = g != null ? servicio.findCuentasPorGrupo(g) : servicio.findAllCuentas(ambiente);
            cuentaForm = new CuCuenta();
            listaSubcuentas = new ArrayList<CuSubCuenta>();
            Clients.showNotification("Cuenta eliminada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
        } catch (Exception e) {
            Clients.showNotification("No se puede eliminar: tiene subcuentas asociadas",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    @Command
    @NotifyChange("subForm")
    public void nuevaSubcuenta() {
        subForm = new CuSubCuenta();
        if (cuentaSelected != null) {
            subForm.setIdCuenta(cuentaSelected);
        }
        subSelected = null;
    }

    @Command
    @NotifyChange({"listaSubcuentas", "subForm"})
    public void guardarSubcuenta() {
        if (subForm.getIdCuenta() == null) {
            Clients.showNotification("Seleccione la cuenta de la subcuenta",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2500, true);
            return;
        }
        if (vacio(subForm.getSubcNumero()) || vacio(subForm.getSubcNombre())) {
            Clients.showNotification("Ingrese código y nombre de la subcuenta",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2500, true);
            return;
        }
        cero(subForm);
        try {
            if (subForm.getIdSubCuenta() == null) {
                servicio.crearSubcuenta(subForm, ambiente);
                Clients.showNotification("Subcuenta registrada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
            } else {
                servicio.modificarSubcuenta(subForm, ambiente);
                Clients.showNotification("Subcuenta actualizada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
            }
            listaSubcuentas = servicio.findSubcuentasPorCuenta(subForm.getIdCuenta());
            subForm = new CuSubCuenta();
            subForm.setIdCuenta(cuentaSelected);
        } catch (Exception e) {
            Clients.showNotification("No se pudo guardar la subcuenta",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    @Command
    @NotifyChange({"listaSubcuentas", "subForm"})
    public void eliminarSubcuenta() {
        if (subForm.getIdSubCuenta() == null) {
            return;
        }
        CuCuenta c = subForm.getIdCuenta();
        try {
            servicio.eliminarSubcuenta(subForm);
            listaSubcuentas = c != null ? servicio.findSubcuentasPorCuenta(c) : new ArrayList<CuSubCuenta>();
            subForm = new CuSubCuenta();
            Clients.showNotification("Subcuenta eliminada", Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 2000, true);
        } catch (Exception e) {
            Clients.showNotification("No se pudo eliminar la subcuenta",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    private boolean vacio(String v) {
        return v == null || v.trim().length() == 0;
    }

    public List<CuClase> getListaClases() {
        return listaClases;
    }

    public void setListaClases(List<CuClase> listaClases) {
        this.listaClases = listaClases;
    }

    public List<CuGrupo> getListaGrupos() {
        return listaGrupos;
    }

    public void setListaGrupos(List<CuGrupo> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }

    public List<CuCuenta> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<CuCuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public List<CuSubCuenta> getListaSubcuentas() {
        return listaSubcuentas;
    }

    public void setListaSubcuentas(List<CuSubCuenta> listaSubcuentas) {
        this.listaSubcuentas = listaSubcuentas;
    }

    public CuClase getClaseForm() {
        return claseForm;
    }

    public void setClaseForm(CuClase claseForm) {
        this.claseForm = claseForm;
    }

    public CuGrupo getGrupoForm() {
        return grupoForm;
    }

    public void setGrupoForm(CuGrupo grupoForm) {
        this.grupoForm = grupoForm;
    }

    public CuCuenta getCuentaForm() {
        return cuentaForm;
    }

    public void setCuentaForm(CuCuenta cuentaForm) {
        this.cuentaForm = cuentaForm;
    }

    public CuSubCuenta getSubForm() {
        return subForm;
    }

    public void setSubForm(CuSubCuenta subForm) {
        this.subForm = subForm;
    }

    public CuClase getClaseSelected() {
        return claseSelected;
    }

    public void setClaseSelected(CuClase claseSelected) {
        this.claseSelected = claseSelected;
    }

    public CuGrupo getGrupoSelected() {
        return grupoSelected;
    }

    public void setGrupoSelected(CuGrupo grupoSelected) {
        this.grupoSelected = grupoSelected;
    }

    public CuCuenta getCuentaSelected() {
        return cuentaSelected;
    }

    public void setCuentaSelected(CuCuenta cuentaSelected) {
        this.cuentaSelected = cuentaSelected;
    }

    public CuSubCuenta getSubSelected() {
        return subSelected;
    }

    public void setSubSelected(CuSubCuenta subSelected) {
        this.subSelected = subSelected;
    }
}
