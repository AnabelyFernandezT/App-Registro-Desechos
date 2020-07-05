package com.caircb.rcbtracegadere.models.request;

import java.util.Date;
import java.util.List;

public class RequestLote {
    private Integer tipDestinatarioFinRuta;
    private Date fecha;

    public RequestLote(Integer tipDestinatarioFinRuta, Date fecha){
        this.tipDestinatarioFinRuta = tipDestinatarioFinRuta;
        this.fecha = fecha;
    }

    public Integer getTipDestinatarioFinRuta() {
        return tipDestinatarioFinRuta;
    }

    public void setTipDestinatarioFinRuta(Integer tipDestinatarioFinRuta) {
        this.tipDestinatarioFinRuta = tipDestinatarioFinRuta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
