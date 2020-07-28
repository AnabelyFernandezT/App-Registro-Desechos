package com.caircb.rcbtracegadere.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DtoUserCredential {
    private String id;
    private boolean actualizarVersion;
    private boolean exito;
    private String mensaje;
    private String correo;
    private Integer idUsuario;

    @SerializedName("listaEmpresas")
    @Expose
    private List<DtoListaEmpresa> listaEmpresas;

    //public DtoUserCredential() {
    //}

    public boolean isActualizarVersion() {
        return actualizarVersion;
    }

    public void setActualizarVersion(boolean actualizarVersion) {
        this.actualizarVersion = actualizarVersion;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<DtoListaEmpresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<DtoListaEmpresa> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
