package com.vanbios.beaconsranging.database;

/**
 * Created by Ihor Bilous on 10.12.2015.
 */
public class SqlQueries {

    // table for log info about enter/exit stores
    // state: 1 - you enter, 0 - you leave
    // visibility: 1 - app show push with message, 0 - app ignore this one 'cause it's already shown
    public static final String create_ibstoreentrylogs_table = "CREATE TABLE IBStoreEntryLogs (" +
            "datetime       INTEGER PRIMARY KEY," +
            "id_store       INTEGER NOT NULL," +
            "state          INTEGER NOT NULL," +
            "visibility     INTEGER NOT NULL" +
            ");";

    // table for log info about enter/exit beacons ranging zones
    // state: 1 - you enter, 0 - you leave
    // type: 1 - entry beacon, 0 - advertising beacon
    public static final String create_ibzonelogs_table = "CREATE TABLE IBZoneLogs (" +
            "datetime       INTEGER PRIMARY KEY," +
            "id_beacon      INTEGER NOT NULL," +
            "type           INTEGER NOT NULL," +
            "state          INTEGER NOT NULL" +
            ");";

    // table for log info about advertising push messages
    // visibility: 1 - app show push with message, 0 - app ignore this one 'cause it's already shown
    public static final String create_ibadvertisinglogs_table = "CREATE TABLE IBAdvertisingLogs (" +
            "datetime       INTEGER PRIMARY KEY," +
            "id_beacon      INTEGER NOT NULL," +
            "message        TEXT NOT NULL," +
            "visibility     INTEGER NOT NULL" +
            ");";

    public static final String create_ibeacon_enter_table = "CREATE TABLE ibeacon_enter (" +
            "uuid           TEXT NOT NULL," +
            "major          TEXT NOT NULL," +
            "minor          TEXT NOT NULL," +
            "is_notify       INTEGER NOT NULL" +
            ");";

    public static final String create_ibeacon_ads_table = "CREATE TABLE ibeacon_ads (" +
            "uuid           TEXT NOT NULL," +
            "major          TEXT NOT NULL," +
            "minor          TEXT NOT NULL," +
            "is_notify       INTEGER NOT NULL" +
            ");";

}