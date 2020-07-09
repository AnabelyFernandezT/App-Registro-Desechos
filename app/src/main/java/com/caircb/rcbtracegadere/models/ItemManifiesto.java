package com.caircb.rcbtracegadere.models;

public class ItemManifiesto {
    private int idAppManifiesto;
    private String cliente;
    private String numero;
    private int estado;
    private String sucursal;
    private String direccion;
    private String provincia;
    private String canton;
    private String numeroPlacaVehiculo;


    public ItemManifiesto() {
    }

    public String getNumeroPlacaVehiculo() {
        return numeroPlacaVehiculo;
    }

    public void setNumeroPlacaVehiculo(String numeroPlacaVehiculo) {
        this.numeroPlacaVehiculo = numeroPlacaVehiculo;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }
}
