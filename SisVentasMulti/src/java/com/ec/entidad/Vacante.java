/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "vacante")
@NamedQueries({
    @NamedQuery(name = "Vacante.findAll", query = "SELECT v FROM Vacante v")})
public class Vacante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_vacante")
    private Integer idVacante;
    @Column(name = "vac_nombre")
    private String vacNombre;
    @Column(name = "vac_descripcion")
    private String vacDescripcion;
    @Column(name = "vac_fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date vacFechaInicio;
    @Column(name = "vac_fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date vacFechaFin;
    @Column(name = "vac_area")
    private String vacArea;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "vac_sueldo")
    private BigDecimal vacSueldo;
    @Column(name = "vac_experiencia")
    private String vacExperiencia;
    @Column(name = "vac_referencia")
    private String vacReferencia;
    @Column(name = "vac_path_imagen")
    private String vacPathImagen;
    @Column(name = "vac_nombre_imagen")
    private String vacNombreImagen;
    @Column(name = "vac_estado")
    private Short vacEstado;
    @JoinColumn(name = "cod_tipoambiente", referencedColumnName = "cod_tipoambiente")
    @ManyToOne
    private Tipoambiente codTipoambiente;
    @OneToMany(mappedBy = "idVacante")
    private Collection<Postulaciones> postulacionesCollection;

    public Vacante() {
    }

    public Vacante(Integer idVacante) {
        this.idVacante = idVacante;
    }

    public Integer getIdVacante() {
        return idVacante;
    }

    public void setIdVacante(Integer idVacante) {
        this.idVacante = idVacante;
    }

    public String getVacNombre() {
        return vacNombre;
    }

    public void setVacNombre(String vacNombre) {
        this.vacNombre = vacNombre;
    }

    public String getVacDescripcion() {
        return vacDescripcion;
    }

    public void setVacDescripcion(String vacDescripcion) {
        this.vacDescripcion = vacDescripcion;
    }

    public Date getVacFechaInicio() {
        return vacFechaInicio;
    }

    public void setVacFechaInicio(Date vacFechaInicio) {
        this.vacFechaInicio = vacFechaInicio;
    }

    public Date getVacFechaFin() {
        return vacFechaFin;
    }

    public void setVacFechaFin(Date vacFechaFin) {
        this.vacFechaFin = vacFechaFin;
    }

    public String getVacArea() {
        return vacArea;
    }

    public void setVacArea(String vacArea) {
        this.vacArea = vacArea;
    }

    public BigDecimal getVacSueldo() {
        return vacSueldo;
    }

    public void setVacSueldo(BigDecimal vacSueldo) {
        this.vacSueldo = vacSueldo;
    }

    public String getVacExperiencia() {
        return vacExperiencia;
    }

    public void setVacExperiencia(String vacExperiencia) {
        this.vacExperiencia = vacExperiencia;
    }

    public String getVacReferencia() {
        return vacReferencia;
    }

    public void setVacReferencia(String vacReferencia) {
        this.vacReferencia = vacReferencia;
    }

    public String getVacPathImagen() {
        return vacPathImagen;
    }

    public void setVacPathImagen(String vacPathImagen) {
        this.vacPathImagen = vacPathImagen;
    }

    public String getVacNombreImagen() {
        return vacNombreImagen;
    }

    public void setVacNombreImagen(String vacNombreImagen) {
        this.vacNombreImagen = vacNombreImagen;
    }

    public Short getVacEstado() {
        return vacEstado;
    }

    public void setVacEstado(Short vacEstado) {
        this.vacEstado = vacEstado;
    }

    public Tipoambiente getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Tipoambiente codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }

    public Collection<Postulaciones> getPostulacionesCollection() {
        return postulacionesCollection;
    }

    public void setPostulacionesCollection(Collection<Postulaciones> postulacionesCollection) {
        this.postulacionesCollection = postulacionesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVacante != null ? idVacante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vacante)) {
            return false;
        }
        Vacante other = (Vacante) object;
        if ((this.idVacante == null && other.idVacante != null) || (this.idVacante != null && !this.idVacante.equals(other.idVacante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Vacante[ idVacante=" + idVacante + " ]";
    }
    
}
