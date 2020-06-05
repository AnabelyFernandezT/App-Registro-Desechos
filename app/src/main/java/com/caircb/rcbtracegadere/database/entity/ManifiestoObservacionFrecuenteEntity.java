package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoObservacionFrecuenteEntity.TABLE))
public class ManifiestoObservacionFrecuenteEntity {

    public static final String TABLE = "tb_manifiestos_novedad_frecuente";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idAppManifiesto;

    @NonNull
    private Integer idCatalogo;

    private boolean estadoChek;

    private boolean estadoChekRecepcion;

    public ManifiestoObservacionFrecuenteEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(@NonNull Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    @NonNull
    public Integer getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(@NonNull Integer idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public boolean isEstadoChek() {
        return estadoChek;
    }

    public void setEstadoChek(boolean estadoChek) {
        this.estadoChek = estadoChek;
    }

    public boolean isEstadoChekRecepcion() {
        return estadoChekRecepcion;
    }

    public void setEstadoChekRecepcion(boolean estadoChekRecepcion) {
        this.estadoChekRecepcion = estadoChekRecepcion;
    }
}
