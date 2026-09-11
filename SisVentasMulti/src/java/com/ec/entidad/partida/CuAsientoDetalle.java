package com.ec.entidad.partida;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cu_asiento_detalle")
public class CuAsientoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_detalle")
    private Integer idDetalle;
    @JoinColumn(name = "id_asiento", referencedColumnName = "id_asiento")
    @ManyToOne
    private CuAsiento idAsiento;
    @JoinColumn(name = "id_sub_cuenta", referencedColumnName = "id_sub_cuenta")
    @ManyToOne
    private CuSubCuenta idSubCuenta;
    @Column(name = "det_debe")
    private BigDecimal detDebe;
    @Column(name = "det_haber")
    private BigDecimal detHaber;
    @Column(name = "det_glosa")
    private String detGlosa;

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public CuAsiento getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(CuAsiento idAsiento) {
        this.idAsiento = idAsiento;
    }

    public CuSubCuenta getIdSubCuenta() {
        return idSubCuenta;
    }

    public void setIdSubCuenta(CuSubCuenta idSubCuenta) {
        this.idSubCuenta = idSubCuenta;
    }

    public BigDecimal getDetDebe() {
        return detDebe;
    }

    public void setDetDebe(BigDecimal detDebe) {
        this.detDebe = detDebe;
    }

    public BigDecimal getDetHaber() {
        return detHaber;
    }

    public void setDetHaber(BigDecimal detHaber) {
        this.detHaber = detHaber;
    }

    public String getDetGlosa() {
        return detGlosa;
    }

    public void setDetGlosa(String detGlosa) {
        this.detGlosa = detGlosa;
    }
}
