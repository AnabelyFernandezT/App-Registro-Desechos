package com.caircb.rcbtracegadere.models.response;

import java.util.Date;
import java.util.List;

public class DtoManifiestoSede {
    private Integer idManifiesto;
    private String numeroManifiesto;
    private String nombreCliente;
    private Integer idTransporteVehiculo;
    private List<DtoManifiestoDetalleSede> hojaRutaDetalle;

    public DtoManifiestoSede() {
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
