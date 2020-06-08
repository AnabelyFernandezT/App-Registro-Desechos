package com.caircb.rcbtracegadere.models;

public class RowPrinters {
    private String name;
    private String address;

    public RowPrinters(String name, String address){
        this.name = name;
        this.address = address;
    }


    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }
}
