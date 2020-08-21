package com.caircb.rcbtracegadere.models.request;

import java.util.Date;
import java.util.List;

public class RequestManifiesto {
    private Integer idAppManifiesto;
    private String numeroManifiesto;
    private String numeroManifiestoCliente;
    private String urlFirmaTransportista;
    private String responsableEntregaIdentificacion;
    private String responsableEntregaNombre;
    private String responsableEntregaCorreo;
    private String responsableEntregaTelefono;
    private String urlFirmaResponsableEntrega;
    private String urlFirmaAuxiliarRecolector;//operador1
    private String urlFirmaConductorRecolector; //operador2
    private String novedadReportadaCliente;
    private String urlAudioNovedadCliente;
    private Integer usuarioResponsable;
    private Date fechaRecoleccion;
    private Double latitude;
    private Double longitude;
    private RequestManifiestoPaquete paquete;
    private List<RequestManifiestoDet> detalles;
    private List<RequestManifiestoNovedadFrecuente> novedadFrecuente;
    private List<RequestManifiestoNovedadNoRecoleccion> novedadNoRecoleccion;
    private Integer estado;
    private Date fechaInicioRecoleccion;
    private String correos;
    private List<RequestNovedadPesoPromedio> fotosManifiestoPromedio;
    private String textoEvidenciaPromedio;
    private Integer flagManifiestoSede;

    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNumeroManifiestoCliente() {
        return numeroManifiestoCliente;
    }

    public void setNumeroManifiestoCliente(String numeroManifiestoCliente) {
        this.numeroManifiestoCliente = numeroManifiestoCliente;
    }

    public String getUrlFirmaTransportista() {
        return urlFirmaTransportista;
    }

    public void setUrlFirmaTransportista(String urlFirmaTransportista) {
        this.urlFirmaTransportista = urlFirmaTransportista;
    }

    public String getResponsableEntregaIdentificacion() {
        return responsableEntregaIdentificacion;
    }

    public void setResponsableEntregaIdentificacion(String responsableEntregaIdentificacion) {
        this.responsableEntregaIdentificacion = responsableEntregaIdentificacion;
    }

    public String getResponsableEntregaNombre() {
        return responsableEntregaNombre;
    }

    public void setResponsableEntregaNombre(String responsableEntregaNombre) {
        this.responsableEntregaNombre = responsableEntregaNombre;
    }

    public String getResponsableEntregaCorreo() {
        return responsableEntregaCorreo;
    }

    public void setResponsableEntregaCorreo(String responsableEntregaCorreo) {
        this.responsableEntregaCorreo = responsableEntregaCorreo;
    }

    public String getResponsableEntregaTelefono() {
        return responsableEntregaTelefono;
    }

    public void setResponsableEntregaTelefono(String responsableEntregaTelefono) {
        this.responsableEntregaTelefono = responsableEntregaTelefono;
    }

    public String getUrlFirmaResponsableEntrega() {
        return urlFirmaResponsableEntrega;
    }

    public void setUrlFirmaResponsableEntrega(String urlFirmaResponsableEntrega) {
        this.urlFirmaResponsableEntrega = urlFirmaResponsableEntrega;
    }

    public String getNovedadReportadaCliente() {
        return novedadReportadaCliente;
    }

    public void setNovedadReportadaCliente(String novedadReportadaCliente) {
        this.novedadReportadaCliente = novedadReportadaCliente;
    }

    public String getUrlAudioNovedadCliente() {
        return urlAudioNovedadCliente;
    }

    public void setUrlAudioNovedadCliente(String urlAudioNovedadCliente) {
        this.urlAudioNovedadCliente = urlAudioNovedadCliente;
    }

    public Integer getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(Integer usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public Date getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public void setFechaRecoleccion(Date fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public RequestManifiestoPaquete getPaquete() {
        return paquete;
    }

    public void setPaquete(RequestManifiestoPaquete paquete) {
        this.paquete = paquete;
    }

    public List<RequestManifiestoDet> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RequestManifiestoDet> detalles) {
        this.detalles = detalles;
    }

    public List<RequestManifiestoNovedadFrecuente> getNovedadFrecuente() {
        return novedadFrecuente;
    }

    public void setNovedadFrecuente(List<RequestManifiestoNovedadFrecuente> novedadFrecuente) {
        this.novedadFrecuente = novedadFrecuente;
    }

    public List<RequestManifiestoNovedadNoRecoleccion> getNovedadNoRecoleccion() {
        return novedadNoRecoleccion;
    }

    public void setNovedadNoRecoleccion(List<RequestManifiestoNovedadNoRecoleccion> novedadNoRecoleccion) {
        this.novedadNoRecoleccion = novedadNoRecoleccion;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUrlFirmaAuxiliarRecolector() {
        return urlFirmaAuxiliarRecolector;
    }

    public void setUrlFirmaAuxiliarRecolector(String urlFirmaAuxiliarRecolector) {
        this.urlFirmaAuxiliarRecolector = urlFirmaAuxiliarRecolector;
    }

    public String getUrlFirmaConductorRecolector() {
        return urlFirmaConductorRecolector;
    }

    public void setUrlFirmaConductorRecolector(String urlFirmaConductorRecolector) {
        this.urlFirmaConductorRecolector = urlFirmaConductorRecolector;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Date getFechaInicioRecoleccion() {
        return fechaInicioRecoleccion;
    }

    public void setFechaInicioRecoleccion(Date fechaInicioRecoleccion) {
        this.fechaInicioRecoleccion = fechaInicioRecoleccion;
    }

    public String getCorreos() {
        return correos;
    }

    public void setCorreos(String correos) {
        this.correos = correos;
    }

    public List<RequestNovedadPesoPromedio> getFotosManifiestoPromedio() {
        return fotosManifiestoPromedio;
    }

    public void setFotosManifiestoPromedio(List<RequestNovedadPesoPromedio> fotosManifiestoPromedio) {
        this.fotosManifiestoPromedio = fotosManifiestoPromedio;
    }

    public String getTextoEvidenciaPromedio() {
        return textoEvidenciaPromedio;
    }

    public void setTextoEvidenciaPromedio(String textoEvidenciaPromedio) {
        this.textoEvidenciaPromedio = textoEvidenciaPromedio;
    }

    public Integer getFlagManifiestoSede() {
        return flagManifiestoSede;
    }

    public void setFlagManifiestoSede(Integer flagManifiestoSede) {
        this.flagManifiestoSede = flagManifiestoSede;
    }
}
