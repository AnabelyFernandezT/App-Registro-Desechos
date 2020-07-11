package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = (LotePadreEntity.TABLE))
public class LotePadreEntity {
    public static final String TABLE = "tb_lotes_padre";

    @PrimaryKey(autoGenerate = true)
    private Integer idLotePadre;

    private Integer idManifiestoPadre;

    private String manifiestos;

    private Double total;

    private String nombreCliente;

    private String numeroManifiestoPadre;

    private String placaVehiculo;

    public LotePadreEntity() {
    }

    public Integer getIdLotePadre() {
        return idLotePadre;
    }

    public void setIdLotePadre(Integer idLotePadre) {
        this.idLotePadre = idLotePadre;
    }

    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }

    public String getManifiestos() {
        return manifiestos;
    }

    public void setManifiestos(String manifiestos) {
        this.manifiestos = manifiestos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNumeroManifiestoPadre() {
        return numeroManifiestoPadre;
    }

    public void setNumeroManifiestoPadre(String numeroManifiestoPadre) {
        this.numeroManifiestoPadre = numeroManifiestoPadre;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}
