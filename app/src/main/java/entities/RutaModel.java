package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Javier on 12/03/2018.
 */

public class RutaModel {

    @JsonProperty("id")
    private String id;
    @JsonProperty("numRuta")
    private String numRuta;
    @JsonProperty("nombreCooperativa")
    private String nombreCooperativa;
    @JsonProperty("listasPuntos")
    private List<Punto> listasPuntos;

    @JsonProperty("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumRuta() {
        return numRuta;
    }

    public void setNumRuta(String numRuta) {
        this.numRuta = numRuta;
    }

    public String getNombreCooperativa() {
        return nombreCooperativa;
    }

    public void setNombreCooperativa(String nombreCooperativa) {
        this.nombreCooperativa = nombreCooperativa;
    }


    public List<Punto> getListasPuntos() {
        return listasPuntos;
    }

    public void setListasPuntos(List<Punto> listasPuntos) {
        this.listasPuntos = listasPuntos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RutaModel() {
            this.type = "RutaModel";
    }

    public RutaModel(String id, String numRuta, String nombreCooperativa, List<Punto> listasPuntos,  String type) {
        this.id = id;
        this.numRuta = numRuta;
        this.nombreCooperativa = nombreCooperativa;
        this.listasPuntos = listasPuntos;

        this.type = type;
    }

    @Override
    public String toString() {
        return "RutaModel{" +
                "id='" + id + '\'' +
                ", numRuta='" + numRuta + '\'' +
                ", nombreCooperativa='" + nombreCooperativa + '\'' +
                ", listasPuntos=" + listasPuntos +
                ", type='" + type + '\'' +
                '}';
    }
}

