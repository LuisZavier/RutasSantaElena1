package detectaRuta;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SeleccionUbicacion {

    public interface OnOkOrigenDestino{
        void seleccionadaUbicacion (int seleccionUbicacion);
    }
    public void SeleccionaOpcionUbiDest(Context context, final
    OnOkOrigenDestino onOkOrigenDestino) {
        List<String> opcionOrigenDestino = new ArrayList<>();

        opcionOrigenDestino.add("Punto Origen / Partida");
        opcionOrigenDestino.add("Punto Destino / Meta");

        final CharSequence[] origenDestinoItem = opcionOrigenDestino.toArray(new String[opcionOrigenDestino.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Desea visualizar alrrededor de : ");
        dialogBuilder.setItems(origenDestinoItem, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                String selectedOpcion = origenDestinoItem[item].toString();  //Selected item in listview
                Toast.makeText(context, "Selecciono " + selectedOpcion, Toast.LENGTH_SHORT).show();

                for (int i=0;i<opcionOrigenDestino.size();i++)
                    if (selectedOpcion == opcionOrigenDestino.get(i)){
                        onOkOrigenDestino.seleccionadaUbicacion(i);
                        break;
                    }
            }});

        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();

    }
}
