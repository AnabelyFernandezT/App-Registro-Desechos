package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = (RuteoRecoleccionEntity.TABLE))
public class RuteoRecoleccionEntity {
    public static final String TABLE = "tb_ruteo_recoleccion";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    private Integer idSubRuta;

    private Date fechaInicioRuta;

    private Integer puntoPartida;

    private Integer puntoLlegada;

    private Date fechaLlegadaRuta;

    private boolean estado;


    public RuteoRecoleccionEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }

    public Date getFechaInicioRuta() {
        return fechaInicioRuta;
    }

    public void setFechaInicioRuta(Date fechaInicioRuta) {
        this.fechaInicioRuta = fechaInicioRuta;
    }

    public Integer getPuntoPartida() {
        return puntoPartida;
    }

    public void setPuntoPartida(Integer puntoPartida) {
        this.puntoPartida = puntoPartida;
    }

    public Integer getPuntoLlegada() {
        return puntoLlegada;
    }

    public void setPuntoLlegada(Integer puntoLlegada) {
        this.puntoLlegada = puntoLlegada;
    }

    public Date getFechaLlegadaRuta() {
        return fechaLlegadaRuta;
    }

    public void setFechaLlegadaRuta(Date fechaLlegadaRuta) {
        this.fechaLlegadaRuta = fechaLlegadaRuta;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
