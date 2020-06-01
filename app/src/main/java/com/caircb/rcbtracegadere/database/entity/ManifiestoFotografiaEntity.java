package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoFotografiaEntity.TABLE))
public class ManifiestoFotografiaEntity {
    public static final String TABLE = "tb_manifiestos_novedad_foto";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idAppManifiesto;

    @NonNull
    private Integer idCatalogo;

    @NonNull
    private Integer tipo;

    @NonNull
    private Integer code;

    private String foto;

    private String fotoUrl;

    public ManifiestoFotografiaEntity() {
    }

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

    @NonNull
    public Integer getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(@NonNull Integer idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    @NonNull
    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull Integer tipo) {
        this.tipo = tipo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    @NonNull
    public Integer getCode() { return code; }

    public void setCode(@NonNull Integer code) { this.code = code;  }
}
