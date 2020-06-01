package com.caircb.rcbtracegadere.models.request;

public class RequestHojaRuta {
    private String fecha;
    private Integer idVehiculo;

    public RequestHojaRuta(String fecha, Integer idVehiculo) {
        this.fecha = fecha;
        this.idVehiculo = idVehiculo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }
}
