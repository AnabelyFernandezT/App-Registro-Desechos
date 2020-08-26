package com.caircb.rcbtracegadere.models.request;

public class RequestManifiestoPendienteSede {
    private Integer idLote;
    private Integer idManifiesto;
    private String idsManifiestosCierre;
    private Integer idAutorizacion;

    public RequestManifiestoPendienteSede(Integer idLote) {
        this.idLote = idLote;
    }

    public RequestManifiestoPendienteSede() {
    }

    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public String getIdsManifiestosCierre() {
        return idsManifiestosCierre;
    }

    public void setIdsManifiestosCierre(String idsManifiestosCierre) {
        this.idsManifiestosCierre = idsManifiestosCierre;
    }

    public Integer getIdAutorizacion() {
        return idAutorizacion;
    }

    public void setIdAutorizacion(Integer idAutorizacion) {
        this.idAutorizacion = idAutorizacion;
    }
}
