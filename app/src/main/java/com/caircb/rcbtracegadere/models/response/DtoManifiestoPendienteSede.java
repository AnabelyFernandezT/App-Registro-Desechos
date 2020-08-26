package com.caircb.rcbtracegadere.models.response;

public class DtoManifiestoPendienteSede {

    private Integer idManifiesto;
    private String numeroManifiesto;
    private Integer totBultos;
    private Integer totPendientes;

    public DtoManifiestoPendienteSede() {
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public Integer getTotBultos() {
        return totBultos;
    }

    public void setTotBultos(Integer totBultos) {
        this.totBultos = totBultos;
    }

    public Integer getTotPendientes() {
        return totPendientes;
    }

    public void setTotPendientes(Integer totPendientes) {
        this.totPendientes = totPendientes;
    }
}
