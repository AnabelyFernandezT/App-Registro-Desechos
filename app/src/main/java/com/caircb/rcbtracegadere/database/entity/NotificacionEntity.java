package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = (NotificacionEntity.TABLE))
public class NotificacionEntity {
    public static final String TABLE = "tb_notificaciones";

    @PrimaryKey(autoGenerate = true)
    private Integer idNotificacion;
    private String nombreNotificacion;
    private String estadoNotificacion;
    private String tipoNotificacion;
    private String idManifiesto;
    private String peso;

    public String getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(String idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public NotificacionEntity(){

    }

    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getNombreNotificacion() {
        return nombreNotificacion;
    }

    public void setNombreNotificacion(String nombreNotificacion) {
        this.nombreNotificacion = nombreNotificacion;
    }

    public String getEstadoNotificacion() {
        return estadoNotificacion;
    }

    public void setEstadoNotificacion(String estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion;
    }
}
