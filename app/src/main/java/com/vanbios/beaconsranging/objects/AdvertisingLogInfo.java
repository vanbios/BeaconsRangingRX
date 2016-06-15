package com.vanbios.beaconsranging.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Ihor Bilous on 13.01.2016.
 */

@AllArgsConstructor()
@Getter
public class AdvertisingLogInfo {
    private long datetime;
    private int idBeacon, visibility;
    private String message;

    public static final int VISIBLE = 1, INVISIBLE = 0;


    /*public AdvertisingLogInfo(long datetime, int idBeacon, int visibility, String message) {
        this.datetime = datetime;
        this.idBeacon = idBeacon;
        this.visibility = visibility;
        this.message = message;
    }*/


    /*public long getDatetime() {
        return datetime;
    }

    public int getIdBeacon() {
        return idBeacon;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getMessage() {
        return message;
    }*/
}
