package models;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import notificaciones.Notificaciones;

public class HttpGetAsuntos extends AsyncTask<Void, Void, List<String>> {
    private Notificaciones notificaciones;

    public HttpGetAsuntos(Notificaciones notificaciones) {
        this.notificaciones = notificaciones;
    }

    RestTemplate restTemplate = new RestTemplate();

    public interface AsynGetAsuntos{
        void asuntos(List<String> asuntos);
    }

    public AsynGetAsuntos delegate = null;

    public HttpGetAsuntos(AsynGetAsuntos delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<String> doInBackground(Void... params) {

        try
        {
            System.out.println("Begin /GET request Restful Asuntos!");
            Log.d("WS", "doInBackground: Begin GET request tipos de Asuntos!");

            String url = "http://facsistel.upse.edu.ec:8082/asuntos";

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


             List <String> asuntos = restTemplate.getForObject(url,List.class);

            return asuntos;


        }catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> asuntos) {
        super.onPostExecute(asuntos);
        delegate.asuntos(asuntos);
        System.out.println(asuntos.toString());
    }
}
