package lineas;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.rutas.santaelena.rutas.R;

import java.util.ArrayList;

import detectaRuta.Marcador;
import entities.EstadoBus;
import posicionBus.Getbus;

public class AnimateBusPosicion {
    private int numEjecuciones = 0 ;
    private ArrayList<LatLng> lista = new ArrayList<>();
    Marcador marcador = new Marcador();
    BitmapDescriptor icon;
    LatLng punto;
    int c=0;
    private Marker markerBus;
    private Marker markerNewPosBus;

    private void llenapuntosprueba()
    {
        lista.add(new LatLng( -2.220875, -80.912307));
        lista.add(new LatLng( -2.222269, -80.914152));
        lista.add(new LatLng( -2.222762, -80.912865));
        lista.add(new LatLng( -2.221615, -80.910655));
        lista.add(new LatLng( -2.232291, -80.878378));

    }

    /**
     *
     * @param mMap
     * @return posicion del bus en la ruta
     */

    public void estadobus(final GoogleMap mMap){//,final LatLng latLng ) {

        Handler h = new Handler();

        llenapuntosprueba();

        h.post(new Runnable() {
            @Override
            public void run() {

                AsyncTask<Void, Void, EstadoBus> getbus = new Getbus(new Getbus.Posicionbus() {
                    @Override
                    public void busPosicion(EstadoBus estadoBus) {

                        double lat = estadoBus.getPosicionActual().getX();
                        double lo = estadoBus.getPosicionActual().getY();
                        punto = new LatLng(lat, lo);
                        int linea = estadoBus.getLinea();
                        int numPasajeros = estadoBus.getCantidadUsuarios();
                        String dato = String.valueOf("# Pasajeros " + numPasajeros + ", Linea" + linea);

                        icon = BitmapDescriptorFactory.fromResource(R.drawable.busposicionruta);
                        //markerBus = marcador.colocarMarcador(punto, dato, icon,mMap);
                        if (c==0) {
                            markerBus = marcador.verBus(punto,dato,icon,mMap); //marcador.colocarMarcador(punto, dato, icon,mMap,context);
                            c++;
                        }else {
                            markerNewPosBus = animateMarker(markerBus, lista.get(c), false, mMap);
                            c++;
                        }

                    }}).execute();

                if (numEjecuciones < 4) {
                    h.postDelayed(this, 15000);//30 segundos
                    numEjecuciones ++;
                }
            }
        });
        // return markerBus;
    }

    /**
     *
     * @param marker
     * @param toPosition
     * @param hideMarker
     * @param mMap
     * @return nueva posicion del bus
     */

    public Marker animateMarker(final Marker marker, final LatLng toPosition,
                                final boolean hideMarker, final GoogleMap mMap) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        android.graphics.Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = toPosition.longitude ;
                double lat = toPosition.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
        return marker;
    }

}
