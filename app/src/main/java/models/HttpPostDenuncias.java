package models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.rutas.santaelena.app.rutas.HeadersAuth;
import com.rutas.santaelena.app.rutas.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;

import denuncias.Notificaciones;
import entities.Reporte;

public class HttpPostDenuncias extends AsyncTask<Object,Object, Reporte> {

    private Spinner spinnerAsunto;
    private RadioButton rb;
    private String usuario;
    private String movil;
    private String asunto;
    private String numeroDisco;
    private String ubicacion;
    private Date fecha_del_incidente;
    private String linea;
    private String mensaje;
    private boolean estado;
    private Context context;

    private String username;
    private String password;

    private Notificaciones notificaciones;

    public HttpPostDenuncias(Notificaciones notificaciones) {
        this.notificaciones = notificaciones;
    }

    public interface AsynDenuncias{
        void processDenuncia(Reporte reporte);
    }
    public AsynDenuncias delegate = null;

    public HttpPostDenuncias(AsynDenuncias delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Reporte doInBackground(Object... report) {
        context = (Context) report[0];
        estado = true;
        spinnerAsunto = (Spinner) report[1];
        rb = (RadioButton) report[2];
        usuario = (String) report[3];
        movil = (String) report[4];
        asunto = (String) spinnerAsunto.getSelectedItem();
        numeroDisco = (String)report[5];
        fecha_del_incidente = Calendar.getInstance().getTime();
        linea = (String) report[6];
        mensaje = (String) report[7];

        username = (String) report[8];
        password =(String)report[9];
        estado = true;


        ubicacion = (String)report[10];

        if(rb.isChecked()== true){
            movil = " ";
            usuario = "Anonimo";
        }
        try
        {

            HeadersAuth headersAuth = new HeadersAuth();

            HttpHeaders headers = headersAuth.createHttpHeaders(username, password);


            final String url = context.getString(R.string.url_reporte);

            Reporte reporte = new Reporte();
            reporte.setMovil(movil);
            reporte.setUsuario(usuario);
            reporte.setAsunto(asunto);
            reporte.setNumeroDisco(numeroDisco);
            reporte.setUbicacion(ubicacion);
            reporte.setFecha(fecha_del_incidente);
            reporte.setLinea(linea);
            reporte.setMensaje(mensaje);
            reporte.setEstado(estado);


            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Reporte> entity = new HttpEntity<Reporte>(reporte,headers);

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Reporte> reporteUsuario = restTemplate.exchange(url, HttpMethod.POST ,entity, Reporte.class);

            return reporteUsuario.getBody();


        }catch (Exception ex) {
            Log.e("", ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Reporte reporte) {
        super.onPostExecute(reporte);
        delegate.processDenuncia(reporte);
    }
}
