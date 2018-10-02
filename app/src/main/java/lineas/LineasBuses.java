package lineas;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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

import entities.Punto;
import entities.RutaModel;
import models.HttpLineas;

public class LineasBuses extends FragmentActivity implements OnMapReadyCallback {
    //CLASE QUE MUESTRA EN EL MAPA LA RUTA Y SUS PARADAS DEL BUS SELECCIONADO  EN EL MENU
    String lineaBus = null;
    private GoogleMap mMap;
    List<Punto> listPuntos;
    List<String> listParadas;
    private ArrayList<LatLng> lista = new ArrayList<>();
    Marker markerEstacionamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lineaBus = extras.getString("linea");
        }
        System.out.println("LINEA" +  lineaBus);

    /*   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
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
        LatLng SantaElena = new LatLng(-2.223136, -80.906428);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SantaElena,14));
    }

    public void consultaLinea(){

        AsyncTask<String, Void, RutaModel> httpLineas = new HttpLineas(new HttpLineas.AsyncResponse2() {
            @Override
            public void processFinish(RutaModel rutaModel) {
                listPuntos = rutaModel.getListasPuntos();
                polilineaRestful(listPuntos);
                // listParadas = rutaModel.getListasParadas();
                //paraderos(listParadas);
            }
        }).execute(lineaBus);

    }

    public void polilineaRestful(List<Punto> listasPuntos) {

        for (int i = 0; i < listasPuntos.size(); i++) {

            double latitude =  listasPuntos.get(i).getLatitud();
            double longitude = listasPuntos.get(i).getLongitud();

            LatLng punto = new LatLng(latitude, longitude);
            lista.add(punto);
        }
       mMap.addPolyline(new PolylineOptions().addAll(lista).width(5).color(Color.BLACK));
    }

   /* public void paraderos(List<String> listParadas) {

        for (int i = 0; i < listParadas.size(); i++) {

            double latitude =  listParadas.get(i).getLatitud();
            double longitude = listParadas.get(i).getLongitud();

            LatLng puntoWpt = new LatLng(latitude, longitude);
            wpt(puntoWpt);
        }
    } */
    private void wpt(LatLng estacionamiento){
        //   if (markerEstacionamiento != null) markerEstacionamiento.remove();
        markerEstacionamiento = mMap.addMarker(new MarkerOptions()
                .position(estacionamiento)
                .title("Paradero")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }
/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent= new Intent(LineasBuses.this,MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contactos) {
            Intent intent= new Intent(LineasBuses.this, Notificaciones.class);
            startActivity(intent);

        } else if (id == R.id.nav_7) {
            String lineaBus = "7";
            Intent intent= new Intent(LineasBuses.this, LineasBuses.class);
            intent.putExtra("linea",lineaBus);
            startActivity(intent);


        } else if (id == R.id.nav_8) {
            String lineaBus = "8";
            Intent intent= new Intent(LineasBuses.this, LineasBuses.class);
            intent.putExtra("linea",lineaBus);
            startActivity(intent);

        } else if (id == R.id.nav_11) {
            Toast.makeText(getApplicationContext(), "linea 11:  ", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
