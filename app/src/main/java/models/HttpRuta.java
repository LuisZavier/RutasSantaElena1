package models;

import android.os.AsyncTask;
import android.util.Log;

import com.rutas.santaelena.rutas.MapsActivity;
import com.rutas.santaelena.rutas.PruebaActivity;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import entities.RutaModel;

/*CLASE QUE ME DEVUELVE TODAS LAS RUTAS EN UNA LISTA DE TIPO RUTA MODEL*/

public class HttpRuta extends AsyncTask<Void, Void, List<RutaModel>> {

    private PruebaActivity pruebaActivity;

    public HttpRuta(PruebaActivity pruebaActivity) {
        this.pruebaActivity = pruebaActivity;   //es necesario para prueba actyvity
    }

    private MapsActivity mapsActivity;

    public HttpRuta(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    RestTemplate restTemplate = new RestTemplate() ;

    public interface AsyncResponse {
        void processFinish(List<RutaModel> rutaModel);
    }

    public AsyncResponse delegate = null;

    public HttpRuta(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected  List<RutaModel> doInBackground(Void... params) {
        try {
            System.out.println("Begin /GET request Restful!");
            Log.d("WS", "doInBackground: Begin GET request Restful!");
            String getUrl = "http://192.168.101.1:8082/rutas/";
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            List<RutaModel> rutaModels = restTemplate.getForObject(getUrl, List.class);

            return rutaModels;
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;

    }

    @Override
    protected void onPostExecute(List<RutaModel> rutaModel) {
        super.onPostExecute(rutaModel);
        delegate.processFinish(rutaModel);
    }

    public void ExecuteApiRest() {
        //new HttpRuta().execute();
    }


}
