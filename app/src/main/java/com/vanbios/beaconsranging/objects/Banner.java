package com.vanbios.beaconsranging.objects;

/**
 * Created by Ihor Bilous on 21.01.2016.
 */
public class Banner {
    private int beaconId;
    private String text, url;

    public Banner(int beaconId, String text, String url) {
        this.beaconId = beaconId;
        this.text = text;
        this.url = url;
    }

    public int getBeaconId() {
        return beaconId;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}
