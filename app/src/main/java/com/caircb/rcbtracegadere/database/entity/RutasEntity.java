package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (RutasEntity.TABLE))

public class RutasEntity {
    public static final String TABLE = "tb_rutas";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    private Integer codigo;

    private String nombre;
    private Integer tiposubruta;


    public RutasEntity(Integer codigo, String nombre, Integer tiposubruta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tiposubruta=tiposubruta;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTiposubruta() {
        return tiposubruta;
    }

    public void setTiposubruta(Integer tiposubruta) {
        this.tiposubruta = tiposubruta;
    }
}
