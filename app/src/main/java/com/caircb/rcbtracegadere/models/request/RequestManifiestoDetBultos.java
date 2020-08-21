package com.caircb.rcbtracegadere.models.request;

import java.math.BigDecimal;

public class RequestManifiestoDetBultos {
    private Integer index;
    private double peso;
    private String descripcion;
    private String codigoQr;
    private double pesoTara;

    public RequestManifiestoDetBultos(Integer index, double peso, String descripcion, String codigoQr, double pesoTara) {
        this.index = index;
        this.peso = peso;
        this.descripcion = descripcion;
        this.codigoQr = codigoQr;
        this.pesoTara = pesoTara;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public double getPesoTara() {
        return pesoTara;
    }

    public void setPesoTara(double pesoTara) {
        this.pesoTara = pesoTara;
    }
}