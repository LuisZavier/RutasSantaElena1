package denuncias;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rutas.santaelena.app.rutas.R;

import entities.Reporte;

public class VerDenunciaEnviada {

    public void alertVerDenuncia(Reporte reporte,Context context
                            ){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View convertView = (View) inflater.inflate(R.layout.activity_denuncias_ver, null);

        alertDialog.setView(convertView);

        TextView TxtAsunto = (TextView) convertView.findViewById(R.id.Txt_asunto);
        TxtAsunto.setText(reporte.getAsunto().toString());

        TextView TxtMovil = (TextView) convertView.findViewById(R.id.Txt_movil);
        TxtMovil.setText(reporte.getMovil().toString());

        TextView TxtNombre = (TextView) convertView.findViewById(R.id.Txt_nombre);
        TxtNombre.setText(reporte.getUsuario().toString());

        TextView TxtDisco = (TextView) convertView.findViewById(R.id.Txt_numeroDisco);
        TxtDisco.setText(reporte.getNumeroDisco().toString());

        TextView TxtRecorrido = (TextView) convertView.findViewById(R.id.Txt_linea_bus);
        TxtRecorrido.setText(reporte.getLinea().toString());

        /*TextView TxtUbicacionAccidente = (TextView) convertView.findViewById(R.id.Txt_ubicacionIncidente);
        TxtUbicacionAccidente.setText(reporte.getUbicacion().toString()); */

        TextView TxtMensaje = (TextView) convertView.findViewById(R.id.Txt_mensage);
        TxtMensaje.setText(reporte.getMensaje().toString());

        alertDialog.setNegativeButton("Volver ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alertDialog.show();



    }
}
