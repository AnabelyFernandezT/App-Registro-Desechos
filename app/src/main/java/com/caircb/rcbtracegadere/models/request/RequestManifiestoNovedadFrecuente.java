package com.caircb.rcbtracegadere.models.request;

import java.util.List;

public class RequestManifiestoNovedadFrecuente {
    private Integer idCatalogo;
    private List<RequestNovedadFoto> fotos;
    private String url;

    public RequestManifiestoNovedadFrecuente(Integer idCatalogo, List<RequestNovedadFoto> fotos, String url) {
        this.idCatalogo = idCatalogo;
        this.fotos = fotos;
        this.url=url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
