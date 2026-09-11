package com.ec.untilitario;

import java.math.BigDecimal;

public class ModeloEstadoLinea {

    private String codigo;
    private String nombre;
    private BigDecimal valor = BigDecimal.ZERO;
    private boolean titulo;
    private boolean total;

    public ModeloEstadoLinea() {
    }

    public ModeloEstadoLinea(String nombre, boolean titulo) {
        this.nombre = nombre;
        this.titulo = titulo;
    }

    public ModeloEstadoLinea(String codigo, String nombre, BigDecimal valor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.valor = valor;
    }

    public static ModeloEstadoLinea totalDe(String nombre, BigDecimal valor) {
        ModeloEstadoLinea l = new ModeloEstadoLinea(nombre, false);
        l.setValor(valor);
        l.setTotal(true);
        return l;
    }

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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isTitulo() {
        return titulo;
    }

    public void setTitulo(boolean titulo) {
        this.titulo = titulo;
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }
}
