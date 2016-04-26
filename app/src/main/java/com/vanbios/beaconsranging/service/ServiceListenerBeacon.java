package com.vanbios.beaconsranging.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.vanbios.beaconsranging.MainActivity;
import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.fragments.FrgBeaconsList;
import com.vanbios.beaconsranging.objects.AdvertisingLogInfo;
import com.vanbios.beaconsranging.objects.Banner;
import com.vanbios.beaconsranging.objects.BeaconRangeInfo;
import com.vanbios.beaconsranging.objects.BeaconZoneLogInfo;
import com.vanbios.beaconsranging.objects.IBeaconAds;
import com.vanbios.beaconsranging.objects.IBeaconBase;
import com.vanbios.beaconsranging.objects.IBeaconEnter;
import com.vanbios.beaconsranging.objects.StoreEntryLogInfo;
import com.vanbios.beaconsranging.singleton.InfoSingleton;
import com.vanbios.beaconsranging.util.DBStateLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ihor Bilous on 08.12.2015.
 */
public class ServiceListenerBeacon extends Service {

    public static final String BEACON_ENTER = "f7826da6-4fa2-4e98-8024-bc5b71e0893e";
    public static final String BEACON_ADS = "b9407f30-f5f8-466e-aff9-25556b57fe6d";
    public static final String REGION = "region_listener";

    private BeaconManager beaconManager;
    private Region region;
    private ArrayList<IBeaconBase> entryList;
    private ArrayList<IBeaconBase> advertisingList;
    private static ArrayList<Beacon> previousBeaconsList;
    private Context context;
    private int notificationCount = 0;

    public ServiceListenerBeacon() {}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        previousBeaconsList = new ArrayList<>();

        entryList = InfoSingleton.getInstance().getEntryList();
        advertisingList = InfoSingleton.getInstance().getAdvertisingList();
        region = new Region(REGION, null, null, null);
        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new com.estimote.sdk.BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(com.estimote.sdk.Region region, List<Beacon> list) {
                List<Beacon> list1 = new ArrayList<>();
                List<Beacon> list2 = new ArrayList<>();

                if (!previousBeaconsList.isEmpty() && !list.isEmpty()) {
                    list1.addAll(previousBeaconsList);
                    list2.addAll(list);
                    Collection<Beacon> col1 = new ArrayList<>(list1);
                    Collection<Beacon> col2 = new ArrayList<>(list2);

                    list1.removeAll(col2);
                    list2.removeAll(col1);

                    if (!list1.isEmpty()) {
                        // log exit beacon zones info
                        for (Beacon beacon : list1) {
                            logBeaconZoneInfoToDB(beacon, BeaconZoneLogInfo.EXIT);
                        }
                    }

                    if (!list2.isEmpty()) {
                        // log enter beacon zones info
                        for (Beacon beacon : list2) {
                            logBeaconZoneInfoToDB(beacon, BeaconZoneLogInfo.ENTER);
                        }
                    }
                } else if (!previousBeaconsList.isEmpty()) {
                    // log exit beacon zones info
                    for (Beacon beacon : previousBeaconsList) {
                        logBeaconZoneInfoToDB(beacon, BeaconZoneLogInfo.EXIT);
                    }
                } else if (!list.isEmpty()) {
                    // log enter beacon zones info
                    for (Beacon beacon : list) {
                        logBeaconZoneInfoToDB(beacon, BeaconZoneLogInfo.ENTER);
                    }
                }

                previousBeaconsList.clear();
                previousBeaconsList.addAll(list);

                ArrayList<BeaconRangeInfo> beaconRangeList = new ArrayList<>();
                if (!list.isEmpty()) {
                    for (Beacon beacon : list) {
                        listenAdsBeacons(beacon);
                        listenEnterBeacons(beacon);

                        String uuid = beacon.getProximityUUID().toString();
                        int major = beacon.getMajor();
                        int minor = beacon.getMinor();
                        int rssi = beacon.getRssi();
                        int power = beacon.getMeasuredPower();

                        beaconRangeList.add(new BeaconRangeInfo(
                                String.format(getString(R.string.placeholder_beacons_info_list),
                                        uuid, major, minor, rssi, power), rssi));
                    }
                }

                InfoSingleton.getInstance().setBeaconsRangeList(beaconRangeList);

                Intent intent = new Intent(FrgBeaconsList.BROADCAST_FRG_BEACONS_LIST);
                intent.putExtra(FrgBeaconsList.PARAM_STATUS_FRG_BEACONS_LIST, FrgBeaconsList.STATUS_BEACONS_LIST_UPDATED);
                context.sendBroadcast(intent);
            }
        });

        beaconManager.connect(new com.estimote.sdk.BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    private void logBeaconZoneInfoToDB(Beacon beacon, int state) {
        String uuid = beacon.getProximityUUID().toString();
        if (uuid.equals(BEACON_ENTER)
                || uuid.equals(BEACON_ADS)) {
            int type = uuid.equals(BEACON_ENTER) ? 1 : 0;
            int beaconId = type == 1 ?
                    InfoSingleton.getInstance().getEntryBeaconIdByMajorMinor(beacon.getMajor(), beacon.getMinor()) :
                    InfoSingleton.getInstance().getAdvertisingBeaconIdByMajorMinor(beacon.getMajor(), beacon.getMinor());

            if (beaconId > 0) {
                new DBStateLogger().insertBeaconZoneLog(
                        new BeaconZoneLogInfo(System.currentTimeMillis(), beaconId, type, state));
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.stopRanging(region);
    }

    private void listenEnterBeacons(Beacon beacon) {
        if (beacon != null) {
            if (beacon.getProximityUUID().toString().equals(BEACON_ENTER)) {
                for (int i = 0; i < entryList.size(); i++) {
                    if ((beacon.getMajor() == entryList.get(i).getMajor() &&
                            beacon.getMinor() == entryList.get(i).getMinor()) &&
                            ((-1) * beacon.getRssi()) <= entryList.get(i).getRssiLimit()) {
                        InfoSingleton.getInstance().getDataSourceLocal().insertIBeaconEnter(
                                new IBeaconEnter(BEACON_ENTER,
                                        beacon.getMajor(),
                                        beacon.getMinor(),
                                        false));
                        int visibility = StoreEntryLogInfo.UNVISIBLE;
                        if (!InfoSingleton.getInstance().getDataSourceLocal().isNotifyEnter(beacon.getMajor(),
                                beacon.getMinor())) {
                            //ok
                            showNotify("HELLO!!!", "Welcome to shop " + entryList.get(i).getStoreName());
                            notificationCount++;
                            InfoSingleton.getInstance().getDataSourceLocal().updateNotifyStatusEnter(true,
                                    beacon.getMajor(), beacon.getMinor());
                            visibility = StoreEntryLogInfo.VISIBLE;
                        }
                        int storeId = InfoSingleton.getInstance().getStoreIdByEntryMajorMinor(beacon.getMajor(), beacon.getMinor());
                        if (storeId > 0) {
                            new DBStateLogger().insertStoreEntryLog(
                                    new StoreEntryLogInfo(System.currentTimeMillis(), storeId, StoreEntryLogInfo.ENTER, visibility));
                        }
                        break;
                    }
                }
            }
        }
    }

    private void listenAdsBeacons(Beacon beacon) {
        if (beacon != null) {
            if (beacon.getProximityUUID().toString().equals(BEACON_ADS)) {
                for (int i = 0; i < advertisingList.size(); i++) {
                    if ((beacon.getMajor() == advertisingList.get(i).getMajor() &&
                            beacon.getMinor() == advertisingList.get(i).getMinor()) &&
                            ((-1) * beacon.getRssi()) <= advertisingList.get(i).getRssiLimit()) {
                        InfoSingleton.getInstance().getDataSourceLocal().insertIBeaconAds(
                                new IBeaconAds(BEACON_ADS,
                                        beacon.getMajor(),
                                        beacon.getMinor(),
                                        false));
                        // get beacon id and advertising message for push notification and log info
                        int beaconId = InfoSingleton.getInstance()
                                .getAdvertisingBeaconIdByMajorMinor(beacon.getMajor(), beacon.getMinor());
                        Banner banner = InfoSingleton.getInstance().findBannerByBeaconId(beaconId);
                        String message = banner == null ? "" : banner.getText();

                        //ignore
                        int visibility = AdvertisingLogInfo.UNVISIBLE;
                        if (!InfoSingleton.getInstance().getDataSourceLocal().isNotifyAds(beacon.getMajor(),
                                beacon.getMinor())) {
                            //ok
                            showNotify("ADS!!!", "Action in the shop " + advertisingList.get(i).getStoreName());
                            notificationCount++;
                            InfoSingleton.getInstance().getDataSourceLocal().updateNotifyStatusAds(true,
                                    beacon.getMajor(), beacon.getMinor());
                            visibility = AdvertisingLogInfo.VISIBLE;
                        }

                        // insert log info into db
                        if (beaconId > 0) {
                            new DBStateLogger().insertAdvertisingLog(
                                    new AdvertisingLogInfo(System.currentTimeMillis(), beaconId, visibility, message));
                        }
                        break;
                    }
                }
            }
        }
    }

    private void showNotify(String title, String msg) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setGroup("Beacons");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationCount /* ID of notification */, notificationBuilder.build());
    }
}
