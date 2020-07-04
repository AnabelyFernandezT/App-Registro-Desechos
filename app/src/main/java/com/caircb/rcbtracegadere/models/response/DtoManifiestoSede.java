package com.caircb.rcbtracegadere.models.response;

import java.util.Date;
import java.util.List;

public class DtoManifiestoSede {
    private Integer idAppManifiesto;
    private String numeroManifiesto;
    private String nombreCliente;
    private List<DtoManifiestoDetalleSede> hojaRutaDetalle;

    public DtoManifiestoSede() {
    }

    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
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
}
