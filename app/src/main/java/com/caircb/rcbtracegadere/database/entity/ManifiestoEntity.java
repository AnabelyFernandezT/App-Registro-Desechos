package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = (ManifiestoEntity.TABLE))
public class ManifiestoEntity {
    public static final String TABLE = "tb_manifiestos";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idAppManifiesto;

    private String numManifiestoCliente;

    private String nombreCliente;

    private String direccionCliente;

    private String identificacionCliente;

    private String direccionRetiro;

    private Integer idRuta;

    private Integer idSubRuta;

    @NonNull
    private String numeroManifiesto;

    private String licenciaAmbiental;

    private String numeroGeneradorDesecho;

    private String provincia;

    private String canton;

    private String parroquia;

    private String empresaDestinataria;

    private String empresaTransportista;

    //private String transportistaFirmaImg;

    //private String transportistaFirmaUrl;

    private Integer idTecnicoGenerador;

    private String tecnicoIdentificacion;

    private String tecnicoResponsable;

    private String tecnicoTelefono;

    private String tecnicoCelular;

    private String tecnicoCorreo;

    private String correoAlterno;

    //private String tecnicoFirmaImg;

    //private String tecnicoFirmaUrl;

    private String conductorIdentificacion;

    private String conductorNombre;

    private String auxiliarIdentificacion;

    private String auxiliarNombre;

    private Integer idOperadorRecolector;

    private String identificacionOperadorRecolector;

    private String nombreOperadorRecolector;

    private String novedadEncontrada;

    private String codigo;

    @NonNull
    private Date fechaManifiesto;

    private Date fechaRecoleccion;

    private Date fechaRecepcionPlanta;

    @NonNull
    private Integer idLugar;

    @NonNull
    private Integer estado;

    private Integer tipoPaquete;

    private Integer idTecnicoManifiesto;

    private Double peso;

    //private String nombreFirma;

    //private String firmaImg;

    //private String nombreNovedadAudio;

    //private String novedadAudio;
    private Boolean sincronizado;

    private Integer idTransporteVehiculo;

    private String tiempoAudio;

    private Integer estadoFinRuta;

    private String nombreDestinatario;

    private Date fechaInicioRecorrecion;

    private Integer idChoferRecolector;

    private String numeroPlacaVehiculo;

    private Integer idDestinatarioFinRutaCatalogo;

    private String correos;

    private  Integer Apertura1;
    private Integer Apertura2;
    private Integer Cierre1;
    private Integer Cierre2;

    public ManifiestoEntity() {
    }

    public String getNumeroPlacaVehiculo() {
        return numeroPlacaVehiculo;
    }

    public void setNumeroPlacaVehiculo(String numeroPlacaVehiculo) {
        this.numeroPlacaVehiculo = numeroPlacaVehiculo;
    }

    public Integer getIdChoferRecolector() {
        return idChoferRecolector;
    }

    public void setIdChoferRecolector(Integer idChoferRecolector) {
        this.idChoferRecolector = idChoferRecolector;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(@NonNull Integer idAppManifiesto) { this.idAppManifiesto = idAppManifiesto; }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) { this.direccionCliente = direccionCliente; }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) { this.identificacionCliente = identificacionCliente; }

    public String getDireccionRetiro() {
        return direccionRetiro;
    }

    public void setDireccionRetiro(String direccionRetiro) { this.direccionRetiro = direccionRetiro; }

    @NonNull
    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(@NonNull String numeroManifiesto) { this.numeroManifiesto = numeroManifiesto; }

    public String getLicenciaAmbiental() {
        return licenciaAmbiental;
    }

    public void setLicenciaAmbiental(String licenciaAmbiental) { this.licenciaAmbiental = licenciaAmbiental; }

    public String getNumeroGeneradorDesecho() {
        return numeroGeneradorDesecho;
    }

    public void setNumeroGeneradorDesecho(String numeroGeneradorDesecho) { this.numeroGeneradorDesecho = numeroGeneradorDesecho; }

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

    public String getTecnicoIdentificacion() {
        return tecnicoIdentificacion;
    }

    public void setTecnicoIdentificacion(String tecnicoIdentificacion) { this.tecnicoIdentificacion = tecnicoIdentificacion;}

    public String getTecnicoResponsable() {
        return tecnicoResponsable;
    }

    public void setTecnicoResponsable(String tecnicoResponsable) { this.tecnicoResponsable = tecnicoResponsable; }

    public String getTecnicoTelefono() {
        return tecnicoTelefono;
    }

    public void setTecnicoTelefono(String tecnicoTelefono) { this.tecnicoTelefono = tecnicoTelefono;}

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

    public String getConductorIdentificacion() {
        return conductorIdentificacion;
    }

    public void setConductorIdentificacion(String conductorIdentificacion) { this.conductorIdentificacion = conductorIdentificacion;}

    public String getConductorNombre() {
        return conductorNombre;
    }

    public void setConductorNombre(String conductorNombre) { this.conductorNombre = conductorNombre;}

    public String getAuxiliarIdentificacion() {
        return auxiliarIdentificacion;
    }

    public void setAuxiliarIdentificacion(String auxiliarIdentificacion) { this.auxiliarIdentificacion = auxiliarIdentificacion; }

    public String getAuxiliarNombre() {
        return auxiliarNombre;
    }

    public void setAuxiliarNombre(String auxiliarNombre) {
        this.auxiliarNombre = auxiliarNombre;
    }

    public String getNovedadEncontrada() {
        return novedadEncontrada;
    }

    public void setNovedadEncontrada(String novedadEncontrada) { this.novedadEncontrada = novedadEncontrada; }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @NonNull
    public Integer getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(@NonNull Integer idLugar) {
        this.idLugar = idLugar;
    }

    @NonNull
    public Integer getEstado() {
        return estado;
    }

    public void setEstado(@NonNull Integer estado) { this.estado = estado;}

    @NonNull
    public Date getFechaManifiesto() {
        return fechaManifiesto;
    }

    public void setFechaManifiesto(@NonNull Date fechaManifiesto) { this.fechaManifiesto = fechaManifiesto; }

    public String getNumManifiestoCliente() {
        return numManifiestoCliente;
    }

    public void setNumManifiestoCliente(String numManifiestoCliente) { this.numManifiestoCliente = numManifiestoCliente; }

    public Integer getIdTecnicoGenerador() {
        return idTecnicoGenerador;
    }

    public void setIdTecnicoGenerador(Integer idTecnicoGenerador) { this.idTecnicoGenerador = idTecnicoGenerador; }

    public String getEmpresaDestinataria() {
        return empresaDestinataria;
    }

    public void setEmpresaDestinataria(String empresaDestinataria) { this.empresaDestinataria = empresaDestinataria; }

    public String getEmpresaTransportista() {
        return empresaTransportista;
    }

    public void setEmpresaTransportista(String empresaTransportista) { this.empresaTransportista = empresaTransportista;}

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

    public String getTiempoAudio() {
        return tiempoAudio;
    }

    public void setTiempoAudio(String tiempoAudio) {
        this.tiempoAudio = tiempoAudio;
    }

    public void setIdTecnicoManifiesto(Integer idTecnicoManifiesto) { this.idTecnicoManifiesto = idTecnicoManifiesto; }

    public Integer getIdTecnicoManifiesto() {
        return idTecnicoManifiesto;
    }

    public Date getFechaRecoleccion() { return fechaRecoleccion; }

    public void setFechaRecoleccion(Date fechaRecoleccion) { this.fechaRecoleccion = fechaRecoleccion; }

    public Boolean getSincronizado() { return sincronizado;}

    public void setSincronizado(Boolean sincronizado) { this.sincronizado = sincronizado;}

    public Date getFechaRecepcionPlanta() {
        return fechaRecepcionPlanta;
    }

    public void setFechaRecepcionPlanta(Date fechaRecepcionPlanta) {
        this.fechaRecepcionPlanta = fechaRecepcionPlanta;
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

    public Integer getIdTransporteVehiculo() {
        return idTransporteVehiculo;
    }

    public void setIdTransporteVehiculo(Integer idTransporteVehiculo) {
        this.idTransporteVehiculo = idTransporteVehiculo;
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

    public Date getFechaInicioRecorrecion() {
        return fechaInicioRecorrecion;
    }

    public void setFechaInicioRecorrecion(Date fechaInicioRecorrecion) {
        this.fechaInicioRecorrecion = fechaInicioRecorrecion;
    }

    public Integer getIdDestinatarioFinRutaCatalogo() {
        return idDestinatarioFinRutaCatalogo;
    }

    public void setIdDestinatarioFinRutaCatalogo(Integer idDestinatarioFinRutaCatalogo) {
        this.idDestinatarioFinRutaCatalogo = idDestinatarioFinRutaCatalogo;
    }

    public String getCorreos() {
        return correos;
    }

    public void setCorreos(String correos) {
        this.correos = correos;
    }


    public Integer getApertura1() {
        return Apertura1;
    }

    public void setApertura1(Integer apertura1) {
        Apertura1 = apertura1;
    }

    public Integer getApertura2() {
        return Apertura2;
    }

    public void setApertura2(Integer apertura2) {
        Apertura2 = apertura2;
    }

    public Integer getCierre1() {
        return Cierre1;
    }

    public void setCierre1(Integer cierre1) {
        Cierre1 = cierre1;
    }

    public Integer getCierre2() {
        return Cierre2;
    }

    public void setCierre2(Integer cierre2) {
        Cierre2 = cierre2;
    }
}


