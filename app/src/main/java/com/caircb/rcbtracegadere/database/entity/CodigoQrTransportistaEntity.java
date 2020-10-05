package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (CodigoQrTransportistaEntity.TABLE))
public class CodigoQrTransportistaEntity {
    public static final String TABLE = "tb_codigoqrtransportista";

    @PrimaryKey(autoGenerate = true)
    private Integer idCodigoQr;

    private String codigoQr;

    private String fechaCierre;

    private String idLote;

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

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getIdLote() {
        return idLote;
    }

    public void setIdLote(String idLote) {
        this.idLote = idLote;
    }
}
