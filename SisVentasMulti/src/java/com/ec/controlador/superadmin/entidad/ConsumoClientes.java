/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.superadmin.entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Darwin Morocho
 */
@Entity
@Table(name = "consumoclientes")
public class ConsumoClientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @Column(name = "detalle_cobro_plan_contratado")
    private String detalleCobroPlanContratado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "documentos")
    private BigInteger documentos;
    @Column(name = "am_ruc")
    private String amRuc;
    @Column(name = "am_razon_social")
    private String amRazonSocial;

    public ConsumoClientes() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetalleCobroPlanContratado() {
        return detalleCobroPlanContratado;
    }

    public void setDetalleCobroPlanContratado(String detalleCobroPlanContratado) {
        this.detalleCobroPlanContratado = detalleCobroPlanContratado;
    }

    public BigInteger getDocumentos() {
        return documentos == null ? BigInteger.ZERO : documentos;
    }

    public void setDocumentos(BigInteger documentos) {
        this.documentos = documentos;
    }

    public String getAmRuc() {
        return amRuc == null ? "" : amRuc;
    }

    public void setAmRuc(String amRuc) {
        this.amRuc = amRuc;
    }

    public String getAmRazonSocial() {
        return amRazonSocial == null ? "" : amRazonSocial;
    }

    public void setAmRazonSocial(String amRazonSocial) {
        this.amRazonSocial = amRazonSocial;
    }

}
