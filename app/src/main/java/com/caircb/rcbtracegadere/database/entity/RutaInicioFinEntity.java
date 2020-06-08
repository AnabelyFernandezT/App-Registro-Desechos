package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = (RutaInicioFinEntity.TABLE))
public class RutaInicioFinEntity {

    public static final String TABLE = "tb_rutaInicioFin";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    private Integer idRutaInicioFin;

    @NonNull
    private Integer idTransporteRecolector;

    @NonNull
    private Integer idTransporteVehiculo;

    @NonNull
    private Date fechaInicio;

    private Date fechaFin;

    @NonNull
    private String kilometrajeInicio;

    private String kilometrajeFin;

    @NonNull
    private Integer estado;

    @NonNull
    private Boolean sincronizado;

    public RutaInicioFinEntity(@NonNull Integer idRutaInicioFin, @NonNull Integer idTransporteRecolector, @NonNull Integer idTransporteVehiculo, @NonNull Date fechaInicio, Date fechaFin, @NonNull String kilometrajeInicio, String kilometrajeFin, @NonNull Integer estado) {
        this.idRutaInicioFin = idRutaInicioFin;
        this.idTransporteRecolector = idTransporteRecolector;
        this.idTransporteVehiculo = idTransporteVehiculo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.kilometrajeInicio = kilometrajeInicio;
        this.kilometrajeFin = kilometrajeFin;
        this.estado = estado;
        this.sincronizado=false;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getIdRutaInicioFin() {
        return idRutaInicioFin;
    }

    public void setIdRutaInicioFin(Integer idRutaInicioFin) {
        this.idRutaInicioFin = idRutaInicioFin;
    }

    @NonNull
    public Integer getIdTransporteRecolector() {
        return idTransporteRecolector;
    }

    public void setIdTransporteRecolector(@NonNull Integer idTransporteRecolector) {
        this.idTransporteRecolector = idTransporteRecolector;
    }

    @NonNull
    public Integer getIdTransporteVehiculo() {
        return idTransporteVehiculo;
    }

    public void setIdTransporteVehiculo(@NonNull Integer idTransporteVehiculo) {
        this.idTransporteVehiculo = idTransporteVehiculo;
    }

    @NonNull
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(@NonNull Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @NonNull
    public String getKilometrajeInicio() {
        return kilometrajeInicio;
    }

    public void setKilometrajeInicio(@NonNull String kilometrajeInicio) {
        this.kilometrajeInicio = kilometrajeInicio;
    }

    public String getKilometrajeFin() {
        return kilometrajeFin;
    }

    public void setKilometrajeFin(String kilometrajeFin) {
        this.kilometrajeFin = kilometrajeFin;
    }

    @NonNull
    public Integer getEstado() {
        return estado;
    }

    public void setEstado(@NonNull Integer estado) {
        this.estado = estado;
    }

    public Boolean getSincronizado() { return sincronizado;  }

    public void setSincronizado(Boolean sincronizado) { this.sincronizado = sincronizado; }
}