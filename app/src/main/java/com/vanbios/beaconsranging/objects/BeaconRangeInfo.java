package com.vanbios.beaconsranging.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Ihor Bilous on 28.01.2016.
 */

@AllArgsConstructor()
@Getter
public class BeaconRangeInfo {
    private String text;
    private int rssi;

    public static final int NEAR = 1, MIDDLE = 2, FAR = 3;


    /*public BeaconRangeInfo(String text, int rssi) {
        this.text = text;
        this.rssi = rssi;
    }

    public String getText() {
        return text;
    }

    public int getRssi() {
        return rssi;
    }*/

    public int getTypeByRSSI(int rssi) {
        rssi *= -1;
        if (rssi > 90) return FAR;
        return rssi < 75 ? NEAR : MIDDLE;
    }
}
