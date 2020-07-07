package com.caircb.rcbtracegadere.models.request;

import java.util.Date;
import java.util.List;

public class RequestLote {
    private Integer idLoteCotenedor;
    private Date fecha;

    public RequestLote(Integer idLoteCotenedor, Date fecha) {
        this.idLoteCotenedor = idLoteCotenedor;
        this.fecha = fecha;
    }

    public Integer getIdLoteCotenedor() {
        return idLoteCotenedor;
    }

    public void setIdLoteCotenedor(Integer idLoteCotenedor) {
        this.idLoteCotenedor = idLoteCotenedor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
