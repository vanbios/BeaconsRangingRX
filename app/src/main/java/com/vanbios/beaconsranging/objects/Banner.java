package com.vanbios.beaconsranging.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Ihor Bilous on 21.01.2016.
 */

@AllArgsConstructor()
@Getter
public class Banner {
    private int beaconId;
    private String text, url;
}
