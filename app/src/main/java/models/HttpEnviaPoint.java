package models;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.rutas.santaelena.rutas.MapsActivity;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import entities.Parada;

/* CLASE QUE ME DEVUELVE UNA LISTA DE LOS PARADEROS CERCANOS A MI PUNTO ORIGEN EN EL MAPA*/

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
                //Todo hacer parametro de radio configurable.
                final String url = "http://facsistel.upse.edu.ec:8082/paradasCercanasRadio/70/";
               // final String url = context.getString(R.string.url_paradas_cercanas);
                LatLng loc = params[0]; //obtengo latitid y longitud a enviar
                HashMap<String, String> hmap = new LinkedHashMap<>();
                hmap.put("x",String.valueOf(loc.latitude));
                hmap.put("y",String.valueOf(loc.longitude));

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                //FIXME Creo que debe ser una peticion GET... Por que es POST? Revisar, esto... puede que sea error de Davids, cuando el cambie cambioamos aqui.
                //List<Parada> paradas = restTemplate.postForObject(url, hmap,  List.class);
                Parada[] paradas = restTemplate.postForObject(url,hmap,Parada[].class);
                return Arrays.asList(paradas);

            }catch (Exception ex) {
                Log.e("", ex.getMessage());
            }
            return null;
        }

    @Override
    protected void onPostExecute( List<Parada> paradas) {
        super.onPostExecute(paradas);
        delegate.paradas(paradas);
    }
}