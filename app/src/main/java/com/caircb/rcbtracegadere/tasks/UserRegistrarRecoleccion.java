package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;

public class UserRegistrarRecoleccion extends MyRetrofitApi implements RetrofitCallbacks {
    Integer idAppManifiesto;

    public UserRegistrarRecoleccion(Context context,
                                    Integer idAppManifiesto) {
        super(context);
        this.idAppManifiesto=idAppManifiesto;
    }

    @Override
    public void execute() {
        //obtener datos para realizar el registro...
        ManifiestoEntity m = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        if(m!=null){
            RequestManifiesto rq = new RequestManifiesto();
            rq.setIdAppManifiesto(idAppManifiesto);
            rq.setNumeroManifiesto(m.getNumeroManifiesto());
            rq.setNumeroManifiestoCliente(m.getNumManifiestoCliente());
            rq.setUrlFirmaTransportista(m.getTransportistaFirmaUrl());
            //rq.setResponsableEntregaIdentificacion(m.getTecnicoIdentificacion());
            rq.setUrlFirmaResponsableEntrega(m.getTecnicoFirmaUrl());
            rq.setUsuarioResponsable(MySession.getIdUsuario());
            rq.setNovedadReportadaCliente(m.getNovedadEncontrada());
            rq.setUrlAudioNovedadCliente(m.getNovedadAudio());
            rq.setFechaRecoleccion(m.getFechaManifiesto());

            //obtner detalles


        }

    }

    private void registrar(){

    }
}
