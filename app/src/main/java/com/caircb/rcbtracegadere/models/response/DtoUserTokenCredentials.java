package com.caircb.rcbtracegadere.models.response;

public class DtoUserTokenCredentials {
    private boolean exito;
    private int idRegistro;
    private String mensaje;
    private String id;
    //private int secuencial;

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
/*
    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
