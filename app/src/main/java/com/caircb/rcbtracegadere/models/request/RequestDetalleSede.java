package com.caircb.rcbtracegadere.models.request;

public class RequestDetalleSede {
    private Integer idManifiestoDetalleValor;

    public RequestDetalleSede(Integer idManifiestDetalleValor) {
        this.idManifiestoDetalleValor = idManifiestDetalleValor;
    }

    public Integer getIdManifiestDetalleValor() {
        return idManifiestoDetalleValor;
    }

    public void setIdManifiestDetalleValor(Integer idManifiestDetalleValor) {
        this.idManifiestoDetalleValor = idManifiestDetalleValor;
    }
}
