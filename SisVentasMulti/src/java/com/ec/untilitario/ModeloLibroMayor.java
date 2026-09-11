package com.ec.untilitario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ModeloLibroMayor {

    private String codigo;
    private String nombre;
    private BigDecimal saldoInicial = BigDecimal.ZERO;
    private BigDecimal debe = BigDecimal.ZERO;
    private BigDecimal haber = BigDecimal.ZERO;
    private BigDecimal saldoFinal = BigDecimal.ZERO;
    private BigDecimal saldoDeudor = BigDecimal.ZERO;
    private BigDecimal saldoAcreedor = BigDecimal.ZERO;
    private List<ModeloLibroMayorMov> movimientos = new ArrayList<ModeloLibroMayorMov>();

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public BigDecimal getSaldoDeudor() {
        return saldoDeudor;
    }

    public void setSaldoDeudor(BigDecimal saldoDeudor) {
        this.saldoDeudor = saldoDeudor;
    }

    public BigDecimal getSaldoAcreedor() {
        return saldoAcreedor;
    }

    public void setSaldoAcreedor(BigDecimal saldoAcreedor) {
        this.saldoAcreedor = saldoAcreedor;
    }

    public List<ModeloLibroMayorMov> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<ModeloLibroMayorMov> movimientos) {
        this.movimientos = movimientos;
    }
}
