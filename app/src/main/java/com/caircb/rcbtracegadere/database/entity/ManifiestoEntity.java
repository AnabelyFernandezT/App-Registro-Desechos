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

    @NonNull
    private String numeroManifiesto;

    private String licenciaAmbiental;

    private String numeroGeneradorDesecho;

    private String provincia;

    private String canton;

    private String parroquia;

    private String empresaDestinataria;

    private String empresaTransportista;

    private String transportistaFirmaImg;

    private String transportistaFirmaUrl;

    private String idTecnicoGenerador;

    private String tecnicoIdentificacion;

    private String tecnicoResponsable;

    private String tecnicoTelefono;

    private String tecnicoCelular;

    private String tecnicoCorreo;

    private String tecnicoFirmaImg;

    private String tecnicoFirmaUrl;

    private String conductorIdentificacion;

    private String conductorNombre;

    private String auxiliarIdentificacion;

    private String auxiliarNombre;

    private String novedadEncontrada;

    private String codigo;

    @NonNull
    private Date fechaManifiesto;

    @NonNull
    private Integer idLugar;

    @NonNull
    private Integer estado;

    private Integer tipoPaquete;

    public Integer getIdTecnicoManifiesto() {
        return idTecnicoManifiesto;
    }

    private Double peso;

    private String nombreFirma;

    private String firmaImg;

    public void setIdTecnicoManifiesto(Integer idTecnicoManifiesto) {
        this.idTecnicoManifiesto = idTecnicoManifiesto;
    }

    private Integer idTecnicoManifiesto;

    public ManifiestoEntity() {
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

    public void setIdAppManifiesto(@NonNull Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getDireccionRetiro() {
        return direccionRetiro;
    }

    public void setDireccionRetiro(String direccionRetiro) {
        this.direccionRetiro = direccionRetiro;
    }

    @NonNull
    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(@NonNull String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getLicenciaAmbiental() {
        return licenciaAmbiental;
    }

    public void setLicenciaAmbiental(String licenciaAmbiental) {
        this.licenciaAmbiental = licenciaAmbiental;
    }

    public String getNumeroGeneradorDesecho() {
        return numeroGeneradorDesecho;
    }

    public void setNumeroGeneradorDesecho(String numeroGeneradorDesecho) {
        this.numeroGeneradorDesecho = numeroGeneradorDesecho;
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

    public String getTecnicoIdentificacion() {
        return tecnicoIdentificacion;
    }

    public void setTecnicoIdentificacion(String tecnicoIdentificacion) {
        this.tecnicoIdentificacion = tecnicoIdentificacion;
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

    public String getTecnicoFirmaImg() {
        return tecnicoFirmaImg;
    }

    public void setTecnicoFirmaImg(String tecnicoFirmaImg) {
        this.tecnicoFirmaImg = tecnicoFirmaImg;
    }

    public String getTecnicoFirmaUrl() {
        return tecnicoFirmaUrl;
    }

    public void setTecnicoFirmaUrl(String tecnicoFirmaUrl) {
        this.tecnicoFirmaUrl = tecnicoFirmaUrl;
    }

    public String getConductorIdentificacion() {
        return conductorIdentificacion;
    }

    public void setConductorIdentificacion(String conductorIdentificacion) {
        this.conductorIdentificacion = conductorIdentificacion;
    }

    public String getConductorNombre() {
        return conductorNombre;
    }

    public void setConductorNombre(String conductorNombre) {
        this.conductorNombre = conductorNombre;
    }

    public String getAuxiliarIdentificacion() {
        return auxiliarIdentificacion;
    }

    public void setAuxiliarIdentificacion(String auxiliarIdentificacion) {
        this.auxiliarIdentificacion = auxiliarIdentificacion;
    }

    public String getAuxiliarNombre() {
        return auxiliarNombre;
    }

    public void setAuxiliarNombre(String auxiliarNombre) {
        this.auxiliarNombre = auxiliarNombre;
    }

    public String getNovedadEncontrada() {
        return novedadEncontrada;
    }

    public void setNovedadEncontrada(String novedadEncontrada) {
        this.novedadEncontrada = novedadEncontrada;
    }

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

    public void setEstado(@NonNull Integer estado) {
        this.estado = estado;
    }

    @NonNull
    public Date getFechaManifiesto() {
        return fechaManifiesto;
    }

    public void setFechaManifiesto(@NonNull Date fechaManifiesto) {
        this.fechaManifiesto = fechaManifiesto;
    }

    public String getNumManifiestoCliente() {
        return numManifiestoCliente;
    }

    public void setNumManifiestoCliente(String numManifiestoCliente) {
        this.numManifiestoCliente = numManifiestoCliente;
    }

    public String getIdTecnicoGenerador() {
        return idTecnicoGenerador;
    }

    public void setIdTecnicoGenerador(String idTecnicoGenerador) {
        this.idTecnicoGenerador = idTecnicoGenerador;
    }

    public String getEmpresaDestinataria() {
        return empresaDestinataria;
    }

    public void setEmpresaDestinataria(String empresaDestinataria) {
        this.empresaDestinataria = empresaDestinataria;
    }

    public String getTransportistaFirmaImg() {
        return transportistaFirmaImg;
    }

    public void setTransportistaFirmaImg(String transportistaFirmaImg) {
        this.transportistaFirmaImg = transportistaFirmaImg;
    }

    public String getTransportistaFirmaUrl() {
        return transportistaFirmaUrl;
    }

    public void setTransportistaFirmaUrl(String transportistaFirmaUrl) {
        this.transportistaFirmaUrl = transportistaFirmaUrl;
    }

    public String getEmpresaTransportista() {
        return empresaTransportista;
    }

    public void setEmpresaTransportista(String empresaTransportista) {
        this.empresaTransportista = empresaTransportista;
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
}


