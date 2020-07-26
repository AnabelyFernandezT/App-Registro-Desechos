package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = (CatalogoEntity.TABLE))
public class OffLineEntity {
    public static final String TABLE = "tb_offline_proceso";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer tipo;

    @NonNull
    private String proceso;

    @NonNull
    private Integer registro;

    @NonNull
    private String parametros;

    @NonNull
    private Date fecha;

    @NonNull
    private boolean sincronizado;

    public OffLineEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull Integer tipo) {
        this.tipo = tipo;
    }

    @NonNull
    public String getProceso() {
        return proceso;
    }

    public void setProceso(@NonNull String proceso) {
        this.proceso = proceso;
    }

    @NonNull
    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(@NonNull Integer registro) {
        this.registro = registro;
    }

    @NonNull
    public String getParametros() {
        return parametros;
    }

    public void setParametros(@NonNull String parametros) {
        this.parametros = parametros;
    }

    @NonNull
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(@NonNull Date fecha) {
        this.fecha = fecha;
    }

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }
}
