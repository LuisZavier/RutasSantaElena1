package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Punto implements Serializable{

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("epsilon")
    private double epsilon;
    @JsonProperty("latitud")
    private double latitud;
    @JsonProperty("longitud")
    private double longitud;

    public Punto() {
    }

    public Punto(String id, String type, double epsilon, double latitud, double longitud) {
        this.id = id;
        this.type = type;
        this.epsilon = epsilon;
        this.latitud = latitud;
        this.longitud = longitud;
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

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
