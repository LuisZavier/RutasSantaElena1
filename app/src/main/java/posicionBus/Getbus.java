package posicionBus;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import entities.EstadoBus;
import lineas.AnimateBusPosicion;

public class Getbus extends AsyncTask <Void,Void, EstadoBus>{

    private AnimateBusPosicion animateBusPosicion;

    public Getbus(AnimateBusPosicion animateBusPosicion) {
        this.animateBusPosicion = animateBusPosicion;
    }


  /*  private MapsActivity mapsActivity;

    public Getbus(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }
*/
    RestTemplate restTemplate = new RestTemplate();

    public interface Posicionbus{
        void busPosicion(EstadoBus estadoBus);
    }

    public Posicionbus delegate = null;

    public Getbus(Posicionbus delegate) {
        this.delegate = delegate;
    }

    @Override
    protected EstadoBus doInBackground(Void... params) {

                try{
                    System.out.println("Begin /GET request Restful estado bus en el mapa!");
                    Log.d("WS", "doInBackground: Begin GET request Restful bus mapa!");

                    String getUrl = "http://facsistel.upse.edu.ec:8082/buses/ABC1234/estadoactual/";
//http://facsistel.upse.edu.ec/8082/buses/ABC1234/estadoactual/
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                    EstadoBus estadoBus = restTemplate.getForObject(getUrl, EstadoBus.class);

                    return estadoBus;

                }catch(Exception ex){
                    Log.e("", ex.getMessage());
                }

                return null;
    }


    @Override
    protected void onPostExecute(EstadoBus estadoBus) {
        super.onPostExecute(estadoBus);

        delegate.busPosicion(estadoBus);
    }
}
