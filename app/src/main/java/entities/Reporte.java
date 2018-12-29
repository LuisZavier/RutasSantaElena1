package entities;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Javier on 29/05/2018.
 */

public class Reporte {

    @JsonProperty("id")
    private String id;
    @JsonProperty("asunto")
    private String asunto;
    @JsonProperty("mensaje")
    private String mensaje;
    @JsonProperty("usuario")
    private String usuario;
    @JsonProperty("movil")
    private String movil;
    @JsonProperty("numeroDisco")
    private String numeroDisco;
    @JsonProperty("ubicacion")
    private Point ubicacion; //(String)
    @JsonProperty("fecha")
    private Date fecha;
    @JsonProperty("linea")
    private String linea;
    @JsonProperty("estado")
    private boolean estado;

    public Reporte() {
    }

    public Reporte(String id, String usuario, String movil, String asunto, String numeroDisco, Point ubicacion,
                   Date fecha, String linea, String mensaje,boolean estado ) {
        super();
        this.id = id;
        this.usuario = usuario;
        this.movil = movil;
        this.asunto = asunto;
        this.numeroDisco = numeroDisco;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.linea = linea;
        this.mensaje = mensaje;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getNumeroDisco() {
        return numeroDisco;
    }

    public void setNumeroDisco(String numeroDisco) {
        this.numeroDisco = numeroDisco;
    }

    public Point getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Point ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

}
