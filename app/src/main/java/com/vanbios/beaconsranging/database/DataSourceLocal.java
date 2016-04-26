package com.vanbios.beaconsranging.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vanbios.beaconsranging.objects.AdvertisingLogInfo;
import com.vanbios.beaconsranging.objects.BeaconZoneLogInfo;
import com.vanbios.beaconsranging.objects.IBeaconAds;
import com.vanbios.beaconsranging.objects.IBeaconEnter;
import com.vanbios.beaconsranging.objects.StoreEntryLogInfo;
import com.vanbios.beaconsranging.util.DateUtils;

import java.util.ArrayList;

/**
 * Created by Ihor Bilous on 10.12.2015.
 */
public class DataSourceLocal {
    private DBHelperLocal dbHelperLocal;
    private SQLiteDatabase db;

    public DataSourceLocal(Context context) {
        dbHelperLocal = new DBHelperLocal(context);
    }

    private void openLocalToWrite() throws SQLException {
        db = dbHelperLocal.getWritableDatabase();
    }

    private void openLocalToRead() throws SQLException {
        db = dbHelperLocal.getReadableDatabase();
    }

    private void closeLocal() {
        db.close();
    }

    public void cleanDBAdsEnter() {
        openLocalToWrite();
        db.delete("ibeacon_ads", null, null);
        db.delete("ibeacon_enter", null, null);
        closeLocal();
    }

    public void cleanDBLogs() {
        openLocalToWrite();
        db.delete("IBStoreEntryLogs", null, null);
        db.delete("IBZoneLogs", null, null);
        db.delete("IBAdvertisingLogs", null, null);
        closeLocal();
    }


    public void insertIBeaconEnter(IBeaconEnter iBeaconEnter) {
        ContentValues values = new ContentValues();
        values.put("uuid", iBeaconEnter.getUUID());
        values.put("major", iBeaconEnter.getMajor());
        values.put("minor", iBeaconEnter.getMinor());
        values.put("is_notify", iBeaconEnter.isNotify() ? 1 : 0);
        IBeaconEnter checkIBeaconEnter = getIBeaconEnter(iBeaconEnter.getMajor(), iBeaconEnter.getMinor());
        openLocalToWrite();
        if (checkIBeaconEnter == null) db.insert("ibeacon_enter", null, values);
        closeLocal();
    }

    public void insertIBeaconAds(IBeaconAds iBeaconAds) {
        ContentValues values = new ContentValues();
        values.put("uuid", iBeaconAds.getUUID());
        values.put("major", iBeaconAds.getMajor());
        values.put("minor", iBeaconAds.getMinor());
        values.put("is_notify", iBeaconAds.isNotify() ? 1 : 0);
        openLocalToWrite();
        IBeaconAds checkIBeaconAds = getIBeaconAds(iBeaconAds.getMajor(), iBeaconAds.getMinor());
        openLocalToWrite();
        if (checkIBeaconAds == null) db.insert("ibeacon_ads", null, values);
        closeLocal();
    }

    public void updateNotifyStatusEnter(boolean isNotify, int major, int minor) {
        ContentValues values = new ContentValues();
        values.put("is_notify", isNotify ? 1 : 0);
        openLocalToWrite();
        db.update("ibeacon_enter", values, "major = " + major + " and minor = " + minor, null);
        closeLocal();
    }

    public void updateNotifyStatusAds(boolean isNotify, int major, int minor) {
        ContentValues values = new ContentValues();
        values.put("is_notify", isNotify ? 1 : 0);
        openLocalToWrite();
        db.update("ibeacon_ads", values, "major = " + major + " and minor = " + minor, null);
        closeLocal();
    }

    private boolean isNotify;

    public boolean isNotifyEnter(int major, int minor) {
        String selectQuery = "SELECT * FROM ibeacon_enter WHERE  major = " + major + " and minor = " + minor;
        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int isNotifyColumn = cursor.getColumnIndex("is_notify");
            do {
                int isNotifyInt = cursor.getInt(isNotifyColumn);
                Log.d("wtf", "GET VALUE isNotifyInt = " + isNotifyInt);
                isNotify = isNotifyInt == 1;
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeLocal();
        return isNotify;
    }

    public boolean isNotifyAds(int major, int minor) {
        String selectQuery = "SELECT * FROM ibeacon_ads WHERE  major = " + major + " and minor = " + minor;
        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int isNotifyColumn = cursor.getColumnIndex("is_notify");
            do {
                int isNotifyInt = cursor.getInt(isNotifyColumn);
                isNotify = isNotifyInt == 1;
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeLocal();
        return isNotify;
    }


    public IBeaconEnter getIBeaconEnter(int major, int minor) {
        IBeaconEnter result = null;
        String selectQuery = "SELECT * FROM ibeacon_enter WHERE major = " + major + " and minor = " + minor;
        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int uuidIndex = cursor.getColumnIndex("uuid");
            int majorIndex = cursor.getColumnIndex("major");
            int minorIndex = cursor.getColumnIndex("minor");
            do {
                result = new IBeaconEnter(cursor.getString(uuidIndex),
                        cursor.getInt(majorIndex),
                        cursor.getInt(minorIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeLocal();
        return result;
    }

    public IBeaconAds getIBeaconAds(int major, int minor) {
        IBeaconAds result = null;
        String selectQuery = "SELECT * FROM ibeacon_ads WHERE major = " + major + " and minor = " + minor;
        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int uuidIndex = cursor.getColumnIndex("uuid");
            int majorIndex = cursor.getColumnIndex("major");
            int minorIndex = cursor.getColumnIndex("minor");
            do {
                result = new IBeaconAds(cursor.getString(uuidIndex),
                        cursor.getInt(majorIndex),
                        cursor.getInt(minorIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeLocal();
        return result;
    }


    public void insertStoreEntryLog(StoreEntryLogInfo storeEntryLogInfo) {
        ContentValues cv = new ContentValues();
        long datetime = storeEntryLogInfo.getDatetime();
        cv.put("datetime", datetime);
        cv.put("id_store", storeEntryLogInfo.getIdStore());
        cv.put("state", storeEntryLogInfo.getState());
        cv.put("visibility", storeEntryLogInfo.getVisibility());

        openLocalToWrite();
        if (db.update("IBStoreEntryLogs", cv, "datetime = '" + datetime + "' ", null) == 0) {
            db.insert("IBStoreEntryLogs", null, cv);
        }
        closeLocal();
    }


    public void insertAdvertisingLog(AdvertisingLogInfo advertisingLogInfo) {
        ContentValues cv = new ContentValues();
        long datetime = advertisingLogInfo.getDatetime();
        cv.put("datetime", datetime);
        cv.put("id_beacon", advertisingLogInfo.getIdBeacon());
        cv.put("message", advertisingLogInfo.getMessage());
        cv.put("visibility", advertisingLogInfo.getVisibility());

        openLocalToWrite();
        if (db.update("IBAdvertisingLogs", cv, "datetime = '" + datetime + "' ", null) == 0) {
            db.insert("IBAdvertisingLogs", null, cv);
        }
        closeLocal();
    }


    public void insertBeaconZoneLog(BeaconZoneLogInfo beaconZoneLogInfo) {
        ContentValues cv = new ContentValues();
        long datetime = beaconZoneLogInfo.getDatetime();
        cv.put("datetime", datetime);
        cv.put("id_beacon", beaconZoneLogInfo.getIdBeacon());
        cv.put("type", beaconZoneLogInfo.getType());
        cv.put("state", beaconZoneLogInfo.getState());
        openLocalToWrite();
        if (db.update("IBZoneLogs", cv, "datetime = '" + datetime + "' ", null) == 0) {
            db.insert("IBZoneLogs", null, cv);
        }
        closeLocal();
    }

    public ArrayList<StoreEntryLogInfo> getStoreEntryLogListByDate(long datetime) {
        long start = DateUtils.getTimeStampStartOfDay(datetime);
        long end = DateUtils.getTimeStampEndOfDay(datetime);
        ArrayList<StoreEntryLogInfo> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM IBStoreEntryLogs " +
                "WHERE datetime BETWEEN " + start +
                " AND " + end;

        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int datetimeIndex = cursor.getColumnIndex("datetime");
            int idIndex = cursor.getColumnIndex("id_store");
            int stateIndex = cursor.getColumnIndex("state");
            int visibilityIndex = cursor.getColumnIndex("visibility");
            do {
                result.add(new StoreEntryLogInfo(
                        cursor.getLong(datetimeIndex),
                        cursor.getInt(idIndex),
                        cursor.getInt(stateIndex),
                        cursor.getInt(visibilityIndex)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeLocal();
        return result;
    }


    public ArrayList<AdvertisingLogInfo> getAdvertisingLogListByDate(long datetime) {
        long start = DateUtils.getTimeStampStartOfDay(datetime);
        long end = DateUtils.getTimeStampEndOfDay(datetime);
        ArrayList<AdvertisingLogInfo> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM IBAdvertisingLogs " +
                "WHERE datetime BETWEEN " + start +
                " AND " + end;

        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int datetimeIndex = cursor.getColumnIndex("datetime");
            int idIndex = cursor.getColumnIndex("id_beacon");
            int messageIndex = cursor.getColumnIndex("message");
            int visibilityIndex = cursor.getColumnIndex("visibility");
            do {
                result.add(new AdvertisingLogInfo(
                        cursor.getLong(datetimeIndex),
                        cursor.getInt(idIndex),
                        cursor.getInt(visibilityIndex),
                        cursor.getString(messageIndex)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeLocal();
        return result;
    }


    public ArrayList<BeaconZoneLogInfo> getBeaconZoneLogListByDate(long datetime) {
        long start = DateUtils.getTimeStampStartOfDay(datetime);
        long end = DateUtils.getTimeStampEndOfDay(datetime);
        ArrayList<BeaconZoneLogInfo> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM IBZoneLogs " +
                "WHERE datetime BETWEEN " + start +
                " AND " + end;

        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int datetimeIndex = cursor.getColumnIndex("datetime");
            int idIndex = cursor.getColumnIndex("id_beacon");
            int typeIndex = cursor.getColumnIndex("type");
            int stateIndex = cursor.getColumnIndex("state");
            do {
                result.add(new BeaconZoneLogInfo(
                        cursor.getLong(datetimeIndex),
                        cursor.getInt(idIndex),
                        cursor.getInt(typeIndex),
                        cursor.getInt(stateIndex)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeLocal();
        return result;
    }


    public void deleteStoreEntryLogs(long datetime) {
        openLocalToWrite();
        db.delete("IBStoreEntryLogs", "datetime < '" + datetime + "' ", null);
        closeLocal();
    }


    public void deleteAdvertisingLogs(long datetime) {
        openLocalToWrite();
        db.delete("IBAdvertisingLogs", "datetime < '" + datetime + "' ", null);
        closeLocal();
    }


    public void deleteBeaconZoneLogs(long datetime) {
        openLocalToWrite();
        db.delete("IBZoneLogs", "datetime < '" + datetime + "' ", null);
        closeLocal();
    }
}
