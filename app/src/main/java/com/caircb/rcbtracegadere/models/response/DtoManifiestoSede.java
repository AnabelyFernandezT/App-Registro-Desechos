package com.caircb.rcbtracegadere.models.response;

import java.util.Date;
import java.util.List;

public class DtoManifiestoSede {
    private Integer idManifiestoPadre;
    private String manifiestos;
    private String nombreCliente;
    private List<DtoManifiestoDetalleSede> hojaRutaDetalle;

    public DtoManifiestoSede() {
    }


    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }

    public String getManifiestos() {
        return manifiestos;
    }

    public void setManifiestos(String manifiestos) {
        this.manifiestos = manifiestos;
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
