package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (RecepcionQrPlantaDetalleEntity.TABLE))
public class RecepcionQrPlantaDetalleEntity {
    public static final String TABLE = "tb_recepcion_qr_detalle_planta";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;
    private String nombreDesecho;
    private Double pesoDesecho;

    public RecepcionQrPlantaDetalleEntity(){

    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getNombreDesecho() {
        return nombreDesecho;
    }

    public void setNombreDesecho(String nombreDesecho) {
        this.nombreDesecho = nombreDesecho;
    }

    public Double getPesoDesecho() {
        return pesoDesecho;
    }

    public void setPesoDesecho(Double pesoDesecho) {
        this.pesoDesecho = pesoDesecho;
    }
}
