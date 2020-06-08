package com.caircb.rcbtracegadere.models;

public class ItemFoto {
    private Integer code;
    private String foto;

    private Integer tipo;
    private String fotoUrl;


    public ItemFoto(Integer code, String foto, Integer tipo, String fotoUrl) {
        this.code = code;
        this.foto = foto;
        this.tipo = tipo;
        this.fotoUrl = fotoUrl;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
