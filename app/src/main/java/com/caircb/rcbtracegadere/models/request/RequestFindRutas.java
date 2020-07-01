package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestFindRutas {

    private Integer idTransportista;
    private Date fechaManifiestos;

    public RequestFindRutas(Integer idTransportista, Date fechaManifiestos) {
        this.idTransportista = idTransportista;
        this.fechaManifiestos = fechaManifiestos;
    }

    public Integer getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    public Date getFechaManifiestos() {
        return fechaManifiestos;
    }

    public void setFechaManifiestos(Date fechaManifiestos) {
        this.fechaManifiestos = fechaManifiestos;
    }


}
