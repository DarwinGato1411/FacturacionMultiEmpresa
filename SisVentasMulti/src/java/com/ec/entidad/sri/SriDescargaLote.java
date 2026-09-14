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
@Table(name = "sri_descarga_lote")
public class SriDescargaLote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdl_id")
    private Integer sdlId;
    @Column(name = "std_codigo")
    private String stdCodigo;
    @Column(name = "sdl_fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date sdlFechaInicio;
    @Column(name = "sdl_fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date sdlFechaFin;
    @Column(name = "sdl_fecha_ejecucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sdlFechaEjecucion;
    @Column(name = "sdl_origen")
    private String sdlOrigen;
    @Column(name = "sdl_estado")
    private String sdlEstado;
    @Column(name = "sdl_mensaje")
    private String sdlMensaje;
    @Column(name = "sdl_ruta_archivo")
    private String sdlRutaArchivo;
    @Column(name = "sdl_cantidad")
    private Integer sdlCantidad;
    @Column(name = "sdl_nuevos")
    private Integer sdlNuevos;
    @Column(name = "sdl_duplicados")
    private Integer sdlDuplicados;
    @Column(name = "sdl_usuario")
    private String sdlUsuario;
    @ManyToOne
    @JoinColumn(name = "cod_tipoambiente", referencedColumnName = "cod_tipoambiente")
    private Tipoambiente codTipoambiente;

    public Integer getSdlId() {
        return sdlId;
    }

    public void setSdlId(Integer sdlId) {
        this.sdlId = sdlId;
    }

    public String getStdCodigo() {
        return stdCodigo;
    }

    public void setStdCodigo(String stdCodigo) {
        this.stdCodigo = stdCodigo;
    }

    public Date getSdlFechaInicio() {
        return sdlFechaInicio;
    }

    public void setSdlFechaInicio(Date sdlFechaInicio) {
        this.sdlFechaInicio = sdlFechaInicio;
    }

    public Date getSdlFechaFin() {
        return sdlFechaFin;
    }

    public void setSdlFechaFin(Date sdlFechaFin) {
        this.sdlFechaFin = sdlFechaFin;
    }

    public Date getSdlFechaEjecucion() {
        return sdlFechaEjecucion;
    }

    public void setSdlFechaEjecucion(Date sdlFechaEjecucion) {
        this.sdlFechaEjecucion = sdlFechaEjecucion;
    }

    public String getSdlOrigen() {
        return sdlOrigen;
    }

    public void setSdlOrigen(String sdlOrigen) {
        this.sdlOrigen = sdlOrigen;
    }

    public String getSdlEstado() {
        return sdlEstado;
    }

    public void setSdlEstado(String sdlEstado) {
        this.sdlEstado = sdlEstado;
    }

    public String getSdlMensaje() {
        return sdlMensaje;
    }

    public void setSdlMensaje(String sdlMensaje) {
        this.sdlMensaje = sdlMensaje;
    }

    public String getSdlRutaArchivo() {
        return sdlRutaArchivo;
    }

    public void setSdlRutaArchivo(String sdlRutaArchivo) {
        this.sdlRutaArchivo = sdlRutaArchivo;
    }

    public Integer getSdlCantidad() {
        return sdlCantidad;
    }

    public void setSdlCantidad(Integer sdlCantidad) {
        this.sdlCantidad = sdlCantidad;
    }

    public Integer getSdlNuevos() {
        return sdlNuevos;
    }

    public void setSdlNuevos(Integer sdlNuevos) {
        this.sdlNuevos = sdlNuevos;
    }

    public Integer getSdlDuplicados() {
        return sdlDuplicados;
    }

    public void setSdlDuplicados(Integer sdlDuplicados) {
        this.sdlDuplicados = sdlDuplicados;
    }

    public String getSdlUsuario() {
        return sdlUsuario;
    }

    public void setSdlUsuario(String sdlUsuario) {
        this.sdlUsuario = sdlUsuario;
    }

    public Tipoambiente getCodTipoambiente() {
        return codTipoambiente;
    }

    public void setCodTipoambiente(Tipoambiente codTipoambiente) {
        this.codTipoambiente = codTipoambiente;
    }
}
