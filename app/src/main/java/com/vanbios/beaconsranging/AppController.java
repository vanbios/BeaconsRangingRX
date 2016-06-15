package com.vanbios.beaconsranging;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.vanbios.beaconsranging.database.DBHelperBeacons;
import com.vanbios.beaconsranging.service.ServiceListenerBeacon;
import com.vanbios.beaconsranging.util.SharedPref;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ihor Bilous on 08.12.2015.
 */
public class AppController extends Application {
    private static final String TAG = "COLLIDER";
    private static final String DATA_BASE = "IBLogs.db";
    private static Context context;

    private BeaconManager beaconManager;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SharedPref sharedPref = new SharedPref(context);

        if (sharedPref.isFirstStartApp()){
            sharedPref.setIsFirstStartApp(false);
            context.deleteDatabase(DATA_BASE);
        }

        Log.d(TAG, "App started up");
        prepareBeaconsDB();
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.connect(() -> beaconManager.startMonitoring(
                new Region("monitored region", null, null, null)));

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                showNotification("Region", "You in beacon region!");
                startService(new Intent(context, ServiceListenerBeacon.class));
            }

            @Override
            public void onExitedRegion(Region region) {
                showNotification("Region", "You left beacon region!");
                stopService(new Intent(context, ServiceListenerBeacon.class));
            }
        });
    }

    private void prepareBeaconsDB() {
        DBHelperBeacons myDbHelper = new DBHelperBeacons(context);
        try {
            myDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return context;
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2222, notification);
    }

}