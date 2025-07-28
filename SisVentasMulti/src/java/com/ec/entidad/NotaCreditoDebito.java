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
@Table(name = "nota_credito_debito")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotaCreditoDebito.findAll", query = "SELECT n FROM NotaCreditoDebito n")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findUltimaNC", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facNumero IS NOT NULL ORDER BY  n.facNumero DESC")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByIdNota", query = "SELECT n FROM NotaCreditoDebito n WHERE n.idNota = :idNota")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacFecha", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facFecha = :facFecha")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacSubtotal", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facSubtotal = :facSubtotal")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacIva", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facIva = :facIva")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacTotal", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facTotal = :facTotal")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacEstado", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facEstado = :facEstado")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacTipo", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facTipo = :facTipo")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacAbono", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facAbono = :facAbono")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacSaldo", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facSaldo = :facSaldo")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacDescripcion", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facDescripcion = :facDescripcion")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacNumProforma", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facNumProforma = :facNumProforma")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByTipodocumento", query = "SELECT n FROM NotaCreditoDebito n WHERE n.tipodocumento = :tipodocumento")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByPuntoemision", query = "SELECT n FROM NotaCreditoDebito n WHERE n.puntoemision = :puntoemision")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByCodestablecimiento", query = "SELECT n FROM NotaCreditoDebito n WHERE n.codestablecimiento = :codestablecimiento")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacNumeroText", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facNumeroText = :facNumeroText")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacTipoIdentificadorComprobador", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facTipoIdentificadorComprobador = :facTipoIdentificadorComprobador")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacDescuento", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facDescuento = :facDescuento")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacCodIce", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facCodIce = :facCodIce")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacCodIva", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facCodIva = :facCodIva")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacTotalBaseCero", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facTotalBaseCero = :facTotalBaseCero")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacTotalBaseGravaba", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facTotalBaseGravaba = :facTotalBaseGravaba")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByCodigoPorcentaje", query = "SELECT n FROM NotaCreditoDebito n WHERE n.codigoPorcentaje = :codigoPorcentaje")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacPorcentajeIva", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facPorcentajeIva = :facPorcentajeIva")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacMoneda", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facMoneda = :facMoneda")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByIdFormaPago", query = "SELECT n FROM NotaCreditoDebito n WHERE n.idFormaPago = :idFormaPago")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacPlazo", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facPlazo = :facPlazo")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacUnidadTiempo", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facUnidadTiempo = :facUnidadTiempo")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByEstadosri", query = "SELECT n FROM NotaCreditoDebito n WHERE n.estadosri = :estadosri")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByMensajesri", query = "SELECT n FROM NotaCreditoDebito n WHERE n.mensajesri = :mensajesri")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacFechaAutorizacion", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facFechaAutorizacion = :facFechaAutorizacion")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacClaveAcceso", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facClaveAcceso = :facClaveAcceso")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByCodTipoambiente", query = "SELECT n FROM NotaCreditoDebito n WHERE n.codTipoambiente = :codTipoambiente")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacClaveAutorizacion", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facClaveAutorizacion = :facClaveAutorizacion")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacPath", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facPath = :facPath")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByTipodocumentomod", query = "SELECT n FROM NotaCreditoDebito n WHERE n.tipodocumentomod = :tipodocumentomod")
    ,
    @NamedQuery(name = "NotaCreditoDebito.findByFacFechaSustento", query = "SELECT n FROM NotaCreditoDebito n WHERE n.facFechaSustento = :facFechaSustento")})
public class NotaCreditoDebito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_nota")
    private Integer idNota;
    @Column(name = "fac_numero")
    private Integer facNumero;
    @Column(name = "fac_fecha")
    @Temporal(TemporalType.DATE)
    private Date facFecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fac_subtotal")
    private BigDecimal facSubtotal;
    @Column(name = "fac_iva")
    private BigDecimal facIva;
    @Column(name = "fac_total")
    private BigDecimal facTotal;
    @Size(max = 20)
    @Column(name = "fac_estado")
    private String facEstado;
    @Size(max = 20)
    @Column(name = "fac_tipo")
    private String facTipo;
    @Column(name = "fac_abono")
    private BigDecimal facAbono;
    @Column(name = "fac_saldo")
    private BigDecimal facSaldo;
    @Size(max = 150)
    @Column(name = "fac_descripcion")
    private String facDescripcion;
    @Column(name = "fac_num_proforma")
    private Integer facNumProforma;
    @Size(max = 2147483647)
    @Column(name = "tipodocumento")
    private String tipodocumento;
    @Size(max = 2147483647)
    @Column(name = "puntoemision")
    private String puntoemision;
    @Size(max = 2147483647)
    @Column(name = "codestablecimiento")
    private String codestablecimiento;
    @Size(max = 2147483647)
    @Column(name = "fac_numero_text")
    private String facNumeroText;
    @Size(max = 2147483647)
    @Column(name = "fac_tipo_identificador_comprobador")
    private String facTipoIdentificadorComprobador;
    @Column(name = "fac_descuento")
    private BigDecimal facDescuento;
    @Size(max = 2147483647)
    @Column(name = "fac_cod_ice")
    private String facCodIce;
    @Size(max = 2147483647)
    @Column(name = "fac_cod_iva")
    private String facCodIva;
    @Column(name = "fac_total_base_cero")
    private BigDecimal facTotalBaseCero;
    @Column(name = "fac_total_base_gravaba")
    private BigDecimal facTotalBaseGravaba;
    @Size(max = 2147483647)
    @Column(name = "codigo_porcentaje")
    private String codigoPorcentaje;
    @Size(max = 2147483647)
    @Column(name = "fac_porcentaje_iva")
    private String facPorcentajeIva;
    @Size(max = 2147483647)
    @Column(name = "fac_moneda")
    private String facMoneda;
    @Column(name = "id_forma_pago")
    private Integer idFormaPago;
    @Column(name = "fac_plazo")
    private BigDecimal facPlazo;
    @Size(max = 2147483647)
    @Column(name = "fac_unidad_tiempo")
    private String facUnidadTiempo;
    @Size(max = 2147483647)
    @Column(name = "estadosri")
    private String estadosri;
    @Size(max = 2147483647)
    @Column(name = "mensajesri")
    private String mensajesri;
    @Column(name = "fac_fecha_autorizacion")
    @Temporal(TemporalType.DATE)
    private Date facFechaAutorizacion;
    @Size(max = 2147483647)
    @Column(name = "fac_clave_acceso")
    private String facClaveAcceso;
    @Column(name = "cod_tipoambiente")
    private Integer codTipoambiente;
    @Size(max = 2147483647)
    @Column(name = "fac_clave_autorizacion")
    private String facClaveAutorizacion;
    @Size(max = 2147483647)
    @Column(name = "fac_path")
    private String facPath;
    @Size(max = 2147483647)
    @Column(name = "tipodocumentomod")
    private String tipodocumentomod;
    @Column(name = "fac_fecha_sustento")
    @Temporal(TemporalType.DATE)
    private Date facFechaSustento;
    @Column(name = "mensajeinf")
    private String mensajeInf;

    @OneToMany(mappedBy = "idNota")
    private Collection<DetalleNotaDebitoCredito> detalleNotaDebitoCreditoCollection;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura")
    @ManyToOne
    private Factura idFactura;

    @Column(name = "am_razon_social")
    private String amRazonSocial;
    @Column(name = "am_nombre_comercial")
    private String amNombreComercial;
    @Column(name = "am_direccion_atriz")
    private String amDireccionMatriz;
    @Column(name = "llevar_contabilidad")
    private String llevarContabilidad;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "rucEmpresa")
    private String rucEmpresa;
    @Column(name = "dir_establecimiento")
    private String dirEstablecimiento;
    @Column(name = "cod_doc_modificado")
    private String codDocModificado;
    @Column(name = "num_doc_modificado")
    private String numDocModificado;

    @Column(name = "am_codigo")
    private String amCodigo;
    @Column(name = "razon_social_comprador")
    private String razonSocialComprador;
    @Column(name = "identificacion_comprador")
    private String identificacionComprador;
    @Column(name = "am_ambiente")
    private String amAmbiente;
    @Column(name = "am_age_ret")
    private Boolean amAgeRet;
    @Column(name = "am_rimpe")
    private Boolean amRimpe;
    @Column(name = "am_general")
    private Boolean amGeneral;
    @Column(name = "correo")
    private String correo;

    @Column(name = "fac_subt_5")
    private BigDecimal facSubt5;
    @Column(name = "fac_iva_5")
    private BigDecimal facIva5;
    @Column(name = "fac_subt_13")
    private BigDecimal facSubt13;
    @Column(name = "fac_iva_13")
    private BigDecimal facIva13;
    @Column(name = "fac_subt_14")
    private BigDecimal facSubt14;
    @Column(name = "fac_iva_14")
    private BigDecimal facIva14;
    @Column(name = "fac_subt_15")
    private BigDecimal facSubt15;
    @Column(name = "fac_iva_15")
    private BigDecimal facIva15;
    @Column(name = "detalle_sri")
    private String detalleSri;

    @Column(name = "razon_social_empresa")
    private String razonSocialEmpresa;
    @Column(name = "nombre_comercial_empresa")
    private String nombreComercialEmpresa;
    @Column(name = "tipo_identificacion_comp")
    private String tipoIdentificacionComprador;
    @Column(name = "fac_subsidio")
    private BigDecimal facSubsidio;
    @Column(name = "graba_ice")
    private Boolean grabaICE;
//    @Column(name = "codigo_ice")
//    private String codigoICE;

    @Column(name = "fac_valor_ice")
    private BigDecimal facValorIce;

    @Column(name = "codigo_forma_pago")
    private String codigoFormaPago;
//        @Column(name = "graba_ice")
//    private Boolean grabaICE;
    @Column(name = "codigo_ice")
    private String codigoICE;
    @Column(name = "tarifa_ice")
    private BigDecimal facTarifaIce;

    @Column(name = "agente_retencion")
    private Boolean agenteRetencion;
    @Column(name = "regimen_popular")
    private Boolean rimpePolpular;
    @Column(name = "regimen_emprendedor")
    private Boolean rimpeEmprendedor;
    @Column(name = "regimen_general")
    private Boolean regimenGeneral;
    @Column(name = "direccion_comprador")
    private String direccionComprador;
    @Column(name = "correo_comprador")
    private String correoComprador;
    @Column(name = "fac_observacion")
    private String facObservacion;

    @Column(name = "telefono_comprador")
    private String telefonoComprador;

    @Column(name = "direccion_matriz")
    private String direccionMatriz;
    @Column(name = "tipo_emision")
    private String tipoEmision;
    @Column(name = "movil_comprador")
    private String movilComprador;

    public NotaCreditoDebito() {
    }

    public NotaCreditoDebito(Integer idNota) {
        this.idNota = idNota;
    }

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }

    public BigDecimal getFacSubtotal() {
        return facSubtotal;
    }

    public void setFacSubtotal(BigDecimal facSubtotal) {
        this.facSubtotal = facSubtotal;
    }

    public BigDecimal getFacIva() {
        return facIva;
    }

    public void setFacIva(BigDecimal facIva) {
        this.facIva = facIva;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public String getFacEstado() {
        return facEstado;
    }

    public void setFacEstado(String facEstado) {
        this.facEstado = facEstado;
    }

    public String getFacTipo() {
        return facTipo;
    }

    public void setFacTipo(String facTipo) {
        this.facTipo = facTipo;
    }

    public BigDecimal getFacAbono() {
        return facAbono;
    }

    public void setFacAbono(BigDecimal facAbono) {
        this.facAbono = facAbono;
    }

    public BigDecimal getFacSaldo() {
        return facSaldo;
    }

    public void setFacSaldo(BigDecimal facSaldo) {
        this.facSaldo = facSaldo;
    }

    public String getFacDescripcion() {
        return facDescripcion;
    }

    public void setFacDescripcion(String facDescripcion) {
        this.facDescripcion = facDescripcion;
    }

    public Integer getFacNumProforma() {
        return facNumProforma;
    }

    public void setFacNumProforma(Integer facNumProforma) {
        this.facNumProforma = facNumProforma;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getPuntoemision() {
        return puntoemision;
    }

    public void setPuntoemision(String puntoemision) {
        this.puntoemision = puntoemision;
    }

    public String getCodestablecimiento() {
        return codestablecimiento;
    }

    public void setCodestablecimiento(String codestablecimiento) {
        this.codestablecimiento = codestablecimiento;
    }

    public String getFacNumeroText() {
        return facNumeroText;
    }

    public void setFacNumeroText(String facNumeroText) {
        this.facNumeroText = facNumeroText;
    }

    public String getFacTipoIdentificadorComprobador() {
        return facTipoIdentificadorComprobador;
    }

    public void setFacTipoIdentificadorComprobador(String facTipoIdentificadorComprobador) {
        this.facTipoIdentificadorComprobador = facTipoIdentificadorComprobador;
    }

    public BigDecimal getFacDescuento() {
        return facDescuento;
    }

    public void setFacDescuento(BigDecimal facDescuento) {
        this.facDescuento = facDescuento;
    }

    public String getFacCodIce() {
        return facCodIce;
    }

    public void setFacCodIce(String facCodIce) {
        this.facCodIce = facCodIce;
    }

    public String getFacCodIva() {
        return facCodIva;
    }

    public void setFacCodIva(String facCodIva) {
        this.facCodIva = facCodIva;
    }

    public BigDecimal getFacTotalBaseCero() {
        return facTotalBaseCero;
    }

    public void setFacTotalBaseCero(BigDecimal facTotalBaseCero) {
        this.facTotalBaseCero = facTotalBaseCero;
    }

    public BigDecimal getFacTotalBaseGravaba() {
        return facTotalBaseGravaba;
    }

    public void setFacTotalBaseGravaba(BigDecimal facTotalBaseGravaba) {
        this.facTotalBaseGravaba = facTotalBaseGravaba;
    }

    public String getCodigoPorcentaje() {
        return codigoPorcentaje;
    }

    public void setCodigoPorcentaje(String codigoPorcentaje) {
        this.codigoPorcentaje = codigoPorcentaje;
    }

    public String getFacPorcentajeIva() {
        return facPorcentajeIva;
    }

    public void setFacPorcentajeIva(String facPorcentajeIva) {
        this.facPorcentajeIva = facPorcentajeIva;
    }

    public String getFacMoneda() {
        return facMoneda;
    }

    public void setFacMoneda(String facMoneda) {
        this.facMoneda = facMoneda;
    }

    public Integer getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(Integer idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public BigDecimal getFacPlazo() {
        return facPlazo;
    }

    public void setFacPlazo(BigDecimal facPlazo) {
        this.facPlazo = facPlazo;
    }

    public String getFacUnidadTiempo() {
        return facUnidadTiempo;
    }

    public void setFacUnidadTiempo(String facUnidadTiempo) {
        this.facUnidadTiempo = facUnidadTiempo;
    }

    public String getEstadosri() {
        return estadosri;
    }

    public void setEstadosri(String estadosri) {
        this.estadosri = estadosri;
    }

    public String getMensajesri() {
        return mensajesri;
    }

    public void setMensajesri(String mensajesri) {
        this.mensajesri = mensajesri;
    }

    public Date getFacFechaAutorizacion() {
        return facFechaAutorizacion;
    }

    public void setFacFechaAutorizacion(Date facFechaAutorizacion) {
        this.facFechaAutorizacion = facFechaAutorizacion;
    }

    public String getFacClaveAcceso() {
        return facClaveAcceso;
    }

    public void setFacClaveAcceso(String facClaveAcceso) {
        this.facClaveAcceso = facClaveAcceso;
    }

    public Integer getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Integer codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }

    public String getFacClaveAutorizacion() {
        return facClaveAutorizacion;
    }

    public void setFacClaveAutorizacion(String facClaveAutorizacion) {
        this.facClaveAutorizacion = facClaveAutorizacion;
    }

    public String getFacPath() {
        return facPath;
    }

    public void setFacPath(String facPath) {
        this.facPath = facPath;
    }

    public String getTipodocumentomod() {
        return tipodocumentomod;
    }

    public void setTipodocumentomod(String tipodocumentomod) {
        this.tipodocumentomod = tipodocumentomod;
    }

    public Date getFacFechaSustento() {
        return facFechaSustento;
    }

    public void setFacFechaSustento(Date facFechaSustento) {
        this.facFechaSustento = facFechaSustento;
    }

    @XmlTransient
    public Collection<DetalleNotaDebitoCredito> getDetalleNotaDebitoCreditoCollection() {
        return detalleNotaDebitoCreditoCollection;
    }

    public void setDetalleNotaDebitoCreditoCollection(Collection<DetalleNotaDebitoCredito> detalleNotaDebitoCreditoCollection) {
        this.detalleNotaDebitoCreditoCollection = detalleNotaDebitoCreditoCollection;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Factura getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Factura idFactura) {
        this.idFactura = idFactura;
    }

    public Integer getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(Integer facNumero) {
        this.facNumero = facNumero;
    }

    public String getMensajeInf() {
        return mensajeInf;
    }

    public void setMensajeInf(String mensajeInf) {
        this.mensajeInf = mensajeInf;
    }

    public String getAmCodigo() {
        return amCodigo;
    }

    public void setAmCodigo(String amCodigo) {
        this.amCodigo = amCodigo;
    }

    public String getAmRazonSocial() {
        return amRazonSocial;
    }

    public void setAmRazonSocial(String amRazonSocial) {
        this.amRazonSocial = amRazonSocial;
    }

    public String getAmNombreComercial() {
        return amNombreComercial;
    }

    public void setAmNombreComercial(String amNombreComercial) {
        this.amNombreComercial = amNombreComercial;
    }

    public String getAmDireccionMatriz() {
        return amDireccionMatriz;
    }

    public void setAmDireccionMatriz(String amDireccionMatriz) {
        this.amDireccionMatriz = amDireccionMatriz;
    }

    public String getLlevarContabilidad() {
        return llevarContabilidad;
    }

    public void setLlevarContabilidad(String llevarContabilidad) {
        this.llevarContabilidad = llevarContabilidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public String getRazonSocialComprador() {
        return razonSocialComprador;
    }

    public void setRazonSocialComprador(String razonSocialComprador) {
        this.razonSocialComprador = razonSocialComprador;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public String getAmAmbiente() {
        return amAmbiente;
    }

    public void setAmAmbiente(String amAmbiente) {
        this.amAmbiente = amAmbiente;
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

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public String getCodDocModificado() {
        return codDocModificado;
    }

    public void setCodDocModificado(String codDocModificado) {
        this.codDocModificado = codDocModificado;
    }

    public String getNumDocModificado() {
        return numDocModificado;
    }

    public void setNumDocModificado(String numDocModificado) {
        this.numDocModificado = numDocModificado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNota != null ? idNota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaCreditoDebito)) {
            return false;
        }
        NotaCreditoDebito other = (NotaCreditoDebito) object;
        if ((this.idNota == null && other.idNota != null) || (this.idNota != null && !this.idNota.equals(other.idNota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.NotaCreditoDebito[ idNota=" + idNota + " ]";
    }

    public BigDecimal getFacSubt5() {
        return facSubt5 == null ? BigDecimal.ZERO : facSubt5;
    }

    public void setFacSubt5(BigDecimal facSubt5) {
        this.facSubt5 = facSubt5;
    }

    public BigDecimal getFacIva5() {
        return facIva5 == null ? BigDecimal.ZERO : facIva5;
    }

    public void setFacIva5(BigDecimal facIva5) {
        this.facIva5 = facIva5;
    }

    public BigDecimal getFacSubt13() {
        return facSubt13 == null ? BigDecimal.ZERO : facSubt13;
    }

    public void setFacSubt13(BigDecimal facSubt13) {
        this.facSubt13 = facSubt13;
    }

    public BigDecimal getFacIva13() {
        return facIva13 == null ? BigDecimal.ZERO : facIva13;
    }

    public void setFacIva13(BigDecimal facIva13) {
        this.facIva13 = facIva13;
    }

    public BigDecimal getFacSubt14() {
        return facSubt14 == null ? BigDecimal.ZERO : facSubt14;
    }

    public void setFacSubt14(BigDecimal facSubt14) {
        this.facSubt14 = facSubt14;
    }

    public BigDecimal getFacIva14() {
        return facIva14 == null ? BigDecimal.ZERO : facIva14;
    }

    public void setFacIva14(BigDecimal facIva14) {
        this.facIva14 = facIva14;
    }

    public BigDecimal getFacSubt15() {
        return facSubt15 == null ? BigDecimal.ZERO : facSubt15;
    }

    public void setFacSubt15(BigDecimal facSubt15) {
        this.facSubt15 = facSubt15;
    }

    public BigDecimal getFacIva15() {
        return facIva15 == null ? BigDecimal.ZERO : facIva15;
    }

    public void setFacIva15(BigDecimal facIva15) {
        this.facIva15 = facIva15;
    }

    public String getDetalleSri() {
        return detalleSri;
    }

    public void setDetalleSri(String detalleSri) {
        this.detalleSri = detalleSri;
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

    public String getTipoIdentificacionComprador() {
        return tipoIdentificacionComprador;
    }

    public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
        this.tipoIdentificacionComprador = tipoIdentificacionComprador;
    }

    public BigDecimal getFacSubsidio() {
        return facSubsidio;
    }

    public void setFacSubsidio(BigDecimal facSubsidio) {
        this.facSubsidio = facSubsidio;
    }

    public Boolean getGrabaICE() {
        return grabaICE;
    }

    public void setGrabaICE(Boolean grabaICE) {
        this.grabaICE = grabaICE;
    }

    public BigDecimal getFacValorIce() {
        return facValorIce;
    }

    public void setFacValorIce(BigDecimal facValorIce) {
        this.facValorIce = facValorIce;
    }

    public String getCodigoFormaPago() {
        return codigoFormaPago;
    }

    public void setCodigoFormaPago(String codigoFormaPago) {
        this.codigoFormaPago = codigoFormaPago;
    }

    public String getCodigoICE() {
        return codigoICE;
    }

    public void setCodigoICE(String codigoICE) {
        this.codigoICE = codigoICE;
    }

    public BigDecimal getFacTarifaIce() {
        return facTarifaIce;
    }

    public void setFacTarifaIce(BigDecimal facTarifaIce) {
        this.facTarifaIce = facTarifaIce;
    }

    public Boolean getAgenteRetencion() {
        return agenteRetencion;
    }

    public void setAgenteRetencion(Boolean agenteRetencion) {
        this.agenteRetencion = agenteRetencion;
    }

    public Boolean getRimpePolpular() {
        return rimpePolpular;
    }

    public void setRimpePolpular(Boolean rimpePolpular) {
        this.rimpePolpular = rimpePolpular;
    }

    public Boolean getRimpeEmprendedor() {
        return rimpeEmprendedor;
    }

    public void setRimpeEmprendedor(Boolean rimpeEmprendedor) {
        this.rimpeEmprendedor = rimpeEmprendedor;
    }

    public Boolean getRegimenGeneral() {
        return regimenGeneral;
    }

    public void setRegimenGeneral(Boolean regimenGeneral) {
        this.regimenGeneral = regimenGeneral;
    }

    public String getDireccionComprador() {
        return direccionComprador;
    }

    public void setDireccionComprador(String direccionComprador) {
        this.direccionComprador = direccionComprador;
    }

    public String getFacObservacion() {
        return facObservacion;
    }

    public void setFacObservacion(String facObservacion) {
        this.facObservacion = facObservacion;
    }

    public String getTelefonoComprador() {
        return telefonoComprador;
    }

    public void setTelefonoComprador(String telefonoComprador) {
        this.telefonoComprador = telefonoComprador;
    }

    public String getCorreoComprador() {
        return correoComprador;
    }

    public void setCorreoComprador(String correoComprador) {
        this.correoComprador = correoComprador;
    }

    public String getDireccionMatriz() {
        return direccionMatriz;
    }

    public void setDireccionMatriz(String direccionMatriz) {
        this.direccionMatriz = direccionMatriz;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public String getMovilComprador() {
        return movilComprador;
    }

    public void setMovilComprador(String movilComprador) {
        this.movilComprador = movilComprador;
    }

}
