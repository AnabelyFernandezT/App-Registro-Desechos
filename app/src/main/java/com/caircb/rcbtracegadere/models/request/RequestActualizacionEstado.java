package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestActualizacionEstado {

    private Integer idManifiesto;
    private Integer idSubruta;
    private Integer idTransportistaRecolector;

    public RequestActualizacionEstado() {
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
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

}
