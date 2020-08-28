package com.caircb.rcbtracegadere.models;

public class RowItemManifiestosDetalleGestores {

    private Integer idManifiesto;
    private String numeroManifiesto;
    private String cliente;

    public RowItemManifiestosDetalleGestores() {
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
