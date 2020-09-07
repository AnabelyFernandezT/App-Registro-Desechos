package com.caircb.rcbtracegadere.models.request;

public class RequestManifiestoPadreGestores {

    private Integer idManifiestosHijo;
    private Integer idManifiestoDetalleHijo;
    private Integer idManifiestoDetallePadre;
    private Integer idDesecho;
    private Double peso;
    private Double bultos;


    public RequestManifiestoPadreGestores(Integer idManifiestosHijo, Integer idManifiestoDetalleHijo, Integer idManifiestoDetallePadre, Integer idDesecho, Double peso, Double bultos) {
        this.idManifiestosHijo = idManifiestosHijo;
        this.idManifiestoDetalleHijo = idManifiestoDetalleHijo;
        this.idManifiestoDetallePadre = idManifiestoDetallePadre;
        this.idDesecho = idDesecho;
        this.peso = peso;
        this.bultos = bultos;

    }

    public Integer getIdManifiestosHijo() {
        return idManifiestosHijo;
    }

    public void setIdManifiestosHijo(Integer idManifiestosHijo) {
        this.idManifiestosHijo = idManifiestosHijo;
    }

    public Integer getIdManifiestoDetalleHijo() {
        return idManifiestoDetalleHijo;
    }

    public void setIdManifiestoDetalleHijo(Integer idManifiestoDetalleHijo) {
        this.idManifiestoDetalleHijo = idManifiestoDetalleHijo;
    }

    public Integer getIdManifiestoDetallePadre() {
        return idManifiestoDetallePadre;
    }

    public void setIdManifiestoDetallePadre(Integer idManifiestoDetallePadre) {
        this.idManifiestoDetallePadre = idManifiestoDetallePadre;
    }

    public Integer getIdDesecho() {
        return idDesecho;
    }

    public void setIdDesecho(Integer idDesecho) {
        this.idDesecho = idDesecho;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getBultos() {
        return bultos;
    }

    public void setBultos(Double bultos) {
        this.bultos = bultos;
    }


}
