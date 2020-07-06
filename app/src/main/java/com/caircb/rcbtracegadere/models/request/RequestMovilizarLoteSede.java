package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestMovilizarLoteSede {

    private Integer idLoteContenedor;
    private Integer idTransportistaVehiculo;
    private Integer idTransportistaRecolector;
    private Integer idOperador1;
    private Integer idOperador2;
    private Date fecha;
    private Integer tipo;
    private Integer idDestinatarioFinRutaCatalogo;

    public Integer getIdLoteContenedor() {
        return idLoteContenedor;
    }

    public void setIdLoteContenedor(Integer idLoteContenedor) {
        this.idLoteContenedor = idLoteContenedor;
    }

    public Integer getIdTransportistaVehiculo() {
        return idTransportistaVehiculo;
    }

    public void setIdTransportistaVehiculo(Integer idTransportistaVehiculo) {
        this.idTransportistaVehiculo = idTransportistaVehiculo;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }

    public Integer getIdOperador1() {
        return idOperador1;
    }

    public void setIdOperador1(Integer idOperador1) {
        this.idOperador1 = idOperador1;
    }

    public Integer getIdOperador2() {
        return idOperador2;
    }

    public void setIdOperador2(Integer idOperador2) {
        this.idOperador2 = idOperador2;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getIdDestinatarioFinRutaCatalogo() {
        return idDestinatarioFinRutaCatalogo;
    }

    public void setIdDestinatarioFinRutaCatalogo(Integer idDestinatarioFinRutaCatalogo) {
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }
}
