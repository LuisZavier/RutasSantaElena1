package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Parada {


	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
	private String type;
	@JsonProperty("nombre")
	private String nombre;
	@JsonProperty("urlFoto")
	private String urlFoto;
	@JsonProperty("coordenada")
    private Punto coordenada;

	public Parada(String nombre, String urlFoto, Punto coordenada) {
		super();
		this.type = "parada";
		this.nombre = nombre;
		this.urlFoto = urlFoto;
		this.coordenada = coordenada;
	}
	public Parada() {
		super();
		this.type = "parada";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrlFoto() {
		return urlFoto;
	}
	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
	public Punto getCoordenada() {
		return coordenada;
	}
	public void setCoordenada(Punto coordenada) {
		this.coordenada = coordenada;
	}
	@Override
	public String toString() {
		return "Parada [id=" + id + ", type=" + type + ", nombre=" + nombre + ", urlFoto=" + urlFoto + ", coordenada="
				+ coordenada + "]";
	}
}

