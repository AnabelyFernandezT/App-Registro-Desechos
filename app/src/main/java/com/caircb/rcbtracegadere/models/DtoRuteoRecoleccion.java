package com.caircb.rcbtracegadere.models;

import java.util.Date;

public class DtoRuteoRecoleccion {

    private Integer idSubRuta;

    private Date fechaInicioRuta;

    private Integer puntoPartida;

    private Integer puntoLlegada;

    private Date fechaLlegadaRuta;

    private boolean estado;

    public DtoRuteoRecoleccion() {
    }

    public DtoRuteoRecoleccion(Integer idSubRuta, Date fechaInicioRuta, Integer puntoPartida, Integer puntoLlegada, Date fechaLlegadaRuta, boolean estado) {
        this.idSubRuta = idSubRuta;
        this.fechaInicioRuta = fechaInicioRuta;
        this.puntoPartida = puntoPartida;
        this.puntoLlegada = puntoLlegada;
        this.fechaLlegadaRuta = fechaLlegadaRuta;
        this.estado = estado;
    }

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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
