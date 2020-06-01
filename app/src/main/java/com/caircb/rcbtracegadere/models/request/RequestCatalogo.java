package com.caircb.rcbtracegadere.models.request;

public class RequestCatalogo {
    private Integer tipoCatalogo;
    private String fecha;


    public RequestCatalogo(Integer tipoCatalogo, String fecha) {
        this.tipoCatalogo = tipoCatalogo;
        this.fecha = fecha;
    }

    public Integer getTipoCatalogo() {
        return tipoCatalogo;
    }

    public void setTipoCatalogo(Integer tipoCatalogo) {
        this.tipoCatalogo = tipoCatalogo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
