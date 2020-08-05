package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
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
    private Integer idSubRuta;

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

    @NonNull
    private Boolean sincronizadoFin;

    private Integer tiposubruta;

    public RutaInicioFinEntity(@NonNull Integer idRutaInicioFin, @NonNull Integer idTransporteRecolector, @NonNull Integer idSubRuta, @NonNull Date fechaInicio, Date fechaFin, @NonNull String kilometrajeInicio, String kilometrajeFin, @NonNull Integer estado, int tiposubruta) {
        this.idRutaInicioFin = idRutaInicioFin;
        this.idTransporteRecolector = idTransporteRecolector;
        this.idSubRuta = idSubRuta;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.kilometrajeInicio = kilometrajeInicio;
        this.kilometrajeFin = kilometrajeFin;
        this.estado = estado;
        this.sincronizado=false;
        this.sincronizadoFin=false;
        this.tiposubruta=tiposubruta;
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
    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(@NonNull Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
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

    @NonNull
    public Boolean getSincronizadoFin() {
        return sincronizadoFin;
    }

    public void setSincronizadoFin(@NonNull Boolean sincronizadoFin) {
        this.sincronizadoFin = sincronizadoFin;
    }

    public Integer getTiposubruta() {
        return tiposubruta;
    }

    public void setTiposubruta(Integer tiposubruta) {
        this.tiposubruta = tiposubruta;
    }
}