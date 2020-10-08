package com.caircb.rcbtracegadere.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemEtiqueta {
    private Integer idAppManifiestoDetalle;
    private String cliente;
    private String numeroManifiesto;
    private Date fechaRecoleccion;
    private String residuo;
    private String tratamiento;
    private double peso;
    private String codigoQr;
    private Integer indexEtiqueta;
    private Integer totalEtiqueta;
    private String destinatario;
    private Integer incineracion;



    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(Integer idAppManifiestoDetalle) {
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public Date getFechaRecoleccion() {
        return  fechaRecoleccion;
    }

    public void setFechaRecoleccion(Date fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public String getResiduo() {
        return residuo;
    }

    public void setResiduo(String residuo) {
        this.residuo = residuo;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public Integer getIndexEtiqueta() {
        return indexEtiqueta;
    }

    public void setIndexEtiqueta(Integer indexEtiqueta) {
        this.indexEtiqueta = indexEtiqueta;
    }

    public Integer getTotalEtiqueta() {
        return totalEtiqueta;
    }

    public void setTotalEtiqueta(Integer totalEtiqueta) {
        this.totalEtiqueta = totalEtiqueta;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Integer getIncineracion() {
        return incineracion;
    }

    public void setIncineracion(Integer incineracion) {
        this.incineracion = incineracion;
    }
}
