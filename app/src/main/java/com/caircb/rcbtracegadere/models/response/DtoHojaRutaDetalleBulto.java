package com.caircb.rcbtracegadere.models.response;

import java.math.BigDecimal;

public class DtoHojaRutaDetalleBulto {
    private Integer index;
    private BigDecimal peso;
    private String descripcion;
    private String codigoQr;

    public DtoHojaRutaDetalleBulto() {
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
