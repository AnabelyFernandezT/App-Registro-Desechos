package com.caircb.rcbtracegadere.models.response;

public class DtoIdentificacion {
    private String ecuatorianoCedula;
    private String ecuatorianoNombre;

    public DtoIdentificacion() {
    }

    public String getEcuatorianoCedula() {
        return ecuatorianoCedula;
    }

    public void setEcuatorianoCedula(String ecuatorianoCedula) {
        this.ecuatorianoCedula = ecuatorianoCedula;
    }

    public String getEcuatorianoNombre() {
        return ecuatorianoNombre;
    }

    public void setEcuatorianoNombre(String ecuatorianoNombre) {
        this.ecuatorianoNombre = ecuatorianoNombre;
    }
}
