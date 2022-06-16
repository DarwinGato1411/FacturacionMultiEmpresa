/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "historial_declaraciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistorialDeclaraciones.findAll", query = "SELECT h FROM HistorialDeclaraciones h")
    , @NamedQuery(name = "HistorialDeclaraciones.findByIdHistorial", query = "SELECT h FROM HistorialDeclaraciones h WHERE h.idHistorial = :idHistorial")
    , @NamedQuery(name = "HistorialDeclaraciones.findByHisPathDeclaracion", query = "SELECT h FROM HistorialDeclaraciones h WHERE h.hisPathDeclaracion = :hisPathDeclaracion")
    , @NamedQuery(name = "HistorialDeclaraciones.findByHisPathPago", query = "SELECT h FROM HistorialDeclaraciones h WHERE h.hisPathPago = :hisPathPago")
    , @NamedQuery(name = "HistorialDeclaraciones.findByHisDescripcion", query = "SELECT h FROM HistorialDeclaraciones h WHERE h.hisDescripcion = :hisDescripcion")})
public class HistorialDeclaraciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_historial")
    private Integer idHistorial;
    @Column(name = "his_path_declaracion")
    private String hisPathDeclaracion;
    @Column(name = "his_path_pago")
    private String hisPathPago;
    @Column(name = "his_descripcion")
    private String hisDescripcion;
    @JoinColumn(name = "cod_tipoambiente", referencedColumnName = "cod_tipoambiente")
    @ManyToOne
    private Tipoambiente codTipoambiente;

    public HistorialDeclaraciones() {
    }

    public HistorialDeclaraciones(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getHisPathDeclaracion() {
        return hisPathDeclaracion;
    }

    public void setHisPathDeclaracion(String hisPathDeclaracion) {
        this.hisPathDeclaracion = hisPathDeclaracion;
    }

    public String getHisPathPago() {
        return hisPathPago;
    }

    public void setHisPathPago(String hisPathPago) {
        this.hisPathPago = hisPathPago;
    }

    public String getHisDescripcion() {
        return hisDescripcion;
    }

    public void setHisDescripcion(String hisDescripcion) {
        this.hisDescripcion = hisDescripcion;
    }

    public Tipoambiente getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Tipoambiente codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorial != null ? idHistorial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistorialDeclaraciones)) {
            return false;
        }
        HistorialDeclaraciones other = (HistorialDeclaraciones) object;
        if ((this.idHistorial == null && other.idHistorial != null) || (this.idHistorial != null && !this.idHistorial.equals(other.idHistorial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.HistorialDeclaraciones[ idHistorial=" + idHistorial + " ]";
    }
    
}
