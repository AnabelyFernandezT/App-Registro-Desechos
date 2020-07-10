package com.caircb.rcbtracegadere.models.response;

public class DtoCatalogoPlaca {
    private Integer id;
    private String nombre;
    private String codigo;
    private Integer dataAux;

    public DtoCatalogoPlaca() {
    }

    /*public DtoCatalogo(Integer id, String nombre, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }*/

    public Integer getDataAux() {
        return dataAux;
    }

    public void setDataAux(Integer dataAux) {
        this.dataAux = dataAux;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
