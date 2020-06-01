package com.caircb.rcbtracegadere.models.response;

import java.util.List;

public class DtoListaEmpresa {
    private Integer idEmpresa;
    private String nombre;
    private String servicio;
    private List<DtoListaLugar> lugares;

    public DtoListaEmpresa() {
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public List<DtoListaLugar> getLugares() {
        return lugares;
    }

    public void setLugares(List<DtoListaLugar> lugares) {
        this.lugares = lugares;
    }
}
