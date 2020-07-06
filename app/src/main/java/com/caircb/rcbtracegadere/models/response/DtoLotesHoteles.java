package com.caircb.rcbtracegadere.models.response;

import java.util.List;

public class DtoLotesHoteles {
    private String codigoLoteContenedorHotel;
    private String codigoLoteContenedor;
    private Integer idLoteContenedor;
    private String ruta;
    private String subRuta;
    private String placaVehiculo;
    private String operador;

    public DtoLotesHoteles() {
    }

    public String getCodigoLoteContenedorHotel() {
        return codigoLoteContenedorHotel;
    }

    public void setCodigoLoteContenedorHotel(String codigoLoteContenedorHotel) {
        this.codigoLoteContenedorHotel = codigoLoteContenedorHotel;
    }

    public String getCodigoLoteContenedor() {
        return codigoLoteContenedor;
    }

    public void setCodigoLoteContenedor(String codigoLoteContenedor) {
        this.codigoLoteContenedor = codigoLoteContenedor;
    }

    public Integer getIdLoteContenedor() {
        return idLoteContenedor;
    }

    public void setIdLoteContenedor(Integer idLoteContenedor) {
        this.idLoteContenedor = idLoteContenedor;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getSubRuta() {
        return subRuta;
    }

    public void setSubRuta(String subRuta) {
        this.subRuta = subRuta;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }
}
