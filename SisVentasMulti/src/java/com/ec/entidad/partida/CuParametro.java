package com.ec.entidad.partida;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.ec.entidad.Tipoambiente;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cu_parametro")
@NamedQueries({
    @NamedQuery(name = "CuParametro.findAll", query = "SELECT c FROM CuParametro c ORDER BY c.parCodigo")
    , @NamedQuery(name = "CuParametro.findByAmbiente", query = "SELECT c FROM CuParametro c WHERE c.codTipoambiente = :ambiente ORDER BY c.parCodigo")
    , @NamedQuery(name = "CuParametro.findPlantilla", query = "SELECT c FROM CuParametro c WHERE c.codTipoambiente IS NULL ORDER BY c.parCodigo")
    , @NamedQuery(name = "CuParametro.findByParCodigo", query = "SELECT c FROM CuParametro c WHERE c.parCodigo = :parCodigo AND c.codTipoambiente = :ambiente")})
public class CuParametro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_parametro")
    private Integer idParametro;
    @Column(name = "par_codigo")
    private String parCodigo;
    @Column(name = "par_nombre")
    private String parNombre;
    @JoinColumn(name = "id_sub_cuenta", referencedColumnName = "id_sub_cuenta")
    @ManyToOne
    private CuSubCuenta idSubCuenta;
    @JoinColumn(name = "cod_tipoambiente", referencedColumnName = "cod_tipoambiente")
    @ManyToOne
    private Tipoambiente codTipoambiente;
    @javax.persistence.Transient
    private String codigoCuenta;

    public Integer getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(Integer idParametro) {
        this.idParametro = idParametro;
    }

    public String getParCodigo() {
        return parCodigo;
    }

    public void setParCodigo(String parCodigo) {
        this.parCodigo = parCodigo;
    }

    public String getParNombre() {
        return parNombre;
    }

    public void setParNombre(String parNombre) {
        this.parNombre = parNombre;
    }

    public CuSubCuenta getIdSubCuenta() {
        return idSubCuenta;
    }

    public void setIdSubCuenta(CuSubCuenta idSubCuenta) {
        this.idSubCuenta = idSubCuenta;
    }

    public Tipoambiente getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Tipoambiente codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }

    public String getCodigoCuenta() {
        if (codigoCuenta != null) {
            return codigoCuenta;
        }
        if (idSubCuenta != null) {
            return idSubCuenta.getSubcNumero();
        }
        return "";
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }
}
