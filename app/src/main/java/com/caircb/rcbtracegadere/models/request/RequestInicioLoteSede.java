package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestInicioLoteSede {
    private Integer idDestinatarioFinRutaCat;
    private Integer tipo;
    private Date fecha;

    public RequestInicioLoteSede(Integer idDestinatarioFinRutaCat, Integer tipo, Date fecha) {
        this.idDestinatarioFinRutaCat = idDestinatarioFinRutaCat;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public Integer getIdDestinatarioFinRutaCat() {
        return idDestinatarioFinRutaCat;
    }

    public void setIdDestinatarioFinRutaCat(Integer idDestinatarioFinRutaCat) {
        this.idDestinatarioFinRutaCat = idDestinatarioFinRutaCat;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
