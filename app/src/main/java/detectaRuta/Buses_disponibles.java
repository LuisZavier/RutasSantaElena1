package detectaRuta;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rutas.santaelena.app.rutas.R;

import java.util.List;

public class Buses_disponibles extends FragmentActivity {
    Dialog dialog ;
    public interface OnOklineaSeleccionada{
        void seleccionada (int seleccion);
    }
    public void alertLineas(List<String> busesDisponibles, List<String> lineabus, Context context, List<Double> disOrigenRuta,List<Double> disRutaDestino,
                            OnOklineaSeleccionada onOk ){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View convertView = (View) inflater.inflate(R.layout.activity_buses_disponibles, null);

        alertDialog.setView(convertView);

        ListView listViewdisOrigenRuta = (ListView) convertView.findViewById(R.id.id_origenRuta);
        ArrayAdapter<Double> adapterOrigenRuta = new ArrayAdapter<Double>(context,android.R.layout.simple_list_item_1,disOrigenRuta);
        listViewdisOrigenRuta.setAdapter(adapterOrigenRuta);

        ListView listViewdisRutaDestino = (ListView) convertView.findViewById(R.id.id_destinoRuta);
        ArrayAdapter<Double> adapterRutaDestino = new ArrayAdapter<Double>(context,android.R.layout.simple_list_item_1,disRutaDestino);
        listViewdisRutaDestino.setAdapter(adapterRutaDestino);

        ListView listViewBusesDisponibles = (ListView) convertView.findViewById(R.id.id_lineas_buses);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_single_choice,busesDisponibles);
        listViewBusesDisponibles.setAdapter(adapter);

        listViewBusesDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listViewBusesDisponibles.getItemAtPosition(position);

                for (int i = 0; i < lineabus.size(); i++) {
                    if (listItem == lineabus.get(i)){
                        onOk.seleccionada(i);
                        break;
                    }
                }
                Toast.makeText(context, "Selecciono La Linea " + listItem, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        listViewBusesDisponibles.setChoiceMode(listViewBusesDisponibles.CHOICE_MODE_SINGLE);

        dialog = alertDialog.show();

    }



}
