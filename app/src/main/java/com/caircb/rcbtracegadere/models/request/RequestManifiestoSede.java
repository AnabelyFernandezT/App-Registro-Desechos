package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestManifiestoSede {
    private Integer idTransportista;

    public RequestManifiestoSede(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    public Integer getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }
}
