package com.ec.entidad.partida;

import java.io.Serializable;
import com.ec.entidad.Tipoambiente;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

@Entity
@Table(name = "cu_asiento")
@NamedQueries({
    @NamedQuery(name = "CuAsiento.findAll", query = "SELECT c FROM CuAsiento c ORDER BY c.asiFecha DESC, c.asiNumero DESC")
    , @NamedQuery(name = "CuAsiento.findBetweenFecha", query = "SELECT c FROM CuAsiento c WHERE c.codTipoambiente = :ambiente AND c.asiFecha BETWEEN :inicio AND :fin ORDER BY c.asiFecha, c.asiNumero")
    , @NamedQuery(name = "CuAsiento.findByOrigenDocumento", query = "SELECT c FROM CuAsiento c WHERE c.codTipoambiente = :ambiente AND c.asiOrigen = :origen AND c.asiIdDocumento = :idDocumento")})
public class CuAsiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_asiento")
    private Integer idAsiento;
    @Column(name = "asi_fecha")
    @Temporal(TemporalType.DATE)
    private Date asiFecha;
    @Column(name = "asi_numero")
    private Integer asiNumero;
    @Column(name = "asi_glosa")
    private String asiGlosa;
    @Column(name = "asi_origen")
    private String asiOrigen;
    @Column(name = "asi_id_documento")
    private Integer asiIdDocumento;
    @Column(name = "asi_tipo_doc")
    private String asiTipoDoc;
    @Column(name = "asi_estado")
    private String asiEstado;
    @JoinColumn(name = "cod_tipoambiente", referencedColumnName = "cod_tipoambiente")
    @ManyToOne
    private Tipoambiente codTipoambiente;
    @OneToMany(mappedBy = "idAsiento", cascade = CascadeType.ALL)
    private Collection<CuAsientoDetalle> detalleCollection = new ArrayList<CuAsientoDetalle>();

    public Integer getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Integer idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Date getAsiFecha() {
        return asiFecha;
    }

    public void setAsiFecha(Date asiFecha) {
        this.asiFecha = asiFecha;
    }

    public Integer getAsiNumero() {
        return asiNumero;
    }

    public void setAsiNumero(Integer asiNumero) {
        this.asiNumero = asiNumero;
    }

    public String getAsiGlosa() {
        return asiGlosa;
    }

    public void setAsiGlosa(String asiGlosa) {
        this.asiGlosa = asiGlosa;
    }

    public String getAsiOrigen() {
        return asiOrigen;
    }

    public void setAsiOrigen(String asiOrigen) {
        this.asiOrigen = asiOrigen;
    }

    public Integer getAsiIdDocumento() {
        return asiIdDocumento;
    }

    public void setAsiIdDocumento(Integer asiIdDocumento) {
        this.asiIdDocumento = asiIdDocumento;
    }

    public String getAsiTipoDoc() {
        return asiTipoDoc;
    }

    public void setAsiTipoDoc(String asiTipoDoc) {
        this.asiTipoDoc = asiTipoDoc;
    }

    public String getAsiEstado() {
        return asiEstado;
    }

    public void setAsiEstado(String asiEstado) {
        this.asiEstado = asiEstado;
    }

    public Tipoambiente getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Tipoambiente codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }

    public Collection<CuAsientoDetalle> getDetalleCollection() {
        return detalleCollection;
    }

    public void setDetalleCollection(Collection<CuAsientoDetalle> detalleCollection) {
        this.detalleCollection = detalleCollection;
    }
}
