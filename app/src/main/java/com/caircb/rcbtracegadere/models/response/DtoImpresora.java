package com.caircb.rcbtracegadere.models.response;

import java.util.Date;

public class DtoImpresora {
    private String id;
    private String codigo;
    private String direccionmac;
    private Integer tipo;
    private boolean eliminado;
    private String fechaModificacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDireccionmac() {
        return direccionmac;
    }

    public void setDireccionmac(String direccionmac) {
        this.direccionmac = direccionmac;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
