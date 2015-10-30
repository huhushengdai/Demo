package com.windy.bluetooth.bean;

import java.io.Serializable;

/**
 * author: wang
 * time: 2015/10/29
 * description:
 */
public class DeviceInfo implements Serializable{
    private String address;
    private String name;

    public DeviceInfo() {
    }

    public DeviceInfo(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
