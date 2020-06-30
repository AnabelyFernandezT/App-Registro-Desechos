package com.caircb.rcbtracegadere.models.response;

public class DtoFindRutas {

    private Integer idSubRuta;
    private String nombreSubRuta;

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
}
