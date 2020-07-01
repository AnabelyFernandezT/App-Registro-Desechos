package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestHojaRuta {
    private Date fecha;
    private Integer idVehiculo;
    private Integer idSubRuta;

    public  RequestHojaRuta(Date fecha, Integer idVehiculo, Integer IdRuta) {
        this.fecha = fecha;
        this.idVehiculo = idVehiculo;
        this.idSubRuta = IdRuta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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
