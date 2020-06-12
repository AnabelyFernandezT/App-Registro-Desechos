package com.caircb.rcbtracegadere.models.response;

public class DtoManifiestoDetalle {

    private Integer idAppManifiestoDetalle;
    private Integer idAppManifiesto;
    private Integer idTipoDesecho;
    private double pesoUnidad;
    private Integer idTipoUnidad;
    private double cantidadDesecho;
    private Integer estado;
    private Integer pesajeBultoFlag;
    private Integer tipoPaquete;
    private boolean eliminado;
    private Integer idDestinatario;

    public DtoManifiestoDetalle() {
    }

    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(Integer idAppManifiestoDetalle) {
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
    }

    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    public Integer getIdTipoDesecho() {
        return idTipoDesecho;
    }

    public void setIdTipoDesecho(Integer idTipoDesecho) {
        this.idTipoDesecho = idTipoDesecho;
    }

    public double getPesoUnidad() {
        return pesoUnidad;
    }

    public void setPesoUnidad(double pesoUnidad) {
        this.pesoUnidad = pesoUnidad;
    }

    public Integer getIdTipoUnidad() {
        return idTipoUnidad;
    }

    public void setIdTipoUnidad(Integer idTipoUnidad) {
        this.idTipoUnidad = idTipoUnidad;
    }

    public double getCantidadDesecho() { return cantidadDesecho;}

    public void setCantidadDesecho(double cantidadDesecho) { this.cantidadDesecho = cantidadDesecho;}

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Integer getPesajeBultoFlag() { return pesajeBultoFlag; }

    public void setPesajeBultoFlag(Integer pesajeBultoFlag) { this.pesajeBultoFlag = pesajeBultoFlag; }

    public Integer getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(Integer tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    public Integer getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(Integer idDestinatario) {
        this.idDestinatario = idDestinatario;
    }
}
