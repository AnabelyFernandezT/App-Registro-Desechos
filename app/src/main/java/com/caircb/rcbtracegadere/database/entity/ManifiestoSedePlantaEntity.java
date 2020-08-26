package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = (ManifiestoSedePlantaEntity.TABLE))
public class ManifiestoSedePlantaEntity {
    public static final String TABLE = "tb_manifiesto_sede_planta";

    @PrimaryKey (autoGenerate = true)
    private Integer _id;

    private Integer idManifiestoPadre;

    private Integer idManifiesto;

    private String numeroManifiesto;

    private Integer totBultos;

    private Integer totPendientes;

    private Boolean estadoCheck;

    public ManifiestoSedePlantaEntity() {
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

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public Integer getTotBultos() {
        return totBultos;
    }

    public void setTotBultos(Integer totBultos) {
        this.totBultos = totBultos;
    }

    public Integer getTotPendientes() {
        return totPendientes;
    }

    public void setTotPendientes(Integer totPendientes) {
        this.totPendientes = totPendientes;
    }

    public Boolean getEstadoCheck() {
        return estadoCheck;
    }

    public void setEstadoCheck(Boolean estadoCheck) {
        this.estadoCheck = estadoCheck;
    }

    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }
}
