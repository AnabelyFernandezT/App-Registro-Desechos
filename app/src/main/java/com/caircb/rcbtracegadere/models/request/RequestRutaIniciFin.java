package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestRutaIniciFin {
    private Integer idRutaInicioFin;
    private Integer idTransporteRecolector;
    private Integer idTransporteVehiculo;
    private Date fechaInicio;
    private Date fechaFin;
    private String kilometrajeInicio;
    private String kilometrajeFin;
    private Integer estado;

    public RequestRutaIniciFin(Integer idRutaInicioFin, Integer idTransporteRecolector, Integer idTransporteVehiculo, Date fechaInicio, Date fechaFin, String kilometrajeInicio, String kilometrajeFin, Integer estado) {

    }

    public Integer getIdRutaInicioFin() {
        return idRutaInicioFin;
    }

    public void setIdRutaInicioFin(Integer idRutaInicioFin) {
        this.idRutaInicioFin = idRutaInicioFin;
    }

    public Integer getIdTransporteRecolector() {
        return idTransporteRecolector;
    }

    public void setIdTransporteRecolector(Integer idTransporteRecolector) {
        this.idTransporteRecolector = idTransporteRecolector;
    }

    public Integer getIdTransporteVehiculo() {
        return idTransporteVehiculo;
    }

    public void setIdTransporteVehiculo(Integer idTransporteVehiculo) {
        this.idTransporteVehiculo = idTransporteVehiculo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getKilometrajeInicio() {
        return kilometrajeInicio;
    }

    public void setKilometrajeInicio(String kilometrajeInicio) {
        this.kilometrajeInicio = kilometrajeInicio;
    }

    public String getKilometrajeFin() {
        return kilometrajeFin;
    }

    public void setKilometrajeFin(String kilometrajeFin) {
        this.kilometrajeFin = kilometrajeFin;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
