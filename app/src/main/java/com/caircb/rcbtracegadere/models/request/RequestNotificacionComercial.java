package com.caircb.rcbtracegadere.models.request;

public class RequestNotificacionComercial {
    private String identificacion;
    private String nombreCliente;
    private String manifiesto;
    private String pesoReferencial;
    private String pesoSolicitado;
    private Integer flagTipoPKG;

    public RequestNotificacionComercial() {
    }

    public String getManifiesto() {
        return manifiesto;
    }

    public void setManifiesto(String manifiesto) {
        this.manifiesto = manifiesto;
    }

    public String getPesoReferencial() {
        return pesoReferencial;
    }

    public void setPesoReferencial(String pesoReferencial) {
        this.pesoReferencial = pesoReferencial;
    }

    public String getPesoSolicitado() {
        return pesoSolicitado;
    }

    public void setPesoSolicitado(String pesoSolicitado) {
        this.pesoSolicitado = pesoSolicitado;
    }

    public Integer getFlagTipoPKG() {
        return flagTipoPKG;
    }

    public void setFlagTipoPKG(Integer flagTipoPKG) {
        this.flagTipoPKG = flagTipoPKG;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
