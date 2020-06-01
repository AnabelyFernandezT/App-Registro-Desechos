package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ParametroEntity.TABLE))
public class ParametroEntity {
    public static final String TABLE = "tb_parametros";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private String nombre;

    @NonNull
    private String valor;

    public ParametroEntity(@NonNull String nombre, @NonNull String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getValor() {
        return valor;
    }

    public void setValor(@NonNull String valor) {
        this.valor = valor;
    }
}
