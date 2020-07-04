package com.caircb.rcbtracegadere.models;

public class ItemManifiestoSede {
    private int idManifiestoPadre;
    private String manifiestos;
    private String nombreCliente;


    public ItemManifiestoSede() {
    }

    public int getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(int idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }

    public String getManifiestos() {
        return manifiestos;
    }

    public void setManifiestos(String manifiestos) {
        this.manifiestos = manifiestos;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
