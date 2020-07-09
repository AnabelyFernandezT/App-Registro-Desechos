package com.caircb.rcbtracegadere.models.request;

import java.util.List;

public class RequestNovedadesGestores {

    private List<RequestNovedadFoto> fotos;
    private String url;

    public RequestNovedadesGestores(List<RequestNovedadFoto> fotos, String url) {
        this.fotos = fotos;
        this.url = url;
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
