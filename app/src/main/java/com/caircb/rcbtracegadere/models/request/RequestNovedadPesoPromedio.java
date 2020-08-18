package com.caircb.rcbtracegadere.models.request;

public class RequestNovedadPesoPromedio {
    private String fotoMPRImagen;
    private String fotoMPRUrl;
    private Integer fotoMPREstado;
    private Integer indx;

    public RequestNovedadPesoPromedio(String fotoMPRImagen, String fotoMPRUrl, Integer fotoMPREstado, Integer indx) {
        this.fotoMPRImagen = fotoMPRImagen;
        this.fotoMPRUrl = fotoMPRUrl;
        this.fotoMPREstado = fotoMPREstado;
        this.indx = indx;
    }

    public String getFotoMPRImagen() {
        return fotoMPRImagen;
    }

    public void setFotoMPRImagen(String fotoMPRImagen) {
        this.fotoMPRImagen = fotoMPRImagen;
    }

    public String getFotoMPRUrl() {
        return fotoMPRUrl;
    }

    public void setFotoMPRUrl(String fotoMPRUrl) {
        this.fotoMPRUrl = fotoMPRUrl;
    }

    public Integer getFotoMPREstado() {
        return fotoMPREstado;
    }

    public void setFotoMPREstado(Integer fotoMPREstado) {
        this.fotoMPREstado = fotoMPREstado;
    }

    public Integer getIndx() {
        return indx;
    }

    public void setIndx(Integer indx) {
        this.indx = indx;
    }
}
