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
}
