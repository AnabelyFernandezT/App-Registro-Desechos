package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity(tableName = (ManifiestoSedeDetalleValorEntity.TABLE))
public class ManifiestoSedeDetalleValorEntity {
    public static final String TABLE = "tb_manifiestos_sede_det_valor";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idManifiestoDetalle;

    private Double peso;

    private String codigoQR;

    private Integer idManifiestoDetalleValor;

    private String nombreBulto;

    private Boolean estado;




    public ManifiestoSedeDetalleValorEntity() {
    }

    public String getNombreBulto() {
        return nombreBulto;
    }

    public void setNombreBulto(String nombreBulto) {
        this.nombreBulto = nombreBulto;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(@NonNull Integer idManifiestoDetalle) {
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getIdManifiestoDetalleValor() {
        return idManifiestoDetalleValor;
    }

    public void setIdManifiestoDetalleValor(Integer idManifiestoDetalleValor) {
        this.idManifiestoDetalleValor = idManifiestoDetalleValor;
    }
}


