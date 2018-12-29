package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.TimeZone;

public class EstadoBus {

	@JsonProperty("creationDate")
	private Calendar creationDate;
	@JsonProperty("velocidad")
	private int velocidad;
	@JsonProperty("cantidadUsuarios")
	private int cantidadUsuarios;
	@JsonProperty("posicionActual")
	private Point posicionActual;
	@JsonProperty("estadoPuerta")
	private Boolean estadoPuerta;
	@JsonProperty("linea")
	private int linea;

	public EstadoBus(Calendar creationDate, int velocidad, int cantidadUsuarios, Point posicionActual, Boolean estadoPuerta, int linea) {
		this.creationDate = creationDate;
		this.velocidad = velocidad;
		this.cantidadUsuarios = cantidadUsuarios;
		this.posicionActual = posicionActual;
		this.estadoPuerta = estadoPuerta;
		this.linea = linea;
	}

	public EstadoBus() {
		super();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("ECT"));
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
		now.set(Calendar.HOUR_OF_DAY,0);
		this.creationDate = now;
	}


	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public int getCantidadUsuarios() {
		return cantidadUsuarios;
	}

	public void setCantidadUsuarios(int cantidadUsuarios) {
		this.cantidadUsuarios = cantidadUsuarios;
	}

	public Point getPosicionActual() {
		return posicionActual;
	}

	public void setPosicionActual(Point posicionActual) {
		this.posicionActual = posicionActual;
	}

	public Boolean getEstadoPuerta() {
		return estadoPuerta;
	}

	public void setEstadoPuerta(Boolean estadoPuerta) {
		this.estadoPuerta = estadoPuerta;
	}

	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	@Override
	public String toString() {
		return "EstadoBus{" +
				"creationDate=" + creationDate +
				", velocidad=" + velocidad +
				", cantidadUsuarios=" + cantidadUsuarios +
				", posicionActual=" + posicionActual +
				", estadoPuerta=" + estadoPuerta +
				", linea=" + linea +
				'}';
	}
}
