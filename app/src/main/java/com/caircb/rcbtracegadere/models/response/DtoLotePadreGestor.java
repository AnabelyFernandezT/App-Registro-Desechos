package com.caircb.rcbtracegadere.models.response;

import java.util.List;

public class DtoLotePadreGestor {
    private Integer idManifiestoPadre;
    private String manifiestos;
    private Double total;
    private String nombreCliente;
    private String numeroManifiestoPadre;
    private String placaVehiculo;
    private List<DtoLotePadreDetalleGestor> manifiestoPadreDetalle;

    public Integer getIdManifiestoPadre() {
        return idManifiestoPadre;
    }

    public void setIdManifiestoPadre(Integer idManifiestoPadre) {
        this.idManifiestoPadre = idManifiestoPadre;
    }

    public String getManifiestos() {
        return manifiestos;
    }

    public void setManifiestos(String manifiestos) {
        this.manifiestos = manifiestos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNumeroManifiestoPadre() {
        return numeroManifiestoPadre;
    }

    public void setNumeroManifiestoPadre(String numeroManifiestoPadre) {
        this.numeroManifiestoPadre = numeroManifiestoPadre;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public List<DtoLotePadreDetalleGestor> getManifiestoPadreDetalle() {
        return manifiestoPadreDetalle;
    }

    public void setManifiestoPadreDetalle(List<DtoLotePadreDetalleGestor> manifiestoPadreDetalle) {
        this.manifiestoPadreDetalle = manifiestoPadreDetalle;
    }
}
