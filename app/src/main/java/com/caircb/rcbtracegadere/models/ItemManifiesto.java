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
    private  Integer Apertura1;
    private Integer Apertura2;
    private Integer Cierre1;
    private Integer Cierre2;



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


    public Integer getApertura1() {
        return Apertura1;
    }

    public void setApertura1(Integer apertura1) {
        Apertura1 = apertura1;
    }

    public Integer getApertura2() {
        return Apertura2;
    }

    public void setApertura2(Integer apertura2) {
        Apertura2 = apertura2;
    }

    public Integer getCierre1() {
        return Cierre1;
    }

    public void setCierre1(Integer cierre1) {
        Cierre1 = cierre1;
    }

    public Integer getCierre2() {
        return Cierre2;
    }

    public void setCierre2(Integer cierre2) {
        Cierre2 = cierre2;
    }
}
