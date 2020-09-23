package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestCodigoQrTransportista {
    private Integer idSubruta;
    private Integer idTransportistaRecolector;
    private Date fechaDispositivo;

    public RequestCodigoQrTransportista(Integer idSubruta, Integer idTransportistaRecolector, Date fechaDispositivo){
        this.idSubruta=idSubruta;
        this.idTransportistaRecolector=idTransportistaRecolector;
        this.fechaDispositivo=fechaDispositivo;
    }

    public Integer getIdSubruta() {
        return idSubruta;
    }

    public void setIdSubruta(Integer idSubruta) {
        this.idSubruta = idSubruta;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }

    public Date getFechaDispositivo() {
        return fechaDispositivo;
    }

    public void setFechaDispositivo(Date fechaDispositivo) {
        this.fechaDispositivo = fechaDispositivo;
    }
}
