package com.caircb.rcbtracegadere.models;

public class RowItemManifiestosDetalleGestores {

    private Integer id;
    private Integer idManifiesto;
    private String numeroManifiesto;
    private String cliente;
    private Double peso;
    private Double cantidadBulto;

    public RowItemManifiestosDetalleGestores() {
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getCantidadBulto() {
        return cantidadBulto;
    }

    public void setCantidadBulto(Double cantidadBulto) {
        this.cantidadBulto = cantidadBulto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
