package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ConsultarFirmaUsuarioEntity.TABLE))
public class ConsultarFirmaUsuarioEntity {
    public static final String TABLE = "tb_firma_usuario";

    @PrimaryKey(autoGenerate = true)
    private Integer idFirma;
    private String firmaBase64;
    private Integer idFinRutaCatalogo;

    public ConsultarFirmaUsuarioEntity(){

    }

    public Integer getIdFirma() {
        return idFirma;
    }

    public void setIdFirma(Integer idFirma) {
        this.idFirma = idFirma;
    }

    public String getFirmaBase64() {
        return firmaBase64;
    }

    public void setFirmaBase64(String firmaBase64) {
        this.firmaBase64 = firmaBase64;
    }

    public Integer getIdFinRutaCatalogo() {
        return idFinRutaCatalogo;
    }

    public void setIdFinRutaCatalogo(Integer idFinRutaCatalogo) {
        this.idFinRutaCatalogo = idFinRutaCatalogo;
    }
}
