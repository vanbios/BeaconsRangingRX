package com.vanbios.beaconsranging.objects;

/**
 * Created by Ihor Bilous on 11.01.2016.
 */
public class IBeaconBase {
    private int idBeacon, major, minor, idStore, floor, idTradeCenter, rssiLimit;
    private String storeName, tradeCenterName, address;

    public IBeaconBase(int idBeacon, int major, int minor, int idStore, int floor, int idTradeCenter,
                       int rssiLimit, String storeName, String tradeCenterName, String address) {
        this.idBeacon = idBeacon;
        this.major = major;
        this.minor = minor;
        this.idStore = idStore;
        this.floor = floor;
        this.idTradeCenter = idTradeCenter;
        this.rssiLimit = rssiLimit;
        this.storeName = storeName;
        this.tradeCenterName = tradeCenterName;
        this.address = address;
    }

    public int getIdBeacon() {
        return idBeacon;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getIdStore() {
        return idStore;
    }

    public int getFloor() {
        return floor;
    }

    public int getIdTradeCenter() {
        return idTradeCenter;
    }

    public int getRssiLimit() {
        return rssiLimit;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getTradeCenterName() {
        return tradeCenterName;
    }

    public String getAddress() {
        return address;
    }
}
