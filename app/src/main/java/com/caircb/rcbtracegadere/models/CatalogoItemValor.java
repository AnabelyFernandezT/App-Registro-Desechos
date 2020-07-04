package com.caircb.rcbtracegadere.models;

public class CatalogoItemValor {
    private int idCatalogo;
    private String valor;
    private String tipo;
    private boolean impresion;

    public CatalogoItemValor(int idCatalogo, String valor, String tipo, boolean impresion) {
        this.idCatalogo = idCatalogo;
        this.valor = valor;
        this.tipo = tipo;
        this.impresion = impresion;
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

    public boolean isImpresion() {
        return impresion;
    }

    public void setImpresion(boolean impresion) {
        this.impresion = impresion;
    }
}
