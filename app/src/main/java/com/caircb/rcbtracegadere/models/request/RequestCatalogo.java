package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestCatalogo {
    private Integer tipoCatalogo;
    private Date fecha;


    public RequestCatalogo(Integer tipoCatalogo, Date fecha) {
        this.tipoCatalogo = tipoCatalogo;
        this.fecha = fecha;
    }

    public Integer getTipoCatalogo() {
        return tipoCatalogo;
    }

    public void setTipoCatalogo(Integer tipoCatalogo) {
        this.tipoCatalogo = tipoCatalogo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
