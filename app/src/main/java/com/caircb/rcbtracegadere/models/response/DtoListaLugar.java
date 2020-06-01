package com.caircb.rcbtracegadere.models.response;

import java.util.List;

public class DtoListaLugar {
    private Integer idAplicacion;
    private Integer idLugar;
    private Integer idPerfil;
    private String nombre;
    private List<DtoListaMenu> menus;

    public Integer getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(Integer idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public Integer getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(Integer idLugar) {
        this.idLugar = idLugar;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DtoListaMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<DtoListaMenu> menus) {
        this.menus = menus;
    }
}
