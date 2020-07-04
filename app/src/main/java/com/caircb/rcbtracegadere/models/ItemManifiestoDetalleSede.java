package com.caircb.rcbtracegadere.models;

public class ItemManifiestoDetalleSede {
    private int idManifiestoDetalle;
    private String codigoMae;
    private String codigo;
    private String nombreDesecho;



    public ItemManifiestoDetalleSede() {
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
