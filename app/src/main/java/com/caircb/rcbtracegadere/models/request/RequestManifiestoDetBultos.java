package com.caircb.rcbtracegadere.models.request;

import java.math.BigDecimal;

public class RequestManifiestoDetBultos {
    private Integer index;
    private BigDecimal peso;
    private String descripcion;
    private String codigoQr;

    public RequestManifiestoDetBultos(Integer index, BigDecimal peso, String descripcion, String codigoQr) {
        this.index = index;
        this.peso = peso;
        this.descripcion = descripcion;
        this.codigoQr = codigoQr;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
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
}
