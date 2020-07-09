package com.caircb.rcbtracegadere.models.request;

import java.util.Date;
import java.util.List;

public class RequestLote {
    private Integer idLoteContenedor;
    private Date fecha;

    public RequestLote(Integer idLoteContenedor, Date fecha) {
        this.idLoteContenedor = idLoteContenedor;
        this.fecha = fecha;
    }

    public Integer getIdLoteContenedor() {
        return idLoteContenedor;
    }

    public void setIdLoteContenedor(Integer idLoteContenedor) {
        this.idLoteContenedor = idLoteContenedor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
