package sesion;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.rutas.santaelena.app.rutas.R;

import detectaRuta.SeleccionUbicacion;

public  class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;

    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    public CustomInfoWindowAdapter() {

    }

    public View getInfoContents(final Marker m) {

        String info1 = m.getTitle();
        String info2 = m.getSnippet();

        View v = inflater.inflate(R.layout.infowindow_layout, null);

       if (m.getTag()=="TAG_WAYPOINT") {// MOSTRAR LOS BUSES QUE PASARAN POR ESE PARADERI

           ((TextView)v.findViewById(R.id.info_window_title)).setText(info1);
           ((TextView)v.findViewById(R.id.info_window_snniple)).setText("Paradero");

           new SeleccionUbicacion().viewSitiosParaderos(m,this.inflater.getContext());


       }else{

           ((TextView)v.findViewById(R.id.info_window_title)).setText(info1);
           ((TextView)v.findViewById(R.id.info_window_snniple)).setText(info2);

       }

        return v;

    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }




}