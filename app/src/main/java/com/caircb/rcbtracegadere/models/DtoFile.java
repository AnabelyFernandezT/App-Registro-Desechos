package com.caircb.rcbtracegadere.models;

public class DtoFile {
    Long id;
    String url;
    String file;

    public DtoFile(String url, String file) {
        this.url = url;
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
