package com.caircb.rcbtracegadere.models;

public class ItemLote {
    private int idAppLote;
    private int numeroLote;
    private String rutasRecolectadas;
    private String subRuta;
    private String placa;
    private String chofer;
    private String operadores;

    public int getIdAppLote() {
        return idAppLote;
    }

    public void setIdAppLote(int idAppLote) {
        this.idAppLote = idAppLote;
    }

    public int getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(int numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getRutasRecolectadas() {
        return rutasRecolectadas;
    }

    public void setRutasRecolectadas(String rutasRecolectadas) {
        this.rutasRecolectadas = rutasRecolectadas;
    }

    public String getSubRuta() {
        return subRuta;
    }

    public void setSubRuta(String subRuta) {
        this.subRuta = subRuta;
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

    public String getOperadores() {
        return operadores;
    }

    public void setOperadores(String operadores) {
        this.operadores = operadores;
    }
}
