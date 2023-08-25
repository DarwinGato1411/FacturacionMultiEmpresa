/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

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

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "postulaciones")
@NamedQueries({
    @NamedQuery(name = "Postulaciones.findAll", query = "SELECT p FROM Postulaciones p")})
public class Postulaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_postulacion")
    private Integer idPostulacion;
    @Column(name = "pos_fecha")
    @Temporal(TemporalType.DATE)
    private Date posFecha;
    @Column(name = "pos_aprobado")
    private Short posAprobado;
    @Column(name = "pos_estado")
    private Short posEstado;
    @Column(name = "pos_observacion")
    private String posObservacion;
    @Column(name = "pos_experiencia")
    private String posExperiencia;
    @Column(name = "pos_trabajo_anterior")
    private String posTrabajoAnterior;
    @Column(name = "pos_conocimientos")
    private String posConocimientos;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;
    @JoinColumn(name = "id_vacante", referencedColumnName = "id_vacante")
    @ManyToOne
    private Vacante idVacante;

    public Postulaciones() {
    }

    public Postulaciones(Integer idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public Integer getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(Integer idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public Date getPosFecha() {
        return posFecha;
    }

    public void setPosFecha(Date posFecha) {
        this.posFecha = posFecha;
    }

    public Short getPosAprobado() {
        return posAprobado;
    }

    public void setPosAprobado(Short posAprobado) {
        this.posAprobado = posAprobado;
    }

    public Short getPosEstado() {
        return posEstado;
    }

    public void setPosEstado(Short posEstado) {
        this.posEstado = posEstado;
    }

    public String getPosObservacion() {
        return posObservacion;
    }

    public void setPosObservacion(String posObservacion) {
        this.posObservacion = posObservacion;
    }

    public String getPosExperiencia() {
        return posExperiencia;
    }

    public void setPosExperiencia(String posExperiencia) {
        this.posExperiencia = posExperiencia;
    }

    public String getPosTrabajoAnterior() {
        return posTrabajoAnterior;
    }

    public void setPosTrabajoAnterior(String posTrabajoAnterior) {
        this.posTrabajoAnterior = posTrabajoAnterior;
    }

    public String getPosConocimientos() {
        return posConocimientos;
    }

    public void setPosConocimientos(String posConocimientos) {
        this.posConocimientos = posConocimientos;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Vacante getIdVacante() {
        return idVacante;
    }

    public void setIdVacante(Vacante idVacante) {
        this.idVacante = idVacante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPostulacion != null ? idPostulacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Postulaciones)) {
            return false;
        }
        Postulaciones other = (Postulaciones) object;
        if ((this.idPostulacion == null && other.idPostulacion != null) || (this.idPostulacion != null && !this.idPostulacion.equals(other.idPostulacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Postulaciones[ idPostulacion=" + idPostulacion + " ]";
    }
    
}
