package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = (LogEntity.TABLE))
public class LogEntity {

    public static final String TABLE = "tb_logs";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    private String error;

    private String usuario;

    private Date fecha;

    private String imei;

    private Integer tipo;

    public LogEntity(String error, String usuario, Date fecha, String imei, Integer tipo) {
        this.error = error;
        this.usuario = usuario;
        this.fecha = fecha;
        this.imei = imei;
        this.tipo = tipo;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
