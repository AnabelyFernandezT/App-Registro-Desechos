package com.caircb.rcbtracegadere.models.request;

public class RequestHojaRuta {
    private String fecha;
    private Integer idVehiculo;
    private Integer idSubRuta;

    public RequestHojaRuta(String fecha, Integer idVehiculo, Integer IdRuta) {
        this.fecha = fecha;
        this.idVehiculo = idVehiculo;
        this.idSubRuta = IdRuta;
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

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }
}
