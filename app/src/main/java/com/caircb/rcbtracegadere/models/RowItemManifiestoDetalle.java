package com.caircb.rcbtracegadere.models;

public class RowItemManifiestoDetalle {
    private int id;
    private String unidad;
    private double peso;
    private String codigo;
    private String descripcion;
    private String tratamiento;
    private double cantidadBulto;
    private Integer tipoItem;
    private Integer tipoPaquete;
    private boolean estado;
    private int tipoBalanza;
    private double pesoReferencial;
    private String codigoMae;
    private String tipoContenedor;
    private String estadoFisico;
    private Integer residuoSujetoFiscalizacion;
    private Integer requiereDevolucionRecipientes;
    private Integer tieneDisponibilidadMontacarga;
    private Integer tieneDisponibilidadBalanza;
    private Integer requiereIncineracionPresenciada;
    private String observacionResiduos;
    private Integer cantidadRefencial;


    public RowItemManifiestoDetalle() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public double getCantidadBulto() {
        return cantidadBulto;
    }

    public void setCantidadBulto(double cantidadBulto) {
        this.cantidadBulto = cantidadBulto;
    }

    public Integer getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(Integer tipoItem) {
        this.tipoItem = tipoItem;
    }

    public Integer getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(Integer tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getTipoBalanza() {
        return tipoBalanza;
    }

    public void setTipoBalanza(int tipoBalanza) {
        this.tipoBalanza = tipoBalanza;
    }

    public double getPesoReferencial() {
        return pesoReferencial;
    }

    public void setPesoReferencial(double pesoReferencial) {
        this.pesoReferencial = pesoReferencial;
    }

    public String getCodigoMae() {
        return codigoMae;
    }

    public void setCodigoMae(String codigoMae) {
        this.codigoMae = codigoMae;
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
}
