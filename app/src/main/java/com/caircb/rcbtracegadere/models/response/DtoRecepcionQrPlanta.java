package com.caircb.rcbtracegadere.models.response;

import java.math.BigDecimal;
import java.util.List;

public class DtoRecepcionQrPlanta {

    private BigDecimal pesoTotalLote;
    private Integer cantidadTotalBultos;
    private Integer cantidadTotalManifiestos;
    private String numerosManifiesto;
    private List<DtoHojaRutaDetallePlantaLote> hojaRutaDetallePlantaLote;

    public DtoRecepcionQrPlanta(){

    }

    public BigDecimal getPesoTotalLote() {
        return pesoTotalLote;
    }

    public void setPesoTotalLote(BigDecimal pesoTotalLote) {
        this.pesoTotalLote = pesoTotalLote;
    }

    public Integer getCantidadTotalBultos() {
        return cantidadTotalBultos;
    }

    public void setCantidadTotalBultos(Integer cantidadTotalBultos) {
        this.cantidadTotalBultos = cantidadTotalBultos;
    }

    public Integer getCantidadTotalManifiestos() {
        return cantidadTotalManifiestos;
    }

    public void setCantidadTotalManifiestos(Integer cantidadTotalManifiestos) {
        this.cantidadTotalManifiestos = cantidadTotalManifiestos;
    }

    public String getNumerosManifiesto() {
        return numerosManifiesto;
    }

    public void setNumerosManifiesto(String numerosManifiesto) {
        this.numerosManifiesto = numerosManifiesto;
    }

    public List<DtoHojaRutaDetallePlantaLote> getHojaRutaDetallePlantaLote() {
        return hojaRutaDetallePlantaLote;
    }

    public void setHojaRutaDetallePlantaLote(List<DtoHojaRutaDetallePlantaLote> hojaRutaDetallePlantaLote) {
        this.hojaRutaDetallePlantaLote = hojaRutaDetallePlantaLote;
    }
}
