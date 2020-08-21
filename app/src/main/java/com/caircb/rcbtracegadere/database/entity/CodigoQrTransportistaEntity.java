package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (CodigoQrTransportistaEntity.TABLE))
public class CodigoQrTransportistaEntity {
    public static final String TABLE = "tb_codigoqrtransportista";

    @PrimaryKey(autoGenerate = true)
    private Integer idCodigoQr;
    private String codigoQr;

    public CodigoQrTransportistaEntity(){

    }

    public Integer getIdCodigoQr() {
        return idCodigoQr;
    }

    public void setIdCodigoQr(Integer idCodigoQr) {
        this.idCodigoQr = idCodigoQr;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }
}
