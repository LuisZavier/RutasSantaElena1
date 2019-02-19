package sesion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rutas.santaelena.app.rutas.R;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import denuncias.AbstractAsyncActivity;
import entities.SegUsuario;

public class RegistraUsuario extends AbstractAsyncActivity{
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

        setContentView(R.layout.activity_registro_usuario);

        final Button btnEnviarRegistro = (Button) findViewById(R.id.btnInicioSesionRegistro);
                btnEnviarRegistro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AddUserTask().execute();
                    }
                });
    }


    private void displayResponse(SegUsuario segUsuario) {
        Toast.makeText(this,"Usuario "+ segUsuario.getNombre() + " Registrado :)  " , Toast.LENGTH_LONG).show();

    }

    private class AddUserTask extends AsyncTask<Object,Object, SegUsuario>{

        private String email;
        private String password;
        private String idSegPerfil;
        private boolean estado;

        EditText editTextMail = (EditText) findViewById(R.id.editTextCorreoRegistro);
        EditText editTextPass = (EditText) findViewById(R.id.editTextPassRegistro);

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            this.email = editTextMail.getText().toString();
            this.password = editTextPass.getText().toString();
            this.idSegPerfil = "da193373-05ce-4cfa-a391-d0d35aeb07ad";
            this.estado=true;

        }


        @Override
        protected SegUsuario doInBackground(Object... params) {

            try {

                final String url = "http://192.168.101.1:8082/usuarios/";

                SegUsuario segUsuario = new SegUsuario();

                segUsuario.setNombre(email);
                segUsuario.setClave(password);
                segUsuario.setIdSegPerfil(idSegPerfil);
                segUsuario.setEstado(estado);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                SegUsuario UsarioRegistrado = restTemplate.postForObject(url,segUsuario,SegUsuario.class);

                return UsarioRegistrado;

            }catch (Exception ex) {
                Log.e("", ex.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(SegUsuario segUsuario) {
            super.onPostExecute(segUsuario);
            dismissProgressDialog();
            displayResponse(segUsuario);
            System.out.println("usuario + " + segUsuario.toString());
        }
    }

}
