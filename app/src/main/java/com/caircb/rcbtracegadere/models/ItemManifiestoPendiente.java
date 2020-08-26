package com.caircb.rcbtracegadere.models;

public class ItemManifiestoPendiente {

    private Integer idManifiesto;

    private String numeroManifiesto;

    private Integer totBultos;

    private Integer totPendientes;

    private Boolean estadoCheck;

    public ItemManifiestoPendiente() {
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

    public Boolean getEstadoCheck() {
        return estadoCheck;
    }

    public void setEstadoCheck(Boolean estadoCheck) {
        this.estadoCheck = estadoCheck;
    }
}
