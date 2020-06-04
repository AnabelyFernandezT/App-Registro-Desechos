package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (TecnicoEntity.TABLE))
public class TecnicoEntity {
    public static final String TABLE = "tb_tecnicos";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    private Integer idManifiesto;

    @NonNull
    private String nombre;

    @NonNull
    private String identificacion;

    private String correo;

    private String telefono;

    private String celular;

    public TecnicoEntity(Integer idManifiesto, @NonNull String nombre, @NonNull String identificacion, String correo, String telefono) {
        this.idManifiesto = idManifiesto;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.telefono = telefono;
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
    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(@NonNull String identificacion) {
        this.identificacion = identificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }
}
