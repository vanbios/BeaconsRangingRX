package com.vanbios.beaconsranging.util;

import android.content.Intent;

import com.vanbios.beaconsranging.AppController;
import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.fragments.FrgLogs;
import com.vanbios.beaconsranging.fragments.FrgSearch;
import com.vanbios.beaconsranging.objects.AdvertisingLogInfo;
import com.vanbios.beaconsranging.objects.BeaconZoneLogInfo;
import com.vanbios.beaconsranging.objects.IBeaconBase;
import com.vanbios.beaconsranging.objects.LogBase;
import com.vanbios.beaconsranging.objects.StoreEntryLogInfo;
import com.vanbios.beaconsranging.singleton.InfoSingleton;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ihor Bilous on 08.12.2015.
 */
public class DBStateLogger {

    public void insertStoreEntryLog(StoreEntryLogInfo info) {
        InfoSingleton.getInstance().getDataSourceLocal().insertStoreEntryLog(info);
        sendBroadcastToUpdateLogList();
    }

    public void insertAdvertisingLog(AdvertisingLogInfo info) {
        InfoSingleton.getInstance().getDataSourceLocal().insertAdvertisingLog(info);
        sendBroadcastToUpdateLogList();
    }

    public void insertBeaconZoneLog(BeaconZoneLogInfo info) {
        InfoSingleton.getInstance().getDataSourceLocal().insertBeaconZoneLog(info);
        sendBroadcastToUpdateLogList();
    }


    public ArrayList<StoreEntryLogInfo> getStoreEntryLogListByDate(long datetime) {
        return InfoSingleton.getInstance().getDataSourceLocal().getStoreEntryLogListByDate(datetime);
    }

    public ArrayList<AdvertisingLogInfo> getAdvertisingLogListByDate(long datetime) {
        return InfoSingleton.getInstance().getDataSourceLocal().getAdvertisingLogListByDate(datetime);
    }

    public ArrayList<BeaconZoneLogInfo> getBeaconZoneLogListByDate(long datetime) {
        return InfoSingleton.getInstance().getDataSourceLocal().getBeaconZoneLogListByDate(datetime);
    }


    public void deleteStoreEntryLogs(long datetime) {
        InfoSingleton.getInstance().getDataSourceLocal().deleteStoreEntryLogs(datetime);
    }

    public void deleteAdvertisingLogs(long datetime) {
        InfoSingleton.getInstance().getDataSourceLocal().deleteAdvertisingLogs(datetime);
    }

    public void deleteBeaconZoneLogs(long datetime) {
        InfoSingleton.getInstance().getDataSourceLocal().deleteBeaconZoneLogs(datetime);
    }


    // Logs to show in UI

    public ArrayList<LogBase> getStoreEntryLogsToShow(long datetime) {
        ArrayList<StoreEntryLogInfo> list = getStoreEntryLogListByDate(datetime);
        ArrayList<LogBase> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (StoreEntryLogInfo info : list) {
                IBeaconBase beacon = InfoSingleton.getInstance().findBeaconByIdStore(info.getIdStore());
                if (beacon != null) {

                    result.add(new LogBase(
                            String.format(
                                    AppController.getContext().getString(R.string.placeholder_store_log_info),
                                    DateUtils.dateTimeToString(new Date(info.getDatetime())),
                                    info.getState() == 1 ?
                                            AppController.getContext().getString(R.string.enter) :
                                            AppController.getContext().getString(R.string.exit),
                                    beacon.getStoreName(),
                                    beacon.getTradeCenterName(),
                                    beacon.getFloor(),
                                    beacon.getAddress(),
                                    info.getVisibility() == 1 ?
                                            AppController.getContext().getString(R.string.visible) :
                                            AppController.getContext().getString(R.string.invisible)),
                            info.getVisibility()
                    ));
                }
            }
        }
        return result;
    }

    public ArrayList<LogBase> getAdvertisingLogsToShow(long datetime) {
        ArrayList<AdvertisingLogInfo> list = getAdvertisingLogListByDate(datetime);
        ArrayList<LogBase> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (AdvertisingLogInfo info : list) {
                IBeaconBase beacon = InfoSingleton.getInstance().findAdvertisingBeaconById(info.getIdBeacon());
                if (beacon != null) {

                    result.add(new LogBase(
                            String.format(
                                    AppController.getContext().getString(R.string.placeholder_advertising_log_info),
                                    DateUtils.dateTimeToString(new Date(info.getDatetime())),
                                    "b9407f30-f5f8-466e-aff9-25556b57fe6d",
                                    beacon.getMajor(),
                                    beacon.getMinor(),
                                    info.getMessage(),
                                    info.getVisibility() == 1 ?
                                            AppController.getContext().getString(R.string.visible) :
                                            AppController.getContext().getString(R.string.invisible)),
                            info.getVisibility()
                    ));
                }
            }
        }
        return result;
    }

    public ArrayList<LogBase> getBeaconZoneLogsToShow(long datetime) {
        ArrayList<BeaconZoneLogInfo> list = getBeaconZoneLogListByDate(datetime);
        ArrayList<LogBase> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (BeaconZoneLogInfo info : list) {
                IBeaconBase beacon = InfoSingleton.getInstance().findBeaconById(info.getType() == 1 ?
                        FrgSearch.ENTRY : FrgSearch.ADVERTISING, info.getIdBeacon());
                if (beacon != null) {

                    result.add(new LogBase(
                            String.format(
                                    AppController.getContext().getString(R.string.placeholder_beacon_zone_log_info),
                                    DateUtils.dateTimeToString(new Date(info.getDatetime())),
                                    info.getState() == 1 ?
                                            AppController.getContext().getString(R.string.enter) :
                                            AppController.getContext().getString(R.string.exit),
                                    info.getType() == 1 ?
                                            "f7826da6-4fa2-4e98-8024-bc5b71e0893e" :
                                            "b9407f30-f5f8-466e-aff9-25556b57fe6d",
                                    beacon.getMajor(),
                                    beacon.getMinor()),
                            info.getState()
                    ));
                }
            }
        }
        return result;
    }

    private void sendBroadcastToUpdateLogList() {
        Intent intentFrgOrdersAll = new Intent(FrgLogs.BROADCAST_FRG_LOGS);
        intentFrgOrdersAll.putExtra(FrgLogs.PARAM_STATUS_FRG_LOGS,
                FrgLogs.STATUS_NEW_LOG_INSERTED);
        AppController.getContext().sendBroadcast(intentFrgOrdersAll);
    }
}
