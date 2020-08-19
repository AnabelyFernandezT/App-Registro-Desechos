package com.caircb.rcbtracegadere.models.request;

public class RequestCodigoQrTransportista {
    private Integer idSubruta;

    public RequestCodigoQrTransportista(Integer idSubruta){
        this.idSubruta=idSubruta;
    }

    public Integer getIdSubruta() {
        return idSubruta;
    }

    public void setIdSubruta(Integer idSubruta) {
        this.idSubruta = idSubruta;
    }
}
