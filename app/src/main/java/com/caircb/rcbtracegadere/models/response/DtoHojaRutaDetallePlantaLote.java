package com.caircb.rcbtracegadere.models.response;

import java.math.BigDecimal;

public class DtoHojaRutaDetallePlantaLote {

    private String nombreDesecho;
    private BigDecimal pesoDesecho;

    public DtoHojaRutaDetallePlantaLote(){

    }

    public String getNombreDesecho() {
        return nombreDesecho;
    }

    public void setNombreDesecho(String nombreDesecho) {
        this.nombreDesecho = nombreDesecho;
    }

    public BigDecimal getPesoDesecho() {
        return pesoDesecho;
    }

    public void setPesoDesecho(BigDecimal pesoDesecho) {
        this.pesoDesecho = pesoDesecho;
    }
}
