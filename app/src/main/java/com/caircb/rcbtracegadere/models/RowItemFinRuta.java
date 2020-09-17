package com.caircb.rcbtracegadere.models;

public class RowItemFinRuta {

    private String fecha;
    private String manifiesto;
    private Integer f55x50;
    private Integer f63x76;
    private Integer fPc1;
    private Integer fPc2;
    private Integer fPc3;

    public RowItemFinRuta(String fecha, String manifiesto, Integer f55x50, Integer f63x76, Integer fPc1, Integer fPc2, Integer fPc3) {
        this.fecha = fecha;
        this.manifiesto = manifiesto;
        this.f55x50 = f55x50;
        this.f63x76 = f63x76;
        this.fPc1 = fPc1;
        this.fPc2 = fPc2;
        this.fPc3 = fPc3;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getManifiesto() {
        return manifiesto;
    }

    public void setManifiesto(String manifiesto) {
        this.manifiesto = manifiesto;
    }

    public Integer getF55x50() {
        return f55x50;
    }

    public void setF55x50(Integer f55x50) {
        this.f55x50 = f55x50;
    }

    public Integer getF63x76() {
        return f63x76;
    }

    public void setF63x76(Integer f63x76) {
        this.f63x76 = f63x76;
    }

    public Integer getfPc1() {
        return fPc1;
    }

    public void setfPc1(Integer fPc1) {
        this.fPc1 = fPc1;
    }

    public Integer getfPc2() {
        return fPc2;
    }

    public void setfPc2(Integer fPc2) {
        this.fPc2 = fPc2;
    }

    public Integer getfPc3() {
        return fPc3;
    }

    public void setfPc3(Integer fPc3) {
        this.fPc3 = fPc3;
    }
}
