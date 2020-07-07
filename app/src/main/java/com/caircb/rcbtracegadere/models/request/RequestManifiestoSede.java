package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestManifiestoSede {
    private Integer idTransportistaVehiculo;
    private Integer idDestinatarioFinRuta;

    public RequestManifiestoSede(Integer idTransportistaVehiculo, Integer idDestinatarioFinRuta) {
        this.idTransportistaVehiculo = idTransportistaVehiculo;
        this.idDestinatarioFinRuta = idDestinatarioFinRuta;
    }

    public Integer getIdTransportistaVehiculo() {
        return idTransportistaVehiculo;
    }

    public void setIdTransportistaVehiculo(Integer idTransportistaVehiculo) {
        this.idTransportistaVehiculo = idTransportistaVehiculo;
    }

    public Integer getIdDestinatarioFinRuta() {
        return idDestinatarioFinRuta;
    }

    public void setIdDestinatarioFinRuta(Integer idDestinatarioFinRuta) {
        this.idDestinatarioFinRuta = idDestinatarioFinRuta;
    }
}
