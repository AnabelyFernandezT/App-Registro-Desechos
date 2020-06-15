package com.caircb.rcbtracegadere.models.request;

import java.util.List;

public class RequestManifiestoNovedadNoRecoleccion {
    private Integer idCatalogo;
    private String url;
    private List<RequestNovedadFoto> fotos;

    public RequestManifiestoNovedadNoRecoleccion(Integer idCatalogo, List<RequestNovedadFoto> fotos, String url) {
        this.idCatalogo = idCatalogo;
        this.fotos = fotos;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
