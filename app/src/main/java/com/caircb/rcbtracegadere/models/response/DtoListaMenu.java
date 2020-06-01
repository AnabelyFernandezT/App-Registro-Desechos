package com.caircb.rcbtracegadere.models.response;

public class DtoListaMenu {
    private String icono;
    private Integer idMenu;
    private Integer idPerfil;
    private boolean isHabilitado;
    private String nombre;

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public boolean isHabilitado() {
        return isHabilitado;
    }

    public void setHabilitado(boolean habilitado) {
        isHabilitado = habilitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
