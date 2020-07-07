package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestLotePadre {
    private Integer idTransportista;
    private Date fecha;
    private Integer idDestinatarioFinRutaCatalogo;

    public RequestLotePadre(Integer idTransportista, Date fecha, Integer idDestinatarioFinRutaCatalogo) {
        this.idTransportista = idTransportista;
        this.fecha = fecha;
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }

    public Integer getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getIdDestinatarioFinRutaCatalogo() {
        return idDestinatarioFinRutaCatalogo;
    }

    public void setIdDestinatarioFinRutaCatalogo(Integer idDestinatarioFinRutaCatalogo) {
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }
}
