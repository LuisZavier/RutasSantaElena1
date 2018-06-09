package directions;

import android.os.AsyncTask;

import com.rutas.santaelena.rutas.MapsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class TaskRequestDirectionss2 extends AsyncTask<String, Void, String> {

    private MapsActivity mapsActivity;

   public TaskRequestDirectionss2(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;   //es necesario para prueba actyvity
    }

    public TaskRequestDirectionss2() {

    }


    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @Override
    protected String doInBackground(String... strings) {
        String responseString = "";
        try {
            responseString = requestDirection(strings[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  responseString;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Parse json here
        TaskParser taskParser = new TaskParser();
        taskParser.execute(s);
    }


    public interface AsyncRespDirections {
        void processFinish2(List puntos);
    }
    public AsyncRespDirections delegate = null;

    public TaskRequestDirectionss2(AsyncRespDirections delegate){
        this.delegate = delegate;
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);

           delegate.processFinish2(lists);

        }

    }

}
