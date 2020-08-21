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
    private String Apertura1;
    private String Apertura2;
    private String Cierre1;
    private String Cierre2;
    private String telefono;
    private String frecuencia;
    private Integer tipoPaquete;
    private Double pesoPromedio;
    private String referencia;



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

    public String getApertura1() {
        return Apertura1;
    }

    public void setApertura1(String apertura1) {
        Apertura1 = apertura1;
    }

    public String getApertura2() {
        return Apertura2;
    }

    public void setApertura2(String apertura2) {
        Apertura2 = apertura2;
    }

    public String getCierre1() {
        return Cierre1;
    }

    public void setCierre1(String cierre1) {
        Cierre1 = cierre1;
    }

    public String getCierre2() {
        return Cierre2;
    }

    public void setCierre2(String cierre2) {
        Cierre2 = cierre2;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Integer getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(Integer tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    public Double getPesoPromedio() {
        return pesoPromedio;
    }

    public void setPesoPromedio(Double pesoPromedio) {
        this.pesoPromedio = pesoPromedio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
