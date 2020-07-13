package com.caircb.rcbtracegadere.models.response;

public class DtoInicioRuta {
    private Integer idRutaInicioFin;
    private Integer idSubRuta;
    private Integer idRuta;
    private String placa;
    private Boolean estado;
    private String fechaInicio;
    private String fechaFin;
    private String kilometrajeInicio;
    private String kilometrajeFin;



    public DtoInicioRuta() {
    }

    public Integer getIdRutaInicioFin() {
        return idRutaInicioFin;
    }

    public void setIdRutaInicioFin(Integer idRutaInicioFin) {
        this.idRutaInicioFin = idRutaInicioFin;
    }

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getKilometrajeInicio() {
        return kilometrajeInicio;
    }

    public void setKilometrajeInicio(String kilometrajeInicio) {
        this.kilometrajeInicio = kilometrajeInicio;
    }

    public String getKilometrajeFin() {
        return kilometrajeFin;
    }

    public void setKilometrajeFin(String kilometrajeFin) {
        this.kilometrajeFin = kilometrajeFin;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }
}
