package com.caircb.rcbtracegadere.models;

import java.util.Date;

public class ItemEtiquetaHospitalario {
    private String nombreGenerador;
    private String puntoRecoleccion;
    private String rucGenerador;
    private String fechaRecolecion;
    private String claveManifiestoSap;
    private String claveManifiesto;
    private String direccion;
    private String destinatario;
    private String firmaNombreGenerador;
    private String firmaNombreTransportista;
    private String firmaCedulaGenerador;
    private String firmaCedulaTransportista;

    public ItemEtiquetaHospitalario(String nombreGenerador, String puntoRecoleccion, String rucGenerador, String fechaRecolecion, String claveManifiestoSap, String claveManifiesto, String direccion, String destinatario, String firmaNombreGenerador, String firmaNombreTransportista, String firmaCedulaGenerador, String firmaCedulaTransportista) {
        this.nombreGenerador = nombreGenerador;
        this.puntoRecoleccion = puntoRecoleccion;
        this.rucGenerador = rucGenerador;
        this.fechaRecolecion = fechaRecolecion;
        this.claveManifiestoSap = claveManifiestoSap;
        this.claveManifiesto = claveManifiesto;
        this.direccion = direccion;
        this.destinatario = destinatario;
        this.firmaNombreGenerador = firmaNombreGenerador;
        this.firmaNombreTransportista = firmaNombreTransportista;
        this.firmaCedulaGenerador = firmaCedulaGenerador;
        this.firmaCedulaTransportista = firmaCedulaTransportista;
    }

    public String getNombreGenerador() {
        return nombreGenerador;
    }

    public void setNombreGenerador(String nombreGenerador) {
        this.nombreGenerador = nombreGenerador;
    }

    public String getPuntoRecoleccion() {
        return puntoRecoleccion;
    }

    public void setPuntoRecoleccion(String puntoRecoleccion) {
        this.puntoRecoleccion = puntoRecoleccion;
    }

    public String getRucGenerador() {
        return rucGenerador;
    }

    public void setRucGenerador(String rucGenerador) {
        this.rucGenerador = rucGenerador;
    }

    public String getFechaRecolecion() {
        return fechaRecolecion;
    }

    public void setFechaRecolecion(String fechaRecolecion) {
        this.fechaRecolecion = fechaRecolecion;
    }

    public String getClaveManifiestoSap() {
        return claveManifiestoSap;
    }

    public void setClaveManifiestoSap(String claveManifiestoSap) {
        this.claveManifiestoSap = claveManifiestoSap;
    }

    public String getClaveManifiesto() {
        return claveManifiesto;
    }

    public void setClaveManifiesto(String claveManifiesto) {
        this.claveManifiesto = claveManifiesto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
}
