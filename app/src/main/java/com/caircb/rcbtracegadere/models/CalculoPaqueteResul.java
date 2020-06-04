package com.caircb.rcbtracegadere.models;

public class CalculoPaqueteResul {

    Integer pqh;
    Integer adicionalFunda;
    Integer adicionalGuardian;

    public CalculoPaqueteResul() {
        this.pqh=0;
        this.adicionalFunda=0;
        this.adicionalGuardian=0;
    }

    public Integer getPqh() {
        return pqh;
    }

    public void setPqh(Integer pqh) {
        this.pqh = pqh;
    }

    public Integer getAdicionalFunda() {
        return adicionalFunda;
    }

    public void setAdicionalFunda(Integer adicionalFunda) {
        this.adicionalFunda = adicionalFunda;
    }

    public Integer getAdicionalGuardian() {
        return adicionalGuardian;
    }

    public void setAdicionalGuardian(Integer adicionalGuardian) {
        this.adicionalGuardian = adicionalGuardian;
    }
}
