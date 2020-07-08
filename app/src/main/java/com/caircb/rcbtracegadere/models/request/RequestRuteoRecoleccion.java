package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestRuteoRecoleccion {

    private Integer idSubRuta;
    private Date fechaInicioRuta;
    private Integer puntoPartida;
    private Integer puntoLlegada;
    private Date fechaLlegadaRuta;
    private Integer tipo;

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }

    public Date getFechaInicioRuta() {
        return fechaInicioRuta;
    }

    public void setFechaInicioRuta(Date fechaInicioRuta) {
        this.fechaInicioRuta = fechaInicioRuta;
    }

    public Integer getPuntoPartida() {
        return puntoPartida;
    }

    public void setPuntoPartida(Integer puntoPartida) {
        this.puntoPartida = puntoPartida;
    }

    public Integer getPuntoLlegada() {
        return puntoLlegada;
    }

    public void setPuntoLlegada(Integer puntoLlegada) {
        this.puntoLlegada = puntoLlegada;
    }

    public Date getFechaLlegadaRuta() {
        return fechaLlegadaRuta;
    }

    public void setFechaLlegadaRuta(Date fechaLlegadaRuta) {
        this.fechaLlegadaRuta = fechaLlegadaRuta;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
