package com.rutas.santaelena.rutas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import detectaRuta.EncuentraRuta;
import detectaRuta.Marcador;
import directions.DirectionsParser;
import directions.DisponibleNet;
import directions.TaskRequestPuntos;
import entities.Parada;
import entities.Punto;
import entities.RutaModel;
import lineas.LineasBuses;
import models.HttpEnviaPoint;
import notificaciones.Notificaciones;

import static com.rutas.santaelena.rutas.R.id.map;
import static java.lang.Double.parseDouble;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    PlaceAutocompleteFragment placeAutoCompleteDestino;
    Polyline lineRuta,poliRuDes;
    LatLng findPtoDesPoli;
    LatLng findPtoUbiPoli;
    BitmapDescriptor icon;
    ArrayList<LatLng> MarkerPoints;
    double lat = 0;
    double lng = 0;
    int c = 0;
    LatLng MiDestino = new LatLng(lat, lng);
    LatLng MiUbicacion = new LatLng(lat, lng);
    private GoogleMap mMap;
    private ArrayList<LatLng> lista = new ArrayList<>();
    private List<ArrayList<LatLng>> listaRutas = new ArrayList<>();
    List<Punto> listPuntos ;
    private Marker markerOrigen, markerDestino, markerFinBus, markerRutaBus,markerEstacionamiento;
    String recorridoDestRuta = "";
    String recorridoOriRuta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        MarkerPoints = new ArrayList<>();//inicializamos el marcador que se utilizara para agregar al mapa

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
/*    AsyncTask<Void, Void, RutaModel> httpRuta2 = new HttpRuta2(new HttpRuta2.AsyncResponse2() {
        @Override
        public void processFinish(RutaModel rutaModel) {
            listPuntos = rutaModel.getListasPuntos();
            polilineaRestful(listPuntos);
        }
    }).execute(); */

   /* public void getTodasLineasBusesWS(){
        AsyncTask<Void, Void, List<RutaModel>> httpRuta = new HttpRuta(new HttpRuta.AsyncResponse() {
            @Override
            public void processFinish(List<RutaModel> rutaModel) {
                lineasTodas(rutaModel);
            }
        }).execute();
    } */
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
            Intent intent= new Intent(MapsActivity.this,MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contactos) {
            Intent intent= new Intent(MapsActivity.this, Notificaciones.class);
            startActivity(intent);

        } else if (id == R.id.nav_7) {
            String lineaBus = "7";
            Intent intent= new Intent(MapsActivity.this, LineasBuses.class);
            intent.putExtra("linea",lineaBus);
            startActivity(intent);


        } else if (id == R.id.nav_8) {
            String lineaBus = "8";
            Intent intent= new Intent(MapsActivity.this, LineasBuses.class);
            intent.putExtra("linea",lineaBus);
            startActivity(intent);

        } else if (id == R.id.nav_11) {
            Toast.makeText(getApplicationContext(), "linea 11:  ", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        /*lIMITA LA VISUALIZACION DEL MAPA SOLO EN LA PROVINCIA DE SANTA ELENA*/
        LatLngBounds limiteSantaElena = new LatLngBounds(
                new LatLng(-2.291430, -81.008619), new LatLng(-2.162431, -80.851164));
        mMap.setLatLngBoundsForCameraTarget(limiteSantaElena);
        /**/
        LatLng SantaElena = new LatLng(-2.2228707, -80.9421662);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SantaElena,12));
        //getTodasLineasBusesWS();
        currentLocation();
        placeAutoCompleteDestino  = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_destino);
        EditText etPlace = (EditText)placeAutoCompleteDestino.getView().findViewById(R.id.place_autocomplete_search_input);
        etPlace.setHint("Su sitio preferido aqui");

        placeAutoCompleteDestino.setBoundsBias(new LatLngBounds(
                new LatLng(-2.291430, -81.008619), new LatLng(-2.162431, -80.851164)));

        ImageView searchIcon = (ImageView)((LinearLayout)placeAutoCompleteDestino.getView()).getChildAt(0);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START); //Despliega el menu
            }
        });
        /////////////  BUSQUEDA DEL DESTINO INGRESANDO POR TEXTO ///////////////////////
        placeAutoCompleteDestino.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.e("Tag", "Place: "
                        + place.getAddress()
                        + place.getPhoneNumber()
                        + place.getLatLng().latitude
                        + place.getLatLng().longitude);

                LatLng latlangObj = place.getLatLng(); //recuepramos la LatLong de la busqueda
                Log.v("latitude:", "" + latlangObj.latitude);
                Log.v("longitude:", "" + latlangObj.longitude);

                if (MarkerPoints.size()== 0){
                    MiUbicacion = latlangObj;
                    colocarEnMapaPuntoOrigenDestino(MiUbicacion);
                }else{
                    MiDestino = latlangObj;
                    colocarEnMapaPuntoOrigenDestino(MiDestino);
                }
            }
            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });
        /* METODOD PARA AGREGAR EN MARKER EN EL MAPA PULSO EN PANTALLA */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng pointOri_Dest) {
                colocarEnMapaPuntoOrigenDestino(pointOri_Dest);
            }
        });
    }
    Marcador colocarMarker = new Marcador();

    //FIXME Refactorizar esto porque es el mismo codigo para origen y destino...

    //FIXME Refactorizar esto porque es el mismo codigo para colocarMarcadorPuntoOrigenEnMapa y destino...

   private void origen(){
       icon= BitmapDescriptorFactory.fromResource(R.drawable.ubicacion);
       markerOrigen = colocarMarker.colocarMarcadorPuntoOrigenEnMapa(MiUbicacion,"Origen",icon,mMap);
   }
   private void destino(){
       if (markerDestino != null) markerDestino.remove();
       icon = BitmapDescriptorFactory.fromResource( R.drawable.meta);
       markerDestino = colocarMarker.colocarMarcadorPuntoDestinoEnMapa(MiDestino, recorridoDestRuta,icon,mMap);
       markerDestino.showInfoWindow();
   }
   private void origenRuta(){
       icon = BitmapDescriptorFactory.fromResource(R.drawable.paradadeautobus);
       markerRutaBus = colocarMarker.colocarMarcadorRutaBusMasCercanaOrigen(findPtoUbiPoli, recorridoOriRuta,icon,mMap);
       MarkerPoints.add(findPtoUbiPoli);
   }
   private void destinoRuta(){
       icon = BitmapDescriptorFactory.fromResource(R.drawable.llegadabus);
       markerFinBus =colocarMarker.colocarMarcadorRutaBusMasCercanaDetino(findPtoDesPoli,"Llegue hasta aqui", icon,mMap);
       MarkerPoints.add(findPtoDesPoli);
   }
    private void ParaderosCercanos(LatLng estacionamiento){
        icon=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        markerEstacionamiento = colocarMarker.colocarMarcador(estacionamiento,"Paradero",icon,mMap);
    }
    /***METODO PARA POSICIONAR EL PUNTO EN EL MAPA Y BORRADO DE MARCADORES  + METODO USADO PARA BUSQUEDAS POR TEXTO Y PULSO*/
    private void colocarEnMapaPuntoOrigenDestino(LatLng pointOriDest) {
        int radioCiruloTolerancia = 100;
        //limpiamos el mapa y generamos un nuevo marcador si pulsamos o buscamos 2 veces en el map
        if (MarkerPoints.size() > 1) {
            c=0;
            mMap.clear();
            MarkerPoints = new ArrayList<>();
        }
        // agregamos punto destino al array list
        MarkerPoints.add(pointOriDest);
        if (MarkerPoints.size() == 1){
            MiUbicacion = MarkerPoints.get(0);
            origen();
        }

        if (MarkerPoints.size() == 2) {
            MiDestino = MarkerPoints.get(1);
            //TODO Refactorizar este metodo para mejorar el codigo. Hacer un metodo aparte que se llame algo como encontrar rutas cercanas punto.
            EncuentraRuta encuentraRuta = new EncuentraRuta();
            boolean encuentraBordePoli = encuentraRuta.encuentraRutaBus(MiDestino, lista, false, radioCiruloTolerancia,mMap);

            if (encuentraBordePoli == true) {
                //getParadasCercanasAPuntoWS();
                lineRuta = mMap.addPolyline(new PolylineOptions().addAll(lista).width(5).color(Color.BLUE).visible(true));

                EncPuntoCerPoli encPuntoCerPoli = new EncPuntoCerPoli();

                findPtoUbiPoli = encPuntoCerPoli.findNearestPoint(MiUbicacion, lista);//Encontramos el punto mas cercano Mi Ubi - Poli
                findPtoDesPoli = encPuntoCerPoli.findNearestPoint(MiDestino, lista);//Encontramos el punto mas cercano Dest-Poli

                DisponibleNet disponibleNet = new DisponibleNet();
                boolean conecta2 = disponibleNet.compruebaConexion(this);
                if (conecta2 == true) {//Solo si hay connexion a internet mostrara el recorrido entre los puntos
                    getRecorridoAPie(MiUbicacion, findPtoUbiPoli);
                    getRecorridoAPie(findPtoDesPoli, MiDestino);
                     destinoRuta();
                }else{ //caso contrario solo ubicara los marcadores si no hay conexion a net sea wifi o datos
                    recorridoDestRuta ="";
                    recorridoOriRuta ="";
                    origenRuta();
                    destinoRuta();
                    destino();
                }
            } else {
                if (encuentraBordePoli == false) {
                    c=0; recorridoDestRuta ="";
                    Toast.makeText(getApplicationContext(), "No se Encontro ninguna ruta Seleccione otro destino ", Toast.LENGTH_SHORT).show();
                    destino();
                    MarkerPoints.remove(1);
                }
            }
        }
    }
    private void getParadasCercanasAPuntoWS(){
        AsyncTask<LatLng, Void, List<Parada>> ubicacion = new HttpEnviaPoint(new HttpEnviaPoint.AsynParadas() {
            @Override
            public void paradas(List<Parada> paradas) {
                paraderosWpt(paradas);
                System.out.println("paradas cercanas "+ paradas);
            }
        }).execute(MiUbicacion);
    }

    private void getRecorridoAPie(LatLng p1, LatLng p2){
        DirectionsParser directionsParser = new DirectionsParser();
        String url = directionsParser.getRequestUrl(p1, p2);

        TaskRequestPuntos taskRequestPuntos = new TaskRequestPuntos(new TaskRequestPuntos.AsyncRespDirections() {
            @Override
            public void processFinish2(List puntos) {
                dibujaRecorridoAPie(puntos);
            }
        });
        taskRequestPuntos.execute(url);
    }

    private void dibujaRecorridoAPie(List<List<HashMap<String, String>>> lists) {
        ArrayList points = null;
        PolylineOptions polylineOptions = null;
        String distance = "";
        String duration = "";

        for(int i=0;i<lists.size();i++){
            points = new ArrayList();
            polylineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = lists.get(i);
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);
                if(j==0){	// Get distance from the list
                    distance = (String)point.get("distance"); continue;
                }else if(j==1){ // Get duration from the list
                    duration = (String)point.get("duration"); continue;
                }
                double lat = parseDouble(point.get("lat"));
                double lon = parseDouble(point.get("lon"));

                points.add(new LatLng(lat,lon));
            }
            poliRuDes= mMap.addPolyline(new PolylineOptions().addAll(points).width(5).color(Color.BLACK));
        }

        if (polylineOptions!=null) { mMap.addPolyline(polylineOptions);
        } else { Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();}

        if (c==0){
            recorridoOriRuta = ("Distancia:" + distance + ", Duracion:" + duration);
             origenRuta();
        }else {
            recorridoDestRuta = ("Distancia:" + distance + ", Duracion:" + duration);
            destino();
        }
        c++;
    }
    private void currentLocation() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Location", "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;
        }
        Log.i("Location", "Permisos necesarios OK!.");
        mMap.setMyLocationEnabled(true);
    }

    public  void lineasTodas(List<RutaModel> rutas){
        //FIXME Descubrir porque no funciona directamentecon lista de RutaModel (error de casteo)
       /* for (int i = 0; i < rutas.size(); i++) {
            RutaModel misRutas = rutas.get(i);
            List<Punto> misPuntos = misRutas.getListasPuntos();
           for (int j=0; j<misPuntos.size();j++){
               double latitude =  misPuntos.get(j).getLatitud();
               double longitude = misPuntos.get(j).getLongitud();

                LatLng punto = new LatLng(latitude, longitude);
                lista.add(punto);
           }
            listaRutas.add(i, new ArrayList<LatLng>(lista));
            lista.clear();

        } */

        JSONArray misRutas= new JSONArray(rutas);
        for(int i=0;i<misRutas.length();i++){
            JSONObject jsonobject= null;
            try {
                jsonobject = (JSONObject) misRutas.get(i);

                JSONArray lisLatLong = jsonobject.getJSONArray("listasPuntos");

                for(int j=0;j<lisLatLong.length();j++){
                    JSONObject jsonLatLong = (JSONObject) lisLatLong.get(j);

                    double latitud = jsonLatLong.optDouble("latitud");
                    double longitud = jsonLatLong.optDouble("longitud");
                    LatLng punto = new LatLng(latitud, longitud);
                    lista.add(punto);
                }
                listaRutas.add(i, new ArrayList<LatLng>(lista));
                lista.clear();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        dibujaRutas(listaRutas);
    }
    public void dibujaRutas(List<ArrayList<LatLng>> listaRutasTodas){
        for (int i = 0; i < listaRutasTodas.size(); i++)
            lineRuta =  mMap.addPolyline(new PolylineOptions().addAll(listaRutasTodas.get(i)).width(5).color(Color.GREEN));
    }
    public void polilineaRestful(List<Punto> listasPuntos) {

        for (int i = 0; i < listasPuntos.size(); i++) {

            double latitude =  listasPuntos.get(i).getLatitud();
            double longitude = listasPuntos.get(i).getLongitud();

            LatLng punto = new LatLng(latitude, longitude);
            lista.add(punto);
        }
    }
    public void paraderosWpt(List<Parada> listParadas ) {

        JSONArray myListsParaderos= new JSONArray(listParadas);

        for(int i=0;i<myListsParaderos.length();i++){

            JSONObject jsonobject= null;
            try {
                jsonobject = (JSONObject) myListsParaderos.get(i);
                JSONObject coord = jsonobject.getJSONObject("coordenada");
                String x = coord.optString("x");
                String y = coord.optString("y");

                double latitude = Double.parseDouble(x);
                double longitud = Double.parseDouble(y);

                LatLng punto = new LatLng(latitude,longitud);
                ParaderosCercanos(punto);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}