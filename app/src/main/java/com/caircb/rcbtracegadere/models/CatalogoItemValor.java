package com.caircb.rcbtracegadere.models;

public class CatalogoItemValor {
    private int idCatalogo;
    private String valor;
    private String tipo;

    public CatalogoItemValor(int idCatalogo, String valor, String tipo) {
        this.idCatalogo = idCatalogo;
        this.valor = valor;
        this.tipo = tipo;
    }

    public int getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {  return tipo; }

    public void setTipo(String tipo) { this.tipo = tipo; }
}
