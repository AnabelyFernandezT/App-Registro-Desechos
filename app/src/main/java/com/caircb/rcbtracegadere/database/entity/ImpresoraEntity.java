package com.caircb.rcbtracegadere.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = (ImpresoraEntity.TABLE))
public class ImpresoraEntity {

    public static final String TABLE = "tb_impresora";

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Integer _id;

    @NonNull
    private String printID;

    @NonNull
    private String code;

    @NonNull
    private String address;

    @NonNull
    private Integer type;

    @NonNull
    private Boolean useActive;

    public ImpresoraEntity() {
    }

    public ImpresoraEntity(String printID, String code, String address, Integer type, Boolean defaulActive) {
        this.printID = printID;
        this.code = code;
        this.address = address;
        this.type = type;
        this.useActive=defaulActive;
    }

    public Integer get_id() { return _id; }

    public void set_id(Integer _id) { this._id = _id; }

    public String getPrintID() {
        return printID;
    }

    public void setPrintID(String printID) {
        this.printID = printID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getUseActive() {return useActive;}

    public void setUseActive(Boolean useActive) {this.useActive = useActive;}
}
