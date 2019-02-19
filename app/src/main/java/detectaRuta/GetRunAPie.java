package detectaRuta;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rutas.santaelena.app.rutas.R;

import java.util.List;

import interfaceClass.RetrofitMaps;
import recorridosDistancia.ListRoute;
import recorridosDistancia.Polydecode;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class GetRunAPie {
    Polyline poliRuDes;
    String recorridoOriRuta ="" ;
    String recorridoDestRuta ="";
    int c=0;

    public Marker markerDestino, markerRutaBus;
    Marcador marcador = new Marcador();
    public void getRecorridoAPie(String type, LatLng puntoa, LatLng puntob, GoogleMap mMap, Context context) {

        String url = context.getString(R.string.url_directions);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<ListRoute> call = service.getDistanceDuration("metric", puntoa.latitude + "," + puntoa.longitude, puntob.latitude + "," + puntob.longitude, type);
        call.enqueue(new Callback<ListRoute>() {
            @Override
            public void onResponse(Response<ListRoute> response, Retrofit retrofit) {
                try {
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();

                        if (c == 0) {
                            recorridoOriRuta = ("Distancia:" + distance + ", Duracion:" + time);
                            origenRuta(mMap,puntob,context);
                        } else {
                            recorridoDestRuta = ("Distancia:" + distance + ", Duracion:" + time);
                            destino(mMap,puntob,context,recorridoDestRuta);
                        }


                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        Polydecode polydecode = new Polydecode();
                        List<LatLng> list = polydecode.decodePoly(encodedString);
                        poliRuDes = mMap.addPolyline(new PolylineOptions().addAll(list).width(5).color(Color.BLACK).geodesic(true));
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    public void origenRuta(GoogleMap mMap,LatLng findPtoOrigenRuta,Context context ) {
        markerRutaBus = marcador.colocarMarcadorRutaBusMasCercanaOrigenDestino(findPtoOrigenRuta, recorridoOriRuta, mMap,context);
        c=1;
    }

    public void destino(GoogleMap mMap,LatLng miDestino,Context context, String title) {
        if (markerDestino != null) markerDestino.remove();
        markerDestino = marcador.colocarMarcadorRutaBusMasCercanaOrigenDestino(miDestino, title, mMap,context);
        markerDestino.showInfoWindow();
        c=0;
    }


}
