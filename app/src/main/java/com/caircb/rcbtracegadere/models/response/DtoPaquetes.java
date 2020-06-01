package com.caircb.rcbtracegadere.models.response;

public class DtoPaquetes {

    private Integer idSistema;
    private Integer index;
    private String descripcion;
    private String funda;
    private String guardian;
    private Boolean flagAdicionales;
    private Boolean flagAdicionalFunda;
    private Boolean flagAdicionalGuardian;
    private Integer paquetePorRecolccion;

    public DtoPaquetes() {
    }

    public Integer getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Integer idSistema) {
        this.idSistema = idSistema;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFunda() {
        return funda;
    }

    public void setFunda(String funda) {
        this.funda = funda;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public Boolean getFlagAdicionales() {
        return flagAdicionales;
    }

    public void setFlagAdicionales(Boolean flagAdicionales) {
        this.flagAdicionales = flagAdicionales;
    }

    public Boolean getFlagAdicionalFunda() {
        return flagAdicionalFunda;
    }

    public void setFlagAdicionalFunda(Boolean flagAdicionalFunda) {
        this.flagAdicionalFunda = flagAdicionalFunda;
    }

    public Boolean getFlagAdicionalGuardian() {
        return flagAdicionalGuardian;
    }

    public void setFlagAdicionalGuardian(Boolean flagAdicionalGuardian) {
        this.flagAdicionalGuardian = flagAdicionalGuardian;
    }

    public Integer getPaquetePorRecolccion() {
        return paquetePorRecolccion;
    }

    public void setPaquetePorRecolccion(Integer paquetePorRecolccion) {
        this.paquetePorRecolccion = paquetePorRecolccion;
    }
}
