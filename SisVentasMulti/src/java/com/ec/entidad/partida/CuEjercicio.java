package com.ec.entidad.partida;

import com.ec.entidad.Tipoambiente;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "cu_ejercicio")
@NamedQueries({
    @NamedQuery(name = "CuEjercicio.findByAmbienteAnio",
            query = "SELECT c FROM CuEjercicio c WHERE c.codTipoambiente = :ambiente AND c.ejeAnio = :anio")
    , @NamedQuery(name = "CuEjercicio.findByAmbiente",
            query = "SELECT c FROM CuEjercicio c WHERE c.codTipoambiente = :ambiente ORDER BY c.ejeAnio DESC")})
public class CuEjercicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ejercicio")
    private Integer idEjercicio;
    @Column(name = "eje_anio")
    private Integer ejeAnio;
    @Column(name = "eje_estado")
    private String ejeEstado;
    @Column(name = "eje_fecha_cierre")
    @Temporal(TemporalType.DATE)
    private Date ejeFechaCierre;
    @Column(name = "id_asiento")
    private Integer idAsiento;
    @JoinColumn(name = "cod_tipoambiente", referencedColumnName = "cod_tipoambiente")
    @ManyToOne
    private Tipoambiente codTipoambiente;

    public Integer getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Integer idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public Integer getEjeAnio() {
        return ejeAnio;
    }

    public void setEjeAnio(Integer ejeAnio) {
        this.ejeAnio = ejeAnio;
    }

    public String getEjeEstado() {
        return ejeEstado;
    }

    public void setEjeEstado(String ejeEstado) {
        this.ejeEstado = ejeEstado;
    }

    public Date getEjeFechaCierre() {
        return ejeFechaCierre;
    }

    public void setEjeFechaCierre(Date ejeFechaCierre) {
        this.ejeFechaCierre = ejeFechaCierre;
    }

    public Integer getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Integer idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Tipoambiente getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Tipoambiente codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }

    public boolean isCerrado() {
        return "CERRADO".equals(ejeEstado);
    }
}
