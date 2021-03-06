package com.caircb.rcbtracegadere.models;

import java.util.Date;

public class ItemLoteHoteles {
    private String codigoLoteContenedorHotel;
    private String codigoLoteContenedor;
    private Integer idLoteContenedor;
    private String ruta;
    private String subRuta;
    private String placaVehiculo;
    private String operador;
    private String chofer;
    private String hoteles;

    public String getChofer() {
        return chofer;
    }

    public void setChofer(String chofer) {
        this.chofer = chofer;
    }

    public String getHoteles() {
        return hoteles;
    }

    public void setHoteles(String hoteles) {
        this.hoteles = hoteles;
    }

    public Integer getIdLoteContenedor() {
        return idLoteContenedor;
    }

    public void setIdLoteContenedor(Integer idLoteContenedor) {
        this.idLoteContenedor = idLoteContenedor;
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
