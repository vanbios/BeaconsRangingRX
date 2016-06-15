package com.vanbios.beaconsranging.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Ihor Bilous on 13.01.2016.
 */

@AllArgsConstructor()
@Getter
public class BeaconZoneLogInfo {
    private long datetime;
    private int idBeacon, type, state;

    public static final int ENTER = 1, EXIT = 0, ENTRY = 1, ADVERTISING = 0;


    /*public BeaconZoneLogInfo(long datetime, int idBeacon, int type, int state) {
        this.datetime = datetime;
        this.idBeacon = idBeacon;
        this.type = type;
        this.state = state;
    }


    public long getDatetime() {
        return datetime;
    }

    public int getIdBeacon() {
        return idBeacon;
    }

    public int getType() {
        return type;
    }

    public int getState() {
        return state;
    }*/
}
