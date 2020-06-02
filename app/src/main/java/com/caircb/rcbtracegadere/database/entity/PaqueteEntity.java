package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (PaqueteEntity.TABLE))
public class PaqueteEntity {
    public static final String TABLE = "tb_paquetes";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idSistema;

    @NonNull
    private Integer index;

    @NonNull
    private String descripcion;

    @NonNull
    private String funda;

    @NonNull
    private String guardian;

    private Boolean flagAdicionales;

    private Boolean flagAdicionalFunda;

    private Boolean flagAdicionalGuardian;

    private Integer paquetePorRecolccion;

    public PaqueteEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(@NonNull Integer idSistema) {
        this.idSistema = idSistema;
    }

    @NonNull
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NonNull String descripcion) {
        this.descripcion = descripcion;
    }

    @NonNull
    public String getFunda() {
        return funda;
    }

    public void setFunda(@NonNull String funda) {
        this.funda = funda;
    }

    @NonNull
    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(@NonNull String guardian) {
        this.guardian = guardian;
    }

    @NonNull
    public Integer getIndex() {
        return index;
    }

    public void setIndex(@NonNull Integer index) {
        this.index = index;
    }

    public Boolean getFlagAdicionales() {
        return flagAdicionales;
    }

    public void setFlagAdicionales(Boolean flagAdicionales) {
        this.flagAdicionales = flagAdicionales;
    }

    public Boolean getFlagAdicionalFunda() {
        return flagAdicionalFunda;
    }

    public void setFlagAdicionalFunda(Boolean flagAdicionalFunda) {
        this.flagAdicionalFunda = flagAdicionalFunda;
    }

    public Boolean getFlagAdicionalGuardian() {
        return flagAdicionalGuardian;
    }

    public void setFlagAdicionalGuardian(Boolean flagAdicionalGuardian) {
        this.flagAdicionalGuardian = flagAdicionalGuardian;
    }

    public Integer getPaquetePorRecolccion() {
        return paquetePorRecolccion;
    }

    public void setPaquetePorRecolccion(Integer paquetePorRecolccion) {
        this.paquetePorRecolccion = paquetePorRecolccion;
    }
}
