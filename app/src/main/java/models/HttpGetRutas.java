package models;

import android.os.AsyncTask;
import android.util.Log;

import com.rutas.santaelena.rutas.MapsActivity;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import entities.Ruta;
import lineas.LineAllWayPcercanos;

/*CLASE QUE ME DEVUELVE TODAS LAS RUTAS EN UNA LISTA DE TIPO RUTA MODEL*/

public class HttpGetRutas extends AsyncTask<Void, Void, List<Ruta>> {

    private LineAllWayPcercanos lineAllWayPcercanos;

    public HttpGetRutas(LineAllWayPcercanos lineAllWayPcercanos) {
        this.lineAllWayPcercanos = lineAllWayPcercanos;
    }

    private MapsActivity mapsActivity;

    public HttpGetRutas(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    RestTemplate restTemplate = new RestTemplate() ;

    public interface AsyncResponse {
        void processFinish(List<Ruta> ruta);
    }

    public AsyncResponse delegate = null;

    public HttpGetRutas(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected  List<Ruta> doInBackground(Void... params) {
        try {
            System.out.println("Begin /GET request Restful rutas todas!");
            Log.d("WS", "doInBackground: Begin GET request Restful!");
            String getUrl = "http://facsistel.upse.edu.ec:8082/rutas";

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Ruta[] rutas = restTemplate.getForObject(getUrl, Ruta[].class);

            return Arrays.asList(rutas);
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;

    }

    @Override
    protected void onPostExecute(List<Ruta> ruta) {
        super.onPostExecute(ruta);
        delegate.processFinish(ruta);
        System.out.println("rutas " + ruta.toString());
    }

    public void ExecuteApiRest() {
        //new HttpGetRutas().execute();
    }


}
