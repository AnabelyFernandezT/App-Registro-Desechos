package com.caircb.rcbtracegadere.models;

public class ItemManifiestoPlantaCodigoQR {
    private int idAppManifiesto;
    private String numeroManifiesto;
    private String nombreCliente;
    private Integer totalBultos;
    private Integer bultosSelecionado;
    private double peso;
    private String nombreDesecho;

    public ItemManifiestoPlantaCodigoQR() {
    }

    public int getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(int idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Integer getTotalBultos() {
        return totalBultos;
    }

    public void setTotalBultos(Integer totalBultos) {
        this.totalBultos = totalBultos;
    }

    public Integer getBultosSelecionado() {
        return bultosSelecionado;
    }

    public void setBultosSelecionado(Integer bultosSelecionado) {
        this.bultosSelecionado = bultosSelecionado;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getNombreDesecho() {
        return nombreDesecho;
    }

    public void setNombreDesecho(String nombreDesecho) {
        this.nombreDesecho = nombreDesecho;
    }
}
