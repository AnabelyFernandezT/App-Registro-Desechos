package com.caircb.rcbtracegadere.models.request;

import java.math.BigDecimal;
import java.util.List;

public class RequestManifiestoDet {
    private Integer idAppManifiestoDetalle;
    private BigDecimal peso;
    private BigDecimal cantidad;
    private List<RequestManifiestoDetBultos> bultos;

    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(Integer idAppManifiestoDetalle) {
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public List<RequestManifiestoDetBultos> getBultos() {
        return bultos;
    }

    public void setBultos(List<RequestManifiestoDetBultos> bultos) {
        this.bultos = bultos;
    }
}
