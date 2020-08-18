package com.caircb.rcbtracegadere.models.response;

public class DtoFindRutas {

    private Integer idSubRuta;
    private String nombreSubRuta;
    private String placa;
    private String nombreChofer;
    private String nombreAuxiliar;
    private String nombreConductor;
    private String nombreRuta;
    private  Integer tiposubruta;

    public DtoFindRutas() {
    }

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }

    public String getNombreRuta() {
        return nombreSubRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreSubRuta = nombreRuta;
    }

    public String getNombreSubRuta() { return nombreSubRuta; }

    public void setNombreSubRuta(String nombreSubRuta) { this.nombreSubRuta = nombreSubRuta; }

    public String getPlaca() { return placa; }

    public void setPlaca(String placa) { this.placa = placa; }

    public String getNombreChofer() { return nombreChofer; }

    public void setNombreChofer(String nombreChofer) { this.nombreChofer = nombreChofer; }

    public String getNombreAuxiliar() { return nombreAuxiliar; }

    public void setNombreAuxiliar(String nombreAuxiliar) { this.nombreAuxiliar = nombreAuxiliar; }

    public String getNombreConductor() { return nombreConductor; }

    public void setNombreConductor(String nombreConductor) { this.nombreConductor = nombreConductor; }

    public Integer getTiposubruta() {
        return tiposubruta;
    }

    public void setTiposubruta(Integer tiposubruta) {
        this.tiposubruta = tiposubruta;
    }
}
