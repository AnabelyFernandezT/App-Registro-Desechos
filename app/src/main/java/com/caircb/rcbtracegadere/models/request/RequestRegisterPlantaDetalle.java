package com.caircb.rcbtracegadere.models.request;

import com.caircb.rcbtracegadere.models.DtoDetallesPlanta;
import com.caircb.rcbtracegadere.models.DtoFotoPlanta;

import java.util.List;

public class RequestRegisterPlantaDetalle {
    private Integer idManifiesto;
    private List<DtoDetallesPlanta> Detalle;
    private List<DtoFotoPlanta> fotos;
    private String observacion;
    private String urlFima;
    private Integer idPlantaRecolector;
    private Integer tipoRecoleccion;
    private String numeroManifiesto;

    public RequestRegisterPlantaDetalle() {
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public List<DtoDetallesPlanta> getDetalle() {
        return Detalle;
    }

    public void setDetalle(List<DtoDetallesPlanta> detalle) {
        Detalle = detalle;
    }

    public List<DtoFotoPlanta> getFotos() {
        return fotos;
    }

    public void setFotos(List<DtoFotoPlanta> fotos) {
        this.fotos = fotos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUrlFima() {
        return urlFima;
    }

    public void setUrlFima(String urlFima) {
        this.urlFima = urlFima;
    }

    public Integer getIdPlantaRecolector() {
        return idPlantaRecolector;
    }

    public void setIdPlantaRecolector(Integer idPlantaRecolector) {
        this.idPlantaRecolector = idPlantaRecolector;
    }

    public Integer getTipoRecoleccion() {
        return tipoRecoleccion;
    }

    public void setTipoRecoleccion(Integer tipoRecoleccion) {
        this.tipoRecoleccion = tipoRecoleccion;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }
}