package com.caircb.rcbtracegadere.models;

import java.util.List;

public class DtoFotoPlanta {

    private List<String> urlImagen;
    private String url;

    public DtoFotoPlanta(List<String> urlImagen, String url) {
        this.urlImagen = urlImagen;
        this.url = url;
    }

    public List<String> getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(List<String> urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
