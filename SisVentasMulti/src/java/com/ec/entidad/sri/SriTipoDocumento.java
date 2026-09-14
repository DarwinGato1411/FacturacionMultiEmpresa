package com.ec.entidad.sri;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sri_tipo_documento")
public class SriTipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "std_codigo")
    private String stdCodigo;
    @Column(name = "std_nombre")
    private String stdNombre;
    @Column(name = "std_nombre_portal")
    private String stdNombrePortal;
    @Column(name = "std_codigo_portal")
    private Integer stdCodigoPortal;
    @Column(name = "std_orden")
    private Integer stdOrden;
    @Column(name = "std_activo")
    private Boolean stdActivo;

    public String getStdCodigo() {
        return stdCodigo;
    }

    public void setStdCodigo(String stdCodigo) {
        this.stdCodigo = stdCodigo;
    }

    public String getStdNombre() {
        return stdNombre;
    }

    public void setStdNombre(String stdNombre) {
        this.stdNombre = stdNombre;
    }

    public String getStdNombrePortal() {
        return stdNombrePortal;
    }

    public void setStdNombrePortal(String stdNombrePortal) {
        this.stdNombrePortal = stdNombrePortal;
    }

    public Integer getStdCodigoPortal() {
        return stdCodigoPortal;
    }

    public void setStdCodigoPortal(Integer stdCodigoPortal) {
        this.stdCodigoPortal = stdCodigoPortal;
    }

    public Integer getStdOrden() {
        return stdOrden;
    }

    public void setStdOrden(Integer stdOrden) {
        this.stdOrden = stdOrden;
    }

    public Boolean getStdActivo() {
        return stdActivo;
    }

    public void setStdActivo(Boolean stdActivo) {
        this.stdActivo = stdActivo;
    }
}
