package com.caircb.rcbtracegadere.models.request;

public class RequestNotificacion {

    private Integer idReceptor;
    private String mensaje;
    private Integer idEmisor;
    private String titulo;
    private Integer idManifiesto;
    private String idHojaRuta;
    private Integer idCatNotificacion;
    private Double pesoExtra;


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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public Double getPesoExtra() {
        return pesoExtra;
    }

    public void setPesoExtra(Double pesoExtra) {
        this.pesoExtra = pesoExtra;
    }
}
