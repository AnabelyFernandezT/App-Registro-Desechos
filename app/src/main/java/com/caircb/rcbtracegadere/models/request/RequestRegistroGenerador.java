package com.caircb.rcbtracegadere.models.request;

import java.util.List;

public class RequestRegistroGenerador  {
    private String novedadEncontrada;
    private Double pesoRecolectado;
    private Double pesoGestorAlterno;
    private Integer idManifiestoPadre;
    private String fotoMPImagen;
    private String fotoMPUrl;
    private Integer fotoMPEstado;
    private String correoGestorAlterno;
    private List<RequestNovedadesGestores> fotosNovedades;

    public String getNovedadEncontrada() {
        return novedadEncontrada;
    }

    public void setNovedadEncontrada(String novedadEncontrada) {
        this.novedadEncontrada = novedadEncontrada;
    }

    public Double getPesoRecolectado() {
        return pesoRecolectado;
    }

    public void setPesoRecolectado(Double pesoRecolectado) {
        this.pesoRecolectado = pesoRecolectado;
    }

    public Double getPesoGestorAlterno() {
        return pesoGestorAlterno;
    }

    public void setPesoGestorAlterno(Double pesoGestorAlterno) {
        this.pesoGestorAlterno = pesoGestorAlterno;
    }

    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }

    public String getFotoMPImagen() {
        return fotoMPImagen;
    }

    public void setFotoMPImagen(String fotoMPImagen) {
        this.fotoMPImagen = fotoMPImagen;
    }

    public String getFotoMPUrl() {
        return fotoMPUrl;
    }

    public void setFotoMPUrl(String fotoMPUrl) {
        this.fotoMPUrl = fotoMPUrl;
    }

    public Integer getFotoMPEstado() {
        return fotoMPEstado;
    }

    public void setFotoMPEstado(Integer fotoMPEstado) {
        this.fotoMPEstado = fotoMPEstado;
    }

    public String getCorreoGestorAlterno() {
        return correoGestorAlterno;
    }

    public void setCorreoGestorAlterno(String correoGestorAlterno) {
        this.correoGestorAlterno = correoGestorAlterno;
    }

    public List<RequestNovedadesGestores> getFotosNovedades() {
        return fotosNovedades;
    }

    public void setFotosNovedades(List<RequestNovedadesGestores> fotosNovedades) {
        this.fotosNovedades = fotosNovedades;
    }
}
