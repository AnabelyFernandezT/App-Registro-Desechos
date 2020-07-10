package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestInicioLoteHotel {
    private Integer idLoteContenedorHotel;
    private Integer idSubRuta;
    private Date fecha;
    private Integer idDestinatarioFinRutaCatalogo;
    private Integer idTransportistaVehiculo;

    public RequestInicioLoteHotel() {
    }

    public Integer getIdLoteContenedorHotel() {
        return idLoteContenedorHotel;
    }

    public void setIdLoteContenedorHotel(Integer idLoteContenedorHotel) {
        this.idLoteContenedorHotel = idLoteContenedorHotel;
    }

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getIdDestinatarioFinRutaCatalogo() {
        return idDestinatarioFinRutaCatalogo;
    }

    public void setIdDestinatarioFinRutaCatalogo(Integer idDestinatarioFinRutaCatalogo) {
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }

    public Integer getIdTransportistaVehiculo() {
        return idTransportistaVehiculo;
    }

    public void setIdTransportistaVehiculo(Integer idTransportistaVehiculo) {
        this.idTransportistaVehiculo = idTransportistaVehiculo;
    }
}
