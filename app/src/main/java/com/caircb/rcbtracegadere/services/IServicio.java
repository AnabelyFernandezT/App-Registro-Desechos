package com.caircb.rcbtracegadere.services;


import com.caircb.rcbtracegadere.models.request.RequestCatalogo;
import com.caircb.rcbtracegadere.models.request.RequestHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IServicio {

    @POST("Catalogo/obtenerCatalogo")
    Call<List<DtoCatalogo>> getCatalogos(@Body RequestCatalogo model);

    @POST("HojaRuta/obtenerHojaRuta")
    Call<List<DtoManifiesto>> getHojaRuta(@Body RequestHojaRuta model);



   /* @PUT("Registro/saveInicioFinRuta")
    Call<List<DtoRutaInicioFin>> putInicioFin(@Path ("id") int id,@Body RequestRutaIniciFin model);

    @PATCH("Registro/saveInicioFinRuta")
    Call<List<DtoRutaInicioFin>> patch(@Path ("id") int id ,@Body RequestRutaIniciFin model);*/
}
