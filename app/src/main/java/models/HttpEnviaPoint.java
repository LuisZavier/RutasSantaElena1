package models;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.rutas.santaelena.rutas.MapsActivity;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import entities.Parada;

public class HttpEnviaPoint extends AsyncTask<LatLng,Void, List<Parada>> {


private MapsActivity mapsActivity;

    public HttpEnviaPoint(MapsActivity mapsActivity) {
    this.mapsActivity = mapsActivity;
    }

    RestTemplate restTemplate = new RestTemplate();

    public  interface  AsynParadas {
        void paradas(List<Parada> paradas);
    }

    public AsynParadas delegate = null;

    public HttpEnviaPoint(AsynParadas delegate){
        this.delegate = delegate;
    }

        @Override
        protected List<Parada> doInBackground(LatLng... params) {

            try {

                final String url = "http://facsistel.upse.edu.ec:8082/paradasCercanasRadio/70";
                LatLng loc = params[0]; //obtengo latitid y longitud
                HashMap<String, String> hmap = new LinkedHashMap<>();
                hmap.put("y",String.valueOf(loc.longitude));
                hmap.put("x",String.valueOf(loc.latitude));

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                List<Parada> paradas = restTemplate.postForObject(url, hmap, List.class);

                return paradas;
            }catch (Exception ex) {
                Log.e("", ex.getMessage());
            }
            return null;
        }

    @Override
    protected void onPostExecute(List<Parada> paradas) {
        super.onPostExecute(paradas);
        delegate.paradas(paradas);
    }
}







