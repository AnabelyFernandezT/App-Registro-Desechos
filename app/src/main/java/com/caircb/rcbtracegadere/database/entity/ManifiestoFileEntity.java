package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoFileEntity.TABLE))
public class ManifiestoFileEntity {
    public static final String TABLE = "tb_manifiestos_file";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idAppManifiesto;

    private Integer idCatalogo;

    @NonNull
    private Integer tipo;

    private Integer code;

    private String file;

    private String fileUrl;

    @NonNull
    private Boolean sincronizado;

    @NonNull
    private Integer status;

    public ManifiestoFileEntity() {
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

    public Integer getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Integer idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    @NonNull
    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull Integer tipo) {
        this.tipo = tipo;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @NonNull
    public Boolean getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(@NonNull Boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    @NonNull
    public Integer getStatus() {
        return status;
    }

    public void setStatus(@NonNull Integer status) {
        this.status = status;
    }
}
