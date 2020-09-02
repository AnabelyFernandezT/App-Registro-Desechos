package com.caircb.rcbtracegadere.models;

import java.math.BigDecimal;

public class ItemQrDetallePlanta {
    private String nombreDesecho;
    private Double pesoDesecho;

    public ItemQrDetallePlanta(){

    }

    public String getNombreDesecho() {
        return nombreDesecho;
    }

    public void setNombreDesecho(String nombreDesecho) {
        this.nombreDesecho = nombreDesecho;
    }

    public Double getPesoDesecho() {
        return pesoDesecho;
    }

    public void setPesoDesecho(Double pesoDesecho) {
        this.pesoDesecho = pesoDesecho;
    }
}
