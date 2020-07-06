package com.caircb.rcbtracegadere.models.request;

import java.math.BigDecimal;
import java.util.List;

public class RequestManifiestoDet {
    private Integer idAppManifiestoDetalle;
    private double peso;
    private double cantidad;
    private List<RequestManifiestoDetBultos> bultos;
    private Integer tipoBalanza;

    public RequestManifiestoDet(Integer idAppManifiestoDetalle, double peso, double cantidad, List<RequestManifiestoDetBultos> bultos, Integer tipoBalanza) {
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
        this.peso = peso;
        this.cantidad = cantidad;
        this.bultos = bultos;
        this.tipoBalanza = tipoBalanza;
    }

    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(Integer idAppManifiestoDetalle) {
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public List<RequestManifiestoDetBultos> getBultos() {
        return bultos;
    }

    public void setBultos(List<RequestManifiestoDetBultos> bultos) {
        this.bultos = bultos;
    }

    public Integer getTipoBalanza() {
        return tipoBalanza;
    }

    public void setTipoBalanza(Integer tipoBalanza) {
        this.tipoBalanza = tipoBalanza;
    }
}
