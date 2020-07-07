package com.caircb.rcbtracegadere.models.response;

import java.util.List;

public class DtoLotePadreDetalleGestor {
    private Integer idManifiesto;
    private Integer idManifiestoDetalle;
    private String codigoMae;
    private String codigo;
    private String nombreDesecho;
    private Integer idManifiestoPadre;
    private List<DtoLotePadreDetalleValorGestor> manifiestoPadreDetalleValor;

    public List<DtoLotePadreDetalleValorGestor> getManifiestoPadreDetalleValor() {
        return manifiestoPadreDetalleValor;
    }

    public void setManifiestoPadreDetalleValor(List<DtoLotePadreDetalleValorGestor> manifiestoPadreDetalleValor) {
        this.manifiestoPadreDetalleValor = manifiestoPadreDetalleValor;
    }

    public Integer getIdManifiesto() {
        return idManifiesto;
    }

    public void setIdManifiesto(Integer idManifiesto) {
        this.idManifiesto = idManifiesto;
    }

    public Integer getIdManifiestoDetalle() {
        return idManifiestoDetalle;
    }

    public void setIdManifiestoDetalle(Integer idManifiestoDetalle) {
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

    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }
}
