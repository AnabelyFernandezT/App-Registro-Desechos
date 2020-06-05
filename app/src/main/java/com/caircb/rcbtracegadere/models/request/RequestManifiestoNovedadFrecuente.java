package com.caircb.rcbtracegadere.models.request;

import java.util.List;

public class RequestManifiestoNovedadFrecuente {
    private Integer idCatalogo;
    private List<RequestNovedadFoto> fotos;

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
