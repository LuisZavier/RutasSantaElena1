package notificaciones;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.rutas.santaelena.rutas.R;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import entities.Point;
import entities.Reporte;
import models.HttpGetAsuntos;


/**
 * Created by Javier on 29/05/2018.
 */

public class Notificaciones extends AbstractAsyncActivity {

    protected static final String TAG = Notificaciones.class.getSimpleName();
    RadioButton rb;
    Spinner spinnerAsunto;
    ArrayList<String> listaAsuntosTransporte = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notificaciones);
        spinnerAsunto = (Spinner) findViewById(R.id.id_asuntoSpinner);
        llenarSpinner();
        rb = (RadioButton) findViewById(R.id.anonimo);

        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb.isChecked()){
                    EditText editTextMov = (EditText) findViewById(R.id.id_movil);
                    editTextMov.setEnabled(false);

                    EditText editTextUsuario = (EditText) findViewById(R.id.id_nombre);
                    editTextUsuario.setEnabled(false);
                }
            }
        });

        final Button submitButton = (Button) findViewById(R.id.btnEnviar);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new FetchSecuredResourceTask().execute();
            }
        });
    }

    private void displayResponse(Void response) {
        Toast.makeText(this,"enviado " +  response.toString(), Toast.LENGTH_LONG).show();
    }

    private void llenarSpinner(){

        AsyncTask<Void, Void, List<String>> httpGetAsuntos = new HttpGetAsuntos(new HttpGetAsuntos.AsynGetAsuntos() {
            @Override
            public void asuntos(List<String> asuntos) {
                for(int j=0;j<asuntos.size();j++)
                    listaAsuntosTransporte.add(j,asuntos.get(j));
                presentarDatos(listaAsuntosTransporte);

            }
        }).execute();
    }

    private void presentarDatos(ArrayList listaAsuntos){
        ArrayAdapter<String> adapterAsuntos = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listaAsuntos);
        spinnerAsunto.setAdapter(adapterAsuntos);
    }

    private class FetchSecuredResourceTask extends AsyncTask<Object, Object, Void> {

        private String usuario;
        private String movil;
        private String asunto;
        private String numeroDisco;
        private Point ubicacion; //(String)
        private Date fecha_del_incidente;
        private String linea;
        private String mensaje;
        private boolean estado;

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            String asun = (String) spinnerAsunto.getSelectedItem();

            this.asunto = asun;

            if(rb.isChecked()== true){
                this.movil = " ";
                this.usuario = "Anonimo";
            }else {
                EditText editTextMovil = (EditText) findViewById(R.id.id_movil);
                this.movil = editTextMovil.getText().toString();

                EditText editTextUsuario = (EditText) findViewById(R.id.id_nombre);
                this.usuario = editTextUsuario.getText().toString();
            }

            EditText editTextNumeroDisco = (EditText)  findViewById(R.id.id_numeroDisco);
            this.numeroDisco = editTextNumeroDisco.getText().toString();

            EditText editubicacion = findViewById(R.id.id_ubicacionIncidente);
            this.ubicacion = null;

            Date fechaIncidente = Calendar.getInstance().getTime();
            this.fecha_del_incidente = fechaIncidente;

            EditText editTextRecorridoBus = (EditText)  findViewById(R.id.id_linea_bus);
            this.linea = editTextRecorridoBus.getText().toString();

            EditText  editTextMensaje = (EditText)  findViewById(R.id.id_mensage);
            this.mensaje = editTextMensaje.getText().toString();

            this.estado = true;
        }

        @Override
        protected Void doInBackground(Object... params) {
            try
            {
                final String url = getString(R.string.url_reporte);
                Reporte reporte = new Reporte();
                reporte.setMovil(movil);
                reporte.setUsuario(usuario);
                reporte.setAsunto(asunto);
                reporte.setNumeroDisco(numeroDisco);
                reporte.setUbicacion(ubicacion);
                reporte.setFecha(fecha_del_incidente);
                reporte.setLinea(linea);
                reporte.setMensaje(mensaje);
                reporte.setEstado(estado);


                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                String reporteUsuario = restTemplate.postForObject(url, reporte, String.class);


            }catch (Exception ex) {
                Log.e("", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dismissProgressDialog();
            displayResponse(result);
        }

    }

}
