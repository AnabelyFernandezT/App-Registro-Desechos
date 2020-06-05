package com.caircb.rcbtracegadere.models.request;

import java.util.List;

public class RequestManifiestoNovedadNoRecoleccion {
    private Integer idCatalogo;
    private List<RequestNovedadFoto> fotos;

    public RequestManifiestoNovedadNoRecoleccion(Integer idCatalogo, List<RequestNovedadFoto> fotos) {
        this.idCatalogo = idCatalogo;
        this.fotos = fotos;
    }

    public Integer getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Integer idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public List<RequestNovedadFoto> getFotos() {
        return fotos;
    }

    public void setFotos(List<RequestNovedadFoto> fotos) {
        this.fotos = fotos;
    }
}
