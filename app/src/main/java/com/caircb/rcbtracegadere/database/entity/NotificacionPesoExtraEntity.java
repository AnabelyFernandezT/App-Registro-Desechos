package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (NotificacionPesoExtraEntity.TABLE))
public class NotificacionPesoExtraEntity {
    public static final String TABLE = "tb_notificacion_peso_extra";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    private Integer idManifiesto;

    private Integer idManifiestoDetalle;

    private Double nuevoPesoReferencial;

    private Integer autorizacion;

    public NotificacionPesoExtraEntity(Integer idManifiesto, Integer idManifiestoDetalle, Integer autorizacion) {
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.autorizacion = autorizacion;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public Integer getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(Integer idManifiestoDetalle) {
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    public Double getNuevoPesoReferencial() {
        return nuevoPesoReferencial;
    }

    public void setNuevoPesoReferencial(Double nuevoPesoReferencial) {
        this.nuevoPesoReferencial = nuevoPesoReferencial;
    }

    public Integer getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(Integer autorizacion) {
        this.autorizacion = autorizacion;
    }
}
