package com.caircb.rcbtracegadere.models.request;

public class RequestNuevoKilometraje {

    Integer idSubRuta;
    String nuevoKilometraje;
    String kilometrajeFinal;

    public RequestNuevoKilometraje() {
    }

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }

    public String getNuevoKilometraje() {
        return nuevoKilometraje;
    }

    public void setNuevoKilometraje(String nuevoKilometraje) {
        this.nuevoKilometraje = nuevoKilometraje;
    }

    public String getKilometrajeFinal() {
        return kilometrajeFinal;
    }

    public void setKilometrajeFinal(String kilometrajeFinal) {
        this.kilometrajeFinal = kilometrajeFinal;
    }
}
