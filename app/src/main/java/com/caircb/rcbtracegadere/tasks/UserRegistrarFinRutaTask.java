package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.dao.ParametroDao;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestFinRuta;
import com.caircb.rcbtracegadere.models.request.RequestIniciaRuta;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRegistrarFinRutaTask extends MyRetrofitApi implements RetrofitCallbacks {


    public interface OnIniciaRutaListener {
        public void onSuccessful();
        public void onFailure();
    }

    private OnIniciaRutaListener mOnIniciaRutaListener;
    private Long idRegistro;
    private RutaInicioFinEntity model;
    private ParametroEntity parametro;
    Integer idLote;


    public UserRegistrarFinRutaTask(Context context, long idRegistro) {
        super(context);
        this.idRegistro = idRegistro;
    }

    @Override
    public void execute() {
        register();
    }

    private void  register(){
        progressShow("Sincronizando con servidor el final de ruta");
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(idRegistro.intValue());
        RequestFinRuta requestFinRuta = createRequestFin();
        Gson g = new Gson();
        String f = g.toJson(requestFinRuta);
        System.out.println(f);
        if(requestFinRuta!=null){
            WebService.api().putFin(requestFinRuta).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        MyApp.getDBO().rutaInicioFinDao().actualizarFinRutaToSincronizado(
                                idRegistro.intValue());
                        MyApp.getDBO().rutaInicioFinDao().eliminarInicioFin();
                        MyApp.getDBO().parametroDao().eliminarLotes("manifiesto_lote");
                        progressHide();
                        if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onSuccessful();
                    }else{
                        progressHide();
                        if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onFailure();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                    if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onFailure();
                }
            });
            progressHide();
        }else {
            message("Request no encontrado");
            progressHide();
        }
    }

    private RequestFinRuta createRequestFin(){
        RequestFinRuta rq=null;
        Integer idTransportistaRecolector=MySession.getIdUsuario();
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        Integer idDestino = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());
        parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("manifiesto_lote");
        Integer idInsumo = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("idInsumo")==null?0:Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("idInsumo"));
        if(parametro!=null){idLote =Integer.parseInt(parametro.getValor());}else{idLote=0;}

        if (model!=null){
            rq =new RequestFinRuta();
            rq.setId(model.getIdRutaInicioFin());
            rq.setIdSubRuta(model.getIdSubRuta());
            rq.setFechaDispositivo(model.getFechaFin());
            rq.setKilometraje(model.getKilometrajeFin());
            rq.setTipo(2);
            rq.setIdDestinatarioFinRutaCatalogo(idDestino);
            rq.setIdTransportistaRecolector(idTransportistaRecolector);
            rq.setIdLote(idLote);
            rq.setIdInsumo(idInsumo);
        }
        return rq;
    }

    public void setOnIniciaRutaListener(@NonNull OnIniciaRutaListener l){
        mOnIniciaRutaListener =l;
    }


}
