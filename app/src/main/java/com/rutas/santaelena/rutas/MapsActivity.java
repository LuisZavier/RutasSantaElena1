package com.rutas.santaelena.rutas;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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

import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    PlaceAutocompleteFragment placeAutoComplete;
    private GoogleMap mMap;

    private List<LatLng> listaWpt = new ArrayList<>();
    private Marker marcador,marcador1;
    ArrayList<LatLng> MarkerPoints;
    double lat = 0;
    double lng = 0;
    LatLng Markerdest = new LatLng(lat, lng);
    LatLng MiUbicacion = new LatLng(lat, lng);
    Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.e("Tag", "Place: "
                        + place.getAddress()
                        + place.getPhoneNumber()
                        + place.getLatLng().latitude
                        + place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }

        });



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MarkerPoints = new ArrayList<>();//inicializamos el marcador que se utilizara para agregar al mapa

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        wpt();
        miUbicacion();


        /* metodo para agregar un marker en el map*/
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                MarkerPoints.add(MiUbicacion);
                //limpiamos el mapa y generamos un nuevo marcador si pulsamos 2 veces en el map
                if (MarkerPoints.size() > 1) {

                    marcador1.setVisible(false);
                    marcador1.remove();

                    MarkerPoints.remove(0);
                    MarkerPoints.remove(1);

                    MarkerPoints = new ArrayList<>();

                    MarkerPoints.add(MiUbicacion); //mi ubicacion siempre sera mi primer punto
                }

                // agregamos los markers al array list
                MarkerPoints.add(point);


                if (MarkerPoints.size() == 1) {
                    //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                } else if (MarkerPoints.size() == 2) {
                            marcador1=mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))); //punto destino
                            Markerdest = MarkerPoints.get(1);
                    if (circle != null) {
                        circle.remove();
                    }
                }

                //dibujams un radio de 300 metros en el punto destino
                circle = mMap.addCircle(new CircleOptions()
                        .center(Markerdest)
                        .radius(300)//metros
                        .strokeColor(Color.GRAY)

                );

                //si existe un marker (paradero dentro de ese radio)
                float[] disResultado = new float[2];
                int c=0;
                for(int i = 0; i < listaWpt.size(); i++){

                    Location.distanceBetween(listaWpt.get(i).latitude, listaWpt.get(i).longitude,
                            circle.getCenter().latitude,
                            circle.getCenter().longitude,
                            disResultado);
                    if(disResultado[0] < circle.getRadius()){
                        c++;
                        //System.out.println(listaWpt.get(i).latitude + " algo "+ listaWpt.get(i).longitude);
                      //  System.out.println("si hay"+ disResultado[0]);
                        //System.out.println("salimos for");
                       // break;
                    //} else {
                      //  System.out.println("no hay ningun paradero en los " + circle.getRadius() + "metros");
                    }
                }
                //System.out.println("hay "+ c +" paraderos en los " + circle.getRadius() + "metros");
                Toast.makeText(getApplicationContext(),"hay "+ c +" paraderos en los " + circle.getRadius() + "metros", Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);

        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 13);
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locListener);
            mMap.getUiSettings().setZoomControlsEnabled(true);
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
        Marker marcador ;
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
            //.addmarker(new markerOptions().position(fasf))
            marcador = mMap.addMarker(new MarkerOptions().position(punto)
                    .title("PARADA")
                    // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        }
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
                        System.out.println("nodo ele:"+newEle);
                    }else    if (etiqueta.equals("time")) {
                        newtime=dato.getFirstChild().getNodeValue();
                        System.out.println("nodo time:"+newtime);
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


}
