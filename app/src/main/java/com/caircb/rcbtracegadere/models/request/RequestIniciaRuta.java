package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestIniciaRuta {
    //private String id;
    private Integer idTransportistaRecolector;
    private Integer idTransporteRecolector;
    private Integer idSubRuta;
    private Date fechaDispositivo;
    private String kilometraje;
    private Integer tipo;
    private Integer idImpresora;

    public Integer getIdTransporteRecolector() {
        return idTransporteRecolector;
    }

    public void setIdTransporteRecolector(Integer idTransporteRecolector) {
        this.idTransporteRecolector = idTransporteRecolector;
    }

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }

    public Date getFechaDispositivo() {
        return fechaDispositivo;
    }

    public void setFechaDispositivo(Date fechaDispositivo) {
        this.fechaDispositivo = fechaDispositivo;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }

    public Integer getIdImpresora() {
        return idImpresora;
    }

    public void setIdImpresora(Integer idImpresora) {
        this.idImpresora = idImpresora;
    }
}
