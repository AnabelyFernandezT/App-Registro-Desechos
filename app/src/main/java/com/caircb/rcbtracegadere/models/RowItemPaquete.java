package com.caircb.rcbtracegadere.models;

public class RowItemPaquete {

    private String nombre;
    private Integer cantidad;
    private Integer pendiente;
    private Integer tipo;

    public RowItemPaquete(){
    }

    public RowItemPaquete(String nombre, Integer cantidad, Integer pendiente) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.pendiente = pendiente;
    }

    public RowItemPaquete(String nombre, Integer cantidad, Integer pendiente, Integer tipo) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.pendiente = pendiente;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getPendiente() {
        return pendiente;
    }

    public void setPendiente(Integer pendiente) {
        this.pendiente = pendiente;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
