package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestHojaRuta {
    private Date fecha;
    private Date fechaSincronizacion;
    private Integer idVehiculo;
    private Integer idSubRuta;
    private Integer idManifiesto;
    private Integer lote;

    public RequestHojaRuta(Date fecha, Date fechaSincronizacion, Integer idVehiculo, Integer idSubRuta) {
        this.fecha = fecha;
        this.fechaSincronizacion = fechaSincronizacion;
        this.idVehiculo = idVehiculo;
        this.idSubRuta = idSubRuta;
    }

    public RequestHojaRuta(Date fecha, Date fechaSincronizacion, Integer idVehiculo, Integer idSubRuta, Integer idManifiesto, Integer lote) {
        this.fecha = fecha;
        this.fechaSincronizacion = fechaSincronizacion;
        this.idVehiculo = idVehiculo;
        this.idSubRuta = idSubRuta;
        this.idManifiesto = idManifiesto;
        this.lote = lote;
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

    public Date getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(Date fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }
}
