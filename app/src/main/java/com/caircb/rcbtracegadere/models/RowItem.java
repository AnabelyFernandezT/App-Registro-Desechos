package com.caircb.rcbtracegadere.models;


public class RowItem {
    private int id;
    private String nombre;
    private Integer icono;
    private boolean isHabilitado;
    private String descripcion;
    private Integer estado;

    public RowItem(int id, String nombre, String descripcion, Integer estado){
        this.id=id;
        this.nombre=nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public RowItem(int id, String nombre){
        this.id=id;
        this.nombre=nombre;
    }

    public RowItem(String nombre, Integer icono, boolean isHabilitado){
        this.icono =icono;
        this.nombre = nombre;
        this.isHabilitado =  isHabilitado;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Integer getIcono() {
        return icono;
    }

    public void setIcono(Integer icono) {
        this.icono = icono;
    }

    public boolean isHabilitado() {
        return isHabilitado;
    }

    public void setHabilitado(boolean habilitado) {
        isHabilitado = habilitado;
    }
}
