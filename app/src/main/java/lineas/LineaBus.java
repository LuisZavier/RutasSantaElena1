package lineas;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rutas.santaelena.rutas.R;

import java.util.ArrayList;
import java.util.List;

import entities.Point;
import entities.Ruta;
import models.HttpGetLinea;

public class LineaBus extends AppCompatActivity implements OnMapReadyCallback {
    //CLASE QUE MUESTRA EN EL MAPA LA RUTA Y SUS PARADAS DEL BUS SELECCIONADO  EN EL MENU
    String lineaBus = null;
    private GoogleMap mMap;
    List<Point> listPuntos;
    List<String> listParadas;
    private ArrayList<LatLng> lista = new ArrayList<>();
    Marker markerEstacionamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineas_buses);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lineaBus = extras.getString("linea");
        }
        System.out.println("LINEA bus seleccionada " +  lineaBus);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        /*lIMITA LA VISUALIZACION DEL MAPA SOLO EN LA PROVINCIA DE SANTA ELENA*/
        LatLngBounds limiteSantaElena = new LatLngBounds(
                new LatLng(-2.291430, -81.008619), new LatLng(-2.162431, -80.851164));
        mMap.setLatLngBoundsForCameraTarget(limiteSantaElena);
        consultaLinea();
        LatLng SantaElena = new LatLng(-2.220806, -80.914484);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SantaElena, 15));
    }

    public void consultaLinea(){

        AsyncTask<String, Void, Ruta> httpLineas = new HttpGetLinea(new HttpGetLinea.AsyncResponse2() {
            @Override
            public void processFinish(Ruta rutaModel) {
                listPuntos = rutaModel.getListasPuntos();
                System.out.println("untos " + listPuntos.toString());
                polilineaRestful(listPuntos);
                // listParadas = rutaModel.getListasParadas();
                //paraderos(listParadas);
            }
        }).execute(lineaBus);

    }

    public void polilineaRestful(List<Point> listasPuntos) {

        for (int i = 0; i < listasPuntos.size(); i++) {

            double latitude =  listasPuntos.get(i).getX();
            double longitude = listasPuntos.get(i).getY();

            LatLng punto = new LatLng(latitude, longitude);
            lista.add(punto);
        }
       mMap.addPolyline(new PolylineOptions().addAll(lista).width(5).color(Color.BLACK));
    }

    private void wpt(LatLng estacionamiento){
        //   if (markerEstacionamiento != null) markerEstacionamiento.remove();
        markerEstacionamiento = mMap.addMarker(new MarkerOptions()
                .position(estacionamiento)
                .title("Paradero")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

}
