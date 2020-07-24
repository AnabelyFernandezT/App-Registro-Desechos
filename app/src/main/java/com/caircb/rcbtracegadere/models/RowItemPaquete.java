package com.caircb.rcbtracegadere.models;

public class RowItemPaquete {

    private String nombre;
    private Integer cantidad;
    private Integer pendiente;
    private Integer tipo;
    private Integer initPendiente;
    private Integer diferencia;

    public RowItemPaquete(){
    }

    public RowItemPaquete(String nombre, Integer cantidad, Integer pendiente) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.pendiente = pendiente;
    }

    public RowItemPaquete(String nombre, Integer cantidad, Integer pendiente, Integer initPediente,Integer diferencia,Integer tipo) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.pendiente = pendiente;
        this.tipo = tipo;
        this.diferencia= diferencia;
        this.initPendiente=initPediente;
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

    public void setInitPendiente(Integer initPendiente) {
        this.initPendiente = initPendiente;
    }

    public Integer getInitPendiente() {
        return initPendiente;
    }

    public Integer getDiferencia() { return diferencia; }

    public void setDiferencia(Integer diferencia) { this.diferencia = diferencia; }
}
