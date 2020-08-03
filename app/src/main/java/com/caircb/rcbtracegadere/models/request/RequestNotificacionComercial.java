package com.caircb.rcbtracegadere.models.request;

public class RequestNotificacionComercial {
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
}
