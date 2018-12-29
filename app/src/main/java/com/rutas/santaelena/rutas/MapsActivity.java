package com.rutas.santaelena.rutas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.HorizontalScrollView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import detectaRuta.EncuentraRuta;
import detectaRuta.Marcador;
import entities.Ruta;
import interfaceClass.RetrofitMaps;
import lineas.LineAllWayPcercanos;
import lineas.LineaBus;
import models.HttpGetRutas;
import netDisponible.DisponibleNet;
import notificaciones.Notificaciones;
import placesNearPoint.SearchPlacesNear;
import recorridosDistancia.ListRoute;
import recorridosDistancia.Polydecode;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.rutas.santaelena.rutas.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private static final int LOCATION_REQUEST = 500;
    public Marker markerOrigen, markerDestino, markerFinBus, markerRutaBus;
    PlaceAutocompleteFragment placeAutoCompleteOrigenDestino;
    Polyline poliRuDes;
    boolean rutaDisponible = false, origenRutaEncontrado = false,conectaNet = false;
    LatLng findPtoDesRuta, findPtoOrigenRuta;
    ArrayList<LatLng> markerPoints;
    double lat = 0;
    double lng = 0;
    int c = 0,lineaEscogida = 0;
    LatLng miDestino = new LatLng(lat, lng);
    LatLng miUbicacion = new LatLng(lat, lng);
    LineAllWayPcercanos lineAllWayPcercanos = new LineAllWayPcercanos();
    String recorridoDestRuta = "", recorridoOriRuta = "",rutaNohay="";
    EncuentraRuta encuentraRuta = new EncuentraRuta();
    boolean[] detectarRutaCercanaPuntoDestino,detectarRutaCercanaPuntoOrigen;
    Marcador colocarMarker = new Marcador();
    DisponibleNet disponibleNet = new DisponibleNet();
    private GoogleMap mMap;
    private List<ArrayList<LatLng>> listaRutas = new ArrayList<>();
    private List<String> lineabus = new ArrayList<>();
    private List<String> lineaBusDisponible = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        markerPoints = new ArrayList<>();//inicializamos el marcador que se utilizara para agregar al mapa

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contactos) {
            Intent intent = new Intent(MapsActivity.this, Notificaciones.class);
            startActivity(intent);
        } else if (id == R.id.nav_7) {
            String lineaBus = "7";
            Intent intent = new Intent(MapsActivity.this, LineaBus.class);
            intent.putExtra("linea", lineaBus);
            startActivity(intent);
        } else if (id == R.id.nav_8) {
            String lineaBus = "8";
            Intent intent = new Intent(MapsActivity.this, LineaBus.class);
            intent.putExtra("linea", lineaBus);
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
       /* LatLngBounds limiteSantaElena = new LatLngBounds(
                new LatLng(-2.190094, -81.011600), new LatLng(-1.672730, -80.254169));
        mMap.setLatLngBoundsForCameraTarget(limiteSantaElena); */
        /**/
        LatLng SantaElena = new LatLng(-2.2228707, -80.9421662);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SantaElena, 12));
        currentLocation();
        getTodasLineasBusesWS();

        placeAutoCompleteOrigenDestino = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_destino);
        EditText etPlace = (EditText) placeAutoCompleteOrigenDestino.getView().findViewById(R.id.place_autocomplete_search_input);
        etPlace.setHint("Su sitio preferido aqui");

        placeAutoCompleteOrigenDestino.setBoundsBias(new LatLngBounds(
                new LatLng(-2.291430, -81.008619), new LatLng(-2.162431, -80.851164)));

        ImageView searchIcon = (ImageView) ((LinearLayout) placeAutoCompleteOrigenDestino.getView()).getChildAt(0);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START); //Despliega el menu
            }
        });
        /////////////  BUSQUEDA DEL DESTINO INGRESANDO POR TEXTO ///////////////////////
        placeAutoCompleteOrigenDestino.setOnPlaceSelectedListener(new PlaceSelectionListener() {
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

                if (markerPoints.size() == 0) {
                    miUbicacion = latlangObj;
                    colocarEnMapaPuntoOrigenDestino(miUbicacion);
                } else {
                    miDestino = latlangObj;
                    colocarEnMapaPuntoOrigenDestino(miDestino);
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

    public void getTodasLineasBusesWS() {
        AsyncTask<Void, Void, List<Ruta>> httpRuta = new HttpGetRutas(new HttpGetRutas.AsyncResponse() {
            @Override
            public void processFinish(List<Ruta> rutaModel) {
                listaRutas = lineAllWayPcercanos.getRecorridoLineas(rutaModel);
                for (int i = 0; i < rutaModel.size(); i++) {
                    Ruta r = rutaModel.get(i);
                    lineabus.add(i, r.getLinea());
                }
            }
        }).execute();
    }
    //FIXME Refactorizar esto porque es el mismo codigo para colocarMarcadorPuntoOrigenEnMapa y destino...
    private void origen() {
        if (markerOrigen != null) markerOrigen.remove();
        markerOrigen = colocarMarker.colocarMarcadorPuntoOrigenEnMapa(miUbicacion, "Origen", mMap,this);
        markerOrigen.showInfoWindow();
    }

    private void destino() {
        if (markerDestino != null) markerDestino.remove();
        markerDestino = colocarMarker.colocarMarcadorPuntoDestinoEnMapa(miDestino, recorridoDestRuta, mMap,this);
        markerDestino.showInfoWindow();
    }

    private void origenRuta() {
        markerRutaBus = colocarMarker.colocarMarcadorRutaBusMasCercanaOrigen(findPtoOrigenRuta, recorridoOriRuta, mMap,this);
    }

    private void destinoRuta() {
        markerFinBus = colocarMarker.colocarMarcadorRutaBusMasCercanaDetino(findPtoDesRuta, "Llegue hasta aqui", mMap,this);
    }

    /***METODO PARA POSICIONAR EL PUNTO EN EL MAPA Y BORRADO DE MARCADORES  + METODO USADO PARA BUSQUEDAS POR TEXTO Y PULSO*/
    private void colocarEnMapaPuntoOrigenDestino(LatLng pointOriDest) {

        int radioCiruloTolerancia = 100;
        //limpiamos el mapa y generamos un nuevo marcador si pulsamos o buscamos 3 veces en el map
        if (markerPoints.size() > 1) {
            c = 0;
            mMap.clear();
            markerPoints = new ArrayList<>();
            rutaDisponible = false;
            origenRutaEncontrado = false;
            lineaBusDisponible = new ArrayList<>();
            HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.H_scroll);
            horizontalScrollView.setVisibility(View.INVISIBLE);
        }
        // agregamos punto destino al array list
        markerPoints.add(pointOriDest);
        if (markerPoints.size() == 1) {

            miUbicacion = markerPoints.get(0);
            detectarRutaCercanaPuntoOrigen = new boolean[listaRutas.size()];

            for (int i = 0; i < listaRutas.size(); i++)
                detectarRutaCercanaPuntoOrigen[i] = encuentraRuta.encuentraRutaBus(miUbicacion, listaRutas.get(i), false, radioCiruloTolerancia, mMap);

            for (int i = 0; i < detectarRutaCercanaPuntoOrigen.length; i++)
                if (detectarRutaCercanaPuntoOrigen[i] == true)
                    origenRutaEncontrado = true;

            if (origenRutaEncontrado == true)
                 origen();
                         else
                             noDetectaRuta(miUbicacion);
        }

        if (markerPoints.size() == 2) {
            miDestino = markerPoints.get(1);
            //TODO Refactorizar este metodo para mejorar el codigo. Hacer un metodo aparte que se llame algo como encontrar rutas cercanas punto.
            detectarRutaCercanaPuntoDestino = new boolean[listaRutas.size()];

            for (int i = 0; i < listaRutas.size(); i++)
                detectarRutaCercanaPuntoDestino[i] = encuentraRuta.encuentraRutaBus(miDestino, listaRutas.get(i), false, radioCiruloTolerancia, mMap);

            for (int i = 0; i < detectarRutaCercanaPuntoDestino.length; i++) {
                if (detectarRutaCercanaPuntoDestino[i] == true && detectarRutaCercanaPuntoOrigen[i] == true) {
                    rutaDisponible = true;
                    lineaBusDisponible.add(lineabus.get(i));
                }
            }
            if (rutaDisponible == false)
                noDetectaRuta(miDestino);
                    else {
                        encuentraRuta.ShowAlertDialogBusesDisponibles(lineaBusDisponible, lineabus, this, new EncuentraRuta.OnOk() {
                            @Override
                            public void seleccionada(int seleccion) {
                                mostrarRutaSeleccionada(seleccion);
                            }});
            }
        }
    }

    private void mostrarRutaSeleccionada(int seleccion) {
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.H_scroll);
        horizontalScrollView.setVisibility(View.VISIBLE);

        EncPuntoCerPoli encPuntoCerPoli = new EncPuntoCerPoli();

        findPtoOrigenRuta = encPuntoCerPoli.findNearestPoint(miUbicacion, listaRutas.get(seleccion));//Encontramos el punto mas cercano Mi Ubi - Poli
        findPtoDesRuta = encPuntoCerPoli.findNearestPoint(miDestino, listaRutas.get(seleccion));//Encontramos el punto mas cercano Dest-Poli

        conectaNet = disponibleNet.compruebaConexion(this);
        if (conectaNet == true) {//Solo si hay connexion a internet mostrara el recorrido entre los puntos
            getRecorridoAPie("walking", miUbicacion, findPtoOrigenRuta);
            getRecorridoAPie("walking", findPtoDesRuta, miDestino);
            destinoRuta();
        } else { //caso contrario solo ubicara los marcadores si no hay conexion a net sea wifi o datos
            recorridoDestRuta = "";
            recorridoOriRuta = "";
            origenRuta();
            destinoRuta();
            destino();
        }
        encuentraRuta.dibujaRutaSeleccionada(Collections.singletonList(listaRutas.get(seleccion)), mMap);  //MOSTRAMOS LA RUTA SELECCIONADA
        lineaEscogida = seleccion;
    }

  private void getRecorridoAPie(String type, LatLng puntoa, LatLng puntob) {
        String url = getString(R.string.url_directions);
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
                            origenRuta();
                            c++;
                        } else {
                            recorridoDestRuta = ("Distancia:" + distance + ", Duracion:" + time);
                            destino();
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

    private void currentLocation() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mMap.setMyLocationEnabled(true);
                break;
        }
    }

    private void noDetectaRuta(LatLng eliminarPunto) {
        recorridoDestRuta = "No hay ruta wey, busca mas allasito";
        Toast.makeText(getApplicationContext(), "No se Encontro ninguna ruta Seleccione otro punto ", Toast.LENGTH_SHORT).show();
        if (markerPoints.size() == 1)
            origen();
             else
                destino();
                     markerPoints.remove(eliminarPunto);
    }

    public void onClick(View v){
        conectaNet = disponibleNet.compruebaConexion(this);

        SearchPlacesNear searchPlacesNear = new SearchPlacesNear();

        if (conectaNet == true)
            searchPlacesNear.onClick(v, mMap, markerPoints,lineabus.get(lineaEscogida),this);
        else
            Toast.makeText(getApplicationContext(), "Se requiere conexion a Internet ", Toast.LENGTH_SHORT).show();

    }
}