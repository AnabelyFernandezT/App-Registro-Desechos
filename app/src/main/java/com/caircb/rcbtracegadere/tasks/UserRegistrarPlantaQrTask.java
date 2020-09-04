package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoNovedadFrecuente;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPlanta;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoQrPlanta;
import com.caircb.rcbtracegadere.models.request.RequestNovedadFoto;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlantaObservacion;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarPlantaQrTask extends MyRetrofitApi implements RetrofitCallbacks {

    private RecepcionQrPlantaEntity recepcionQrPlantaEntity;
    private List<DtoFile> listaFileDefauld;
    DtoFile firmaPlanta;
    String path = "planta";
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
    UserUploadFileTask userUploadFileTask;
    String observacion;


    public interface OnRegisterListener {
        public void onSuccessful();
    }

    private OnRegisterListener mOnRegisterListener;

    public UserRegistrarPlantaQrTask(Context context, RecepcionQrPlantaEntity recepcionQrPlantaEntity, String observacion) {
        super(context);
        this.recepcionQrPlantaEntity = recepcionQrPlantaEntity;
        this.observacion = observacion;
    }

    @Override
    public void execute() {
        progressShow("Registrando recoleccion...");

        String[] array = recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
        int idManifiestoPrimero = Integer.parseInt(array[0]);

        if (array.length > 1) {

            for (String s : array) {
                int idManifiestoConsulta = Integer.parseInt(s.replace(" ", ""));
                listaFileDefauld = new ArrayList<>();
                firmaPlanta = MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idManifiestoConsulta, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, MyConstant.STATUS_RECEPCION_PLANTA);
                if (firmaPlanta != null && !firmaPlanta.isSincronizado())
                    listaFileDefauld.add(firmaPlanta);

                path = path + "/" + getPath() + "/" + idManifiestoConsulta;
                userUploadFileTask = new UserUploadFileTask(getActivity(), path);
                userUploadFileTask.setOnUploadFileListener(new UserUploadFileTask.OnUploadFileListener() {
                    @Override
                    public void onSuccessful() {

                    }

                    @Override
                    public void onFailure(String message) {
                        progressHide();
                        message(message);
                    }
                });
                userUploadFileTask.uploadRecoleccionPlanta(listaFileDefauld, idManifiestoConsulta);
            }
            register();
        } else {
            listaFileDefauld = new ArrayList<>();
            firmaPlanta = MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idManifiestoPrimero, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, MyConstant.STATUS_RECEPCION_PLANTA);
            if (firmaPlanta != null && !firmaPlanta.isSincronizado())
                listaFileDefauld.add(firmaPlanta);

            path = path + "/" + getPath() + "/" + idManifiestoPrimero;
            userUploadFileTask = new UserUploadFileTask(getActivity(), path);
            userUploadFileTask.setOnUploadFileListener(new UserUploadFileTask.OnUploadFileListener() {
                @Override
                public void onSuccessful() {
                    register();
                }

                @Override
                public void onFailure(String message) {
                    progressHide();
                    message(message);
                }
            });
            userUploadFileTask.uploadRecoleccionPlanta(listaFileDefauld, idManifiestoPrimero);
        }
    }

    private void register() {

        final RequestManifiestoQrPlanta request = createRequestManifiestoQrPlanta();
        Gson g = new Gson();
        String f = g.toJson(request);
        System.out.println(f);

        if (request != null) {
            WebService.api().registroManifiestoQrPlanta(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getExito()) {
                            String[] array = recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
                            int idManifiestoPrimero = Integer.parseInt(array[0]);
                            if (array.length > 1) {
                                for (String s : array) {
                                    int idManifiestoConsulta = Integer.parseInt(s.replace(" ", ""));
                                    MyApp.getDBO().manifiestoDao().updateManifiestoToRecolectadoPlanta(idManifiestoConsulta);
                                }
                            } else {
                                MyApp.getDBO().manifiestoDao().updateManifiestoToRecolectadoPlanta(idManifiestoPrimero);
                            }
                            finalizar();
                            progressHide();
                        } else {
                            message(response.body().getMensaje());
                        }
                    } else {
                        message(response.body().getMensaje());
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                }
            });
        }
    }

    private RequestManifiestoQrPlanta createRequestManifiestoQrPlanta() {
        RequestManifiestoQrPlanta rq = null;
        rq = new RequestManifiestoQrPlanta();
        rq.setNumeroManifiesto(recepcionQrPlantaEntity.getNumerosManifiesto());
        rq.setUrlFirmaPlanta(firmaPlanta != null ? (path + "/" + firmaPlanta.getUrl()) : "");
        rq.setFechaRecepcionPlanta(AppDatabase.getDateTime().toString());
        rq.setIdPlantaRecolector(MySession.getIdUsuario());
        rq.setObservacion(observacion);
        rq.setTipoRecoleccion(3);
        rq.setNovedadFrecuentePlanta(createRequestNovedadFrecuente());
        rq.setFlagPlantaSede(1);
        rq.setIdLoteContenedor(0);
        rq.setIdDestinatarioFinRutaCatalogo(0);
        return rq;
    }


    private List<RequestManifiestoNovedadFrecuente> createRequestNovedadFrecuente() {
        String[] array = recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
        int idManifiestoPrimero = Integer.parseInt(array[0]);
        List<RequestManifiestoNovedadFrecuente> resp = new ArrayList<>();
        List<Long> listaFotoNovedadFrecuente = MyApp.getDBO().manifiestoFileDao().consultarFotografiasUploadQr(idManifiestoPrimero, ManifiestoFileDao.FOTO_FOTO_ADICIONAL_PLANTA);

        if (listaFotoNovedadFrecuente != null && listaFotoNovedadFrecuente.size() > 0) {
            resp.add(new RequestManifiestoNovedadFrecuente(
                    -1,
                    createFotografia(-1, ManifiestoFileDao.FOTO_FOTO_ADICIONAL_PLANTA),
                    path + "/" + "novedadfrecuente"
            ));
        }

        return resp;
    }

    private List<RequestNovedadFoto> createFotografia(Integer idCatalogo, Integer tipo) {
        String[] array = recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
        int idManifiestoPrimero = Integer.parseInt(array[0]);
        List<RequestNovedadFoto> lista = MyApp.getDBO().manifiestoFileDao().consultarFotografias(idManifiestoPrimero, idCatalogo, tipo);
        return lista;
    }


    private void finalizar() {
        if (mOnRegisterListener != null) mOnRegisterListener.onSuccessful();
    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l) {
        mOnRegisterListener = l;

    }

    private String getPath() {
        return simpleDate.format(new Date());
    }

}
