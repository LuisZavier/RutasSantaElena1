package models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rutas.santaelena.app.rutas.GlobalClass;
import com.rutas.santaelena.app.rutas.HeadersAuth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import entities.SegUsuario;
import sesion.RegistraUsuario;

public class HttpPostUser extends AsyncTask<Object,Object, SegUsuario>{
    private String email;
    private String password;
    private String perfil;
    private boolean estado;
    private Context context;

    /*********VARIABLES GLOBALES PARA EL USUARIO MOVIL ******************/
    String userMovil = "";
    String passMovil = "";

    private RegistraUsuario registraUsuario;

    public HttpPostUser(RegistraUsuario registraUsuario) {
        this.registraUsuario = registraUsuario;
    }

    public interface AsynUser{
        void processUser(SegUsuario segUsuario);
    }
    public AsynUser delegate=null;

    public HttpPostUser(AsynUser delegate) {
        this.delegate = delegate;
    }

    @Override
    protected SegUsuario doInBackground(Object... segUser) {

        email = (String) segUser[0];
        password = (String) segUser[1];
        context = (Context) segUser[2];

        perfil = "USER_MOVIL";
        estado = true;

        GlobalClass globalClass = (GlobalClass)  context.getApplicationContext();//Accedo a mis variables globales

        userMovil = globalClass.getUserMovil();
        passMovil = globalClass.getPassMovil();

        try {

            HeadersAuth headersAuth = new HeadersAuth();

            HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

            final String url = "http://192.168.101.1:8082/usuarios/";

            SegUsuario segUsuario = new SegUsuario();

            segUsuario.setUsuario(email);
            segUsuario.setClave(password);
            segUsuario.setPerfil(perfil);
            segUsuario.setEstado(estado);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<SegUsuario> entity = new HttpEntity<SegUsuario>(segUsuario,headers);

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<SegUsuario> segUsuarioResponseEntity = restTemplate.exchange(url, HttpMethod.POST,entity,SegUsuario.class);


            return segUsuarioResponseEntity.getBody();


        }catch (Exception ex) {
            Log.e("", ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(SegUsuario segUsuario) {
        super.onPostExecute(segUsuario);
        delegate.processUser(segUsuario);

    }
}
