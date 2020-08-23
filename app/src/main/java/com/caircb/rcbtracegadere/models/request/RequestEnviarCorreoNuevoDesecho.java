package com.caircb.rcbtracegadere.models.request;

public class RequestEnviarCorreoNuevoDesecho {

    private String identificacion;
    private String nombreCliente;
    private String sucursal;
    private String manifiesto;
    private String nombreDesecho;
    private Integer flagTipoPKG;


    public RequestEnviarCorreoNuevoDesecho() {

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

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getManifiesto() {
        return manifiesto;
    }

    public void setManifiesto(String manifiesto) {
        this.manifiesto = manifiesto;
    }

    public String getNombreDesecho() {
        return nombreDesecho;
    }

    public void setNombreDesecho(String nombreDesecho) {
        this.nombreDesecho = nombreDesecho;
    }

    public Integer getFlagTipoPKG() {
        return flagTipoPKG;
    }

    public void setFlagTipoPKG(Integer flagTipoPKG) {
        this.flagTipoPKG = flagTipoPKG;
    }
}
