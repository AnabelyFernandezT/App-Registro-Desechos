package com.caircb.rcbtracegadere.models;

public class RowItemManifiesto {

    private int id;
    private String unidad;
    private double peso;
    private String codigo;
    private String descripcion;
    private String tratamiento;
    private double cantidadBulto;
    private Integer tipoItem;
    private Integer tipoPaquete;
    private boolean estado;
    private int tipoBalanza;
    private double pesoReferencial;
    private boolean faltaImpresiones;
    private Integer tipoMostrar;

    public RowItemManifiesto(){}


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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getTratamiento() { return tratamiento; }

    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public Integer getTipoItem() { return tipoItem; }

    public void setTipoItem(Integer tipoItem) { this.tipoItem = tipoItem;  }

    public Integer getTipoPaquete() {  return tipoPaquete; }

    public void setTipoPaquete(Integer tipoPaquete) { this.tipoPaquete = tipoPaquete;  }

    public String getCodigo() { return codigo;}

    public void setCodigo(String codigo) { this.codigo = codigo;}

    public int getTipoBalanza() {
        return tipoBalanza;
    }

    public void setTipoBalanza(int tipoBalanza) {
        this.tipoBalanza = tipoBalanza;
    }

    public double getPesoReferencial() {
        return pesoReferencial;
    }

    public void setPesoReferencial(double pesoReferencial) {
        this.pesoReferencial = pesoReferencial;
    }

    public boolean isFaltaImpresiones() {
        return faltaImpresiones;
    }

    public void setFaltaImpresiones(boolean faltaImpresiones) {
        this.faltaImpresiones = faltaImpresiones;
    }

    public Integer getTipoMostrar() {
        return tipoMostrar;
    }

    public void setTipoMostrar(Integer tipoMostrar) {
        this.tipoMostrar = tipoMostrar;
    }
}
