package com.caircb.rcbtracegadere.models.response;

public class DtoPaquetes {

    private Integer idPaquete;
    private Integer i;
    private String nombrePaquete;
    private String tamanoFunda;
    private String tamanoGuardian;
    private Boolean flagAdicionales;
    private Boolean flagAdicionalesFundas;
    private Boolean flagAdicionalesGuardian;
    private Integer paquetePorRecoleccion;
    private Integer entregaFundas;
    private Integer entregaGuardianes;


    public DtoPaquetes() {
    }


    public Integer getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Integer idPaquete) {
        this.idPaquete = idPaquete;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public String getNombrePaquete() {
        return nombrePaquete;
    }

    public void setNombrePaquete(String nombrePaquete) {
        this.nombrePaquete = nombrePaquete;
    }

    public String getTamanoFunda() {
        return tamanoFunda;
    }

    public void setTamanoFunda(String tamanoFunda) {
        this.tamanoFunda = tamanoFunda;
    }

    public String getTamanoGuardian() {
        return tamanoGuardian;
    }

    public void setTamanoGuardian(String tamanoGuardian) {
        this.tamanoGuardian = tamanoGuardian;
    }

    public Boolean getFlagAdicionales() {
        return flagAdicionales;
    }

    public void setFlagAdicionales(Boolean flagAdicionales) {
        this.flagAdicionales = flagAdicionales;
    }

    public Boolean getFlagAdicionalesFundas() {
        return flagAdicionalesFundas;
    }

    public void setFlagAdicionalesFundas(Boolean flagAdicionalesFundas) {
        this.flagAdicionalesFundas = flagAdicionalesFundas;
    }

    public Boolean getFlagAdicionalesGuardian() {
        return flagAdicionalesGuardian;
    }

    public void setFlagAdicionalesGuardian(Boolean flagAdicionalesGuardian) {
        this.flagAdicionalesGuardian = flagAdicionalesGuardian;
    }

    public Integer getPaquetePorRecoleccion() {
        return paquetePorRecoleccion;
    }

    public void setPaquetePorRecoleccion(Integer paquetePorRecoleccion) {
        this.paquetePorRecoleccion = paquetePorRecoleccion;
    }

    public Integer getEntregaFundas() {
        return entregaFundas;
    }

    public void setEntregaFundas(Integer entregaFundas) {
        this.entregaFundas = entregaFundas;
    }

    public Integer getEntregaGuardianes() {
        return entregaGuardianes;
    }

    public void setEntregaGuardianes(Integer entregaGuardianes) {
        this.entregaGuardianes = entregaGuardianes;
    }
}
