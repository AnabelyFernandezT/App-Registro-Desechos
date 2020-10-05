package com.caircb.rcbtracegadere.models.response;

public class DtoCodigoQrTransportista {
    private String codigoQr;
    private String loteProcesoId;
    private String fechaCierreLote;

    public DtoCodigoQrTransportista(){

    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public String getLoteProcesoId() {
        return loteProcesoId;
    }

    public void setLoteProcesoId(String loteProcesoId) {
        this.loteProcesoId = loteProcesoId;
    }

    public String getFechaCierreLote() {
        return fechaCierreLote;
    }

    public void setFechaCierreLote(String fechaCierreLote) {
        this.fechaCierreLote = fechaCierreLote;
    }
}
