package com.vanbios.beaconsranging.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Ihor Bilous on 11.01.2016.
 */

@AllArgsConstructor
@Getter
public class IBeaconBase {
    private int idBeacon, major, minor, idStore, floor, idTradeCenter, rssiLimit;
    private String storeName, tradeCenterName, address;
}
