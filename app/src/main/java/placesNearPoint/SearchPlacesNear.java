package placesNearPoint;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.rutas.santaelena.rutas.R;

import java.util.ArrayList;
import java.util.List;

import detectaRuta.EncuentraRuta;
import detectaRuta.SeleccionUbicacion;
import entities.Parada;
import lineas.AnimateBusPosicion;
import lineas.LineAllWayPcercanos;
import models.HttpGetParadasCercanas;

public class SearchPlacesNear extends FragmentActivity{

    LineAllWayPcercanos lineAllWayPcercanos = new LineAllWayPcercanos();
    SeleccionUbicacion seleccionUbicacion = new SeleccionUbicacion();
    EncuentraRuta encuentraRuta = new EncuentraRuta();
    public void onClick(View v , GoogleMap mMap , ArrayList<LatLng> opcionOrigenDestino,String linea,Context context) {

        switch (v.getId()) {

            case R.id.B_bus:

                AnimateBusPosicion animateBusPosicion = new AnimateBusPosicion();
                animateBusPosicion.estadobus(mMap);

                break;

            case R.id.B_paraderos:

                seleccionUbicacion.SeleccionaOpcionUbiDest(context, new SeleccionUbicacion.OnOkOrigenDestino() {
                    @Override
                    public void seleccionadaUbicacion(int seleccionUbicacion) {

                        getParadasCercanasAPuntoWS(linea,mMap,opcionOrigenDestino.get(seleccionUbicacion),context);
                    }
                });
                break;

            case R.id.B_hospital:

                seleccionUbicacion.SeleccionaOpcionUbiDest(context, new SeleccionUbicacion.OnOkOrigenDestino() {
                    @Override
                    public void seleccionadaUbicacion(int seleccionUbicacion) {

                        DataPlaces(mMap,opcionOrigenDestino.get(seleccionUbicacion),"hospital");
                    }
                });

                break;

            case R.id.B_restaurant:

                seleccionUbicacion.SeleccionaOpcionUbiDest(context, new SeleccionUbicacion.OnOkOrigenDestino() {
                    @Override
                    public void seleccionadaUbicacion(int seleccionUbicacion) {

                        DataPlaces(mMap,opcionOrigenDestino.get(seleccionUbicacion),"restaurant");
                    }
                });

                break;

            case R.id.B_school:

                seleccionUbicacion.SeleccionaOpcionUbiDest(context, new SeleccionUbicacion.OnOkOrigenDestino() {
                    @Override
                    public void seleccionadaUbicacion(int seleccionUbicacion) {

                        DataPlaces(mMap,opcionOrigenDestino.get(seleccionUbicacion),"school");
                    }
                });

                break;

        }
    }

    public void DataPlaces(GoogleMap mMap,LatLng opcionOrigenDestino, String text){
        Object dataTransfer[] = new Object[2];

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        dataTransfer = new Object[2];
        String url = getNearbyPlacesData.getUrl(opcionOrigenDestino.latitude, opcionOrigenDestino.longitude, text);
        getNearbyPlacesData = new GetNearbyPlacesData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
    }

    private void getParadasCercanasAPuntoWS(String linea , GoogleMap mMap, LatLng point, Context context) {
        AsyncTask<Object, Void, List<Parada>> httpGetParadasCerca = new HttpGetParadasCercanas(new HttpGetParadasCercanas.AsynParadas() {
            @Override
            public void paradas(List<Parada> paradas) {

                lineAllWayPcercanos.paraderosWpt(paradas, mMap,context);
                encuentraRuta.dibujaRadio(1000,point,mMap);
            }

        }).execute(linea,1, point);
    }

}
