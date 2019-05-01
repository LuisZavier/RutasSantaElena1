package denuncias;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.rutas.santaelena.app.rutas.R;

import java.util.ArrayList;
import java.util.List;

import entities.Reporte;
import models.HttpGetAsuntos;
import models.HttpPostDenuncias;

/**
 * Created by Javier on 29/08/2018.
 */

public class Notificaciones extends AbstractAsyncActivity {

    protected static final String TAG = Notificaciones.class.getSimpleName();
    RadioButton rb;
    Spinner spinnerAsunto;
    ArrayList<String> listaAsuntosTransporte = new ArrayList<String>();
    String username;
    String password;

    EditText editTextMov ;
    EditText editTextUsuario ;

    EditText editTextNumeroDisco ;
    EditText editubicacion ;
    EditText editTextRecorridoBus;
    EditText  editTextMensaje;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_denuncias);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("user");
            password = extras.getString("clave");
        }

        editTextNumeroDisco = (EditText)  findViewById(R.id.id_numeroDisco);
        editubicacion = findViewById(R.id.id_ubicacionIncidente);
        editTextRecorridoBus = (EditText)  findViewById(R.id.id_linea_bus);
        editTextMensaje = (EditText)  findViewById(R.id.id_mensage);
        spinnerAsunto = (Spinner) findViewById(R.id.id_asuntoSpinner);
        editTextMov = (EditText) findViewById(R.id.id_movil);
        editTextUsuario = (EditText) findViewById(R.id.id_nombre);
        llenarSpinner();
        rb = (RadioButton) findViewById(R.id.anonimo);

        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb.isChecked()){
                    editTextMov = (EditText) findViewById(R.id.id_movil);
                    editTextMov.setEnabled(false);

                    editTextUsuario = (EditText) findViewById(R.id.id_nombre);
                    editTextUsuario.setEnabled(false);
                }
            }
        });

        final Button submitButton = (Button) findViewById(R.id.btnEnviar);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(validateRegistro(editTextMensaje.getText().toString()))
                    enviaParametros();
            }
        });
    }

    private void pregunta(Reporte reporte){
        VerDenunciaEnviada verDenunciaEnviada = new VerDenunciaEnviada();
        verDenunciaEnviada.alertVerDenuncia(reporte,this);
    }

    private void displayResponse(Reporte Reporte) {
        Toast.makeText(this,"Mensaje enviado con Exito :)  " , Toast.LENGTH_LONG).show();
        cleanEditext();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Desea Visulizar el mensaje Enviado");
        builder.setMessage("Seleccione ");

        builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                pregunta(Reporte);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void llenarSpinner(){

        AsyncTask<Void, Void, List<String>> httpGetAsuntos = new HttpGetAsuntos(new HttpGetAsuntos.AsynGetAsuntos() {
            @Override
            public void asuntos(List<String> asuntos) {
                for(int j=0;j<asuntos.size();j++)
                    listaAsuntosTransporte.add(j,asuntos.get(j));
                presentarDatos(listaAsuntosTransporte);
            }}).execute();
    }

    private void presentarDatos(ArrayList listaAsuntos){
        ArrayAdapter<String> adapterAsuntos = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listaAsuntos);
        spinnerAsunto.setAdapter(adapterAsuntos);
    }

    private boolean validateRegistro(String mensaje){

        if(mensaje == null || mensaje.trim().length() == 0){
            Toast.makeText(this, "Detalle del Mensaje is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void enviaParametros(){
        AsyncTask<Object, Object, Reporte> httpPostDenuncias = new HttpPostDenuncias(new HttpPostDenuncias.AsynDenuncias() {
            @Override
            public void processDenuncia(Reporte reporte) {
                displayResponse(reporte);
            }
        }).execute(this,spinnerAsunto,rb,editTextUsuario.getText().toString(),editTextMov.getText().toString(),
                editTextNumeroDisco.getText().toString(),editTextRecorridoBus.getText().toString(),editTextMensaje.getText().toString(),
                username,password,editubicacion.getText().toString());
    }

    public void cleanEditext(){
        editTextMensaje.setText("");
        editTextMov.setText("");
        editTextRecorridoBus.setText("");
        editTextNumeroDisco.setText("");
        editTextUsuario.setText("");
        editubicacion.setText("");

    }
}