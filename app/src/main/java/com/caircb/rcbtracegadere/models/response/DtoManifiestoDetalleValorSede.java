package com.caircb.rcbtracegadere.models.response;

import java.math.BigDecimal;

public class DtoManifiestoDetalleValorSede {

    private Integer idManifiestoDetalle;
    private BigDecimal peso;
    private String codigoQR;
    private Integer idManifiestoDetalleValor;


    public DtoManifiestoDetalleValorSede() {
    }

    public Integer getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(Integer idManifiestoDetalle) {
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public Integer getIdManifiestoDetalleValor() {
        return idManifiestoDetalleValor;
    }

    public void setIdManifiestoDetalleValor(Integer idManifiestoDetalleValor) {
        this.idManifiestoDetalleValor = idManifiestoDetalleValor;
    }
}
