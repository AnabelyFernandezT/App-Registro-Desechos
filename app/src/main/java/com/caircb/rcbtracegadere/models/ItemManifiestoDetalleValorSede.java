package com.caircb.rcbtracegadere.models;

public class ItemManifiestoDetalleValorSede {
    private int idManifiestoDetalle;
    private int idManifiestoDetalleValores;
    private Double peso;
    private String codigoQR;
    private String nombreBulto;
    private Boolean estado;

    public ItemManifiestoDetalleValorSede() {
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNombreBulto() {
        return nombreBulto;
    }

    public void setNombreBulto(String nombreBulto) {
        this.nombreBulto = nombreBulto;
    }

    public int getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(int idManifiestoDetalle) {
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    public int getIdManifiestoDetalleValores() {
        return idManifiestoDetalleValores;
    }

    public void setIdManifiestoDetalleValores(int idManifiestoDetalleValores) {
        this.idManifiestoDetalleValores = idManifiestoDetalleValores;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }
}
