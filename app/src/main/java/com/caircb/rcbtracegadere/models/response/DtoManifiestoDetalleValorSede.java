package com.caircb.rcbtracegadere.models.response;

import java.math.BigDecimal;

public class DtoManifiestoDetalleValorSede {

    private Integer idManifiestoDetalle;
    private Double peso;
    private String codigoQR;
    private Integer idManifiestoDetalleValores;
    private String nombreBulto;
    private Boolean estado;


    public DtoManifiestoDetalleValorSede() {
    }

    public Integer getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(Integer idManifiestoDetalle) {
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    public String getNombreBulto() {
        return nombreBulto;
    }

    public void setNombreBulto(String nombreBulto) {
        this.nombreBulto = nombreBulto;
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

    public Integer getIdManifiestoDetalleValores() {
        return idManifiestoDetalleValores;
    }

    public void setIdManifiestoDetalleValores(Integer idManifiestoDetalleValores) {
        this.idManifiestoDetalleValores = idManifiestoDetalleValores;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
