package com.caircb.rcbtracegadere.models.response;

import java.util.Date;
import java.util.List;

public class DtoManifiestoSede {
    private Integer idManifiesto;
    private String numeroManifiesto;
    private String nombreCliente;
    private Integer idTransporteVehiculo;
    private Integer bultosRegistrados;
    private Integer bultosTotal;
    private Integer estado;
    private List<DtoManifiestoDetalleSede> hojaRutaDetalle;

    public DtoManifiestoSede() {
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public Integer getBultosRegistrados() {
        return bultosRegistrados;
    }

    public void setBultosRegistrados(Integer bultosRegistrados) {
        this.bultosRegistrados = bultosRegistrados;
    }

    public Integer getBultosTotal() {
        return bultosTotal;
    }

    public void setBultosTotal(Integer bultosTotal) {
        this.bultosTotal = bultosTotal;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getIdAppManifiesto() {
        return idManifiesto;
    }

    public void setIdAppManifiesto(Integer idAppManifiesto) {
        this.idManifiesto = idAppManifiesto;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<DtoManifiestoDetalleSede> getHojaRutaDetalle() {
        return hojaRutaDetalle;
    }

    public void setHojaRutaDetalle(List<DtoManifiestoDetalleSede> hojaRutaDetalle) {
        this.hojaRutaDetalle = hojaRutaDetalle;
    }

    public Integer getIdTransporteVehiculo() {
        return idTransporteVehiculo;
    }

    public void setIdTransporteVehiculo(Integer idTransporteVehiculo) {
        this.idTransporteVehiculo = idTransporteVehiculo;
    }
}
