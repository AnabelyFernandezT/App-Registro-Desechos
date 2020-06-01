package com.caircb.rcbtracegadere.database.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (CatalogoEntity.TABLE))
public class CatalogoEntity {
    public static final String TABLE = "tb_catalogos";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idSistema;

    private String codigo;

    private String nombre;

    @NonNull
    private Integer tipo;

    public CatalogoEntity(@NonNull Integer idSistema, String codigo, String nombre, @NonNull Integer tipo) {
        this.idSistema = idSistema;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull Integer tipo) {
        this.tipo = tipo;
    }
}
