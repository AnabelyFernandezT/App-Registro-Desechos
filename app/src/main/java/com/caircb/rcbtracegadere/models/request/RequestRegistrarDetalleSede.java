package com.caircb.rcbtracegadere.models.request;

import java.util.List;

public class RequestRegistrarDetalleSede {
    private Integer idLoteContenedor;
    //private List<RequestDetalleSede> detalles;
    private List<Integer> idManifiestoDetalleValor;

    public Integer getIdLoteContenedor() {
        return idLoteContenedor;
    }

    public void setIdLoteContenedor(Integer idLoteContenedor) {
        this.idLoteContenedor = idLoteContenedor;
    }

    /*public List<RequestDetalleSede> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RequestDetalleSede> detalles) {
        this.detalles = detalles;
    }*/

    public List<Integer> getIdManifiestoDetalleValor() {
        return idManifiestoDetalleValor;
    }

    public void setIdManifiestoDetalleValor(List<Integer> idManifiestoDetalleValor) {
        this.idManifiestoDetalleValor = idManifiestoDetalleValor;
    }
}
