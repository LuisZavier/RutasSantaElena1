package detectaRuta;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class EncuentraRuta extends FragmentActivity {
    //isLocationOnEdge: DETERMINA SI UN PUNTO SE ENCUENTRA EN UNA POLILINEA CERCA DENTRO O FUERA DE ELLA CON UN RADIO DE TOLERANCIA
    Circle radioBusqueda;
    int seleccion=0;
    public boolean encuentraRutaBus(LatLng latLng , ArrayList<LatLng> lista, boolean f, int rad,GoogleMap mMap){

        boolean encuentraBordePoli = PolyUtil.isLocationOnEdge(latLng, lista, f, rad);

        while (encuentraBordePoli != true) {
             //cleanCircle(radioBusqueda);
            while (rad < 1000) {
                rad = rad + 100;
                 //dibujaRadio(rad,latLng,mMap);
                 //cleanCircle(radioBusqueda);
                encuentraBordePoli = PolyUtil.isLocationOnEdge(latLng, lista, false, rad);
                if (encuentraBordePoli == true)
                    break;
            }
            break;
        }
        System.out.println(" radio creio hasta "  + rad );
        return encuentraBordePoli;
    }
    public void dibujaRadio(int rad, LatLng marcador,GoogleMap mMap) {
        radioBusqueda = mMap.addCircle(new CircleOptions()
                .center(marcador)
                .radius(rad)//metros
                .strokeWidth(5)
                .strokeColor(Color.GRAY)
        );
    }

    public void cleanCircle(Circle limpiRadioBusqueda) {
        if (limpiRadioBusqueda != null) limpiRadioBusqueda.remove();

    }

    public void dibujaRutaSeleccionada(List<ArrayList<LatLng>> listaRutasTodas, GoogleMap mMap) {
        for (int i = 0; i < listaRutasTodas.size(); i++)
            mMap.addPolyline(new PolylineOptions().addAll(listaRutasTodas.get(i)).width(4).color(Color.BLUE));
    }
    public interface OnOk{
        void seleccionada (int seleccion);
    }
    public void ShowAlertDialogBusesDisponibles(List<String> busesDisponibles, List<String> lineabus, Context context,final
                                                OnOk onOk) {

        final CharSequence[] busDisponibleList = busesDisponibles.toArray(new String[busesDisponibles.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("buses disponibles");
        dialogBuilder.setItems(busDisponibleList, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                String selectedLineaBus = busDisponibleList[item].toString();  //Selected item in listview
                Toast.makeText(context, "Selecciono La Linea  " + selectedLineaBus, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < lineabus.size(); i++) {
                    if (selectedLineaBus == lineabus.get(i)){
                        onOk.seleccionada(i);
                        break;
                    }
                } }});

        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();

    }

}
