package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestCatalogoDestino {
    private Integer tipoCatalogo;
    private Integer fecha;

    public RequestCatalogoDestino(Integer tipoCatalogo, Integer fecha) {
        this.tipoCatalogo = tipoCatalogo;
        this.fecha = fecha;
    }

    public Integer getTipoCatalogo() {
        return tipoCatalogo;
    }

    public void setTipoCatalogo(Integer tipoCatalogo) {
        this.tipoCatalogo = tipoCatalogo;
    }

    public Integer getFecha() {
        return fecha;
    }

    public void setFecha(Integer fecha) {
        this.fecha = fecha;
    }
}
