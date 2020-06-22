package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestNotificacion;

public class UserNotificacionTask extends MyRetrofitApi implements RetrofitCallbacks {
    Integer idAppManifiesto;
    String mensaje;

    public UserNotificacionTask(Context context,
                                Integer idAppManifiesto,
                                String mensaje) {
        super(context);
        this.idAppManifiesto = idAppManifiesto;
        this.mensaje = mensaje;
    }

    @Override
    public void execute() {

        register();
    }

    private void register() {
        RequestNotificacion request = createRequestNotificador();
    }

    private RequestNotificacion createRequestNotificador() {
        RequestNotificacion rq =null;

        return rq;
    }


}
