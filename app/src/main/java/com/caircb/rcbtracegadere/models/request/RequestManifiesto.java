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
    private String novedadReportadaCliente;
    private String urlAudioNovedadCliente;
    private Integer usuarioResponsable;
    private Date fechaRecoleccion;
    private RequestManifiestoPaquete paquete;
    private List<RequestManifiestoDet> detalles;
    private List<RequestManifiestoNovedadFrecuente> novedadFrecuente;
    private List<RequestManifiestoNovedadNoRecoleccion> novedadNoRecoleccion;

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
}
