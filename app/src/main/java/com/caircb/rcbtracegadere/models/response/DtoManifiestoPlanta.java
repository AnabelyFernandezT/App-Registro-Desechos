package com.caircb.rcbtracegadere.models.response;

import java.util.List;

public class DtoManifiestoPlanta {
    private Integer idManifiesto;
    private String numeroManifiesto;
    private String nombreCliente;
    private Integer idTransporteVehiculo;
    private Integer estado;
    private List<DtoManifiestoDetalleSede> hojaRutaDetallePlanta;

    public DtoManifiestoPlanta() {
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
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

    public Integer getIdTransporteVehiculo() {
        return idTransporteVehiculo;
    }

    public void setIdTransporteVehiculo(Integer idTransporteVehiculo) {
        this.idTransporteVehiculo = idTransporteVehiculo;
    }

    public List<DtoManifiestoDetalleSede> getHojaRutaDetallePlanta() {
        return hojaRutaDetallePlanta;
    }

    public void setHojaRutaDetallePlanta(List<DtoManifiestoDetalleSede> hojaRutaDetallePlanta) {
        this.hojaRutaDetallePlanta = hojaRutaDetallePlanta;
    }
}
