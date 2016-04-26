package com.vanbios.beaconsranging.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.vanbios.beaconsranging.objects.Banner;
import com.vanbios.beaconsranging.objects.IBeaconBase;

import java.util.ArrayList;

/**
 * Created by Ihor Bilous on 11.01.2016.
 */
public class DataSourceBeacons {
    private DBHelperBeacons dbHelperBeacons;
    private SQLiteDatabase db;
    public static final String ENTRY_TABLE = "BeaconsEntry", ADVERTISING_TABLE = "BeaconsAdvertising";

    public DataSourceBeacons(Context context) {
        dbHelperBeacons = new DBHelperBeacons(context);
    }

    private void openLocalToRead() throws SQLException {
        db = dbHelperBeacons.getReadableDatabase();
    }

    private void closeLocal() {
        db.close();
    }


    public ArrayList<IBeaconBase> getBeaconsList(String tableName) {
        ArrayList<IBeaconBase> result = new ArrayList<>();
        String selectQuery = "SELECT b.id_beacon, b.major, b.minor, b.rssi_limit, " +
                "s.id_store, s.name AS store_name, s.floor, " +
                "c.id_center, c.name AS center_name, c.address " +
                "FROM " + tableName + " b, Stores s, TraidingCenters c " +
                "WHERE b.id_store = s.id_store AND s.id_center = c.id_center";

        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id_beacon");
            int majorIndex = cursor.getColumnIndex("major");
            int minorIndex = cursor.getColumnIndex("minor");
            int idStoreIndex = cursor.getColumnIndex("id_store");
            int storeNameIndex = cursor.getColumnIndex("store_name");
            int floorIndex = cursor.getColumnIndex("floor");
            int idCenterIndex = cursor.getColumnIndex("id_center");
            int centerNameIndex = cursor.getColumnIndex("center_name");
            int addressIndex = cursor.getColumnIndex("address");
            int rssiLimitIndex = cursor.getColumnIndex("rssi_limit");

            do {
                result.add(new IBeaconBase(
                        cursor.getInt(idIndex),
                        cursor.getInt(majorIndex),
                        cursor.getInt(minorIndex),
                        cursor.getInt(idStoreIndex),
                        cursor.getInt(floorIndex),
                        cursor.getInt(idCenterIndex),
                        cursor.getInt(rssiLimitIndex),
                        cursor.getString(storeNameIndex),
                        cursor.getString(centerNameIndex),
                        cursor.getString(addressIndex)
                ));

            } while (cursor.moveToNext());
        }

        cursor.close();
        closeLocal();
        return result;
    }

    public ArrayList<Banner> getBannersList() {
        ArrayList<Banner> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM Banners ";
        openLocalToRead();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id_beacon");
            int textIndex = cursor.getColumnIndex("text");
            int urlIndex = cursor.getColumnIndex("url");

            do {
                result.add(new Banner(
                        cursor.getInt(idIndex),
                        cursor.getString(textIndex),
                        cursor.getString(urlIndex)
                ));

            } while (cursor.moveToNext());
        }

        cursor.close();
        closeLocal();
        return result;
    }
}
