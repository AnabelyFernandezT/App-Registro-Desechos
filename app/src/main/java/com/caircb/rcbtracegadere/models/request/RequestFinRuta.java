package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestFinRuta {
    private Integer id;
    private Integer idSubRuta;
    private Date fechaDispositivo;
    private String kilometraje;
    private Integer tipo;
    private Integer idDestinatarioFinRutaCatalogo;
    private Integer idTransportistaRecolector;
    private Integer idLote;
    private Integer idInsumo;
    private Integer loteProcesoId;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdDestinatarioFinRutaCatalogo() {
        return idDestinatarioFinRutaCatalogo;
    }

    public void setIdDestinatarioFinRutaCatalogo(Integer idDestinatarioFinRutaCatalogo) {
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }

    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }

    public Integer getLoteProcesoId() {
        return loteProcesoId;
    }

    public void setLoteProcesoId(Integer loteProcesoId) {
        this.loteProcesoId = loteProcesoId;
    }
}
