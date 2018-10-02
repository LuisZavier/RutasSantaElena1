package models;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import entities.RutaModel;
import lineas.LineasBuses;

/*******CLASE QUE ME DEVUELVE UNA RUTA DE BUS ESPECIFICA ****/

public class HttpLineas extends AsyncTask<String, Void, RutaModel> {

   private LineasBuses lineasBuses ;

    public HttpLineas(LineasBuses lineasBuses) {
        this.lineasBuses = lineasBuses;
    }

    RestTemplate restTemplate = new RestTemplate() ;

    public interface AsyncResponse2 {
        void processFinish(RutaModel rutaModel);
    }

    public AsyncResponse2 delegate = null;

    public HttpLineas(AsyncResponse2 delegate){
        this.delegate = delegate;
    }

    @Override
    protected RutaModel doInBackground(String... params) {
        try {
            String linea = params[0];
            System.out.println("Begin /GET request Restful!");
            Log.d("WS", "doInBackground: Begin GET request Restful!");
            String getUrl = "http://192.168.101.1:8082/rutas/linea/"+linea+"/";
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            RutaModel rutaModels = restTemplate.getForObject(getUrl,RutaModel.class);

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
