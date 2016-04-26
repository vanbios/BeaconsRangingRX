package com.vanbios.beaconsranging.objects;


/**
 * Created by Ihor Bilous on 09.12.2015.
 */
public class IBeaconEnter {
    private String UUID;
    private int minor;
    private int major;
    private boolean isNotify;

    public boolean isNotify() {
        return isNotify;
    }

    public void setIsNotify(boolean isNotify) {
        this.isNotify = isNotify;
    }


    @Override
    public boolean equals(Object o) {
        return (this.UUID.equals(((IBeaconEnter) o).UUID) &&
                this.major == (((IBeaconEnter) o).major) &&
                this.minor == (((IBeaconEnter) o ).minor));
    }

    public IBeaconEnter(String UUID,  int major, int minor) {
        this.UUID = UUID;
        this.minor = minor;
        this.major = major;
    }

    public IBeaconEnter(String UUID,  int major, int minor, boolean isNotify) {
        this.UUID = UUID;
        this.minor = minor;
        this.major = major;
        this.isNotify = isNotify;
    }

    public IBeaconEnter(){}

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }


    @Override
    public String toString() {
        return "IBeaconEnter{" +
                "UUID='" + UUID + '\'' +
                ", minor=" + minor +
                ", major=" + major +
                '}';
    }
}
