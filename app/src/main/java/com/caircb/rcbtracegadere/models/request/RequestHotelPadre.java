package com.caircb.rcbtracegadere.models.request;

import java.util.Date;

public class RequestHotelPadre
{
    private Integer idTransportistaRecolector;
    private Date fecha;

    public RequestHotelPadre(Integer idTransportistaRecolector, Date fecha) {
        this.idTransportistaRecolector = idTransportistaRecolector;
        this.fecha = fecha;
    }

    public Integer getIdTransportistaRecolector() {
        return idTransportistaRecolector;
    }

    public void setIdTransportistaRecolector(Integer idTransportistaRecolector) {
        this.idTransportistaRecolector = idTransportistaRecolector;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
