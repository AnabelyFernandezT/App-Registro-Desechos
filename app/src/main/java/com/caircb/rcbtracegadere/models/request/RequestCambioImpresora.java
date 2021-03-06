package com.caircb.rcbtracegadere.models.request;

public class RequestCambioImpresora {

    private Integer idInicioFinRuta;
    private Integer idImpresora;

    public RequestCambioImpresora(Integer idInicioFinRuta, Integer idImpresora) {
        this.idInicioFinRuta = idInicioFinRuta;
        this.idImpresora = idImpresora;
    }

    public Integer getIdInicioFinRuta() {
        return idInicioFinRuta;
    }

    public void setIdInicioFinRuta(Integer idInicioFinRuta) {
        this.idInicioFinRuta = idInicioFinRuta;
    }

    public Integer getIdImpresora() {
        return idImpresora;
    }

    public void setIdImpresora(Integer idImpresora) {
        this.idImpresora = idImpresora;
    }
}
