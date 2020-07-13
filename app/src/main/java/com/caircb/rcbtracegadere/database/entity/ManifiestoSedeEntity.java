package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = (ManifiestoSedeEntity.TABLE))
public class ManifiestoSedeEntity {
    public static final String TABLE = "tb_manifiestos_sede";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idAppManifiesto;

    private String numeroManifiesto;

    private String nombreCliente;

    private Integer idTransporteVehiculo;

    private Integer estado;

    private Integer bultosRegistrados;

    private Integer bultosTotal;

    public Integer getBultosRegistrados() {
        return bultosRegistrados;
    }

    public void setBultosRegistrados(Integer bultosRegistrados) {
        this.bultosRegistrados = bultosRegistrados;
    }

    public Integer getBultosTotal() {
        return bultosTotal;
    }

    public void setBultosTotal(Integer bultosTotal) {
        this.bultosTotal = bultosTotal;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public ManifiestoSedeEntity() {
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

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Integer getIdTransporteVehiculo() {
        return idTransporteVehiculo;
    }

    public void setIdTransporteVehiculo(Integer idTransporteVehiculo) {
        this.idTransporteVehiculo = idTransporteVehiculo;
    }
}


