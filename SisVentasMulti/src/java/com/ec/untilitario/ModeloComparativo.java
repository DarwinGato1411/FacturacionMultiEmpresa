package com.ec.untilitario;

import java.math.BigDecimal;

public class ModeloComparativo {

    private String codigo;
    private String nombre;
    private BigDecimal saldoA = BigDecimal.ZERO;
    private BigDecimal saldoB = BigDecimal.ZERO;
    private BigDecimal diferencia = BigDecimal.ZERO;

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

    public BigDecimal getSaldoA() {
        return saldoA;
    }

    public void setSaldoA(BigDecimal saldoA) {
        this.saldoA = saldoA;
    }

    public BigDecimal getSaldoB() {
        return saldoB;
    }

    public void setSaldoB(BigDecimal saldoB) {
        this.saldoB = saldoB;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }
}
