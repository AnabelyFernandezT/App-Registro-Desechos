package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestRutaIniciFin {
    private String id;
    private Integer idTransporteRecolector;
    private Integer idTransporteVehiculo;
    private Date fechaDispositivo;
    private String kilometraje;
    private Integer tipo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getFechaDispositivo() {
        return fechaDispositivo;
    }

    public void setFechaDispositivo(Date fechaDispositivo) {
        this.fechaDispositivo = fechaDispositivo;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
