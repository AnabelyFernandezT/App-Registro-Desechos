package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoDetalleEntity.TABLE))
public class ManifiestoDetalleEntity {

    public static final String TABLE = "tb_manifiestos_detalle";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idAppManifiestoDetalle;

    @NonNull
    private Integer idAppManifiesto;

    private Integer idTipoDesecho;

    private double pesoUnidad;

    private Integer idTipoUnidad;

    private double cantidadBulto;

    private Integer cantidadDesecho;

    private String tratamiento;

    private Integer tipoItem;

    private Integer tipoPaquete;

    @NonNull
    private boolean estadoChek;

    public ManifiestoDetalleEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(@NonNull Integer idAppManifiestoDetalle) { this.idAppManifiestoDetalle = idAppManifiestoDetalle;}

    @NonNull
    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(@NonNull Integer idAppManifiesto) {this.idAppManifiesto = idAppManifiesto;}

    public Integer getIdTipoDesecho() {
        return idTipoDesecho;
    }

    public void setIdTipoDesecho(Integer idTipoDesecho) {
        this.idTipoDesecho = idTipoDesecho;
    }

    public double getPesoUnidad() {
        return pesoUnidad;
    }

    public void setPesoUnidad(double pesoUnidad) {
        this.pesoUnidad = pesoUnidad;
    }

    public Integer getIdTipoUnidad() {
        return idTipoUnidad;
    }

    public void setIdTipoUnidad(Integer idTipoUnidad) {
        this.idTipoUnidad = idTipoUnidad;
    }

    public double getCantidadBulto() {
        return cantidadBulto;
    }

    public void setCantidadBulto(double cantidadBulto) {
        this.cantidadBulto = cantidadBulto;
    }

    public Integer getCantidadDesecho() {
        return cantidadDesecho;
    }

    public void setCantidadDesecho(Integer cantidadDesecho) { this.cantidadDesecho = cantidadDesecho; }

    public boolean isEstadoChek() {
        return estadoChek;
    }

    public void setEstadoChek(boolean estadoChek) {
        this.estadoChek = estadoChek;
    }

    public String getTratamiento() { return tratamiento; }

    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public Integer getTipoItem() { return tipoItem;  }

    public void setTipoItem(Integer tipoItem) {  this.tipoItem = tipoItem; }

    public Integer getTipoPaquete() { return tipoPaquete;  }

    public void setTipoPaquete(Integer tipoPaquete) {  this.tipoPaquete = tipoPaquete; }
}
