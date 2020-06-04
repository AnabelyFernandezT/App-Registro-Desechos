package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoPaquetesEntity.TABLE))
public class ManifiestoPaquetesEntity {
    public static final String TABLE = "tb_manifiestos_paquete";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idPaquete;

    @NonNull
    private Integer idAppManifiesto;

    @NonNull
    private Integer datosFundas;

    @NonNull
    private Integer datosGuardianes;

    @NonNull
    private Integer pqh;

    @NonNull
    private Integer adGuardianes;

    @NonNull
    private Integer adFundas;

    private Integer datosFundasPendientes;
    private Integer datosFundasDiferencia;
    private Integer datosGuardianesPendientes;
    private Integer datosGuardianesDiferencia;

    public ManifiestoPaquetesEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(@NonNull Integer idPaquete) {
        this.idPaquete = idPaquete;
    }

    @NonNull
    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(@NonNull Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    @NonNull
    public Integer getDatosFundas() {
        return datosFundas;
    }

    public void setDatosFundas(@NonNull Integer datosFundas) {
        this.datosFundas = datosFundas;
    }

    @NonNull
    public Integer getDatosGuardianes() {
        return datosGuardianes;
    }

    public void setDatosGuardianes(@NonNull Integer datosGuardianes) {
        this.datosGuardianes = datosGuardianes;
    }

    @NonNull
    public Integer getPqh() {
        return pqh;
    }

    public void setPqh(@NonNull Integer pqh) {
        this.pqh = pqh;
    }

    @NonNull
    public Integer getAdGuardianes() {
        return adGuardianes;
    }

    public void setAdGuardianes(@NonNull Integer adGuardianes) {
        this.adGuardianes = adGuardianes;
    }

    @NonNull
    public Integer getAdFundas() {
        return adFundas;
    }

    public void setAdFundas(@NonNull Integer adFundas) {
        this.adFundas = adFundas;
    }

    public Integer getDatosFundasPendientes() {
        return datosFundasPendientes;
    }

    public void setDatosFundasPendientes(Integer datosFundasPendientes) {
        this.datosFundasPendientes = datosFundasPendientes;
    }

    public Integer getDatosFundasDiferencia() {
        return datosFundasDiferencia;
    }

    public void setDatosFundasDiferencia(Integer datosFundasDiferencia) {
        this.datosFundasDiferencia = datosFundasDiferencia;
    }

    public Integer getDatosGuardianesPendientes() {
        return datosGuardianesPendientes;
    }

    public void setDatosGuardianesPendientes(Integer datosGuardianesPendientes) {
        this.datosGuardianesPendientes = datosGuardianesPendientes;
    }

    public Integer getDatosGuardianesDiferencia() {
        return datosGuardianesDiferencia;
    }

    public void setDatosGuardianesDiferencia(Integer datosGuardianesDiferencia) {
        this.datosGuardianesDiferencia = datosGuardianesDiferencia;
    }
}
