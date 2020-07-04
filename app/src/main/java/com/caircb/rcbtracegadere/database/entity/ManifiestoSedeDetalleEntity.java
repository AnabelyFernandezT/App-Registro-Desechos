package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoSedeDetalleEntity.TABLE))
public class ManifiestoSedeDetalleEntity {
    public static final String TABLE = "tb_manifiestos_sede_detalle";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idManifiestoPadre;

    private Integer idManifiestoHijo;

    private Integer idManifiestoDetalle;

    private String codigoMae;

    private String codigo;

    private String nombreDesecho;



    public ManifiestoSedeDetalleEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(@NonNull Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }

    public Integer getIdManifiestoHijo() {
        return idManifiestoHijo;
    }

    public void setIdManifiestoHijo(Integer idManifiestoHijo) {
        this.idManifiestoHijo = idManifiestoHijo;
    }

    public Integer getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(Integer idManifiestoDetalle) {
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    public String getCodigoMae() {
        return codigoMae;
    }

    public void setCodigoMae(String codigoMae) {
        this.codigoMae = codigoMae;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreDesecho() {
        return nombreDesecho;
    }

    public void setNombreDesecho(String nombreDesecho) {
        this.nombreDesecho = nombreDesecho;
    }
}


