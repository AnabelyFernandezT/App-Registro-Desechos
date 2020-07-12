package com.caircb.rcbtracegadere.models;

import java.util.List;

public class DtoDetallesBultoPlanta {

    private int idDetalleValor;
    private Double  peso;

    public DtoDetallesBultoPlanta(int idDetalleValor, double peso) {
        this.idDetalleValor = idDetalleValor;
        this.peso = peso;
    }

    public int getIdDetalleValor() {
        return idDetalleValor;
    }

    public void setIdDetalleValor(int idDetalleValor) {
        this.idDetalleValor = idDetalleValor;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }
}
