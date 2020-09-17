package com.caircb.rcbtracegadere.models;

import java.util.Date;

public class ItemEtiquetaFinRuta {

    private Date fechaRecolecion;
    private String destinatario;
    private String firmaNombreGenerador;
    private String firmaNombreTransportista;
    private String firmaNombreGenerador2;
    private String firmaCedulaGenerador;
    private String firmaCedulaTransportista;
    private String subRuta;
    private String placaVehiculo;

    public ItemEtiquetaFinRuta(Date fechaRecolecion, String destinatario, String firmaNombreGenerador, String firmaNombreTransportista, String firmaNombreGenerador2, String firmaCedulaGenerador, String firmaCedulaTransportista,String subRuta,String placaVehiculo) {
        this.fechaRecolecion = fechaRecolecion;
        this.destinatario = destinatario;
        this.firmaNombreGenerador = firmaNombreGenerador;
        this.firmaNombreTransportista = firmaNombreTransportista;
        this.firmaNombreGenerador2 = firmaNombreGenerador2;
        this.firmaCedulaGenerador = firmaCedulaGenerador;
        this.firmaCedulaTransportista = firmaCedulaTransportista;
        this.subRuta = subRuta;
        this.placaVehiculo = placaVehiculo;
    }

    public Date getFechaRecolecion() {
        return fechaRecolecion;
    }

    public void setFechaRecolecion(Date fechaRecolecion) {
        this.fechaRecolecion = fechaRecolecion;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getFirmaNombreGenerador() {
        return firmaNombreGenerador;
    }

    public void setFirmaNombreGenerador(String firmaNombreGenerador) {
        this.firmaNombreGenerador = firmaNombreGenerador;
    }

    public String getFirmaNombreTransportista() {
        return firmaNombreTransportista;
    }

    public void setFirmaNombreTransportista(String firmaNombreTransportista) {
        this.firmaNombreTransportista = firmaNombreTransportista;
    }

    public String getFirmaNombreGenerador2() {
        return firmaNombreGenerador2;
    }

    public void setFirmaNombreGenerador2(String firmaNombreGenerador2) {
        this.firmaNombreGenerador2 = firmaNombreGenerador2;
    }

    public String getFirmaCedulaGenerador() {
        return firmaCedulaGenerador;
    }

    public void setFirmaCedulaGenerador(String firmaCedulaGenerador) {
        this.firmaCedulaGenerador = firmaCedulaGenerador;
    }

    public String getFirmaCedulaTransportista() {
        return firmaCedulaTransportista;
    }

    public void setFirmaCedulaTransportista(String firmaCedulaTransportista) {
        this.firmaCedulaTransportista = firmaCedulaTransportista;
    }

    public String getSubRuta() {
        return subRuta;
    }

    public void setSubRuta(String subRuta) {
        this.subRuta = subRuta;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}
