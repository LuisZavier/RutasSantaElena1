package models;

import android.os.AsyncTask;
import android.util.Log;

import com.rutas.santaelena.rutas.MapsActivity;
import com.rutas.santaelena.rutas.PruebaActivity;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import entities.RutaModel;

public class HttpRuta2 extends AsyncTask<Void, Void, RutaModel> {

    private PruebaActivity pruebaActivity;

    public HttpRuta2(PruebaActivity pruebaActivity) {
        this.pruebaActivity = pruebaActivity;   //es necesario para prueba actyvity
    }

    private MapsActivity mapsActivity;

    public HttpRuta2(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    RestTemplate restTemplate = new RestTemplate() ;

    public interface AsyncResponse2 {
        void processFinish(RutaModel rutaModel);
    }

    public AsyncResponse2 delegate = null;

    public HttpRuta2(AsyncResponse2 delegate){
        this.delegate = delegate;
    }

    // public HttpRuta() {
    // restTemplate = new RestTemplate();
    //}

    //public RutaModel getEntity(){
    //metodo GET para recuperar la ruta y dibujarla en el mapa
    // public class HttpRuta extends AsyncTask<Void, Void, RutaModel> {
    @Override
    protected  RutaModel doInBackground(Void... params) {
        try {
            System.out.println("Begin /GET request Restful!");
            Log.d("WS", "doInBackground: Begin GET request Restful!");
            String getUrl = "http://192.168.101.1:8082/rutas/linea/7";
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            RutaModel rutaModels = restTemplate.getForObject(getUrl, RutaModel.class);

            return rutaModels;
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;

    }

    @Override
    protected void onPostExecute(RutaModel rutaModel) {
        super.onPostExecute(rutaModel);
        Log.i("id", String.valueOf(rutaModel.getId()));
        Log.i("numRuta", rutaModel.getNumRuta());
        Log.i("nombreCooperativa", rutaModel.getNombreCooperativa());
        Log.i("listasPuntos", String.valueOf(rutaModel.getListasPuntos()));
        Log.i("type", rutaModel.getType());
        delegate.processFinish(rutaModel);
    }

    public void ExecuteApiRest() {
        //new HttpRuta().execute();
    }


}
