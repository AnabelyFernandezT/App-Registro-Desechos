package com.caircb.rcbtracegadere.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = (ManifiestoDetalleEntity.TABLE))
public class ManifiestoDetalleEntity {

    public static final String TABLE = "tb_manifiestos_detalle";

    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private Integer idAppManifiestoDetalle;

    @NonNull
    private Integer idAppManifiesto;

    private Integer idTipoDesecho;

    private double pesoUnidad;

    private Integer idTipoUnidad;

    private double cantidadBulto;

    private double cantidadDesecho;

    private Integer cantidadTotalEtiqueta;

    private String tratamiento;

    private Integer tipoItem;

    private Integer tipoPaquete;

    private Integer IdDestinatario;

    private String codigoMAE;

    private String nombreDesecho;

    private String codeQr;

    @NonNull
    private boolean estadoChek;

    private Integer tipoBalanza;

    private String nombreDestinatario;

    private double pesoReferencial;

    private String validadorReferencial;

    private String tipoContenedor;
    private String estadoFisico;

    private Integer residuoSujetoFiscalizacion;
    private Integer requiereDevolucionRecipientes;
    private Integer tieneDisponibilidadMontacarga;
    private Integer tieneDisponibilidadBalanza;
    private Integer requiereIncineracionPresenciada;
    private String observacionResiduos;

    private boolean faltaImpresiones;

    private Integer estadoPaquete;

    private Integer cantidadRefencial;
    private  Integer tipoMostrar;


    public ManifiestoDetalleEntity() {
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @NonNull
    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(@NonNull Integer idAppManifiestoDetalle) { this.idAppManifiestoDetalle = idAppManifiestoDetalle;}

    @NonNull
    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(@NonNull Integer idAppManifiesto) {this.idAppManifiesto = idAppManifiesto;}

    public Integer getIdTipoDesecho() {
        return idTipoDesecho;
    }

    public void setIdTipoDesecho(Integer idTipoDesecho) {
        this.idTipoDesecho = idTipoDesecho;
    }

    public double getPesoUnidad() {
        return pesoUnidad;
    }

    public void setPesoUnidad(double pesoUnidad) {
        this.pesoUnidad = pesoUnidad;
    }

    public Integer getIdTipoUnidad() {
        return idTipoUnidad;
    }

    public void setIdTipoUnidad(Integer idTipoUnidad) {
        this.idTipoUnidad = idTipoUnidad;
    }

    public double getCantidadBulto() {
        return cantidadBulto;
    }

    public void setCantidadBulto(double cantidadBulto) {
        this.cantidadBulto = cantidadBulto;
    }

    public double getCantidadDesecho() { return cantidadDesecho; }

    public void setCantidadDesecho(double cantidadDesecho) { this.cantidadDesecho = cantidadDesecho;}

    public boolean isEstadoChek() {
        return estadoChek;
    }

    public void setEstadoChek(boolean estadoChek) {
        this.estadoChek = estadoChek;
    }

    public String getTratamiento() { return tratamiento; }

    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public Integer getTipoItem() { return tipoItem;  }

    public void setTipoItem(Integer tipoItem) {  this.tipoItem = tipoItem; }

    public Integer getTipoPaquete() { return tipoPaquete;  }

    public void setTipoPaquete(Integer tipoPaquete) {  this.tipoPaquete = tipoPaquete; }

    public String getCodeQr() { return codeQr;}

    public void setCodeQr(String codeQr) { this.codeQr = codeQr;}

    public Integer getCantidadTotalEtiqueta() {return cantidadTotalEtiqueta;}

    public void setCantidadTotalEtiqueta(Integer cantidadTotalEtiqueta) { this.cantidadTotalEtiqueta = cantidadTotalEtiqueta;}

    public Integer getIdDestinatario() {
        return IdDestinatario;
    }

    public void setIdDestinatario(Integer idDestinatario) {
        IdDestinatario = idDestinatario;
    }

    public String getCodigoMAE() {
        return codigoMAE;
    }

    public void setCodigoMAE(String codigoMAE) {
        this.codigoMAE = codigoMAE;
    }

    public String getNombreDesecho() {
        return nombreDesecho;
    }

    public void setNombreDesecho(String nombreDesecho) {
        this.nombreDesecho = nombreDesecho;
    }

    public Integer getTipoBalanza() {
        return tipoBalanza;
    }

    public void setTipoBalanza(Integer tipoBalanza) {
        this.tipoBalanza = tipoBalanza;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public double getPesoReferencial() {
        return pesoReferencial;
    }

    public void setPesoReferencial(double pesoReferencial) {
        this.pesoReferencial = pesoReferencial;
    }

    public String getValidadorReferencial() {
        return validadorReferencial;
    }

    public void setValidadorReferencial(String validadorReferencial) {
        this.validadorReferencial = validadorReferencial;
    }

    public String getTipoContenedor() {
        return tipoContenedor;
    }

    public void setTipoContenedor(String tipoContenedor) {
        this.tipoContenedor = tipoContenedor;
    }

    public String getEstadoFisico() {
        return estadoFisico;
    }

    public void setEstadoFisico(String estadoFisico) {
        this.estadoFisico = estadoFisico;
    }

    public Integer getResiduoSujetoFiscalizacion() {
        return residuoSujetoFiscalizacion;
    }

    public void setResiduoSujetoFiscalizacion(Integer residuoSujetoFiscalizacion) {
        this.residuoSujetoFiscalizacion = residuoSujetoFiscalizacion;
    }

    public Integer getRequiereDevolucionRecipientes() {
        return requiereDevolucionRecipientes;
    }

    public void setRequiereDevolucionRecipientes(Integer requiereDevolucionRecipientes) {
        this.requiereDevolucionRecipientes = requiereDevolucionRecipientes;
    }

    public Integer getTieneDisponibilidadMontacarga() {
        return tieneDisponibilidadMontacarga;
    }

    public void setTieneDisponibilidadMontacarga(Integer tieneDisponibilidadMontacarga) {
        this.tieneDisponibilidadMontacarga = tieneDisponibilidadMontacarga;
    }

    public Integer getTieneDisponibilidadBalanza() {
        return tieneDisponibilidadBalanza;
    }

    public void setTieneDisponibilidadBalanza(Integer tieneDisponibilidadBalanza) {
        this.tieneDisponibilidadBalanza = tieneDisponibilidadBalanza;
    }

    public Integer getRequiereIncineracionPresenciada() {
        return requiereIncineracionPresenciada;
    }

    public void setRequiereIncineracionPresenciada(Integer requiereIncineracionPresenciada) {
        this.requiereIncineracionPresenciada = requiereIncineracionPresenciada;
    }

    public String getObservacionResiduos() {
        return observacionResiduos;
    }

    public void setObservacionResiduos(String observacionResiduos) {
        this.observacionResiduos = observacionResiduos;
    }

    public boolean isFaltaImpresiones() {
        return faltaImpresiones;
    }

    public void setFaltaImpresiones(boolean faltaImpresiones) {
        this.faltaImpresiones = faltaImpresiones;
    }

    public Integer getEstadoPaquete() {
        return estadoPaquete;
    }

    public void setEstadoPaquete(Integer estadoPaquete) {
        this.estadoPaquete = estadoPaquete;
    }

    public Integer getCantidadRefencial() {
        return cantidadRefencial;
    }

    public void setCantidadRefencial(Integer cantidadRefencial) {
        this.cantidadRefencial = cantidadRefencial;
    }

    public Integer getTipoMostrar() {
        return tipoMostrar;
    }

    public void setTipoMostrar(Integer tipoMostrar) {
        this.tipoMostrar = tipoMostrar;
    }
}
