package entities;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SegUsuario {
	@JsonProperty("id")
	private String id;

	@JsonProperty("idSegPerfil")
	private String idSegPerfil;

	@JsonProperty("nombre")
	private String nombre;

	@JsonProperty("clave")
	private String clave;

	@JsonProperty("estado")
	private Boolean estado;


	public SegUsuario() {

	}

	public SegUsuario(String id, String idSegPerfil, String nombre, String clave, Boolean estado) {
		super();
		this.id = id;
		this.idSegPerfil = idSegPerfil;
		this.nombre = nombre;
		this.clave = clave;
		this.estado = estado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdSegPerfil() {
		return idSegPerfil;
	}

	public void setIdSegPerfil(String idSegPerfil) {
		this.idSegPerfil = idSegPerfil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "SegUsuario{" +
				"id='" + id + '\'' +
				", idSegPerfil='" + idSegPerfil + '\'' +
				", nombre='" + nombre + '\'' +
				", clave='" + clave + '\'' +
				", estado=" + estado +
				'}';
	}
}
