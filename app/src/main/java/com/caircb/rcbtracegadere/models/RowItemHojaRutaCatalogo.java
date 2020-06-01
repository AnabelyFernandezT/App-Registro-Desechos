package com.caircb.rcbtracegadere.models;

public class RowItemHojaRutaCatalogo {
    private Integer id;
    private String catalogo;
    private Integer numFotos;
    private boolean estadoChek;

    public RowItemHojaRutaCatalogo() {
    }


    public String getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }


    public boolean isEstadoChek() {
        return estadoChek;
    }

    public void setEstadoChek(boolean estadoChek) {
        this.estadoChek = estadoChek;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumFotos() {
        return numFotos;
    }

    public void setNumFotos(Integer numFotos) {
        this.numFotos = numFotos;
    }
}
