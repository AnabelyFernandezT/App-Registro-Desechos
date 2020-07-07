package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestInicioLoteSede {
    private Integer idDestinatarioFinRutaCat;
    private Integer tipo;
    private Date fecha;
    private Integer idTransportistaRecolector;
    private Integer idTransporteVehiculoLote;


    public RequestInicioLoteSede() {
    }

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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }

    public Integer getIdTransporteVehiculoLote() {
        return idTransporteVehiculoLote;
    }

    public void setIdTransporteVehiculoLote(Integer idTransporteVehiculoLote) {
        this.idTransporteVehiculoLote = idTransporteVehiculoLote;
    }
}
