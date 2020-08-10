package com.caircb.rcbtracegadere.models.request;

public class RequestVisorManifiestoPdf {
    private Integer idManifiesto;
    private Integer estado;

    public RequestVisorManifiestoPdf() {
    }

    public RequestVisorManifiestoPdf(Integer idManifiesto, Integer estado) {
        this.idManifiesto = idManifiesto;
        this.estado = estado;
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}