package com.ec.entidad.sri;

import com.ec.entidad.Tipoambiente;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "sri_comprobante_recibido")
public class SriComprobanteRecibido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scr_id")
    private Integer scrId;
    @Column(name = "std_codigo")
    private String stdCodigo;
    @Column(name = "sdl_id")
    private Integer sdlId;
    @Column(name = "scr_tipo_comprobante")
    private String scrTipoComprobante;
    @Column(name = "scr_serie")
    private String scrSerie;
    @Column(name = "scr_ruc_emisor")
    private String scrRucEmisor;
    @Column(name = "scr_razon_social")
    private String scrRazonSocial;
    @Column(name = "scr_fecha_emision")
    @Temporal(TemporalType.DATE)
    private Date scrFechaEmision;
    @Column(name = "scr_fecha_autorizacion")
    @Temporal(TemporalType.DATE)
    private Date scrFechaAutorizacion;
    @Column(name = "scr_tipo_emision")
    private String scrTipoEmision;
    @Column(name = "scr_identificacion_receptor")
    private String scrIdentificacionReceptor;
    @Column(name = "scr_clave_acceso")
    private String scrClaveAcceso;
    @Column(name = "scr_autorizacion")
    private String scrAutorizacion;
    @Column(name = "scr_total")
    private String scrTotal;
    @Column(name = "scr_clasificacion")
    private String scrClasificacion;
    @Column(name = "scr_estado_clasificacion")
    private String scrEstadoClasificacion;
    @Column(name = "scr_xml_descargado")
    private Boolean scrXmlDescargado;
    @Column(name = "scr_ruta_xml")
    private String scrRutaXml;
    @Column(name = "scr_fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scrFechaRegistro;
    @ManyToOne
    @JoinColumn(name = "cod_tipoambiente", referencedColumnName = "cod_tipoambiente")
    private Tipoambiente codTipoambiente;

    public Integer getScrId() {
        return scrId;
    }

    public void setScrId(Integer scrId) {
        this.scrId = scrId;
    }

    public String getStdCodigo() {
        return stdCodigo;
    }

    public void setStdCodigo(String stdCodigo) {
        this.stdCodigo = stdCodigo;
    }

    public Integer getSdlId() {
        return sdlId;
    }

    public void setSdlId(Integer sdlId) {
        this.sdlId = sdlId;
    }

    public String getScrTipoComprobante() {
        return scrTipoComprobante;
    }

    public void setScrTipoComprobante(String scrTipoComprobante) {
        this.scrTipoComprobante = scrTipoComprobante;
    }

    public String getScrSerie() {
        return scrSerie;
    }

    public void setScrSerie(String scrSerie) {
        this.scrSerie = scrSerie;
    }

    public String getScrRucEmisor() {
        return scrRucEmisor;
    }

    public void setScrRucEmisor(String scrRucEmisor) {
        this.scrRucEmisor = scrRucEmisor;
    }

    public String getScrRazonSocial() {
        return scrRazonSocial;
    }

    public void setScrRazonSocial(String scrRazonSocial) {
        this.scrRazonSocial = scrRazonSocial;
    }

    public Date getScrFechaEmision() {
        return scrFechaEmision;
    }

    public void setScrFechaEmision(Date scrFechaEmision) {
        this.scrFechaEmision = scrFechaEmision;
    }

    public Date getScrFechaAutorizacion() {
        return scrFechaAutorizacion;
    }

    public void setScrFechaAutorizacion(Date scrFechaAutorizacion) {
        this.scrFechaAutorizacion = scrFechaAutorizacion;
    }

    public String getScrTipoEmision() {
        return scrTipoEmision;
    }

    public void setScrTipoEmision(String scrTipoEmision) {
        this.scrTipoEmision = scrTipoEmision;
    }

    public String getScrIdentificacionReceptor() {
        return scrIdentificacionReceptor;
    }

    public void setScrIdentificacionReceptor(String scrIdentificacionReceptor) {
        this.scrIdentificacionReceptor = scrIdentificacionReceptor;
    }

    public String getScrClaveAcceso() {
        return scrClaveAcceso;
    }

    public void setScrClaveAcceso(String scrClaveAcceso) {
        this.scrClaveAcceso = scrClaveAcceso;
    }

    public String getScrAutorizacion() {
        return scrAutorizacion;
    }

    public void setScrAutorizacion(String scrAutorizacion) {
        this.scrAutorizacion = scrAutorizacion;
    }

    public String getScrTotal() {
        return scrTotal;
    }

    public void setScrTotal(String scrTotal) {
        this.scrTotal = scrTotal;
    }

    public String getScrClasificacion() {
        return scrClasificacion;
    }

    public void setScrClasificacion(String scrClasificacion) {
        this.scrClasificacion = scrClasificacion;
    }

    public String getScrEstadoClasificacion() {
        return scrEstadoClasificacion;
    }

    public void setScrEstadoClasificacion(String scrEstadoClasificacion) {
        this.scrEstadoClasificacion = scrEstadoClasificacion;
    }

    public Boolean getScrXmlDescargado() {
        return scrXmlDescargado;
    }

    public void setScrXmlDescargado(Boolean scrXmlDescargado) {
        this.scrXmlDescargado = scrXmlDescargado;
    }

    public String getScrRutaXml() {
        return scrRutaXml;
    }

    public void setScrRutaXml(String scrRutaXml) {
        this.scrRutaXml = scrRutaXml;
    }

    public Date getScrFechaRegistro() {
        return scrFechaRegistro;
    }

    public void setScrFechaRegistro(Date scrFechaRegistro) {
        this.scrFechaRegistro = scrFechaRegistro;
    }

    public Tipoambiente getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Tipoambiente codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }

    public String getXmlEstadoTexto() {
        return Boolean.TRUE.equals(scrXmlDescargado) ? "XML" : "";
    }
}
