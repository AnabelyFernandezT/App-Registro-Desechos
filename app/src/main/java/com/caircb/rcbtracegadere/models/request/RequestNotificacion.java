package com.caircb.rcbtracegadere.models.request;

public class RequestNotificacion {

    private Integer idReceptor;
    private String mensaje;
    private Integer idEmisor;
    private String token;
    private Integer idManifiesto;
    private String idHojaRuta;
    private Integer idCatNotificacion;

    public Integer getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(Integer idReceptor) {
        this.idReceptor = idReceptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(Integer idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public String getIdHojaRuta() {
        return idHojaRuta;
    }

    public void setIdHojaRuta(String idHojaRuta) {
        this.idHojaRuta = idHojaRuta;
    }

    public Integer getIdCatNotificacion() {
        return idCatNotificacion;
    }

    public void setIdCatNotificacion(Integer idCatNotificacion) {
        this.idCatNotificacion = idCatNotificacion;
    }
}
