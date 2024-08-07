/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.dao;

/**
 *
 * @author Darwin
 */
public class RequestAnulaDoc {

    private Integer facNumero;
    private String rucEmpresa;
    private String establecimientoEmpresa;
    private String puntoEmisionEmpresa;

    public Integer getFacNumero() {
        return facNumero;
    }


    
    public void setFacNumero(Integer facNumero) {
        this.facNumero = facNumero;
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

   

}
