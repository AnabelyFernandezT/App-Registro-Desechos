package com.caircb.rcbtracegadere.models.response;

import java.util.List;

public class DtoManifiestoPlantaObservacion {
    private Integer idManifiesto;
    private double pesoRecolectado;
    private double pesoPlanta;
    private String observacionPeso;
    private String observacionOtra;
    public DtoManifiestoPlantaObservacion() {
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public double getPesoRecolectado() {
        return pesoRecolectado;
    }

    public void setPesoRecolectado(double pesoRecolectado) {
        this.pesoRecolectado = pesoRecolectado;
    }

    public double getPesoPlanta() {
        return pesoPlanta;
    }

    public void setPesoPlanta(double pesoPlanta) {
        this.pesoPlanta = pesoPlanta;
    }

    public String getObservacionPeso() {
        return observacionPeso;
    }

    public void setObservacionPeso(String observacionPeso) {
        this.observacionPeso = observacionPeso;
    }

    public String getObservacionOtra() {
        return observacionOtra;
    }

    public void setObservacionOtra(String observacionOtra) {
        this.observacionOtra = observacionOtra;
    }
}
