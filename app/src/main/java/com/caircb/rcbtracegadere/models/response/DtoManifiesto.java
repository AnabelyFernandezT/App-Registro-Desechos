package com.caircb.rcbtracegadere.models.response;

import java.util.Date;
import java.util.List;

public class DtoManifiesto {
    private Integer idAppManifiesto;
    private String numeroManifiesto;
    private String idTecnicoGenerador;
    private String tecnicoResponsable;
    private String tecnicoTelefono;
    private String tecnicoCelular;
    private String tecnicoCorreo;
    private String correoAlterno;
    private String resolutivoInstalacion;
    private String numeroGeneradorDesecho;
    private String numLicenciaAmbiental;
    private Integer idCliente;
    private String identificacionCliente;
    private String razonSocial;
    //
    private String direccion;
//proviencia = sucursal
    private String provincia;

    private String canton;

    private String parroquia;
    private String telefono;
    private Integer idLugar;
    private String nombreLugar;
    private Integer idChoferRecolector;
    private String nombreChoferRecolector;
    private String identificacionChoferRecolector;
    private Integer idAuxiliarRecolector;
    private String identificacionAuxiliarRecolector;
    private String nombreAuxiliarRecolector;
    //agregadas
    private Integer idOperadorRecolector;
    private String identificacionOperadorRecolector;
    private String nombreOperadorRecolector;

    private Integer idTransporte;
    private String identificacionTransportista;
        private String razonSocialTransportista;
    private String numeroLicenciaAmbientalTransportista;
    private String numeroPlacaVehiculo;
    private String modeloVehiculo;
    //private Integer idDestinatario;
    //private String identificacionDestinatario;
    //private String razonSocialDestinatario;
    //private String numeroLicenciaAmbientalDestinatario;
    private Integer idTransporteVehiculo;
    private Integer estadoApp;
    private Boolean eliminado;
    private Date fechaTemp;
    private Integer tipoPaquete;
    private List<DtoManifiestoDetalle> hojaRutaDetalle;
    private List<DtoManifiestoObservacionFrecuente> hojaRutaCatalogo;
    private String numManifiestoCliente;
    private Double peso;
    private String nombreFirma;
    private String firmaImg;
    private Integer idRuta;
    private Integer idSubRuta;
    private Integer estadoFinRuta;
    private String nombreDestinatario;
    private Integer idDestinatarioFinRutaCatalogo;
    private String apertura1;
    private String apertura2;
    private String cierre1;
    private String cierre2;
    private String serie;
    private String frecuencia;
    private String sucursal;
    private String fechaModificacion;
    private String firmaChoferRecolector;
    private String firmaAuxiliarRecolector;
    private String firmaOperadorRecolector;


    public DtoManifiesto() {
    }

    public String getNumManifiestoCliente() {
        return numManifiestoCliente;
    }

    public void setNumManifiestoCliente(String numManifiestoCliente) {
        this.numManifiestoCliente = numManifiestoCliente;
    }

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

    public String getTecnicoResponsable() {
        return tecnicoResponsable;
    }

    public void setTecnicoResponsable(String tecnicoResponsable) {
        this.tecnicoResponsable = tecnicoResponsable;
    }

    public String getTecnicoTelefono() {
        return tecnicoTelefono;
    }

    public void setTecnicoTelefono(String tecnicoTelefono) {
        this.tecnicoTelefono = tecnicoTelefono;
    }

    public String getTecnicoCelular() {
        return tecnicoCelular;
    }

    public void setTecnicoCelular(String tecnicoCelular) {
        this.tecnicoCelular = tecnicoCelular;
    }

    public String getTecnicoCorreo() {
        return tecnicoCorreo;
    }

    public void setTecnicoCorreo(String tecnicoCorreo) {
        this.tecnicoCorreo = tecnicoCorreo;
    }

    public String getResolutivoInstalacion() {
        return resolutivoInstalacion;
    }

    public void setResolutivoInstalacion(String resolutivoInstalacion) {
        this.resolutivoInstalacion = resolutivoInstalacion;
    }

    public String getNumeroGeneradorDesecho() {
        return numeroGeneradorDesecho;
    }

    public void setNumeroGeneradorDesecho(String numeroGeneradorDesecho) {
        this.numeroGeneradorDesecho = numeroGeneradorDesecho;
    }

    public String getNumLicenciaAmbiental() {
        return numLicenciaAmbiental;
    }

    public void setNumLicenciaAmbiental(String numLicenciaAmbiental) {
        this.numLicenciaAmbiental = numLicenciaAmbiental;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(Integer idLugar) {
        this.idLugar = idLugar;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    public Integer getIdChoferRecolector() {
        return idChoferRecolector;
    }

    public void setIdChoferRecolector(Integer idChoferRecolector) {
        this.idChoferRecolector = idChoferRecolector;
    }

    public Integer getIdAuxiliarRecolector() {
        return idAuxiliarRecolector;
    }

    public void setIdAuxiliarRecolector(Integer idAuxiliarRecolector) {
        this.idAuxiliarRecolector = idAuxiliarRecolector;
    }

    public String getIdentificacionChoferRecolector() {
        return identificacionChoferRecolector;
    }

    public void setIdentificacionChoferRecolector(String identificacionChoferRecolector) {
        this.identificacionChoferRecolector = identificacionChoferRecolector;
    }

    public String getNombreChoferRecolector() {
        return nombreChoferRecolector;
    }

    public void setNombreChoferRecolector(String nombreChoferRecolector) {
        this.nombreChoferRecolector = nombreChoferRecolector;
    }

    public String getIdentificacionAuxiliarRecolector() {
        return identificacionAuxiliarRecolector;
    }

    public void setIdentificacionAuxiliarRecolector(String identificacionAuxiliarRecolector) {
        this.identificacionAuxiliarRecolector = identificacionAuxiliarRecolector;
    }

    public String getNombreAuxiliarRecolector() {
        return nombreAuxiliarRecolector;
    }

    public void setNombreAuxiliarRecolector(String nombreAuxiliarRecolector) {
        this.nombreAuxiliarRecolector = nombreAuxiliarRecolector;
    }

    public Integer getIdTransporte() {
        return idTransporte;
    }

    public void setIdTransporte(Integer idTransporte) {
        this.idTransporte = idTransporte;
    }

    public String getIdentificacionTransportista() {
        return identificacionTransportista;
    }

    public void setIdentificacionTransportista(String identificacionTransportista) {
        this.identificacionTransportista = identificacionTransportista;
    }

    public String getRazonSocialTransportista() {
        return razonSocialTransportista;
    }

    public void setRazonSocialTransportista(String razonSocialTransportista) {
        this.razonSocialTransportista = razonSocialTransportista;
    }

    public String getNumeroLicenciaAmbientalTransportista() {
        return numeroLicenciaAmbientalTransportista;
    }

    public void setNumeroLicenciaAmbientalTransportista(String numeroLicenciaAmbientalTransportista) {
        this.numeroLicenciaAmbientalTransportista = numeroLicenciaAmbientalTransportista;
    }

    public Integer getIdTransporteVehiculo() {
        return idTransporteVehiculo;
    }

    public void setIdTransporteVehiculo(Integer idTransporteVehiculo) {
        this.idTransporteVehiculo = idTransporteVehiculo;
    }

    public String getNumeroPlacaVehiculo() {
        return numeroPlacaVehiculo;
    }

    public void setNumeroPlacaVehiculo(String numeroPlacaVehiculo) {
        this.numeroPlacaVehiculo = numeroPlacaVehiculo;
    }

    public String getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setModeloVehiculo(String modeloVehiculo) {
        this.modeloVehiculo = modeloVehiculo;
    }



    public Integer getEstadoApp() {
        return estadoApp;
    }

    public void setEstadoApp(Integer estadoApp) {
        this.estadoApp = estadoApp;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Date getFechaTemp() {
        return fechaTemp;
    }

    public void setFechaTemp(Date fechaTemp) {
        this.fechaTemp = fechaTemp;
    }

    public List<DtoManifiestoDetalle> getHojaRutaDetalle() {
        return hojaRutaDetalle;
    }

    public void setHojaRutaDetalle(List<DtoManifiestoDetalle> hojaRutaDetalle) {
        this.hojaRutaDetalle = hojaRutaDetalle;
    }

    public List<DtoManifiestoObservacionFrecuente> getHojaRutaCatalogo() {
        return hojaRutaCatalogo;
    }

    public void setHojaRutaCatalogo(List<DtoManifiestoObservacionFrecuente> hojaRutaCatalogo) {
        this.hojaRutaCatalogo = hojaRutaCatalogo;
    }

    public String getIdTecnicoGenerador() {
        return idTecnicoGenerador;
    }

    public void setIdTecnicoGenerador(String idTecnicoGenerador) {
        this.idTecnicoGenerador = idTecnicoGenerador;
    }

    public Integer getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(Integer tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getNombreFirma() {
        return nombreFirma;
    }

    public void setNombreFirma(String nombreFirma) {
        this.nombreFirma = nombreFirma;
    }

    public String getFirmaImg() {
        return firmaImg;
    }

    public void setFirmaImg(String firmaImg) {
        this.firmaImg = firmaImg;
    }

    public String getCorreoAlterno() {
        return correoAlterno;
    }

    public void setCorreoAlterno(String correoAlterno) {
        this.correoAlterno = correoAlterno;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public Integer getIdSubRuta() {
        return idSubRuta;
    }

    public void setIdSubRuta(Integer idSubRuta) {
        this.idSubRuta = idSubRuta;
    }

    public Integer getIdOperadorRecolector() {
        return idOperadorRecolector;
    }

    public void setIdOperadorRecolector(Integer idOperadorRecolector) {
        this.idOperadorRecolector = idOperadorRecolector;
    }

    public String getIdentificacionOperadorRecolector() {
        return identificacionOperadorRecolector;
    }

    public void setIdentificacionOperadorRecolector(String identificacionOperadorRecolector) {
        this.identificacionOperadorRecolector = identificacionOperadorRecolector;
    }

    public String getNombreOperadorRecolector() {
        return nombreOperadorRecolector;
    }

    public void setNombreOperadorRecolector(String nombreOperadorRecolector) {
        this.nombreOperadorRecolector = nombreOperadorRecolector;
    }

    public Integer getEstadoFinRuta() {
        return estadoFinRuta;
    }

    public void setEstadoFinRuta(Integer estadoFinRuta) {
        this.estadoFinRuta = estadoFinRuta;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public Integer getIdDestinatarioFinRutaCatalogo() {
        return idDestinatarioFinRutaCatalogo;
    }

    public void setIdDestinatarioFinRutaCatalogo(Integer idDestinatarioFinRutaCatalogo) {
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }

    public String getApertura1() {
        return apertura1;
    }

    public void setApertura1(String apertura1) {
        this.apertura1 = apertura1;
    }

    public String getApertura2() {
        return apertura2;
    }

    public void setApertura2(String apertura2) {
        this.apertura2 = apertura2;
    }

    public String getCierre1() {
        return cierre1;
    }

    public void setCierre1(String cierre1) {
        this.cierre1 = cierre1;
    }

    public String getCierre2() {
        return cierre2;
    }

    public void setCierre2(String cierre2) {
        this.cierre2 = cierre2;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getFirmaChoferRecolector() {
        return firmaChoferRecolector;
    }

    public void setFirmaChoferRecolector(String firmaChoferRecolector) {
        this.firmaChoferRecolector = firmaChoferRecolector;
    }

    public String getFirmaAuxiliarRecolector() {
        return firmaAuxiliarRecolector;
    }

    public void setFirmaAuxiliarRecolector(String firmaAuxiliarRecolector) {
        this.firmaAuxiliarRecolector = firmaAuxiliarRecolector;
    }

    public String getFirmaOperadorRecolector() {
        return firmaOperadorRecolector;
    }

    public void setFirmaOperadorRecolector(String firmaOperadorRecolector) {
        this.firmaOperadorRecolector = firmaOperadorRecolector;
    }
}
