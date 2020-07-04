package com.caircb.rcbtracegadere.models.response;

public class DtoManifiestoDetalleValorSede {

    private Integer idManifiestoDetalle;
    private String peso;
    private String codigoQR;


    public DtoManifiestoDetalleValorSede() {
    }

    public Integer getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(Integer idManifiestoDetalle) {
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }
}
