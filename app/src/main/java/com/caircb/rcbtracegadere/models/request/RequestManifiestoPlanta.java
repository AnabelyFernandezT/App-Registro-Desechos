package com.caircb.rcbtracegadere.models.request;

import java.util.Date;
import java.util.List;

public class RequestManifiestoPlanta {

    private Integer idAppManifiesto;
    private String numeroManifiesto;
    private Double peso;
    private String urlFirmaPlanta;
    private Date fechaRecepcionPlanta;
    private List<RequestManifiestoNovedadFrecuente> novedadFrecuentePlanta;

    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getUrlFirmaPlanta() {
        return urlFirmaPlanta;
    }

    public void setUrlFirmaPlanta(String urlFirmaPlanta) {
        this.urlFirmaPlanta = urlFirmaPlanta;
    }

    public Date getFechaRecepcionPlanta() {
        return fechaRecepcionPlanta;
    }

    public void setFechaRecepcionPlanta(Date fechaRecepcionPlanta) {
        this.fechaRecepcionPlanta = fechaRecepcionPlanta;
    }

    public List<RequestManifiestoNovedadFrecuente> getNovedadFrecuentePlanta() {
        return novedadFrecuentePlanta;
    }

    public void setNovedadFrecuentePlanta(List<RequestManifiestoNovedadFrecuente> novedadFrecuentePlanta) {
        this.novedadFrecuentePlanta = novedadFrecuentePlanta;
    }
}
