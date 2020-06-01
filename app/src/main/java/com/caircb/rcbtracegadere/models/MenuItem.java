package com.caircb.rcbtracegadere.models;

public class MenuItem {
    private String nombre;
    private boolean visible;
    private boolean enabled;

    public  MenuItem(){
    }

    public MenuItem(String nombre){
        this.nombre=nombre;
    }

    public MenuItem(String nombre, boolean enabled){
        this.nombre=nombre;
        this.enabled=enabled;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
