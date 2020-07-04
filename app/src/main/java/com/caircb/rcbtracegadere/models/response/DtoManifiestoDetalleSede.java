package com.caircb.rcbtracegadere.models.response;

import java.util.List;

public class DtoManifiestoDetalleSede {

    private Integer idAppManifiesto;
    private Integer idManifiestoDetalle;
    private String codigoMae;
    private String codigo;
    private String nombreDesecho;
    private Integer idManifiestoPadre;
    private List<DtoManifiestoDetalleValorSede> hojaRutaDetalleValor;


    public DtoManifiestoDetalleSede() {
    }

    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
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

    public List<DtoManifiestoDetalleValorSede> getHojaRutaDetalleValor() {
        return hojaRutaDetalleValor;
    }

    public void setHojaRutaDetalleValor(List<DtoManifiestoDetalleValorSede> hojaRutaDetalleValor) {
        this.hojaRutaDetalleValor = hojaRutaDetalleValor;
    }
}
