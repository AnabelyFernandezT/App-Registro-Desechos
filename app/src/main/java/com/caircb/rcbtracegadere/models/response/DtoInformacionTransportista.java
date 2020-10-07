package com.caircb.rcbtracegadere.models.response;

public class DtoInformacionTransportista {
    private Integer idFinRutaCatalogo;
    private String nombreCorto;
    private String apkVersion;

    public Integer getIdFinRutaCatalogo() {
        return idFinRutaCatalogo;
    }

    public void setIdFinRutaCatalogo(Integer idFinRutaCatalogo) {
        this.idFinRutaCatalogo = idFinRutaCatalogo;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }
}
