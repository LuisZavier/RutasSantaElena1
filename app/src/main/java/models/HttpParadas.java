package models;

import android.os.AsyncTask;
import android.util.Log;

import com.rutas.santaelena.rutas.MapsActivity;
import com.rutas.santaelena.rutas.PruebaActivity;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import entities.Parada;

public class HttpParadas extends AsyncTask<Void, Void, List<Parada>> {

    private PruebaActivity pruebaActivity;

    public HttpParadas(PruebaActivity pruebaActivity){
        this.pruebaActivity = pruebaActivity ;

    }

    private MapsActivity mapsActivity;

    public HttpParadas(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    RestTemplate restTemplate = new RestTemplate();

    public  interface  AsynParadas {
        void paradas(List<Parada> paradas);
    }

    public AsynParadas delegate = null;

    public HttpParadas(AsynParadas delegate){
        this.delegate = delegate;
    }

    @Override
    protected List<Parada> doInBackground(Void... params) {
        try {
            System.out.println("Begin /GET request Restful!");
            Log.d("WS", "doInBackground: Begin GET request Restful Paraderos!");
            String getUrl = "http://facsistel.upse.edu.ec:8082/paradas/";

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            List<Parada> paradas = restTemplate.getForObject(getUrl,List.class);

            return  paradas;
        }catch (Exception ex){
            Log.e("",ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Parada> paradas) {
        super.onPostExecute(paradas);

        delegate.paradas(paradas);

    }
}
