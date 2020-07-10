package com.caircb.rcbtracegadere.models.response;

public class DtoLotePadreHotel {

    private Integer idLoteContenedorHotel;
    private String codigo;
    private Integer estado;

    public DtoLotePadreHotel() {
    }

    public Integer getIdLoteContenedorHotel() {
        return idLoteContenedorHotel;
    }

    public void setIdLoteContenedorHotel(Integer idLoteContenedorHotel) {
        this.idLoteContenedorHotel = idLoteContenedorHotel;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
