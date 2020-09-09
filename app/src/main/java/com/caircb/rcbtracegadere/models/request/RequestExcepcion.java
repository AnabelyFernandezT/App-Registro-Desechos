package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestExcepcion {
    private Integer idDestinatarioFinRutaCat;
    private Integer tipo;
    private Integer idLoteContenedor;
    private Date fecha;
    private Integer idTransporteVehiculoLote;


    public Integer getIdDestinatarioFinRutaCat() {
        return idDestinatarioFinRutaCat;
    }

    public void setIdDestinatarioFinRutaCat(Integer idDestinatarioFinRutaCat) {
        this.idDestinatarioFinRutaCat = idDestinatarioFinRutaCat;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getIdLoteContenedor() {
        return idLoteContenedor;
    }

    public void setIdLoteContenedor(Integer idLoteContenedor) {
        this.idLoteContenedor = idLoteContenedor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getIdTransporteVehiculoLote() {
        return idTransporteVehiculoLote;
    }

    public void setIdTransporteVehiculoLote(Integer idTransporteVehiculoLote) {
        this.idTransporteVehiculoLote = idTransporteVehiculoLote;
    }
}
