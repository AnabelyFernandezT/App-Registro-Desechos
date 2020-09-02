package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (RecepcionQrPlantaEntity.TABLE))
public class RecepcionQrPlantaEntity {
    public static final String TABLE = "tb_recepcion_qr_planta";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;
    private Double pesoTotalLote;
    private Integer cantidadTotalBultos;
    private Integer cantidadTotalManifiestos;
    private String numerosManifiesto;

    public RecepcionQrPlantaEntity(){

    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Double getPesoTotalLote() {
        return pesoTotalLote;
    }

    public void setPesoTotalLote(Double pesoTotalLote) {
        this.pesoTotalLote = pesoTotalLote;
    }

    public Integer getCantidadTotalBultos() {
        return cantidadTotalBultos;
    }

    public void setCantidadTotalBultos(Integer cantidadTotalBultos) {
        this.cantidadTotalBultos = cantidadTotalBultos;
    }

    public Integer getCantidadTotalManifiestos() {
        return cantidadTotalManifiestos;
    }

    public void setCantidadTotalManifiestos(Integer cantidadTotalManifiestos) {
        this.cantidadTotalManifiestos = cantidadTotalManifiestos;
    }

    public String getNumerosManifiesto() {
        return numerosManifiesto;
    }

    public void setNumerosManifiesto(String numerosManifiesto) {
        this.numerosManifiesto = numerosManifiesto;
    }
}
