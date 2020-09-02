package com.caircb.rcbtracegadere.models;

import java.util.Date;

public class ItemNotificacion {
    private int idNotificacion;
    private String nombreNotificacion;
    private String estadoNotificacion;
    private String tipoNotificacion;
    private String idManifiesto;
    private String peso;

    public String getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(String idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getNombreNotificacion() {
        return nombreNotificacion;
    }

    public void setNombreNotificacion(String nombreNotificacion) {
        this.nombreNotificacion = nombreNotificacion;
    }

    public String getEstadoNotificacion() {
        return estadoNotificacion;
    }

    public void setEstadoNotificacion(String estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion;
    }
}
