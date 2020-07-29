package com.caircb.rcbtracegadere.models.response;

public class DtoManifiestoDetalle {

    private Integer idAppManifiestoDetalle;
    private Integer idAppManifiesto;
    private Integer idTipoDesecho;
    private double pesoUnidad;
    private Integer idTipoUnidad;
    private double cantidadDesecho;
    private Integer estado;
    private Integer pesajeBultoFlag;
    private Integer tipoPaquete;
    private boolean eliminado;
    private Integer IdDestinatario;
    private String codigoMAE;
    private String nombreDesecho;
    private String nombreDestinatario;
    private double pesoReferencial;
    private String tratamiento;
    private String validadorReferencial;
    private String tipoContenedor;
    private String estadoFisico;
    private Integer residuoSujetoFiscalizacion;
    private Integer requiereDevolucionRecipientes;
    private Integer tieneDisponibilidadMontacarga;
    private Integer tieneDisponibilidadBalanza;
    private Integer requiereIncineracionPresenciada;
    private String observacionResiduos;
    private Integer cantidadRefencial;
    private Integer tipoMostrar;

    public DtoManifiestoDetalle() {
    }

    public Integer getIdAppManifiestoDetalle() {
        return idAppManifiestoDetalle;
    }

    public void setIdAppManifiestoDetalle(Integer idAppManifiestoDetalle) {
        this.idAppManifiestoDetalle = idAppManifiestoDetalle;
    }

    public Integer getIdAppManifiesto() {
        return idAppManifiesto;
    }

    public void setIdAppManifiesto(Integer idAppManifiesto) {
        this.idAppManifiesto = idAppManifiesto;
    }

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

    public double getCantidadDesecho() { return cantidadDesecho;}

    public void setCantidadDesecho(double cantidadDesecho) { this.cantidadDesecho = cantidadDesecho;}

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Integer getPesajeBultoFlag() { return pesajeBultoFlag; }

    public void setPesajeBultoFlag(Integer pesajeBultoFlag) { this.pesajeBultoFlag = pesajeBultoFlag; }

    public Integer getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(Integer tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

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

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
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
