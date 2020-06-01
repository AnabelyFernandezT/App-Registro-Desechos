package com.caircb.rcbtracegadere.models.request;

public class RequestCredentials {
    private String usLogin;
    private String usClave;
    private String usImei;
    private Integer usAppVersion;
    private String usChip;
    private String usAplicacion;

    public RequestCredentials() {
    }


    public String getUsLogin() {
        return usLogin;
    }

    public void setUsLogin(String usLogin) {
        this.usLogin = usLogin;
    }

    public String getUsClave() {
        return usClave;
    }

    public void setUsClave(String usClave) {
        this.usClave = usClave;
    }

    public String getUsImei() {
        return usImei;
    }

    public void setUsImei(String usImei) {
        this.usImei = usImei;
    }

    public Integer getUsAppVersion() {
        return usAppVersion;
    }

    public void setUsAppVersion(Integer usAppVersion) {
        this.usAppVersion = usAppVersion;
    }

    public String getUsChip() {
        return usChip;
    }

    public void setUsChip(String usChip) {
        this.usChip = usChip;
    }

    public String getUsAplicacion() {
        return usAplicacion;
    }

    public void setUsAplicacion(String usAplicacion) {
        this.usAplicacion = usAplicacion;
    }
}
