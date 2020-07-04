package com.caircb.rcbtracegadere.models;

public class ItemManifiestoSede {
    private int idAppManifiesto;
    private String numeroManifiesto;
    private String nombreCliente;
    private Integer totalBultos;
    private Integer bultosSelecionado;

    public ItemManifiestoSede() {
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
}
