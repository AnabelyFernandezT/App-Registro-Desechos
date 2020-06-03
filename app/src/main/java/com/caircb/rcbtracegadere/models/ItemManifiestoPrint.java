package com.caircb.rcbtracegadere.models;

import java.util.ArrayList;
import java.util.List;

public class ItemManifiestoPrint {
    private int idAppManifiesto;
    private String cliente;
    private String numero;
    private String fecha;

    public ItemManifiestoPrint(int idAppManifiesto, String cliente, String numero, String fecha) {
        this.idAppManifiesto = idAppManifiesto;
        this.cliente = cliente;
        this.numero = numero;
        this.fecha = fecha;
    }

    public int getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(int idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
