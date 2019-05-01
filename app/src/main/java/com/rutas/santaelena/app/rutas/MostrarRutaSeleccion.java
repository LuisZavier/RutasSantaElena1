package com.rutas.santaelena.app.rutas;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import detectaRuta.EncuentraRuta;
import detectaRuta.GetRunAPie;
import detectaRuta.Marcador;
import netDisponible.DisponibleNet;


public class MostrarRutaSeleccion {
    Marker markerDestino;
    private DisponibleNet disponibleNet = new DisponibleNet();
    private boolean conectaNet;
    private GetRunAPie getRunAPie = new GetRunAPie();
    private EncuentraRuta encuentraRuta = new EncuentraRuta();
    private Marcador colocarMarker = new Marcador();

    public void mostrarRutaSeleccionada(int seleccion, Context context, GoogleMap mMap, LatLng miUbicacion, LatLng findPtoOrigenRuta,
                                        LatLng findPtoDesRuta, LatLng miDestino, List<ArrayList<LatLng>> listaRutas) {

        conectaNet = disponibleNet.compruebaConexion(context);
        if (conectaNet) {//Solo si hay connexion a internet mostrara el recorrido entre los puntos
            destinoRuta(findPtoDesRuta, mMap, context);
            getRunAPie.getRecorridoAPie("walking", miUbicacion, findPtoOrigenRuta, mMap, context);
            getRunAPie.getRecorridoAPie("walking", findPtoDesRuta, miDestino, mMap, context);
        } else { //caso contrario solo ubicara los marcadores si no hay conexion a net sea wifi o datos
            getRunAPie.origenRuta(mMap, findPtoOrigenRuta, context);
            destinoRuta(findPtoDesRuta, mMap, context);
            getRunAPie.destino(mMap, miDestino, context, "");
        }
        encuentraRuta.dibujaRutaSeleccionada(Collections.singletonList(listaRutas.get(seleccion)), mMap);  //MOSTRAMOS LA RUTA SELECCIONADA

    }

public void destinoRuta(LatLng findPtoDesRuta, GoogleMap mMap, Context context){
    markerDestino = colocarMarker.colocarMarcadorRutaBusMasCercanaDetino(findPtoDesRuta, "Llegue hasta aqui", mMap, context);

}
}
