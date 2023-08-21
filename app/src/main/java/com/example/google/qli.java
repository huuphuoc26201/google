package com.example.google;

public class qli {
    public String name,sdt,code;
    public qli(){
    }
    public qli(String name, String sdt, String code) {
        this.name = name;
        this.sdt = sdt;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
