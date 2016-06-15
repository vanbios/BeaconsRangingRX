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

}
