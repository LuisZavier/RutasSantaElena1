package models;

import android.os.AsyncTask;
import android.util.Log;

import com.rutas.santaelena.rutas.MapsActivity;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import entities.RutaModel;

public class HttpReqtask extends AsyncTask<Void, Void, RutaModel> {

   //private PruebaActivity pruebaActivity;

 /* public HttpReqtask(PruebaActivity pruebaActivity) {
       this.pruebaActivity = pruebaActivity;   //es necesario para prueba actyvity
    }*/

      private MapsActivity mapsActivity;

    public HttpReqtask(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    RestTemplate restTemplate = new RestTemplate() ;

    public interface AsyncResponse {
        void processFinish(RutaModel rutaModel);
    }

    public AsyncResponse delegate = null;

    public HttpReqtask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    // public HttpReqtask() {
       // restTemplate = new RestTemplate();
    //}


    //public RutaModel getEntity(){
    //metodo GET para recuperar la ruta y dibujarla en el mapa
    // public class HttpReqtask extends AsyncTask<Void, Void, RutaModel> {
    @Override
    protected RutaModel doInBackground(Void... params) {
        try {
            System.out.println("Begin /GET request Restful!");
            Log.d("WS", "doInBackground: Begin GET request Restful!");
            String getUrl = "http://192.168.101.11:8082/rutas/linea/7/";

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            RutaModel rutaModels = restTemplate.getForObject(getUrl, RutaModel.class);
         //   ResponseEntity<RutaModel> response= restTemplate.getForEntity(getUrl,RutaModel.class);

          //  System.out.println(rutaModels);

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
      //new HttpReqtask().execute();
    }
  

}
