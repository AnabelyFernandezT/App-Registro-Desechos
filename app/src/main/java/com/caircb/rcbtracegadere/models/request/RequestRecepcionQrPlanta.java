package com.caircb.rcbtracegadere.models.request;

public class RequestRecepcionQrPlanta {

    private Integer idDestinatarioFinRuta;
    private Integer idTransportistaVehiculo;


    public RequestRecepcionQrPlanta(Integer idDestinatarioFinRuta, Integer idTransportistaVehiculo){
        this.idDestinatarioFinRuta=idDestinatarioFinRuta;
        this.idTransportistaVehiculo=idTransportistaVehiculo;
    }

    public Integer getIdDestinatarioFinRuta() {
        return idDestinatarioFinRuta;
    }

    public void setIdDestinatarioFinRuta(Integer idDestinatarioFinRuta) {
        this.idDestinatarioFinRuta = idDestinatarioFinRuta;
    }

    public Integer getIdTransportistaVehiculo() {
        return idTransportistaVehiculo;
    }

    public void setIdTransportistaVehiculo(Integer idTransportistaVehiculo) {
        this.idTransportistaVehiculo = idTransportistaVehiculo;
    }
}
