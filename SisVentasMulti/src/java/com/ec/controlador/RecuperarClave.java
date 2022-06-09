/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Parametrizar;
import com.ec.entidad.Tipoambiente;
import com.ec.entidad.Usuario;
import com.ec.servicio.ServicioParametrizar;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.servicio.ServicioUsuario;
import java.math.BigDecimal;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class RecuperarClave {

    @Wire
    Window wOlvideMiclave;
    ServicioUsuario servicioUsuario = new ServicioUsuario();
    private Usuario usuarioSistema = new Usuario();
    private String usuCorreo = "";
    private String usuRuc = "";

    public RecuperarClave() {

    }

    @Command
    public void recuperar() {
        if (usuCorreo != null && !usuCorreo.equals("")
                    && usuRuc != null && !usuCorreo.equals("")) {
            usuarioSistema = servicioUsuario.findRecuperaPassword(usuRuc, usuCorreo);
            if (usuarioSistema != null) {
                wOlvideMiclave.detach();
            } else {
                Clients.showNotification("Verifique su RUc o Correo electronico",
                            Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2000, true);
            }

        } else {
            Clients.showNotification("Debe ingresar un correo.",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 2000, true);
        }
    }

    public String getUsuCorreo() {
        return usuCorreo;
    }

    public void setUsuCorreo(String usuCorreo) {
        this.usuCorreo = usuCorreo;
    }

    public String getUsuRuc() {
        return usuRuc;
    }

    public void setUsuRuc(String usuRuc) {
        this.usuRuc = usuRuc;
    }

}
