package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = (LoteHotelesEntity.TABLE))
public class LoteHotelesEntity {
    public static final String TABLE = "tb_lotes_hoteles";

    @PrimaryKey(autoGenerate = true)
    private Integer idLote;

    private String codigoLoteContenedorHotel;

    private String codigoLoteContenedor;

    private Integer idLoteContenedor;

    private String ruta;

    private String subRuta;

    private String placaVehiculo;

    private String operador;

    private String chofer;

    private String hoteles;

    public LoteHotelesEntity(){

    }

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

    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
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
