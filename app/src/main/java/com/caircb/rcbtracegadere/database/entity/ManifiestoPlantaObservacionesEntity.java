package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoPlantaObservacionesEntity.TABLE))
public class ManifiestoPlantaObservacionesEntity {
    public static final String TABLE = "tb_manifiestos_planta_observaciones";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idAppManifiesto;

    private double pesoRecolectado;

    private double pesoPlanta;

    private String observacionPeso;

    private String observacionOtra;

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

    public double getPesoRecolectado() {
        return pesoRecolectado;
    }

    public void setPesoRecolectado(double pesoRecolectado) {
        this.pesoRecolectado = pesoRecolectado;
    }

    public double getPesoPlanta() {
        return pesoPlanta;
    }

    public void setPesoPlanta(double pesoPlanta) {
        this.pesoPlanta = pesoPlanta;
    }

    public String getObservacionPeso() {
        return observacionPeso;
    }

    public void setObservacionPeso(String observacionPeso) {
        this.observacionPeso = observacionPeso;
    }

    public String getObservacionOtra() {
        return observacionOtra;
    }

    public void setObservacionOtra(String observacionOtra) {
        this.observacionOtra = observacionOtra;
    }
}


