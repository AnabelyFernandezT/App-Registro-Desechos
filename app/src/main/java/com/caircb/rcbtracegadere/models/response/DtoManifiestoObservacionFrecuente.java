package com.caircb.rcbtracegadere.models.response;

public class DtoManifiestoObservacionFrecuente {

    private Integer idAppManifiesto;
    private Integer idCatalogoObservacion;
    private boolean estadoChek;

    public DtoManifiestoObservacionFrecuente() {
    }

    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    public Integer getIdCatalogoObservacion() {
        return idCatalogoObservacion;
    }

    public void setIdCatalogoObservacion(Integer idCatalogoObservacion) {
        this.idCatalogoObservacion = idCatalogoObservacion;
    }

    public boolean isEstadoChek() {
        return estadoChek;
    }

    public void setEstadoChek(boolean estadoChek) {
        this.estadoChek = estadoChek;
    }
}
