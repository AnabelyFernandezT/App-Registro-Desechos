package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoPlantaDetalleValorEntity.TABLE))
public class ManifiestoPlantaDetalleValorEntity {
    public static final String TABLE = "tb_manifiestos_planta_det_valor";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idManifiestoDetalle;

    private Integer idManifiesto;

    private Double peso;

    private String codigoQR;

    private Integer idManifiestoDetalleValor;

    private String nombreBulto;

    private Boolean estado;

    private Double nuevoPeso;

    public ManifiestoPlantaDetalleValorEntity() {
    }

    public Double getNuevoPeso() {
        return nuevoPeso;
    }

    public void setNuevoPeso(Double nuevoPeso) {
        this.nuevoPeso = nuevoPeso;
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

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }
}


