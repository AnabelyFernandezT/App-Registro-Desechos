package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestDataCatalogo {
    private Integer tipo;
    private String data;
    private Date fecha;
    private String dataAuxi;

    public RequestDataCatalogo(Integer tipo, String data, Date fecha, String dataAuxi) {
        this.tipo = tipo;
        this.data = data;
        this.fecha = fecha;
        this.dataAuxi = dataAuxi;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDataAuxi() {
        return dataAuxi;
    }

    public void setDataAuxi(String dataAuxi) {
        this.dataAuxi = dataAuxi;
    }
}
