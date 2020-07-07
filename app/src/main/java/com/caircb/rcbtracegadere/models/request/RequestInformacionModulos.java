package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestInformacionModulos {
    private Date fecha;
    private String placa;
    private Integer idTransportista;
    private Integer tipoProceso;

    public RequestInformacionModulos(Date fecha, String placa,Integer idTransportista, Integer tipoProceso){
        this.fecha = fecha;
        this.placa = placa;
        this.idTransportista = idTransportista;
        this.tipoProceso = tipoProceso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    public Integer getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(Integer tipoProceso) {
        this.tipoProceso = tipoProceso;
    }
}
