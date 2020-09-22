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
    private Integer idInsumo;
    private String fechaEntrega;
    private String fechaLiquidacion;
    private Integer funda63;
    private Integer funda55;
    private Integer pc1;
    private Integer pc2;
    private Integer pc4;


    public RutasEntity(Integer codigo, String nombre, Integer tiposubruta, Integer idInsumo, String fechaEntrega, String fechaLiquidacion, Integer funda63, Integer funda55, Integer pc1, Integer pc2, Integer pc4) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tiposubruta = tiposubruta;
        this.idInsumo = idInsumo;
        this.fechaEntrega = fechaEntrega;
        this.fechaLiquidacion = fechaLiquidacion;
        this.funda55 = funda55;
        this.funda63 = funda63;
        this.pc1=pc1;
        this.pc2=pc2;
        this.pc4=pc4;
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

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(String fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public Integer getFunda63() {
        return funda63;
    }

    public void setFunda63(Integer funda63) {
        this.funda63 = funda63;
    }

    public Integer getFunda55() {
        return funda55;
    }

    public void setFunda55(Integer funda55) {
        this.funda55 = funda55;
    }

    public Integer getPc1() {
        return pc1;
    }

    public void setPc1(Integer pc1) {
        this.pc1 = pc1;
    }

    public Integer getPc2() {
        return pc2;
    }

    public void setPc2(Integer pc2) {
        this.pc2 = pc2;
    }

    public Integer getPc4() {
        return pc4;
    }

    public void setPc4(Integer pc4) {
        this.pc4 = pc4;
    }
}
