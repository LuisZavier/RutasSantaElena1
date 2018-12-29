package models;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import entities.Ruta;
import lineas.LineaBus;

/*******CLASE QUE ME DEVUELVE UNA RUTA DE BUS ESPECIFICA ****/

public class HttpGetLinea extends AsyncTask<String, Void, Ruta> {

   private LineaBus lineaBus;

    public HttpGetLinea(LineaBus lineaBus) {
        this.lineaBus = lineaBus;
    }

    RestTemplate restTemplate = new RestTemplate() ;

    public interface AsyncResponse2 {
        void processFinish(Ruta ruta);
    }

    public AsyncResponse2 delegate = null;

    public HttpGetLinea(AsyncResponse2 delegate){
        this.delegate = delegate;
    }

    @Override
    protected Ruta doInBackground(String... params) {
        try {
            String linea = params[0];
            System.out.println("Begin /GET request Restful linea!");
            Log.d("WS", "doInBackground: Begin GET request Restful!");

            String getUrl = "http://facsistel.upse.edu.ec:8082/rutas/"+linea+"/";

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Ruta rutaModels = restTemplate.getForObject(getUrl, Ruta.class);

            return rutaModels;
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Ruta ruta) {
        super.onPostExecute(ruta);
        System.out.println("envia puntos a clase linea " + ruta.getListasPuntos().toString());
       /* Log.i("id", String.valueOf(ruta.getId()));
        Log.i("numRuta", ruta.getNumRuta());
        Log.i("nombreCooperativa", ruta.getNombreCooperativa());
        Log.i("listasPuntos", String.valueOf(ruta.getListasPuntos()));
        Log.i("type", ruta.getType());*/
        delegate.processFinish(ruta);

    }

    public void ExecuteApiRest() {
        //new HttpGetRutas().execute();
    }


}
