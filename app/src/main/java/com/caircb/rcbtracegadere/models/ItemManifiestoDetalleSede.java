package com.caircb.rcbtracegadere.models;

public class ItemManifiestoDetalleSede {
    private int idManifiestoDetalle;
    private String codigoMae;
    private String codigo;
    private String nombreDesecho;
    private Integer totalBultos;
    private Integer bultosSelecionado;

    public ItemManifiestoDetalleSede() {
    }

    public Integer getBultosSelecionado() {
        return bultosSelecionado;
    }

    public void setBultosSelecionado(Integer bultosSelecionado) {
        this.bultosSelecionado = bultosSelecionado;
    }

    public Integer getTotalBultos() {
        return totalBultos;
    }

    public void setTotalBultos(Integer totalBultos) {
        this.totalBultos = totalBultos;
    }

    public int getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(int idManifiestoDetalle) {
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    public String getCodigoMae() {
        return codigoMae;
    }

    public void setCodigoMae(String codigoMae) {
        this.codigoMae = codigoMae;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreDesecho() {
        return nombreDesecho;
    }

    public void setNombreDesecho(String nombreDesecho) {
        this.nombreDesecho = nombreDesecho;
    }
}
