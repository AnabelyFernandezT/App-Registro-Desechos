package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoDetallePesosEntity.TABLE))
public class ManifiestoDetallePesosEntity {

    public static final String TABLE = "tb_manifiesto_detalle_pesos";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private double valor;

    @NonNull
    private Integer idAppManifiesto;

    @NonNull
    private Integer idAppManifiestoDetalle;

    private String descripcion;

    private Integer tipoPaquete;

    @NonNull
    private String codeQr;

    private boolean impresion;

    public ManifiestoDetallePesosEntity(double valor, @NonNull Integer idAppManifiesto, @NonNull Integer idAppManifiestoDetalle, String descripcion, Integer tipoPaquete, @NonNull String codeQr, boolean impresion) {
        this.valor = valor;
        this.idAppManifiesto = idAppManifiesto;
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
        this.descripcion = descripcion;
        this.tipoPaquete = tipoPaquete;
        this.codeQr = codeQr;
        this.impresion = impresion;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @NonNull
    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(@NonNull Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    @NonNull
    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(@NonNull Integer idAppManifiestoDetalle) {
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(Integer tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    @NonNull
    public String getCodeQr() {
        return codeQr;
    }

    public void setCodeQr(@NonNull String codeQr) {
        this.codeQr = codeQr;
    }

    public boolean isImpresion() {
        return impresion;
    }

    public void setImpresion(boolean impresion) {
        this.impresion = impresion;
    }
}
