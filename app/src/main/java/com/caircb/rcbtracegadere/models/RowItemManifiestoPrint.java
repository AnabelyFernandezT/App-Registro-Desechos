package com.caircb.rcbtracegadere.models;

public class RowItemManifiestoPrint {

    private int id;
    private String unidad;
    private double peso;
    private String descripcion;
    private String tratamiento;
    private double cantidadBulto;
    private Integer tipoItem;
    private Integer tipoPaquete;
    private String devolucionRecp;
    private boolean estado;

    public RowItemManifiestoPrint(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidadBulto() {
        return cantidadBulto;
    }

    public void setCantidadBulto(double cantidadBulto) {
        this.cantidadBulto = cantidadBulto;
    }

    public boolean isEstado() {return estado;}

    public void setEstado(boolean estado) {this.estado = estado; }

    public String getTratamiento() { return tratamiento; }

    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public Integer getTipoItem() { return tipoItem; }

    public void setTipoItem(Integer tipoItem) { this.tipoItem = tipoItem;  }

    public Integer getTipoPaquete() {  return tipoPaquete; }

    public void setTipoPaquete(Integer tipoPaquete) { this.tipoPaquete = tipoPaquete;  }

    public String getDevolucionRecp() {
        return devolucionRecp;
    }

    public void setDevolucionRecp(String devolucionRecp) {
        this.devolucionRecp = devolucionRecp;
    }
}
