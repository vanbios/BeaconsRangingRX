package com.vanbios.beaconsranging.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Ihor Bilous on 09.12.2015.
 */

@AllArgsConstructor
@Getter
@Setter
public class IBeaconAds {
    private String UUID;
    private int minor, major;
    private boolean isNotify;


    /*public IBeaconAds(String UUID,  int major, int minor, boolean isNotify) {
        this.UUID = UUID;
        this.minor = minor;
        this.major = major;
        this.isNotify = isNotify;
    }*/

    public IBeaconAds(String UUID, int minor, int major) {
        this.UUID = UUID;
        this.minor = minor;
        this.major = major;
    }

    @Override
    public boolean equals(Object o) {
        return (this.UUID.equals(((IBeaconAds) o).UUID) &&
                this.major == (((IBeaconAds) o).major) &&
                this.minor == (((IBeaconAds) o ).minor));
    }

    /*public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

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

    public boolean isNotify() {
        return isNotify;
    }

    public void setIsNotify(boolean isNotifyAds) {
        this.isNotify = isNotifyAds;
    }*/

}
