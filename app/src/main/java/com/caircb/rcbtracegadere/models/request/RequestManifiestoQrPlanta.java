package com.caircb.rcbtracegadere.models.request;

import java.util.List;

public class RequestManifiestoQrPlanta {
    private String numeroManifiesto;
    private String urlFirmaPlanta;
    private String fechaRecepcionPlanta;
    private Integer idPlantaRecolector;
    private String observacion;
    private Integer tipoRecoleccion;
    private List<RequestManifiestoNovedadFrecuente> novedadFrecuentePlanta;

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getUrlFirmaPlanta() {
        return urlFirmaPlanta;
    }

    public void setUrlFirmaPlanta(String urlFirmaPlanta) {
        this.urlFirmaPlanta = urlFirmaPlanta;
    }

    public String getFechaRecepcionPlanta() {
        return fechaRecepcionPlanta;
    }

    public void setFechaRecepcionPlanta(String fechaRecepcionPlanta) {
        this.fechaRecepcionPlanta = fechaRecepcionPlanta;
    }

    public Integer getIdPlantaRecolector() {
        return idPlantaRecolector;
    }

    public void setIdPlantaRecolector(Integer idPlantaRecolector) {
        this.idPlantaRecolector = idPlantaRecolector;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getTipoRecoleccion() {
        return tipoRecoleccion;
    }

    public void setTipoRecoleccion(Integer tipoRecoleccion) {
        this.tipoRecoleccion = tipoRecoleccion;
    }

    public List<RequestManifiestoNovedadFrecuente> getNovedadFrecuentePlanta() {
        return novedadFrecuentePlanta;
    }

    public void setNovedadFrecuentePlanta(List<RequestManifiestoNovedadFrecuente> novedadFrecuentePlanta) {
        this.novedadFrecuentePlanta = novedadFrecuentePlanta;
    }
}
