package com.caircb.rcbtracegadere.models;

public class ItemEtiquetaHospitalarioDetalleRecolecion {
    private String descripcionDesecho;
    private String codigoMai;
    private int numeroBultos;
    private double peso;

    public ItemEtiquetaHospitalarioDetalleRecolecion(String descripcionDesecho, String codigoMai, int numeroBultos, double peso) {
        this.descripcionDesecho = descripcionDesecho;
        this.codigoMai = codigoMai;
        this.numeroBultos = numeroBultos;
        this.peso = peso;
    }

    public String getDescripcionDesecho() {
        return descripcionDesecho;
    }

    public void setDescripcionDesecho(String descripcionDesecho) {
        this.descripcionDesecho = descripcionDesecho;
    }

    public String getCodigoMai() {
        return codigoMai;
    }

    public void setCodigoMai(String codigoMai) {
        this.codigoMai = codigoMai;
    }

    public int getNumeroBultos() {
        return numeroBultos;
    }

    public void setNumeroBultos(int numeroBultos) {
        this.numeroBultos = numeroBultos;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
