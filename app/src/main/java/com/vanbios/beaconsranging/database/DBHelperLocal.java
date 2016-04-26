package com.vanbios.beaconsranging.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ihor Bilous on 10.12.2015.
 */
public class DBHelperLocal extends SQLiteOpenHelper {

    Context context;
    public static final String DATABASE_NAME = "IBLogs.db";
    private static final int DATABASE_VERSION = 1;


    public DBHelperLocal(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlQueries.create_ibeacon_enter_table);
        db.execSQL(SqlQueries.create_ibeacon_ads_table);
        db.execSQL(SqlQueries.create_ibstoreentrylogs_table);
        db.execSQL(SqlQueries.create_ibadvertisinglogs_table);
        db.execSQL(SqlQueries.create_ibzonelogs_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}