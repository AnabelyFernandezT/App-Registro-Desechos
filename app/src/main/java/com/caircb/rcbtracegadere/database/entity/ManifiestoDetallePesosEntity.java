package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoDetallePesosEntity.TABLE))
public class ManifiestoDetallePesosEntity {

    public static final String TABLE = "tb_manifiesto_detalle_pesos";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private double valor;

    @NonNull
    private Integer idAppManifiesto;

    @NonNull
    private Integer idAppManifiestoDetalle;

    private String descripcion;


    public ManifiestoDetallePesosEntity(double valor, @NonNull Integer idAppManifiesto, @NonNull Integer idAppManifiestoDetalle, String descripcion) {
        this.valor = valor;
        this.idAppManifiesto = idAppManifiesto;
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
        this.descripcion = descripcion;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @NonNull
    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(@NonNull Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    @NonNull
    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(@NonNull Integer idAppManifiestoDetalle) {
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
