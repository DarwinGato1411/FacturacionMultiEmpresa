/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Cliente;
import com.ec.entidad.Parametrizar;
import com.ec.entidad.Tipoadentificacion;
import com.ec.entidad.Tipoambiente;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioCliente;
import com.ec.servicio.ServicioParametrizar;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.servicio.ServicioTipoIdentificacion;
import com.ec.untilitario.ArchivoUtils;
import com.ec.untilitario.InfoPersona;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import org.json.JSONException;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevoCliente {

    ServicioTipoIdentificacion servicioTipoIdentificacion = new ServicioTipoIdentificacion();
    private Cliente cliente = new Cliente();
    ServicioCliente servicioCliente = new ServicioCliente();
    private String accion = "create";
    private String clietipo = "0"; //0 normal 1 preferencial 1 -> 2 preferencial 2 -> 3 - preferencial 3
    private Date fechaReg = new Date();
    private List<Tipoadentificacion> listaIdentificaciones = new ArrayList<Tipoadentificacion>();
    private Tipoadentificacion tipoadentificacion = null;
    //datos por defecto
    ServicioParametrizar servicioParametrizar = new ServicioParametrizar();
    private Parametrizar parametrizar = null;
    @Wire
    Window windowCliente;

    UserCredential credential = new UserCredential();
    private Tipoambiente amb = new Tipoambiente();
    private String amRuc = "";
    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Cliente cliente, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        listaIdentificaciones = servicioTipoIdentificacion.FindALlTipoadentificacion();
        parametrizar = servicioParametrizar.FindALlParametrizar();
        if (cliente != null) {
            this.cliente = cliente;
            tipoadentificacion = cliente.getIdTipoIdentificacion();
            clietipo = "0";
            accion = "update";
        } else {
            this.cliente = new Cliente();
            this.cliente.setCiudad(parametrizar.getParCiudad() != null ? parametrizar.getParCiudad() : " ");
            this.cliente.setCliDireccion(parametrizar.getParCiudad() != null ? parametrizar.getParCiudad() : " ");
            this.cliente.setCliMontoAsignado(BigDecimal.valueOf(999999));
            this.cliente.setCliMovil(" ");
            this.cliente.setCliTelefono(" ");
            this.cliente.setCliCorreo(parametrizar.getParCorreoDefecto() != null ? parametrizar.getParCorreoDefecto() : " ");
            accion = "create";
        }

    }

    public NuevoCliente() {

        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
//        amRuc = credential.getUsuarioSistema().getUsuRuc();
        amb = servicioTipoAmbiente.findALlTipoambientePorUsuario(credential.getUsuarioSistema());

    }

    @Command
    @NotifyChange({"cliente"})
    public void buscarAduana() throws URISyntaxException, IOException, XPathExpressionException, JSONException {
        
        
        
//       String token=ArchivoUtils.token("");
        InfoPersona aduana = new InfoPersona();
        String nombre = "";
        if (cliente.getCliCedula() != null) {
            if (!cliente.getCliCedula().equals("")) {
                String cedulaBuscar = "";
                if (cliente.getCliCedula().length() == 13) {
                    cedulaBuscar = cliente.getCliCedula();
                    nombre = ArchivoUtils.obtenerPorRuc(cedulaBuscar);
                    cliente.setCliApellidos(nombre);
                    cliente.setCliNombres(nombre);
                    cliente.setCliNombre(nombre);
                    cliente.setCliRazonSocial(nombre);
                } else if (cliente.getCliCedula().length() == 10) {
                    cedulaBuscar = cliente.getCliCedula();
                    aduana = ArchivoUtils.obtenerPorCedula(cedulaBuscar);
                    cliente.setCliApellidos(aduana.getNombre());
                    cliente.setCliNombres(aduana.getNombre());
                    cliente.setCliNombre(aduana.getNombre());
                    cliente.setCliRazonSocial(aduana.getNombre());
                    cliente.setCliDireccion(aduana.getDireccion());
                }

            }
        }

    }

    @Command
    @NotifyChange({"cliente"})
    public void actualizarTipo() {
    }

    @Command
    public void guardar() {
        /*getCliNombre es el nombre comercial*/
        if (cliente.getCliCedula() != null
                    && cliente.getCliNombre() != null
                    && cliente.getCliDireccion() != null
                    && cliente.getCliTelefono() != null
                    && cliente.getCliMovil() != null
                    && cliente.getCiudad() != null
                    && cliente.getCliCorreo() != null
                    && tipoadentificacion != null) {

            if (tipoadentificacion.getTidCodigo().equals("04")) {
                if (cliente.getCliCedula().length() != 13) {
                    Clients.showNotification("Verifique el RUC ingresada debe tener 13 caracteres ",
                                Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
                    return;
                }
            } else if (tipoadentificacion.getTidCodigo().equals("05")) {
                if (cliente.getCliCedula().length() != 10) {
                    Clients.showNotification("Verifique la CEDULA ingresada debe tener 10 caracteres ",
                                Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
                    return;
                }
            }
            cliente.setCliRazonSocial(cliente.getCliNombre());
            cliente.setCodTipoambiente(amb);
            if (accion.equals("create")) {
//                Cliente cliBusca = servicioCliente.FindClienteForCedula(cliente.getCliCedula(), amb);
//                if ((cliBusca == null) || (!cliBusca.getCliDireccion().equals(cliente.getCliDireccion()))) {
                cliente.setClietipo(Integer.valueOf(clietipo));
                cliente.setClieFechaRegistro(fechaReg);
                cliente.setIdTipoIdentificacion(tipoadentificacion);
                cliente.setCliClave(ArchivoUtils.generaraClaveTemporal());
                servicioCliente.crear(cliente);

                windowCliente.detach();
//                } else {
//
//                    Clients.showNotification("El número de documento (CI / RUC) ya se encuentra registrado ",
//                                Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
//                }

            } else {
                if (cliente.getCliClave() == null) {
                    cliente.setCliClave(ArchivoUtils.generaraClaveTemporal());
                }
                cliente.setIdTipoIdentificacion(tipoadentificacion);
                cliente.setClietipo(Integer.valueOf(clietipo));
                servicioCliente.modificar(cliente);
//                Messagebox.show("Guardado con exito");

                windowCliente.detach();
            }

        } else {

            Clients.showNotification("Verifique la informacion requerida",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getClietipo() {
        return clietipo;
    }

    public void setClietipo(String clietipo) {
        this.clietipo = clietipo;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public List<Tipoadentificacion> getListaIdentificaciones() {
        return listaIdentificaciones;
    }

    public void setListaIdentificaciones(List<Tipoadentificacion> listaIdentificaciones) {
        this.listaIdentificaciones = listaIdentificaciones;
    }

    public Tipoadentificacion getTipoadentificacion() {
        return tipoadentificacion;
    }

    public void setTipoadentificacion(Tipoadentificacion tipoadentificacion) {
        this.tipoadentificacion = tipoadentificacion;
    }

}
