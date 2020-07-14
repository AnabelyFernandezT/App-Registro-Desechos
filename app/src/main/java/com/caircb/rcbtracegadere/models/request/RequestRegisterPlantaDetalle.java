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
}
