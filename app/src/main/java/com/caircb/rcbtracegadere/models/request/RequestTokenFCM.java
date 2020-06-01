package com.caircb.rcbtracegadere.models.request;

public class RequestTokenFCM {
    private Integer usuario;
    private Integer aplicacion;
    private Integer empresa;
    private String imei;
    private String credencial;

    public RequestTokenFCM() {
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Integer getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(Integer aplicacion) {
        this.aplicacion = aplicacion;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCredencial() {
        return credencial;
    }

    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }
}
