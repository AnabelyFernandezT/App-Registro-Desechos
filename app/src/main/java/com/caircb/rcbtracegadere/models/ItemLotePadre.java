package com.caircb.rcbtracegadere.models;

import java.util.Date;

public class ItemLotePadre {
    private Integer idManifiestoPadre;
    private String manifiestos;
    private Double total;
    private String nombreCliente;
    private String numeroManifiestoPadre;

    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }

    public String getManifiestos() {
        return manifiestos;
    }

    public void setManifiestos(String manifiestos) {
        this.manifiestos = manifiestos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNumeroManifiestoPadre() {
        return numeroManifiestoPadre;
    }

    public void setNumeroManifiestoPadre(String numeroManifiestoPadre) {
        this.numeroManifiestoPadre = numeroManifiestoPadre;
    }
}
