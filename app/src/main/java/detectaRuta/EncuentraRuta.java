package detectaRuta;

import android.app.Activity;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

public class EncuentraRuta extends Activity {
    //isLocationOnEdge: DETERMINA SI UN PUNTO SE ENCUENTRA EN UNA POLILINEA CERCA DENTRO O FUERA DE ELLA CON UN RADIO DE TOLERANCIA
    Circle radioBusqueda;
    public boolean encuentraRutaBus(LatLng latLng , ArrayList<LatLng> lista, boolean f, int rad,GoogleMap mMap){

        boolean encuentraBordePoli = PolyUtil.isLocationOnEdge(latLng, lista, f, rad);

        while (encuentraBordePoli != true) {
             cleanCircle(radioBusqueda);
            while (rad < 1000) {
                rad = rad + 100;
                 dibujaRadio(rad,latLng,mMap);
                 cleanCircle(radioBusqueda);
                encuentraBordePoli = PolyUtil.isLocationOnEdge(latLng, lista, false, rad);
                if (encuentraBordePoli == true)
                    break;
            }
            break;
        }
        return encuentraBordePoli;
    }
    public void dibujaRadio(int rad, LatLng marcador,GoogleMap mMap) {
        radioBusqueda = mMap.addCircle(new CircleOptions()
                .center(marcador).radius(rad)//metros
                .strokeWidth(5).strokeColor(Color.GRAY)
        );
    }

    public void cleanCircle(Circle limpiRadioBusqueda) {
        if (limpiRadioBusqueda != null) limpiRadioBusqueda.remove();

    }
}
