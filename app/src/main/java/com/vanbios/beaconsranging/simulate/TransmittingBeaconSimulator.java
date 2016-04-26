package com.vanbios.beaconsranging.simulate;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

/**
 * Created by Ihor Bilous on 09.12.2015.
 */
public class TransmittingBeaconSimulator {
    private Context context;
    private BeaconTransmitter beaconTransmitter;
    private int major = 1, minor = 1;

    public TransmittingBeaconSimulator(Context context) {
        this.context = context;
    }

    private boolean checkTransmittingSupport() {
        return BeaconTransmitter.checkTransmissionSupported(context) == BeaconTransmitter.SUPPORTED
                && Build.VERSION.SDK_INT >= 21;
    }

    public boolean startTransmitting() {
        if (checkTransmittingSupport()) {
            Beacon beacon = new Beacon.Builder()
                    .setId1("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
                    .setId2(String.valueOf(major))
                    .setId3(String.valueOf(minor))
                    .setManufacturer(0x0118) // Radius Networks.  Change this for other beacon layouts
                    .setTxPower(-59)
                    .setDataFields(null) // Remove this for beacon layouts without d: fields
                    .build();

            // Change the layout below for other beacon types
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24");
            beaconTransmitter = new BeaconTransmitter(context, beaconParser);
            beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

                @Override
                public void onStartFailure(int errorCode) {
                    Log.e("COLLIDER", "Advertisement start failed with code: " + errorCode);
                }

                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    Log.d("COLLIDER", "Advertisement start succeeded.");
                }
            });
            return true;
        } else {
            Log.d("COLLIDER", "Transmitting not supported on this device");
        }
        return false;
    }

    public boolean stopTransmitting() {
        if (beaconTransmitter != null && beaconTransmitter.isStarted()) {
            beaconTransmitter.stopAdvertising();
            return true;
        }
        return false;
    }


    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }
}
