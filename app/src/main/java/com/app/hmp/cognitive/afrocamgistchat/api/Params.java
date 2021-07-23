package com.app.hmp.cognitive.afrocamgistchat.api;

import java.util.ArrayList;

public class Params {

    private String name;
    private String value;
    private ArrayList<Params> deviceArray;

    public Params(String name , String value) {
        this.value = value;
        this.name = name;
    }

    public Params(String name , ArrayList<Params> deviceArray) {
        this.deviceArray = deviceArray;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    public ArrayList<Params> getDeviceArray() {
        return deviceArray;
    }

    public void setDeviceArray(ArrayList<Params> deviceArray) {
        this.deviceArray = deviceArray;
    }

    public String toString() {
        return "param{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    /*public ArrayList<Params> toArray() {
        return deviceArray;
    }*/

}
