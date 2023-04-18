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
public class InfoAutoriza {

    private String rutaArchivo;
    private String rutaFirma;
    private String passwordFirma;
      private String ambiente;

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getRutaFirma() {
        return rutaFirma;
    }

    public void setRutaFirma(String rutaFirma) {
        this.rutaFirma = rutaFirma;
    }

    public String getPasswordFirma() {
        return passwordFirma;
    }

    public void setPasswordFirma(String passwordFirma) {
        this.passwordFirma = passwordFirma;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

}
