package com.vanbios.beaconsranging.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by Ihor Bilous on 09.12.2015.
 */
public class BluetoothUtils {

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) return bluetoothAdapter.enable();
        else if (!enable && isEnabled) return bluetoothAdapter.disable();
        // no need to change bluetooth state
        return true;
    }


    public static boolean isBLESupportedOnDevice(Context context) {
        return Build.VERSION.SDK_INT >= 19 && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}
