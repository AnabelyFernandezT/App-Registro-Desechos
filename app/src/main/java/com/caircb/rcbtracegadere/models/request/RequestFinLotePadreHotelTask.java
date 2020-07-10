package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestFinLotePadreHotelTask {
    private Integer idLoteContenedorHotel;
    private Date fechaRegistro;
    private Integer idTransportistaRecolector;
    private Integer tipo;

    public RequestFinLotePadreHotelTask() {
    }

    public Integer getIdLoteContenedorHotel() {
        return idLoteContenedorHotel;
    }

    public void setIdLoteContenedorHotel(Integer idLoteContenedorHotel) {
        this.idLoteContenedorHotel = idLoteContenedorHotel;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
