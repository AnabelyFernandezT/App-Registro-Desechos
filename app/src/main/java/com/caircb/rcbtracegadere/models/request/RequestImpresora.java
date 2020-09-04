package com.caircb.rcbtracegadere.models.request;

public class RequestImpresora {
    private String fecha;

    public RequestImpresora(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
