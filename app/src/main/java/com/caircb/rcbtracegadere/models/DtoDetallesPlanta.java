package com.caircb.rcbtracegadere.models;

import java.util.List;

public class DtoDetallesPlanta {

    private int idDetalle;
    private List<DtoDetallesBultoPlanta> bultos;

    public DtoDetallesPlanta(int idDetalle, List<DtoDetallesBultoPlanta> bultos) {
        this.idDetalle = idDetalle;
        this.bultos = bultos;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public List<DtoDetallesBultoPlanta> getBultos() {
        return bultos;
    }

    public void setBultos(List<DtoDetallesBultoPlanta> bultos) {
        this.bultos = bultos;
    }
}
