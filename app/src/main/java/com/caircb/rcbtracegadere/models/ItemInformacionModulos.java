package com.caircb.rcbtracegadere.models;

public class ItemInformacionModulos {
    private String ruta;
    private String subruta;
    private String placa;
    private String chofer;
    private String auxiliarRecoleccion1;
    private String auxiliarRecoleccion2;
    private Double kilometrajeInicio;
    private Integer residuoSujetoFiscalizacion;
    private Integer requiereDevolucioneRecipientes;
    private Integer tieneDisponibilidadMontacarga;
    private Integer tieneDisponibilidadBalanza;
    private Integer requiereIncineracionPresenciada;
    private String ObservacionResuduos;

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getSubruta() {
        return subruta;
    }

    public void setSubruta(String subruta) {
        this.subruta = subruta;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getChofer() {
        return chofer;
    }

    public void setChofer(String chofer) {
        this.chofer = chofer;
    }

    public String getAuxiliarRecoleccion1() {
        return auxiliarRecoleccion1;
    }

    public void setAuxiliarRecoleccion1(String auxiliarRecoleccion1) {
        this.auxiliarRecoleccion1 = auxiliarRecoleccion1;
    }

    public String getAuxiliarRecoleccion2() {
        return auxiliarRecoleccion2;
    }

    public void setAuxiliarRecoleccion2(String auxiliarRecoleccion2) {
        this.auxiliarRecoleccion2 = auxiliarRecoleccion2;
    }

    public Double getKilometrajeInicio() {
        return kilometrajeInicio;
    }

    public void setKilometrajeInicio(Double kilometrajeInicio) {
        this.kilometrajeInicio = kilometrajeInicio;
    }

    public Integer getResiduoSujetoFiscalizacion() {
        return residuoSujetoFiscalizacion;
    }

    public void setResiduoSujetoFiscalizacion(Integer residuoSujetoFiscalizacion) {
        this.residuoSujetoFiscalizacion = residuoSujetoFiscalizacion;
    }

    public Integer getRequiereDevolucioneRecipientes() {
        return requiereDevolucioneRecipientes;
    }

    public void setRequiereDevolucioneRecipientes(Integer requiereDevolucioneRecipientes) {
        this.requiereDevolucioneRecipientes = requiereDevolucioneRecipientes;
    }

    public Integer getTieneDisponibilidadMontacarga() {
        return tieneDisponibilidadMontacarga;
    }

    public void setTieneDisponibilidadMontacarga(Integer tieneDisponibilidadMontacarga) {
        this.tieneDisponibilidadMontacarga = tieneDisponibilidadMontacarga;
    }

    public Integer getTieneDisponibilidadBalanza() {
        return tieneDisponibilidadBalanza;
    }

    public void setTieneDisponibilidadBalanza(Integer tieneDisponibilidadBalanza) {
        this.tieneDisponibilidadBalanza = tieneDisponibilidadBalanza;
    }

    public Integer getRequiereIncineracionPresenciada() {
        return requiereIncineracionPresenciada;
    }

    public void setRequiereIncineracionPresenciada(Integer requiereIncineracionPresenciada) {
        this.requiereIncineracionPresenciada = requiereIncineracionPresenciada;
    }

    public String getObservacionResuduos() {
        return ObservacionResuduos;
    }

    public void setObservacionResuduos(String observacionResuduos) {
        ObservacionResuduos = observacionResuduos;
    }
}
