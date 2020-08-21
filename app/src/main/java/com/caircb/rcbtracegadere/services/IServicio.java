package com.caircb.rcbtracegadere.services;


import com.caircb.rcbtracegadere.models.request.RequestCatalogo;
import com.caircb.rcbtracegadere.models.request.RequestCatalogoDestino;
import com.caircb.rcbtracegadere.models.request.RequestCodigoQrTransportista;
import com.caircb.rcbtracegadere.models.request.RequestDataCatalogo;
import com.caircb.rcbtracegadere.models.request.RequestFinLote;
import com.caircb.rcbtracegadere.models.request.RequestFinLotePadreHotelTask;
import com.caircb.rcbtracegadere.models.request.RequestFinRuta;
import com.caircb.rcbtracegadere.models.request.RequestFindRutas;
import com.caircb.rcbtracegadere.models.request.RequestHojaRuta;
import com.caircb.rcbtracegadere.models.request.RequestHotelPadre;
import com.caircb.rcbtracegadere.models.request.RequestInformacionModulos;
import com.caircb.rcbtracegadere.models.request.RequestInformacionTransportista;
import com.caircb.rcbtracegadere.models.request.RequestInicioLoteHotel;
import com.caircb.rcbtracegadere.models.request.RequestInicioLoteSede;
import com.caircb.rcbtracegadere.models.request.RequestLote;
import com.caircb.rcbtracegadere.models.request.RequestLotePadre;
import com.caircb.rcbtracegadere.models.request.RequestLotesHoteles;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.models.request.RequestIniciaRuta;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPlanta;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoSede;
import com.caircb.rcbtracegadere.models.request.RequestMovilizarLoteSede;
import com.caircb.rcbtracegadere.models.request.RequestNotificacion;
import com.caircb.rcbtracegadere.models.request.RequestNotificacionComercial;
import com.caircb.rcbtracegadere.models.request.RequestObtenerInicioFin;
import com.caircb.rcbtracegadere.models.request.RequestRegistarLotePadreHotel;
import com.caircb.rcbtracegadere.models.request.RequestRegisterPlantaDetalle;
import com.caircb.rcbtracegadere.models.request.RequestRegistrarDetalleSede;
import com.caircb.rcbtracegadere.models.request.RequestRegistroGenerador;
import com.caircb.rcbtracegadere.models.request.RequestRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.request.RequestVisorManifiestoPdf;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoCodigoQrTransportista;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoIdentificacion;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.models.response.DtoInformacionModulos;
import com.caircb.rcbtracegadere.models.response.DtoInformacionTransportista;
import com.caircb.rcbtracegadere.models.response.DtoInicioRuta;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.models.response.DtoLotePadreGestor;
import com.caircb.rcbtracegadere.models.response.DtoLotePadreHotel;
import com.caircb.rcbtracegadere.models.response.DtoLotesHoteles;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlanta;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;
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

    @POST("Registro/registroManifiestoDetallePlanta")
    Call<DtoInfo> registroManifiestoDetallePlanta(@Body RequestRegisterPlantaDetalle model);


    @POST("Notificacion/registrarNotificacion")
    Call<DtoInfo> registrarNotificacion(@Body RequestNotificacion model);

    @POST("Catalogo/findRutas")
    Call<List<DtoFindRutas>> traerRutas(@Body RequestFindRutas model);

    @POST("HojaRuta/obtenerListLoteContenedor")
    Call<List<DtoLote>> traerLotes(@Body RequestLote model);

    @POST("HojaRuta/obtenerListHojaRutaSede")
    Call<List<DtoManifiestoSede>> traerManifiestos(@Body RequestManifiestoSede model);

    @POST("HojaRuta/obtenerListaHojaRutaPlanta")
    Call<List<DtoManifiestoPlanta>> traerManifiestosPlanta(@Body RequestManifiestoSede model);

    @GET("HojaRuta/obtenerListaHojaRutaPlantaNoPesados")
    Call<List<DtoManifiestoPlanta>> obtenerListaHojaRutaPlantaPendientesPeso();

    @PUT("Registro/registroLoteContenedor")
    Call<DtoInfo>registrarLoteinicio (@Body RequestInicioLoteSede model);

    @PUT("Registro/registroLoteContenedor")
    Call<DtoInfo>registrarFinLote(@Body RequestFinLote model);

    @POST("HojaRuta/obtenerListLoteContenedorHotel")
    Call<List<DtoLotesHoteles>> traerLotesHoteles(@Body RequestLotesHoteles model);

    @POST("Registro/registrarLoteContenedorDetalleValor")
    Call<DtoInfo>registrarDetalleRecolectado(@Body RequestRegistrarDetalleSede model);

    @POST("HojaRuta/informacionProceso")
    Call<List<DtoInformacionModulos>> traerInformacionModulos(@Body RequestInformacionModulos model);

    @PUT("Registro/registroMovilizacionLoteContenedor")
    Call<DtoInfo>registrarmovilizacionLoteSede(@Body RequestMovilizarLoteSede model);

    @POST("HojaRuta/obtenerListManifiestoPadre")
    Call<List<DtoLotePadreGestor>> traerLotesPadre(@Body RequestLotePadre model);


    @POST("Catalogo/obtenerCatalogoDataTest")
    Call<List<DtoCatalogo>>obtenerCatalogoPlacasSede(@Body RequestDataCatalogo model);

    @PUT("Registro/registroRutaTrasladoInicioFin")
    Call<DtoInfo>registrarRuteoRecollecion(@Body RequestRuteoRecoleccion model);

    @POST("Registro/cierreManifiestoPadre")
    Call<DtoInfo> registrarRecoleccionGestores(@Body RequestRegistroGenerador model);

    @POST("Catalogo/obtenerLoteContenedorHotel")
    Call<DtoLotePadreHotel> getLotePadreHotel (@Body RequestHotelPadre model);

    @POST("Registro/registrarLoteContenedorParaHoteles")
    Call<DtoInfo> inicioFinLoteHotel (@Body RequestInicioLoteHotel model);

    @PUT("Registro/registroLoteContenedorHotel")
    Call<DtoInfo>registrarFinLotePadreHotel(@Body RequestFinLotePadreHotelTask model);

    @POST("HojaRuta/obtenerRutaInicioFin")
    Call<DtoInicioRuta>obtenerRutainicioFin(@Body RequestObtenerInicioFin model);

    @POST("Registro/registrarMovilizacionLoteContenedorHotel")
    Call<DtoInfo>registrarHotelLote(@Body RequestRegistarLotePadreHotel model);

    @POST("HojaRuta/obtenerInformacionTransportista")
    Call<DtoInformacionTransportista>informacionTransportista(@Body RequestInformacionTransportista model);

    @PUT("Registro/saveFinLoteProceso")
    Call<DtoInfo> putFinLoteHospitales(@Body RequestFinRuta model);

    @POST("Notificacion/enviarCorreoComercial")
    Call<DtoInfo>enviarCorreoComercial(@Body RequestNotificacionComercial model);

    @POST("HojaRuta/obtenerUrlNombrePdfByIdManifiesto")
    Call<DtoInfo> obtenerUrlNombrePdfByIdManifiesto(@Body RequestVisorManifiestoPdf model);

    @POST("HojaRuta/obtenerCodigoQRLoteProceso")
    Call<DtoCodigoQrTransportista> traerCodigoQrTransportista(@Body RequestCodigoQrTransportista model);
}
