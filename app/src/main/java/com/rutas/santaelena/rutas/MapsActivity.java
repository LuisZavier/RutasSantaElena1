package com.rutas.santaelena.rutas;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 1;
    PlaceAutocompleteFragment placeAutoComplete;
    private GoogleMap mMap;
    Polyline line,lineUbicActual,lineRuta;

    LatLng findPunto ;
    LatLng findPunto2;

    private ArrayList<LatLng> listaWpt = new ArrayList<>();
    private ArrayList<LatLng> lista = new ArrayList<>();

    private Marker marcador,marcador1,marcador2;
    ArrayList<LatLng> MarkerPoints;
    double lat = 0;
    double lng = 0;
    int i1=0;
    LatLng Markerdest = new LatLng(lat, lng);
    LatLng MiUbicacion = new LatLng(lat, lng);
    LatLng posicionMasCercana = null;

    RestfulClient restfulClient = new RestfulClient();

    Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        restfulClient.ExecuteApiRest();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MarkerPoints = new ArrayList<>();//inicializamos el marcador que se utilizara para agregar al mapa


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        //LeerRuta leerRuta = new LeerRuta();
        mMap = googleMap;
        miUbicacion();
        polilinea();

        //wpt();

        /////////////  BUSQUEDA DEL DESTINO INGRESANDO POR TEXTO ///////////////////////
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.e("Tag", "Place: "
                        + place.getAddress()
                        + place.getPhoneNumber()
                        + place.getLatLng().latitude
                        + place.getLatLng().longitude);

                LatLng latlangObj = place.getLatLng();
                Log.v("latitude:", "" + latlangObj.latitude);
                Log.v("longitude:", "" + latlangObj.longitude);

                Markerdest=latlangObj;
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

    ///////////////////// CALCULO DEL PUNTO MAS CERCANO AL PUNTO EN LA POLILINEA ///////////////////////////
    ///// HAY QUE MEJORAR ESTE METODO NO ES TAN EFICIENTE
/*private void PuntoCercano(){
    double distanciaActual = Double.MAX_VALUE;

    for(int i=0; i < lista.size(); i++) {
        double distancia = Math.round(SphericalUtil.computeDistanceBetween(Markerdest, lista.get(i)));
        if (distanciaActual > distancia) {
            posicionMasCercana = lista.get(i);
            distanciaActual = distancia;
        }
        System.out.println(i);
    }
    if (marcador2 != null) marcador2.remove();
    //POSICIONAMOS UN NUEVO MARKER EN EL PUNTO MAS CERCANO DE LA POLY
    marcador2 = mMap.addMarker(new MarkerOptions()
            .position(posicionMasCercana)
            .draggable(true)
            .title("Llegue Aqui")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

    MarkerPoints.add(posicionMasCercana);
//System.out.println("tamanio d ela lista MARKERPOINTS al encontrar ruta  # " + MarkerPoints.size());
    Toast.makeText(getApplicationContext(), " Ruta encontrada ", Toast.LENGTH_SHORT).show();
    Toast.makeText(getApplicationContext(), "Estas a:  "+ distanciaActual + " metros de la ruta del bus ", Toast.LENGTH_SHORT).show();

}*/
private  void MarkerCercano(LatLng cercano){

    if (marcador2 != null) marcador2.remove();

    double distancia = Math.round(SphericalUtil.computeDistanceBetween(Markerdest,cercano));
    //POSICIONAMOS UN NUEVO MARKER EN EL PUNTO MAS CERCANO ENTRE EL DESTINO Y LA POLY
    marcador2 = mMap.addMarker(new MarkerOptions()
            .position(cercano)
            .draggable(true)
            .title("Llegue hasta Aqui :)")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

    MarkerPoints.add(cercano);
    Toast.makeText(getApplicationContext(), " Ruta de bus encontrada ", Toast.LENGTH_SHORT).show();
    Toast.makeText(getApplicationContext(), " El bus te deja a " +distancia + " metros de tu destino" , Toast.LENGTH_SHORT).show();

}
/***METODO PARA POSICIONAR EL PUNTO EN EL MAPA Y BORRADO DE MARCADORES Y +
 *  METODO USADO PARA BUSQUEDAS POR TEXTO Y PULSO*/

private void PuntoDestino(LatLng pointDest){

    MarkerPoints.add(MiUbicacion);
    int radioCiruloTolerancia=100;

    //limpiamos el mapa y generamos un nuevo marcador si pulsamos o buscamos 2 veces en el map
    if (MarkerPoints.size()> 2 ) {

        marcador1.setVisible(false);
        marcador1.remove();

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
            marcador1 = mMap.addMarker(new MarkerOptions()
                    .position(pointDest)
                    .title("Su destino")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))); //punto destino
            mMap.animateCamera(miDestino);
           // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pointDest,14));
            Markerdest = MarkerPoints.get(1);

            if (line != null) line.remove();

            if (lineUbicActual != null) lineUbicActual.remove();

            if (lineRuta != null) lineRuta.remove();

            //isLocationOnEdge: DETERMINA SI UN PUNTO SE ENCUENTRA EN UNA POLILINEA CERCA DENTRO O FUERA DE ELLA
            // CON UN RADIO DE TOLERANCIA
            boolean encuentraBordePoli = PolyUtil.isLocationOnEdge(pointDest, lista, false, radioCiruloTolerancia);

            while (encuentraBordePoli != true) {
              cleanCircle();
                // System.out.println("entrammos while ");
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
                findPunto = encPuntoCerPoli.findNearestPoint(Markerdest, lista);

                MarkerCercano(findPunto);//colocamos un marker en el punto mas cercano (Destino - Ruta)

                DibujaPOli(Markerdest,findPunto);

                //PuntoCercano();
                //DibujaPOli(Markerdest, posicionMasCercana);
            } else {
                if (encuentraBordePoli == false) {
                    if (marcador2 != null) marcador2.remove();
                    //MarkerPoints.remove(2);
                    Toast.makeText(getApplicationContext(), "No se Encontro ninguna ruta en un radio de:  " + radioCiruloTolerancia, Toast.LENGTH_SHORT).show();
                }

            }
            cleanCircle();
        }
    }

    Circles(radioCiruloTolerancia);

}

/***********DIBUJA UNA POLILINEA ENTRE EL MARKER DESTINO Y EL PUNTO MAS CERCANO DE LA POLY**********/
    private void DibujaPOli(LatLng latlonD , LatLng latlonP){

        line = mMap.addPolyline(new PolylineOptions()
                .add(latlonD,latlonP)
                .width(5)
                .color(Color.RED));

    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Ud esta Aqui")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        mMap.animateCamera(miUbicacion);
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
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Location", "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;
        }else{
            Log.i("Location", "Permisos necesarios OK!.");
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            actualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500000,0,locListener);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

    }


    private void Circles(int rad){
        circle = mMap.addCircle(new CircleOptions()
                .center(Markerdest)
                .radius(rad)//metros
                .strokeWidth(5)
                .strokeColor(Color.GRAY)

        );
    }
    private void cleanCircle(){
        if (circle != null) {
            circle.remove();
        }
    }
    class GpxNode{

        Location location;
        String ele;
        String time;

        GpxNode(Location newLocation, String newEle, String newtime, String newName){
            location = null;
            ele = "";
            time = "";
        }

        GpxNode(Location loc){
            location = loc;
            ele = "";
            time = "";
        }

        GpxNode(Location loc, String e, String t){
            location = loc;
            ele = e;
            time = t;
        }

        void setEle(String e){
            ele = e;
        }

        void setTime(String t){
            time = t;
        }

        Location getLocation(){
            return location;
        }

        String getLocationString(){
            return location.getLatitude() + ":" + location.getLongitude();
        }

    }

    /**/
    public void wpt(){
        Marker marcadorParadero ;
        Resources res = getResources();
        //  TextView textInfo = (TextView) getView().findViewById(R.id.info);
        String info = "";
        List<GpxNode> gpxList = decodeGPXwayPoint(res.openRawResource(R.raw.transcisa7));
        for(int i = 0; i < gpxList.size(); i++){
            info = gpxList.get(i).getLocationString() ;
            String[] latlong = info.split(":");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng punto = new LatLng(latitude,longitude);
            listaWpt.add(punto);
            marcadorParadero = mMap.addMarker(new MarkerOptions().position(punto)
                    .title("PARADA")
                    // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        }

    }


    public void polilinea(){
       // restfulClient.ExecuteApiRest();

        int i;
        Resources res = getResources();
        TextView textInfo = (TextView)findViewById(R.id.info);
        String info = "";

        List<GpxNode> gpxList = decodeGPX(res.openRawResource(R.raw.transcisa7));
        for(i = 0; i < gpxList.size(); i++){
            info = gpxList.get(i).getLocationString() ;
            String[] latlong = info.split(":");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng punto = new LatLng(latitude,longitude);
            lista.add(punto);
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(latitude, longitude), 16));
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        i1=i;
        //System.out.println("ultimo " +lista.get(i1-1));
        //  mMap.addPolyline(new PolylineOptions().addAll(lista).width(5).color(Color.BLUE).visible(false));
    }

    private List<GpxNode> decodeGPXwayPoint(File f)
    {
        List<GpxNode> list1 = new ArrayList<GpxNode>();
        try {
            FileInputStream fis = new FileInputStream(f);
            list1 = decodeGPXwayPoint(fis);
        }catch (FileNotFoundException fne)
        {
            fne.printStackTrace();
        }
        //
        return list1;
    }
    private List<GpxNode> decodeGPXwayPoint(InputStream is){

        List<GpxNode> list1 = new ArrayList<GpxNode>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //FileInputStream fileInputStream = new FileInputStream();
            Document document = documentBuilder.parse(is);
            Element elementRoot = document.getDocumentElement();

            NodeList nodelist_wpt = elementRoot.getElementsByTagName("wpt");

            for(int i = 0; i < nodelist_wpt.getLength(); i++){

                Node node = nodelist_wpt.item(i);
                NodeList datostrkpt = node.getChildNodes();
                String newEle ="";
                String newtime ="";
                for (int j=0; j<datostrkpt.getLength(); j++)
                {
                    Node dato = datostrkpt.item(j);
                    String etiqueta = dato.getNodeName();
                    if (etiqueta.equals("ele")) {
                        newEle=dato.getFirstChild().getNodeValue();
                        //               System.out.println("nodo ele:"+newEle);
                    }else    if (etiqueta.equals("time")) {
                        newtime=dato.getFirstChild().getNodeValue();
                        //             System.out.println("nodo time:"+newtime);
                    }
                }

                NamedNodeMap attributes = node.getAttributes();

                String newLatitude = attributes.getNamedItem("lat").getTextContent();
                Double newLatitude_double = Double.parseDouble(newLatitude);

                String newLongitude = attributes.getNamedItem("lon").getTextContent();
                Double newLongitude_double = Double.parseDouble(newLongitude);

                String newLocationName = newLatitude + ":" + newLongitude;
                Location newLocation = new Location(newLocationName);
                newLocation.setLatitude(newLatitude_double);
                newLocation.setLongitude(newLongitude_double);

                GpxNode newGpxNode = new GpxNode(newLocation,newEle,newtime);
                list1.add(newGpxNode);
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
        return list1;
    }

    private List<GpxNode> decodeGPX(InputStream is){
        List<GpxNode> list = new ArrayList<GpxNode>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //FileInputStream fileInputStream = new FileInputStream();
            Document document = documentBuilder.parse(is);
            Element elementRoot = document.getDocumentElement();

            NodeList nodelist_trkpt = elementRoot.getElementsByTagName("trkpt");

            for(int i = 0; i < nodelist_trkpt.getLength(); i++){

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

}
