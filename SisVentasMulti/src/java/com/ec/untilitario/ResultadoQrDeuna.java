package com.ec.untilitario;

import java.math.BigDecimal;

public class ResultadoQrDeuna {

    private byte[] imagen;
    private String poId;
    private String deepLink;
    private BigDecimal monto;
    private String mensaje;
    private boolean desdeApi;

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isDesdeApi() {
        return desdeApi;
    }

    public void setDesdeApi(boolean desdeApi) {
        this.desdeApi = desdeApi;
    }
}
