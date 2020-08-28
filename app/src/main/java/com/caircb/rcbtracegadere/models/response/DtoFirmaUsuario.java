package com.caircb.rcbtracegadere.models.response;

public class DtoFirmaUsuario {
    private String firmaBase64;
    private Integer idFinRutaCatalogo;

    public DtoFirmaUsuario() {

    }

    public String getFirmaBase64() {
        return firmaBase64;
    }

    public void setFirmaBase64(String firmaBase64) {
        this.firmaBase64 = firmaBase64;
    }

    public Integer getIdFinRutaCatalogo() {
        return idFinRutaCatalogo;
    }

    public void setIdFinRutaCatalogo(Integer idFinRutaCatalogo) {
        this.idFinRutaCatalogo = idFinRutaCatalogo;
    }
}
