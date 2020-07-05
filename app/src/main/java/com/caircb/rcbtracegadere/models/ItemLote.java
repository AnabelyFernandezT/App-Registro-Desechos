package com.caircb.rcbtracegadere.models;

import java.util.Date;

public class ItemLote {
    private int idLoteContenedor;
    private String codigoLote;
    private Date fechaRegistro;
    private int idDestinatarioFinRutaCatalogo;
    private String nombreDestinatarioFinRutaCatalogo;
    private String numeroManifiesto;
    private String subRuta;
    private String ruta;
    private String placaVehiculo;

    public int getIdLoteContenedor() {
        return idLoteContenedor;
    }

    public void setIdLoteContenedor(int idLoteContenedor) {
        this.idLoteContenedor = idLoteContenedor;
    }

    public String getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdDestinatarioFinRutaCatalogo() {
        return idDestinatarioFinRutaCatalogo;
    }

    public void setIdDestinatarioFinRutaCatalogo(int idDestinatarioFinRutaCatalogo) {
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }

    public String getNombreDestinatarioFinRutaCatalogo() {
        return nombreDestinatarioFinRutaCatalogo;
    }

    public void setNombreDestinatarioFinRutaCatalogo(String nombreDestinatarioFinRutaCatalogo) {
        this.nombreDestinatarioFinRutaCatalogo = nombreDestinatarioFinRutaCatalogo;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getSubRuta() {
        return subRuta;
    }

    public void setSubRuta(String subRuta) {
        this.subRuta = subRuta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}
