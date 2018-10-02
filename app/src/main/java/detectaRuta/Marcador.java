package detectaRuta;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Marcador extends FragmentActivity {

    List<Address> addresses = null;

    public Marker colocarMarcador(LatLng PuntoLatLong, String title , BitmapDescriptor icon, GoogleMap mMap) {

        getNombreCalle(PuntoLatLong);
       // CameraUpdate miOrigen = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 15);
        Marker tempMarker = mMap.addMarker(new MarkerOptions()
                .position(PuntoLatLong)
                .title(title)
                .snippet(addresses.get(0).getAddressLine(0))
                .draggable(true)
                .icon(icon));
      //  mMap.animateCamera(miOrigen);
        return tempMarker;
    }
    public Marker colocarMarcadorRutaBusMasCercanaDetino(LatLng PuntoLatLong, String title , BitmapDescriptor icon, GoogleMap mMap) {
       Marker markerFinBus ;
       // if (markerFinBus != null) markerFinBus.remove();

        markerFinBus =colocarMarcador(PuntoLatLong,title, icon,mMap);

       // MarkerPoints.add(finalBus);
        return markerFinBus;
    }
    public Marker colocarMarcadorPuntoOrigenEnMapa(LatLng PuntoLatLong, String title , BitmapDescriptor icon, GoogleMap mMap){
       Marker markerOrigen;
       // if (markerOrigen != null) markerOrigen.remove();
        markerOrigen = colocarMarcador(PuntoLatLong, title ,icon,mMap);
        CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 15);
        mMap.animateCamera(orig);
        return  markerOrigen;
    }

   public Marker colocarMarcadorRutaBusMasCercanaOrigen(LatLng PuntoLatLong, String title , BitmapDescriptor icon, GoogleMap mMap) {
       // if (markerRutaBus != null) markerRutaBus.remove();
       Marker markerRutaBus;

        markerRutaBus =colocarMarcador(PuntoLatLong, title,icon,mMap);
       // MarkerPoints.add(paradero);
        return markerRutaBus;
    }
    public Marker colocarMarcadorPuntoDestinoEnMapa(LatLng PuntoLatLong, String title , BitmapDescriptor icon, GoogleMap mMap){

        Marker markerDestino;

        markerDestino = colocarMarcador(PuntoLatLong, title,icon,mMap);
       // markerDestino.showInfoWindow();
        CameraUpdate dest = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 15);
        mMap.animateCamera(dest);
        return  markerDestino;
    }
    public List<Address> getNombreCalle(LatLng street) { //Metodo que me devuelve el nombre de la calle
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(street.latitude, street.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  addresses;
    }
}