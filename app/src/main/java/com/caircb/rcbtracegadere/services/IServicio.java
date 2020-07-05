package com.caircb.rcbtracegadere.services;


import com.caircb.rcbtracegadere.models.request.RequestCatalogo;
import com.caircb.rcbtracegadere.models.request.RequestCatalogoDestino;
import com.caircb.rcbtracegadere.models.request.RequestFinRuta;
import com.caircb.rcbtracegadere.models.request.RequestFindRutas;
import com.caircb.rcbtracegadere.models.request.RequestHojaRuta;
import com.caircb.rcbtracegadere.models.request.RequestLote;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.models.request.RequestIniciaRuta;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPlanta;
import com.caircb.rcbtracegadere.models.request.RequestNotificacion;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoIdentificacion;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoPaquetes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IServicio {

    @POST("Catalogo/obtenerCatalogo")
    Call<List<DtoCatalogo>> getCatalogos(@Body RequestCatalogo model);

    @POST("Catalogo/obtenerCatalogo")
    Call<List<DtoCatalogo>> getCatalogoE(@Body RequestCatalogoDestino model);

    @POST("HojaRuta/obtenerHojaRuta")
    Call<List<DtoManifiesto>> getHojaRuta(@Body RequestHojaRuta model);

    @GET("Catalogo/ObtenerCatalogoPaquetes")
    Call<List<DtoPaquetes>> getPaquetes();

    @GET ("Consultar/cedula")
    Call<DtoIdentificacion> getIdentificacion(@Query("c") String identificacion);

    @PUT("Registro/saveHojaRuta")
    Call<DtoInfo> registrarRecoleccion(@Body RequestManifiesto model);


   @PUT("Registro/saveInicioFinRuta")
    Call<DtoInfo> putInicioFin(@Body RequestIniciaRuta model);

    @PUT("Registro/saveInicioFinRuta")
    Call<DtoInfo> putFin(@Body RequestFinRuta model);

    @GET ("HojaRuta/obtenerListHojaRutaPlanta")
    Call<List<DtoManifiesto>> getHojaRutaPlanta();

    @PUT("Registro/registroManifiestoPlanta")
    Call<DtoInfo> registrarPlanta(@Body RequestManifiestoPlanta model);

    @POST("Notificacion/registrarNotificacion")
    Call<DtoInfo> registrarNotificacion(@Body RequestNotificacion model);

    @POST("Catalogo/findRutas")
    Call<List<DtoFindRutas>> traerRutas(@Body RequestFindRutas model);

    @POST("HojaRuta/obtenerListLoteContenedor")
    Call<List<DtoLote>> traerLotes(@Body RequestLote model);

}
