package lineas;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import detectaRuta.Marcador;
import entities.Parada;
import entities.Point;
import entities.Ruta;

public class LineAllWayPcercanos extends Activity{

    Marcador marcador = new Marcador();
    BitmapDescriptor icon;

    private ArrayList<LatLng> listaPuntosRutasTodas = new ArrayList<>();
    private ArrayList<String> nombreParadero = new ArrayList<>();
    private List<ArrayList<LatLng>> listaRutas = new ArrayList<>();

    /**
     *
     * @param lineasTodas
     * @return lista de todas las rutas existentes
     */

    public List<ArrayList<LatLng>> getRecorridoLineas(List<Ruta> lineasTodas){

        for (int i = 0; i < lineasTodas.size(); i++) {
            Ruta rutaModels = lineasTodas.get(i);
            List<Point> ruta = rutaModels.getListasPuntos();
            for (int j = 0; j < ruta.size(); j++) {
                double latitud = ruta.get(j).getX();
                double longitud = ruta.get(j).getY();
                LatLng punto = new LatLng(latitud, longitud);
                listaPuntosRutasTodas.add(punto);
            }
            listaRutas.add(i, new ArrayList<LatLng>(listaPuntosRutasTodas));
            listaPuntosRutasTodas.clear();
        }

        return listaRutas;
    }

    /**
     *
     * @param listParadas
     * @param mMap
     * @return lista de paraderos cercanos .
     */

    public List<Marker> paraderosWpt(List<Parada> listParadas, final GoogleMap mMap, Context context) {
        List<Marker> markers = new ArrayList<>();
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        for (int i = 0; i < listParadas.size(); i++) {

            nombreParadero.add(listParadas.get(i).getNombre());

            Parada coord = listParadas.get(i);
            double latitud = coord.getCoordenada().getX();
            double longitud = coord.getCoordenada().getY();

            LatLng puntoMarcadores = new LatLng(latitud, longitud);

            markers.add(i,marcador.colocarMarcador(puntoMarcadores,nombreParadero.get(i),icon,mMap,context));
        }
        return  markers;
    }
}