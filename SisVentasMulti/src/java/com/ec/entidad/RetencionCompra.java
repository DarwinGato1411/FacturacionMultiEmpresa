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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "retencion_compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RetencionCompra.findAll", query = "SELECT r FROM RetencionCompra r")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoCodigo", query = "SELECT r FROM RetencionCompra r WHERE r.rcoCodigo = :rcoCodigo")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoAutorizacion", query = "SELECT r FROM RetencionCompra r WHERE r.rcoAutorizacion = :rcoAutorizacion")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoDetalle", query = "SELECT r FROM RetencionCompra r WHERE r.rcoDetalle = :rcoDetalle")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoFecha", query = "SELECT r FROM RetencionCompra r WHERE r.rcoFecha = :rcoFecha")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoIva", query = "SELECT r FROM RetencionCompra r WHERE r.rcoIva = :rcoIva")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoPorcentajeIva", query = "SELECT r FROM RetencionCompra r WHERE r.rcoPorcentajeIva = :rcoPorcentajeIva")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoPuntoEmision", query = "SELECT r FROM RetencionCompra r WHERE r.rcoPuntoEmision = :rcoPuntoEmision")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoSecuencial", query = "SELECT r FROM RetencionCompra r WHERE r.rcoSecuencial = :rcoSecuencial")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoSerie", query = "SELECT r FROM RetencionCompra r WHERE r.rcoSerie = :rcoSerie")
    ,
    @NamedQuery(name = "RetencionCompra.findByRcoValorRetencionIva", query = "SELECT r FROM RetencionCompra r WHERE r.rcoValorRetencionIva = :rcoValorRetencionIva")})
public class RetencionCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rco_codigo")
    private Integer rcoCodigo;
    @Column(name = "rco_autorizacion")
    private String rcoAutorizacion;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "rco_detalle")
    private String rcoDetalle;
    @Basic(optional = false)
    @Column(name = "rco_fecha")
    @Temporal(TemporalType.DATE)
    private Date rcoFecha;
    @Basic(optional = false)
    @Column(name = "rco_iva")
    private boolean rcoIva;
    @Basic(optional = false)
    @Column(name = "rco_porcentaje_iva")
    private int rcoPorcentajeIva;
    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "rco_punto_emision")
    private String rcoPuntoEmision;
    @Basic(optional = false)
    @Column(name = "rco_secuencial")
    private int rcoSecuencial;
    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "rco_serie")
    private String rcoSerie;

    @Basic(optional = false)
    @Column(name = "rco_base_grava_iva")
    private BigDecimal rcoBaseGravaIva;
    @Basic(optional = false)
    @Column(name = "rco_valor_retencion_iva")
    private BigDecimal rcoValorRetencionIva;
    @Column(name = "cab_fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cabFechaEmision;
    @Column(name = "rco_secuencial_text")
    private String rcoSecuencialText;
    @Column(name = "drc_mensajesri")
    private String drcMensajesri;
    @Column(name = "rco_msm_Info_sri")
    private String rcoMsmInfoSri;
    @Column(name = "drc_estadosri")
    private String drcEstadosri;
    @Temporal(TemporalType.DATE)
    @Column(name = "rco_fecha_autorizacion")
    private Date rcoFechaAutorizacion;
    @Column(name = "rco_pathret")
    private String rcoPathRet;
    @OneToMany(mappedBy = "rcoCodigo")
    private Collection<DetalleRetencionCompra> detalleRetencionCompraCollection;
    @JoinColumn(name = "id_cabecera", referencedColumnName = "id_cabecera")
    @ManyToOne(optional = false)
    private CabeceraCompra idCabecera;
    @JoinColumn(name = "cod_tipoambiente", referencedColumnName = "cod_tipoambiente")
    @ManyToOne
    private Tipoambiente codTipoambiente;
    @Column(name = "razon_social_empresa")
    private String razonSocialEmpresa;
    @Column(name = "nombre_comercial_empresa")
    private String nombreComercialEmpresa;
    @Column(name = "ruc_empresa ")
    private String rucEmpresa;

    @Column(name = "establecimiento_empresa")
    private String establecimientoEmpresa;

    @Column(name = "punto_emision_empresa")
    private String puntoEmisionEmpresa;

    @Column(name = "dir_matriz")
    private String dirMatriz;

    @Column(name = "dir_Establecimiento")
    private String dirEstablecimiento;

    @Column(name = "obligado_contabilidad")
    private String obligadoContabilidad;

    @Column(name = "tipo_identificacion_sujeto_retenido")
    private String tipoIdentificacionSujetoRetenido;

    @Column(name = "razonSocial_sujeto_retenido")
    private String razonSocialSujetoRetenido;

    @Column(name = "identificacion_Sujeto_Retenido")
    private String identificacionSujetoRetenido;

    @Column(name = "periodo_fiscal")
    @Temporal(TemporalType.DATE)
    private Date periodoFiscal;

    @Column(name = "prov_nombre")
    private String provNombre;
    @Column(name = "prov_cedula")
    private String provCedula;

    @Column(name = "am_age_ret")
    private Boolean amAgeRet;

    @Column(name = "am_rimpe")
    private Boolean amRimpe;

    @Column(name = "am_general")
    private Boolean amGeneral;

    public RetencionCompra() {
    }

    public RetencionCompra(Integer rcoCodigo) {
        this.rcoCodigo = rcoCodigo;
    }

    public RetencionCompra(Integer rcoCodigo, String rcoAutorizacion, String rcoDetalle, Date rcoFecha, boolean rcoIva, int rcoPorcentajeIva, String rcoPuntoEmision, int rcoSecuencial, String rcoSerie, BigDecimal rcoValorRetencionIva) {
        this.rcoCodigo = rcoCodigo;
        this.rcoAutorizacion = rcoAutorizacion;
        this.rcoDetalle = rcoDetalle;
        this.rcoFecha = rcoFecha;
        this.rcoIva = rcoIva;
        this.rcoPorcentajeIva = rcoPorcentajeIva;
        this.rcoPuntoEmision = rcoPuntoEmision;
        this.rcoSecuencial = rcoSecuencial;
        this.rcoSerie = rcoSerie;
        this.rcoValorRetencionIva = rcoValorRetencionIva;
    }

    public Integer getRcoCodigo() {
        return rcoCodigo;
    }

    public void setRcoCodigo(Integer rcoCodigo) {
        this.rcoCodigo = rcoCodigo;
    }

    public String getRcoAutorizacion() {
        return rcoAutorizacion;
    }

    public void setRcoAutorizacion(String rcoAutorizacion) {
        this.rcoAutorizacion = rcoAutorizacion;
    }

    public String getRcoDetalle() {
        return rcoDetalle;
    }

    public void setRcoDetalle(String rcoDetalle) {
        this.rcoDetalle = rcoDetalle;
    }

    public Date getRcoFecha() {
        return rcoFecha;
    }

    public void setRcoFecha(Date rcoFecha) {
        this.rcoFecha = rcoFecha;
    }

    public boolean getRcoIva() {
        return rcoIva;
    }

    public void setRcoIva(boolean rcoIva) {
        this.rcoIva = rcoIva;
    }

    public int getRcoPorcentajeIva() {
        return rcoPorcentajeIva;
    }

    public void setRcoPorcentajeIva(int rcoPorcentajeIva) {
        this.rcoPorcentajeIva = rcoPorcentajeIva;
    }

    public String getRcoPuntoEmision() {
        return rcoPuntoEmision;
    }

    public void setRcoPuntoEmision(String rcoPuntoEmision) {
        this.rcoPuntoEmision = rcoPuntoEmision;
    }

    public int getRcoSecuencial() {
        return rcoSecuencial;
    }

    public void setRcoSecuencial(int rcoSecuencial) {
        this.rcoSecuencial = rcoSecuencial;
    }

    public String getRcoSerie() {
        return rcoSerie;
    }

    public void setRcoSerie(String rcoSerie) {
        this.rcoSerie = rcoSerie;
    }

    public BigDecimal getRcoValorRetencionIva() {
        return rcoValorRetencionIva;
    }

    public void setRcoValorRetencionIva(BigDecimal rcoValorRetencionIva) {
        this.rcoValorRetencionIva = rcoValorRetencionIva;
    }

    @XmlTransient
    public Collection<DetalleRetencionCompra> getDetalleRetencionCompraCollection() {
        return detalleRetencionCompraCollection;
    }

    public void setDetalleRetencionCompraCollection(Collection<DetalleRetencionCompra> detalleRetencionCompraCollection) {
        this.detalleRetencionCompraCollection = detalleRetencionCompraCollection;
    }

    public CabeceraCompra getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(CabeceraCompra idCabecera) {
        this.idCabecera = idCabecera;
    }

    public Date getCabFechaEmision() {
        return cabFechaEmision;
    }

    public void setCabFechaEmision(Date cabFechaEmision) {
        this.cabFechaEmision = cabFechaEmision;
    }

    public String getRcoSecuencialText() {
        return rcoSecuencialText;
    }

    public void setRcoSecuencialText(String rcoSecuencialText) {
        this.rcoSecuencialText = rcoSecuencialText;
    }

    public String getDrcMensajesri() {
        return drcMensajesri;
    }

    public void setDrcMensajesri(String drcMensajesri) {
        this.drcMensajesri = drcMensajesri;
    }

    public String getDrcEstadosri() {
        return drcEstadosri;
    }

    public void setDrcEstadosri(String drcEstadosri) {
        this.drcEstadosri = drcEstadosri;
    }

    public Date getRcoFechaAutorizacion() {
        return rcoFechaAutorizacion;
    }

    public void setRcoFechaAutorizacion(Date rcoFechaAutorizacion) {
        this.rcoFechaAutorizacion = rcoFechaAutorizacion;
    }

    public String getRcoPathRet() {
        return rcoPathRet;
    }

    public void setRcoPathRet(String rcoPathRet) {
        this.rcoPathRet = rcoPathRet;
    }

    public Tipoambiente getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Tipoambiente codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }

    public String getRcoMsmInfoSri() {
        return rcoMsmInfoSri;
    }

    public void setRcoMsmInfoSri(String rcoMsmInfoSri) {
        this.rcoMsmInfoSri = rcoMsmInfoSri;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rcoCodigo != null ? rcoCodigo.hashCode() : 0);
        return hash;
    }

    public BigDecimal getRcoBaseGravaIva() {
        return rcoBaseGravaIva;
    }

    public void setRcoBaseGravaIva(BigDecimal rcoBaseGravaIva) {
        this.rcoBaseGravaIva = rcoBaseGravaIva;
    }

    public String getProvNombre() {
        return provNombre;
    }

    public void setProvNombre(String provNombre) {
        this.provNombre = provNombre;
    }

    public String getProvCedula() {
        return provCedula;
    }

    public void setProvCedula(String provCedula) {
        this.provCedula = provCedula;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RetencionCompra)) {
            return false;
        }
        RetencionCompra other = (RetencionCompra) object;
        if ((this.rcoCodigo == null && other.rcoCodigo != null) || (this.rcoCodigo != null && !this.rcoCodigo.equals(other.rcoCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.RetencionCompra[ rcoCodigo=" + rcoCodigo + " ]";
    }

    public String getRazonSocialEmpresa() {
        return razonSocialEmpresa;
    }

    public void setRazonSocialEmpresa(String razonSocialEmpresa) {
        this.razonSocialEmpresa = razonSocialEmpresa;
    }

    public String getNombreComercialEmpresa() {
        return nombreComercialEmpresa;
    }

    public void setNombreComercialEmpresa(String nombreComercialEmpresa) {
        this.nombreComercialEmpresa = nombreComercialEmpresa;
    }

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public String getEstablecimientoEmpresa() {
        return establecimientoEmpresa;
    }

    public void setEstablecimientoEmpresa(String establecimientoEmpresa) {
        this.establecimientoEmpresa = establecimientoEmpresa;
    }

    public String getPuntoEmisionEmpresa() {
        return puntoEmisionEmpresa;
    }

    public void setPuntoEmisionEmpresa(String puntoEmisionEmpresa) {
        this.puntoEmisionEmpresa = puntoEmisionEmpresa;
    }

    public String getDirMatriz() {
        return dirMatriz;
    }

    public void setDirMatriz(String dirMatriz) {
        this.dirMatriz = dirMatriz;
    }

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getTipoIdentificacionSujetoRetenido() {
        return tipoIdentificacionSujetoRetenido;
    }

    public void setTipoIdentificacionSujetoRetenido(String tipoIdentificacionSujetoRetenido) {
        this.tipoIdentificacionSujetoRetenido = tipoIdentificacionSujetoRetenido;
    }

    public String getRazonSocialSujetoRetenido() {
        return razonSocialSujetoRetenido;
    }

    public void setRazonSocialSujetoRetenido(String razonSocialSujetoRetenido) {
        this.razonSocialSujetoRetenido = razonSocialSujetoRetenido;
    }

    public String getIdentificacionSujetoRetenido() {
        return identificacionSujetoRetenido;
    }

    public void setIdentificacionSujetoRetenido(String identificacionSujetoRetenido) {
        this.identificacionSujetoRetenido = identificacionSujetoRetenido;
    }

    public Date getPeriodoFiscal() {
        return periodoFiscal;
    }

    public void setPeriodoFiscal(Date periodoFiscal) {
        this.periodoFiscal = periodoFiscal;
    }

    public Boolean getAmAgeRet() {
        return amAgeRet;
    }

    public void setAmAgeRet(Boolean amAgeRet) {
        this.amAgeRet = amAgeRet;
    }

    public Boolean getAmRimpe() {
        return amRimpe;
    }

    public void setAmRimpe(Boolean amRimpe) {
        this.amRimpe = amRimpe;
    }

    public Boolean getAmGeneral() {
        return amGeneral;
    }

    public void setAmGeneral(Boolean amGeneral) {
        this.amGeneral = amGeneral;
    }

    
}
