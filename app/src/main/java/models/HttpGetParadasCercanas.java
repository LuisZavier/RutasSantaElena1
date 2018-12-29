package models;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.rutas.santaelena.rutas.MapsActivity;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import entities.Parada;
import placesNearPoint.SearchPlacesNear;

/* CLASE QUE ME DEVUELVE UNA LISTA DE LOS PARADEROS CERCANOS A MI PUNTO ORIGEN EN EL MAPA*/

public class HttpGetParadasCercanas extends AsyncTask<Object,Void, List<Parada>> {

    private MapsActivity mapsActivity;

    public HttpGetParadasCercanas(MapsActivity mapsActivity) {
    this.mapsActivity = mapsActivity;
    }

    private SearchPlacesNear searchPlacesNear;

    public HttpGetParadasCercanas(SearchPlacesNear searchPlacesNear) {
        this.searchPlacesNear = searchPlacesNear;
    }

    RestTemplate restTemplate = new RestTemplate();

    public  interface  AsynParadas {
        void paradas(List<Parada> paradas);
    }

    public AsynParadas delegate = null;

    public HttpGetParadasCercanas(AsynParadas delegate){
        this.delegate = delegate;
    }


        @Override
        protected List<Parada> doInBackground(Object... params) {

            try {
                System.out.println("Begin /GET request Restful paradas cercanas!");
                Log.d("WS", "doInBackground: Begin GET request paradas cercanas!");
                //Todo hacer parametro de radio configurable.
                String linea = (String) params[0];
                int radio =(int) params[1];
                double r = (double) radio;

                LatLng loc = (LatLng) params[2]; //obtengo latitid y longitud a enviar
               /* HashMap<String, String> hmap = new LinkedHashMap<>();
                hmap.put("x",String.valueOf(loc.latitude));
                hmap.put("y",String.valueOf(loc.longitude));
                */
                final String url = "http://facsistel.upse.edu.ec:8082/paradas/"+linea+"/radio/"+r+"/point/x="+loc.latitude+",y="+loc.longitude;
//http://facsistel.upse.edu.ec:8082/buses/ABC1234/estadoActual
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                Parada[] paradas = restTemplate.getForObject(url,Parada[].class);

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







