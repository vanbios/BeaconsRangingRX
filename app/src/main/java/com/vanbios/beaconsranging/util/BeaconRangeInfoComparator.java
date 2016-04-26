package com.vanbios.beaconsranging.util;

import com.vanbios.beaconsranging.objects.BeaconRangeInfo;

import java.util.Comparator;

/**
 * Created by Ihor Bilous on 01.02.2016.
 */
public class BeaconRangeInfoComparator implements Comparator<BeaconRangeInfo> {

    @Override
    public int compare(BeaconRangeInfo lhs, BeaconRangeInfo rhs) {
        if (lhs.getRssi() == rhs.getRssi()) return 0;
        return lhs.getRssi() < rhs.getRssi() ? 1 : -1;
    }
}
