package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoPaquetesEntity.TABLE))
public class ManifiestoPaquetesEntity {
    public static final String TABLE = "tb_manifiestos_paquete";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    private Integer idPAquetes;

    private Integer IdManifiesto;

    private Integer dtosFdas;

    private Integer dtosGuard;

    private Integer qpqh;

    private Integer adGuardiane;

    private Integer adFunda;


    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getIdPAquetes() {
        return idPAquetes;
    }

    public void setIdPAquetes(Integer idPAquetes) {
        this.idPAquetes = idPAquetes;
    }

    public Integer getIdManifiesto() {
        return IdManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        IdManifiesto = idManifiesto;
    }

    public Integer getDtosFdas() {
        return dtosFdas;
    }

    public void setDtosFdas(Integer dtosFdas) {
        this.dtosFdas = dtosFdas;
    }

    public Integer getDtosGuard() {
        return dtosGuard;
    }

    public void setDtosGuard(Integer dtosGuard) {
        this.dtosGuard = dtosGuard;
    }

    public Integer getQpqh() {
        return qpqh;
    }

    public void setQpqh(Integer qpqh) {
        this.qpqh = qpqh;
    }

    public Integer getAdGuardiane() {
        return adGuardiane;
    }

    public void setAdGuardiane(Integer adGuardiane) {
        this.adGuardiane = adGuardiane;
    }

    public Integer getAdFunda() {
        return adFunda;
    }

    public void setAdFunda(Integer adFunda) {
        this.adFunda = adFunda;
    }
}
