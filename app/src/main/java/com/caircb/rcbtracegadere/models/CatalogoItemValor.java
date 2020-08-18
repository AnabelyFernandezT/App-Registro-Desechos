package com.caircb.rcbtracegadere.models;

public class CatalogoItemValor {
    private int idCatalogo;
    private String valor;
    private String tipo;
    private boolean impresion;
    private int numeroBulto;
    private double pesoTaraBulto;

    public CatalogoItemValor(int idCatalogo, String valor, String tipo, boolean impresion, int numeroBulto) { //
        this.idCatalogo = idCatalogo;
        this.valor = valor;
        this.tipo = tipo;
        this.impresion = impresion;
        this.numeroBulto = numeroBulto;
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


    public int getNumeroBulto() {
        return numeroBulto;
    }

    public void setNumeroBulto(int numeroBulto) {
        this.numeroBulto = numeroBulto;
    }

    public double getPesoTaraBulto() {
        return pesoTaraBulto;
    }

    public void setPesoTaraBulto(double pesoTaraBulto) {
        this.pesoTaraBulto = pesoTaraBulto;
    }
}
