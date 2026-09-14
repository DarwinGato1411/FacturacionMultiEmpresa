package com.ec.untilitario;

import java.util.ArrayList;
import java.util.List;

public class ResultadoDescargaSri {

    private boolean ok;
    private String origen;
    private String mensaje;
    private String contenidoTxt;
    private final List<String> detalles = new ArrayList<String>();

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getContenidoTxt() {
        return contenidoTxt;
    }

    public void setContenidoTxt(String contenidoTxt) {
        this.contenidoTxt = contenidoTxt;
    }

    public List<String> getDetalles() {
        return detalles;
    }

    public void addDetalle(String detalle) {
        if (detalle != null && !detalle.isEmpty()) {
            detalles.add(detalle);
        }
    }
}
