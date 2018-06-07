package notificaciones;


/**
 * Created by Javier on 29/05/2018.
 */

public class Message {
    private String id;

    private String asunto;

    private String mensaje;



    public Message() {

    }

    public Message(String id, String subject, String text) {
        super();
        this.id = id;
        this.asunto = subject;
        this.mensaje = text;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }


    @Override
    public String toString() {
        return "Message [id=" + id + ", asunto=" + asunto + ", mensaje=" + mensaje + "]";
    }
}
