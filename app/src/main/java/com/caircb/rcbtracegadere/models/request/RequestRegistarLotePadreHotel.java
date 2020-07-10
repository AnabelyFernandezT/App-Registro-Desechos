package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestRegistarLotePadreHotel {
    private Integer idLoteContenedorHotel;
    private Integer idTransportistaRecolector;
    private Date fecha;
    private Integer tipo;
    private Integer idDestinatarioFinRutaCatalogo;

    public RequestRegistarLotePadreHotel() {
    }

    public Integer getIdLoteContenedorHotel() {
        return idLoteContenedorHotel;
    }

    public void setIdLoteContenedorHotel(Integer idLoteContenedorHotel) {
        this.idLoteContenedorHotel = idLoteContenedorHotel;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getIdDestinatarioFinRutaCatalogo() {
        return idDestinatarioFinRutaCatalogo;
    }

    public void setIdDestinatarioFinRutaCatalogo(Integer idDestinatarioFinRutaCatalogo) {
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }
}
