package notificaciones;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rutas.santaelena.rutas.R;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Javier on 29/05/2018.
 */

public class Notificaciones extends AbstractAsyncActivity {
    protected static final String TAG = Notificaciones.class.getSimpleName();

    // ***************************************
    // Activity methods
    // ***************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactanos);

        // Initiate the request to the protected service
        final Button submitButton = (Button) findViewById(R.id.btnEnviar);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new FetchSecuredResourceTask().execute();
            }
        });
    }

    // ***************************************
    // Private methods
    // ***************************************
    private void displayResponse(Void response) {
      //  Toast.makeText(this, response.getMensaje(), Toast.LENGTH_LONG).show();
    }

    // ***************************************
    // Private classes
    // ***************************************
    private class FetchSecuredResourceTask extends AsyncTask<Object, Object, Void> {

        private String asunto;

        private String mensaje;

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            // build the message object
            EditText editText = (EditText) findViewById(R.id.et_EmailAsunto);
            this.asunto = editText.getText().toString();

            editText = (EditText) findViewById(R.id.et_EmailMensaje);
            this.mensaje = editText.getText().toString();
        }

        @Override
        protected Void doInBackground(Object... params) {
            final String url = getString(R.string.base_uri);

            Message message = new Message();
            message.setAsunto(asunto);
            message.setMensaje(mensaje);

            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            String response = restTemplate.postForObject(url, message, String.class);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dismissProgressDialog();
            displayResponse(result);
        }

    }

}
