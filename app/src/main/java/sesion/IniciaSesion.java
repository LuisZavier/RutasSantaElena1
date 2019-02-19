package sesion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rutas.santaelena.app.rutas.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import denuncias.AbstractAsyncActivity;
import entities.SegUsuario;

public class IniciaSesion extends AbstractAsyncActivity {

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

        setContentView(R.layout.activity_inicio_sesion);

        EditText editTextMail = (EditText) findViewById(R.id.editTextMailRegistrado);
        EditText editTextPass = (EditText) findViewById(R.id.editTextPassRegitrado);

        Button btnIniciaSesion = (Button) findViewById(R.id.btnIniciaSesion);
        btnIniciaSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new iniciaSesion().execute(editTextMail.getText().toString(),editTextPass.getText().toString());
            }
        });

        Button btnRegistro = (Button) findViewById(R.id.btnCrearCuenta);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IniciaSesion.this, RegistraUsuario.class);
                startActivity(intent);
            }
        });


    }

    private void displayResponse(SegUsuario s) {
        Toast.makeText(this,"token   "+ s.getIdSegPerfil() + " :)  " , Toast.LENGTH_LONG).show();
       /* if (segUsuario.getNombre()!=null) {
            Intent intent = new Intent(IniciaSesion.this, Notificaciones.class);
            startActivity(intent);
        } */
    }

    private class iniciaSesion extends AsyncTask<Object,Void, SegUsuario>{

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected SegUsuario doInBackground(Object... params) {
            try {

                String mail = (String) params[0];
                String pass = (String) params[1];

                String url = "http://192.168.101.1:8082/usuarios/"+mail+"/"+pass;

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                SegUsuario tokenHeader = restTemplate.getForObject(url,SegUsuario.class);

                return tokenHeader;

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
           System.out.println("token " + segUsuario.getIdSegPerfil());
        }
    }
}
