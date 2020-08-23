package com.caircb.rcbtracegadere.models.request;

public class RequestFirmaUsuario {
    private Integer idTransportistaRecolector;

    public RequestFirmaUsuario(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }
}
