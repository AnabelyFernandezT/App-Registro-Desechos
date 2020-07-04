package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoSedeDetalleValorEntity.TABLE))
public class ManifiestoSedeDetalleValorEntity {
    public static final String TABLE = "tb_manifiestos_sede_det_valor";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idManifiestoHijo;

    private Integer idManifiestoDetalleValores;

    private String peso;

    private String codigoQR;




    public ManifiestoSedeDetalleValorEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdManifiestoHijo() {
        return idManifiestoHijo;
    }

    public void setIdManifiestoHijo(@NonNull Integer idManifiestoHijo) {
        this.idManifiestoHijo = idManifiestoHijo;
    }

    public Integer getIdManifiestoDetalleValores() {
        return idManifiestoDetalleValores;
    }

    public void setIdManifiestoDetalleValores(Integer idManifiestoDetalleValores) {
        this.idManifiestoDetalleValores = idManifiestoDetalleValores;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

}


