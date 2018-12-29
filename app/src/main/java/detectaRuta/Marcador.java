package detectaRuta;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rutas.santaelena.rutas.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Marcador extends FragmentActivity {

    String nameCalle;
    BitmapDescriptor icon;
    List<Address>  addresses = null;

    public Marker colocarMarcador(LatLng PuntoLatLong, String title, BitmapDescriptor icon, GoogleMap mMap,Context context) {

        nameCalle = getNombreCalles(PuntoLatLong,context);
        Marker tempMarker = mMap.addMarker(new MarkerOptions()
                .position(PuntoLatLong)
                .title(title)
                .snippet(nameCalle)
                .draggable(true)
                .icon(icon));
        return tempMarker;
    }


    public Marker colocarMarcadorRutaBusMasCercanaDetino(LatLng PuntoLatLong, String title, GoogleMap mMap,Context context) {

        icon = BitmapDescriptorFactory.fromResource(R.drawable.llegadabus);

        Marker markerFinBus;
        markerFinBus = colocarMarcador(PuntoLatLong, title, icon, mMap,context);

        return markerFinBus;
    }

    public Marker colocarMarcadorPuntoOrigenEnMapa(LatLng PuntoLatLong, String title, GoogleMap mMap,Context context) {

        icon = BitmapDescriptorFactory.fromResource(R.drawable.ubicacion);

        Marker markerOrigen;

        markerOrigen = colocarMarcador(PuntoLatLong, title, icon, mMap,context);
        CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 15);
        mMap.animateCamera(orig);
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        return markerOrigen;
    }

    public Marker colocarMarcadorRutaBusMasCercanaOrigen(LatLng PuntoLatLong, String title, GoogleMap mMap,Context context) {
        icon = BitmapDescriptorFactory.fromResource(R.drawable.paradadeautobus);
        Marker markerRutaBus;

        markerRutaBus = colocarMarcador(PuntoLatLong, title, icon, mMap,context);
        return markerRutaBus;
    }

    public Marker colocarMarcadorPuntoDestinoEnMapa(LatLng PuntoLatLong, String title, GoogleMap mMap,Context context) {

        icon = BitmapDescriptorFactory.fromResource(R.drawable.meta);

        Marker markerDestino;
        markerDestino = colocarMarcador(PuntoLatLong, title, icon, mMap,context);
        CameraUpdate dest = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 17);
        mMap.animateCamera(dest);
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        return markerDestino;
    }

    public String getNombreCalles(LatLng street,Context context) { //Metodo que me devuelve el nombre de la calle
        String calle="";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(street.latitude, street.longitude, 1);
            if (addresses.size() == 0)
                //return null;
                calle = "No hay Calles";
            else
                calle = (addresses.get(0).getAddressLine(0));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return calle ;
    }
    public Marker verBus(LatLng PuntoLatLong, String title, BitmapDescriptor icon, GoogleMap mMap) {

        Marker busPosicion = mMap.addMarker(new MarkerOptions()
                .position(PuntoLatLong)
                .title(title)
                .draggable(true)
                .icon(icon));
        return busPosicion;
    }
}