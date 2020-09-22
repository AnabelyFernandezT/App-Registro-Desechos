package com.caircb.rcbtracegadere.models.response;

import com.itextpdf.text.pdf.PRIndirectReference;

public class DtoFindRutas {

    private Integer idSubRuta;
    private String nombreSubRuta;
    private String placa;
    private String nombreChofer;
    private String nombreAuxiliar;
    private String nombreConductor;
    private String nombreRuta;
    private Integer tiposubruta;
    private Integer idInsumo;
    private String fechaEntrega;
    private String fechaLiquidacion;
    private Integer funda63;
    private Integer funda55;
    private Integer pc1;
    private Integer pc2;
    private Integer pc4;

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

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(String fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public Integer getFunda63() {
        return funda63;
    }

    public void setFunda63(Integer funda63) {
        this.funda63 = funda63;
    }

    public Integer getFunda55() {
        return funda55;
    }

    public void setFunda55(Integer funda55) {
        this.funda55 = funda55;
    }

    public Integer getPc1() {
        return pc1;
    }

    public void setPc1(Integer pc1) {
        this.pc1 = pc1;
    }

    public Integer getPc2() {
        return pc2;
    }

    public void setPc2(Integer pc2) {
        this.pc2 = pc2;
    }

    public Integer getPc4() {
        return pc4;
    }

    public void setPc4(Integer pc4) {
        this.pc4 = pc4;
    }
}
