package interfaceClass;

import java.util.List;

import entities.RutaModel;

/**
 * Created by Javier on 23/05/2018.
 */

public interface AsyncResponse {

    void processFinish(List<RutaModel> rutaModel);
}

