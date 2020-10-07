package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.request.RequestIniciaRuta;
import com.caircb.rcbtracegadere.models.request.RequestObtenerInicioFin;
import com.caircb.rcbtracegadere.models.response.DtoInicioRuta;
import com.caircb.rcbtracegadere.models.response.DtoLotePadreHotel;
import com.caircb.rcbtracegadere.models.response.DtoLotesHoteles;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarInicioRutaTask extends MyRetrofitApi implements RetrofitCallbacks {
    private RutaInicioFinEntity model;
    Date fechaInicio = AppDatabase.getDateTime();
    DtoLotePadreHotel hotel;

    public UserConsultarInicioRutaTask(Context context) {
        super(context);
    }

    public interface OnRegisterListener {
        public void onSuccessful();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute() {
        progressShow("Consultando inicio de sesion previa...");
        WebService.api().obtenerRutainicioFin(new RequestObtenerInicioFin(MySession.getIdUsuario(),new Date())).enqueue(new Callback<DtoInicioRuta>() {
            @Override
            public void onResponse(Call<DtoInicioRuta> call, Response<DtoInicioRuta> response) {
                 if(response.isSuccessful()){
                    progressHide();
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+response.body().getPlaca());
                    MyApp.getDBO().parametroDao().saveOrUpdate("estado_transporte",""+response.body().getEstado());
                    MyApp.getDBO().parametroDao().saveOrUpdate("tipoSubRuta",""+response.body().getTiposubruta());

                    MyApp.getDBO().impresoraDao().updateDisabledAllImpresoraWorked();
                    MyApp.getDBO().impresoraDao().updateDefaulImpresoraWorked(response.body().getIdImpresora());
                     if(response.body().getIdHotel()>0){
                         hoteles(response.body());
                         MyApp.getDBO().hotelLotePadreDao().saveOrUpdare(hotel,MySession.getIdUsuario());
                     }

                    if (!verificarInicioRuta()){
                        if(response.body().getEstado()){

                            MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(response.body().getIdRutaInicioFin(),
                                    MySession.getIdUsuario(),
                                    response.body().getIdSubRuta(),
                                    new Date(),
                                    null,
                                    response.body().getKilometrajeInicio(),
                                    response.body().getKilometrajeFin(),
                                    1,
                                    response.body().getTiposubruta());


                            MyApp.getDBO().parametroDao().saveOrUpdate("current_ruta",""+response.body().getIdSubRuta());
                            MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "SI");
                            MySession.setIdSubruta(response.body().getIdSubRuta());
                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(response.body().getIdSubRuta(), fechaInicio, 0, null, null, false));
                            message("Ha iniciado previamente sesion, SINCRONICE manifiestos pendientes de recolección");
                            if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                        }
                    }else {
                       // if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                        progressHide();
                    }
                }else{
                     progressHide();
                 }
            }

            @Override
            public void onFailure(Call<DtoInicioRuta> call, Throwable t) {
                progressHide();
            }
        });

    }

    private Boolean verificarInicioRuta(){
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());

        if(model!=null){
            return true;
        }else {
            return false;
        }

    }

    private DtoLotePadreHotel hoteles (DtoInicioRuta inicio){
        hotel = new DtoLotePadreHotel();
        hotel.setCodigo("");
        hotel.setEstado(1);
        hotel.setIdLoteContenedorHotel(inicio.getIdHotel());

        if(inicio.getIdHotel()>0){
            MyApp.getDBO().parametroDao().saveOrUpdate("current_hotel",""+1);
        }

        return hotel;
    }
    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;
    }
}
