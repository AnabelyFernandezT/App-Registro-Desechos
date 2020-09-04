package com.caircb.rcbtracegadere.models;

public class RowItemManifiestosDetalleGestores {

    private Integer idManifiestoPadre;

    private Integer idManifiestosHijo;

    private Integer idManifiestoDetalleHijo;

    private Integer idManifiestoDetallePadre;

    private Integer idDesecho;

    private Double peso;

    private Double bultos;

    private String numeroManifiestoPadre;

    private Boolean checkHijo;

    private String cliente;

    private String numeroManifiestoHijo;

    public RowItemManifiestosDetalleGestores() {
    }

    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
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

    public String getNumeroManifiestoPadre() {
        return numeroManifiestoPadre;
    }

    public void setNumeroManifiestoPadre(String numeroManifiestoPadre) {
        this.numeroManifiestoPadre = numeroManifiestoPadre;
    }

    public Boolean getCheckHijo() {
        return checkHijo;
    }

    public void setCheckHijo(Boolean checkHijo) {
        this.checkHijo = checkHijo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNumeroManifiestoHijo() {
        return numeroManifiestoHijo;
    }

    public void setNumeroManifiestoHijo(String numeroManifiestoHijo) {
        this.numeroManifiestoHijo = numeroManifiestoHijo;
    }
}
