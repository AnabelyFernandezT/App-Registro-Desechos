package com.caircb.rcbtracegadere.models.request;

public class RequestRecepcionQrPlanta {

    private Integer idDestinatarioFinRuta;
    private Integer idTransportistaVehiculo;
    private Integer idLoteProceso;


    public RequestRecepcionQrPlanta(Integer idDestinatarioFinRuta, Integer idTransportistaVehiculo, Integer idLoteProceso) {
        this.idDestinatarioFinRuta = idDestinatarioFinRuta;
        this.idTransportistaVehiculo = idTransportistaVehiculo;
        this.idLoteProceso = idLoteProceso;
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

    public Integer getIdLoteProceso() {
        return idLoteProceso;
    }

    public void setIdLoteProceso(Integer idLoteProceso) {
        this.idLoteProceso = idLoteProceso;
    }
}
