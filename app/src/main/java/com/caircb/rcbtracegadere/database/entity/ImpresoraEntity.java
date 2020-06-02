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
    private String name;

    @NonNull
    private String address;

    public ImpresoraEntity(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
