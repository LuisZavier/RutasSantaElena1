package com.rutas.santaelena.rutas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import directions.DirectionsParser;
import directions.DisponibleNet;
import models.RestfulClient;

import static com.rutas.santaelena.rutas.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST = 500;
    PlaceAutocompleteFragment placeAutoComplete;
    Polyline lineRuta, polylineFinal;
    LatLng findPunto;
    LatLng findPunto2;
    ArrayList<LatLng> MarkerPoints;
    double lat = 0;
    double lng = 0;
    LatLng Markerdest = new LatLng(lat, lng);
    LatLng MiUbicacion = new LatLng(lat, lng);
    RestfulClient restfulClient = new RestfulClient();
    Circle circle;
    private GoogleMap mMap;
    private ArrayList<LatLng> lista = new ArrayList<>();
    private Marker marcador, markerDestino, markerFinBus, markerRutaBus;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        MarkerPoints = new ArrayList<>();//inicializamos el marcador que se utilizara para agregar al mapa
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
        polilinea();
        //wpt();
        /*LatLngBounds  limiteSantaElena = (new LatLngBounds(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)));*/

        /////////////  BUSQUEDA DEL DESTINO INGRESANDO POR TEXTO ///////////////////////
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);

        ImageView searchIcon = (ImageView)((LinearLayout)placeAutoComplete.getView()).getChildAt(0);

// Set the desired icon
      searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_home);
                drawer.openDrawer(GravityCompat.START);
               //Toast.makeText(getApplicationContext(), "Aqui deberia de Desplegar Menu JAJAJA:  ", Toast.LENGTH_SHORT).show();
            }
        });
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
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

                Markerdest = latlangObj;
                PuntoDestino(Markerdest);
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }

        });
        /* METODOD PARA AGREGAR EN MARKER EN EL MAPA PULSO EN PANTALLA */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng pointDest) {
                PuntoDestino(pointDest);
            }
        });
    }
    private void MarkerCercano(LatLng cercano) {
        if (markerFinBus != null) markerFinBus.remove();
        //POSICIONAMOS UN NUEVO MARKER EN EL PUNTO MAS CERCANO ENTRE EL DESTINO Y LA POLY
        markerFinBus = mMap.addMarker(new MarkerOptions()
                .position(cercano)
                .draggable(true)
                .title("Llegue hasta Aqui :)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.llegadabus))); //llegada
        MarkerPoints.add(cercano);
    }
    private void IrRutabus(LatLng cercano) {

        if (markerRutaBus != null) markerRutaBus.remove();
        //Posicionamos un nuevo marker entre la ubicacion del usuario y la ruta del bus
        //le muestra el punto mas cernaco a tomar el bus
        markerRutaBus = mMap.addMarker(new MarkerOptions()
                .position(cercano)
                .draggable(true)
                .title("Aqui coga el bus :)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.paradadeautobus))); //llegada
    }
    /***METODO PARA POSICIONAR EL PUNTO EN EL MAPA Y BORRADO DE MARCADORES Y +
     *  METODO USADO PARA BUSQUEDAS POR TEXTO Y PULSO*/
    private void PuntoDestino(LatLng pointDest) {

        MarkerPoints.add(MiUbicacion);
        int radioCiruloTolerancia = 100;
        //limpiamos el mapa y generamos un nuevo marcador si pulsamos o buscamos 2 veces en el map
        if (MarkerPoints.size() > 2) {

            markerDestino.setVisible(false);
            markerDestino.remove();
            MarkerPoints.remove(0);
            MarkerPoints.remove(1);

            MarkerPoints = new ArrayList<>();
            MarkerPoints.add(MiUbicacion); //mi ubicacion siempre sera mi primer punto
        }

        // agregamos punto destino al array list
        MarkerPoints.add(pointDest);

        if (MarkerPoints.size() == 1) {
            //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        } else {
            if (MarkerPoints.size() >= 2) {
                CameraUpdate miDestino = CameraUpdateFactory.newLatLngZoom(pointDest, 15);
                markerDestino = mMap.addMarker(new MarkerOptions()
                        .position(pointDest)
                        .title("Su destino")
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.meta)));//punto destino

                mMap.animateCamera(miDestino);
                Markerdest = MarkerPoints.get(1);

                if (lineRuta != null) lineRuta.remove();
                if (polylineFinal !=null) polylineFinal.remove();

                //isLocationOnEdge: DETERMINA SI UN PUNTO SE ENCUENTRA EN UNA POLILINEA CERCA DENTRO O FUERA DE ELLA
                // CON UN RADIO DE TOLERANCIA
                boolean encuentraBordePoli = PolyUtil.isLocationOnEdge(pointDest, lista, false, radioCiruloTolerancia);

                while (encuentraBordePoli != true) {
                    cleanCircle();
                    while (radioCiruloTolerancia < 1000) { //Radio Iterativo

                        radioCiruloTolerancia = radioCiruloTolerancia + 100;

                        Circles(radioCiruloTolerancia);

                        cleanCircle();
                        encuentraBordePoli = PolyUtil.isLocationOnEdge(pointDest, lista, false, radioCiruloTolerancia);
                        if (encuentraBordePoli == true)
                            break;
                    }
                    break;
                }

                if (encuentraBordePoli == true) {

                    lineRuta = mMap.addPolyline(new PolylineOptions().addAll(lista).width(5).color(Color.BLUE).visible(true));

                    EncPuntoCerPoli encPuntoCerPoli = new EncPuntoCerPoli();

                    findPunto2 = encPuntoCerPoli.findNearestPoint(MiUbicacion, lista);
                    findPunto = encPuntoCerPoli.findNearestPoint(Markerdest, lista);

                    DisponibleNet disponibleNet = new DisponibleNet();
                    boolean conecta2 = disponibleNet.compruebaConexion(this);

                    if (conecta2 == true) {
                        getRequest(MiUbicacion,findPunto2);
                        MarkerCercano(findPunto);//colocamos un marker en el punto mas cercano (Destino - Ruta)
                        //Solo si hay connexion a internet mostrara el recorrido entre los puntos
                       getRequest(findPunto,Markerdest);
                        IrRutabus(findPunto2);//colocamos un marcador en el punto mas cercano a la ruta del bus
                    }else //caso contrario solo ubicara los marcadores
                        IrRutabus(findPunto2);
                        MarkerCercano(findPunto);{
                    }
                } else {
                    if (encuentraBordePoli == false) {
                        mMap.clear();
                        Toast.makeText(getApplicationContext(), "No se Encontro ninguna ruta en un radio de:  " + radioCiruloTolerancia, Toast.LENGTH_SHORT).show();
                    }

                }
                cleanCircle();
            }
        }
        Circles(radioCiruloTolerancia);
    }
    private void getRequest(LatLng p1, LatLng p2){
        String url = getRequestUrl(p1, p2);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);
    }
   private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=walking";
        //Build the full param
        String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {

            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }
                polylineFinal= mMap.addPolyline(new PolylineOptions().addAll(points).width(5).color(Color.BLACK));
            }
            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

   private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        if (marcador != null) marcador.remove();
        //marcador = mMap.addMarker(new MarkerOptions()
        //      .position(coordenadas)
        //    .title("Ud esta Aqui"));
        //    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ubicacion)));//ubicacion actual
        //mMap.animateCamera(miUbicacion);
        MiUbicacion = coordenadas;
        // mMap.setOnMarkerClickListener(this);
    }
    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);
        }
    }
    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {actualizarUbicacion(location);}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };
    private void miUbicacion() {
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Location", "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;

        } else {
            Log.i("Location", "Permisos necesarios OK!.");
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            actualizarUbicacion(location);
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 0, locListener);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
    }
    private void Circles(int rad) {
        circle = mMap.addCircle(new CircleOptions()
                .center(Markerdest)
                .radius(rad)//metros
                .strokeWidth(5)
                .strokeColor(Color.GRAY)

        );
    }

    private void cleanCircle() {
        if (circle != null) {
            circle.remove();
        }
    }
    public void polilinea() {
        Resources res = getResources();
        TextView textInfo = (TextView) findViewById(R.id.info);
        String info = "";

        List<GpxNode> gpxList = decodeGPX(res.openRawResource(R.raw.transcisa7));
        for (int i = 0; i < gpxList.size(); i++) {
            info = gpxList.get(i).getLocationString();
            String[] latlong = info.split(":");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng punto = new LatLng(latitude, longitude);
            lista.add(punto);
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(latitude, longitude), 16));
        }
    }

    private List<GpxNode> decodeGPX(InputStream is) {
        List<GpxNode> list = new ArrayList<GpxNode>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //FileInputStream fileInputStream = new FileInputStream();
            Document document = documentBuilder.parse(is);
            Element elementRoot = document.getDocumentElement();

            NodeList nodelist_trkpt = elementRoot.getElementsByTagName("trkpt");

            for (int i = 0; i < nodelist_trkpt.getLength(); i++) {

                Node node = nodelist_trkpt.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String newLatitude = attributes.getNamedItem("lat").getTextContent();
                Double newLatitude_double = Double.parseDouble(newLatitude);

                String newLongitude = attributes.getNamedItem("lon").getTextContent();
                Double newLongitude_double = Double.parseDouble(newLongitude);

                String newLocationName = newLatitude + ":" + newLongitude;
                Location newLocation = new Location(newLocationName);
                newLocation.setLatitude(newLatitude_double);
                newLocation.setLongitude(newLongitude_double);

                GpxNode newGpxNode = new GpxNode(newLocation);
                list.add(newGpxNode);

            }

            is.close();

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    class GpxNode {

        Location location;
        String ele;
        String time;

        GpxNode(Location newLocation, String newEle, String newtime, String newName) {
            location = null;
            ele = "";
            time = "";
        }

        GpxNode(Location loc) {
            location = loc;
            ele = "";
            time = "";
        }

        GpxNode(Location loc, String e, String t) {
            location = loc;
            ele = e;
            time = t;
        }

        void setEle(String e) {
            ele = e;
        }

        void setTime(String t) {
            time = t;
        }

        Location getLocation() {
            return location;
        }

        String getLocationString() {
            return location.getLatitude() + ":" + location.getLongitude();
        }

    }

}