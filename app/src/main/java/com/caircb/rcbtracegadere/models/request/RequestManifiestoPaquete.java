package com.caircb.rcbtracegadere.models.request;

public class RequestManifiestoPaquete {
    private Integer idPaquete;
    private Integer cantidadFunda;
    private Integer cantidadGuardian;
    private Integer cantidadPendienteFunda;
    private Integer cantidadPendienteGuardian;
    private Integer pqh;
    private Integer adicionalFunda;
    private Integer adicionalGuardian;

    public Integer getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Integer idPaquete) {
        this.idPaquete = idPaquete;
    }

    public Integer getCantidadFunda() {
        return cantidadFunda;
    }

    public void setCantidadFunda(Integer cantidadFunda) {
        this.cantidadFunda = cantidadFunda;
    }

    public Integer getCantidadGuardian() {
        return cantidadGuardian;
    }

    public void setCantidadGuardian(Integer cantidadGuardian) {
        this.cantidadGuardian = cantidadGuardian;
    }

    public Integer getCantidadPendienteFunda() {
        return cantidadPendienteFunda;
    }

    public void setCantidadPendienteFunda(Integer cantidadPendienteFunda) {
        this.cantidadPendienteFunda = cantidadPendienteFunda;
    }

    public Integer getCantidadPendienteGuardian() {
        return cantidadPendienteGuardian;
    }

    public void setCantidadPendienteGuardian(Integer cantidadPendienteGuardian) {
        this.cantidadPendienteGuardian = cantidadPendienteGuardian;
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
