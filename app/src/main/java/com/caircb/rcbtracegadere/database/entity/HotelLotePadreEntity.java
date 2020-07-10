package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (HotelLotePadreEntity.TABLE))
public class HotelLotePadreEntity {

    public static final String TABLE = "tb_hotel_padre";
    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    private Integer idLoteContenedorHotel;

    private String codigo;

    private Integer estado;

    private Integer idTransportistaRecolector;

    public HotelLotePadreEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
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

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }
}
