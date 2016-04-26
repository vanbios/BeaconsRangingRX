package com.vanbios.beaconsranging.singleton;

import com.vanbios.beaconsranging.AppController;
import com.vanbios.beaconsranging.database.DataSourceBeacons;
import com.vanbios.beaconsranging.database.DataSourceLocal;
import com.vanbios.beaconsranging.fragments.FrgSearch;
import com.vanbios.beaconsranging.objects.Banner;
import com.vanbios.beaconsranging.objects.BeaconRangeInfo;
import com.vanbios.beaconsranging.objects.IBeaconBase;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Ihor Bilous on 11.12.2015.
 */
public class InfoSingleton {

    private static volatile InfoSingleton instance;
    private DataSourceLocal dataSourceLocal;
    private DataSourceBeacons dataSourceBeacons;
    private HashSet<Long> beaconSet;
    private ArrayList<IBeaconBase> entryList, advertisingList;
    private ArrayList<Banner> bannersList;
    private ArrayList<BeaconRangeInfo> beaconsRangeList;

    private InfoSingleton() {
        dataSourceLocal = new DataSourceLocal(AppController.getContext());
        dataSourceBeacons = new DataSourceBeacons(AppController.getContext());
        beaconSet = new HashSet<>();
        entryList = dataSourceBeacons.getBeaconsList(DataSourceBeacons.ENTRY_TABLE);
        advertisingList = dataSourceBeacons.getBeaconsList(DataSourceBeacons.ADVERTISING_TABLE);
        bannersList = dataSourceBeacons.getBannersList();
        beaconsRangeList = new ArrayList<>();
    }

    public boolean addToBeaconSet(long beaconId) {
        return beaconSet.add(beaconId);
    }

    public DataSourceLocal getDataSourceLocal() {
        return dataSourceLocal;
    }

    public DataSourceBeacons getDataSourceBeacons() {
        return dataSourceBeacons;
    }

    public ArrayList<IBeaconBase> getEntryList() {
        return entryList;
    }

    public ArrayList<IBeaconBase> getAdvertisingList() {
        return advertisingList;
    }

    public ArrayList<BeaconRangeInfo> getBeaconsRangeList() {
        return beaconsRangeList;
    }

    public void setBeaconsRangeList(ArrayList<BeaconRangeInfo> list) {
        beaconsRangeList.clear();
        beaconsRangeList.addAll(list);
    }

    public int getEntryBeaconIdByMajorMinor(int major, int minor) {
        return getBeaconIdByMajorMinor(major, minor, entryList);
    }

    public int getAdvertisingBeaconIdByMajorMinor(int major, int minor) {
        return getBeaconIdByMajorMinor(major, minor, advertisingList);
    }

    private int getBeaconIdByMajorMinor(int major, int minor, ArrayList<IBeaconBase> list) {
        for (IBeaconBase beacon : list) {
            if (beacon.getMajor() == major && beacon.getMinor() == minor) return beacon.getIdBeacon();
        }
        return 0;
    }

    public int getStoreIdByEntryMajorMinor(int major, int minor) {
        return getStoreIdByMajorMinor(major, minor, entryList);
    }

    private int getStoreIdByMajorMinor(int major, int minor, ArrayList<IBeaconBase> list) {
        for (IBeaconBase beacon : list) {
            if (beacon.getMajor() == major && beacon.getMinor() == minor) return beacon.getIdStore();
        }
        return 0;
    }

    public IBeaconBase findAdvertisingBeaconById(int id) {
        for (IBeaconBase beacon : advertisingList) {
            if (beacon.getIdBeacon() == id) return beacon;
        }
        return null;
    }

    public IBeaconBase findBeaconById(int type, int id) {
        ArrayList<IBeaconBase> list = null;
        switch (type) {
            case FrgSearch.ENTRY: {
                list = entryList;
                break;
            }
            case FrgSearch.ADVERTISING: {
                list = advertisingList;
                break;
            }
        }
        if (list != null && !list.isEmpty()) {
            for (IBeaconBase beacon : list) {
                if (beacon.getIdBeacon() == id) return beacon;
            }
        }
        return null;
    }

    public IBeaconBase findBeaconByMajorMinor(int type, int major, int minor) {
        ArrayList<IBeaconBase> list = null;
        switch (type) {
            case FrgSearch.ENTRY: {
                list = entryList;
                break;
            }
            case FrgSearch.ADVERTISING: {
                list = advertisingList;
                break;
            }
        }
        if (list != null && !list.isEmpty()) {
            for (IBeaconBase beacon : list) {
                if (beacon.getMajor() == major && beacon.getMinor() == minor) return beacon;
            }
        }
        return null;
    }

    public IBeaconBase findBeaconByIdStore(int idStore) {
        for (IBeaconBase beacon : advertisingList) {
            if (beacon.getIdStore() == idStore) return beacon;
        }
        for (IBeaconBase beacon : entryList) {
            if (beacon.getIdStore() == idStore) return beacon;
        }
        return null;
    }

    public Banner findBannerByBeaconId(int beaconId) {
        if (bannersList != null && bannersList.size() > 0) {
            for (Banner banner : bannersList) {
                if (banner.getBeaconId() == beaconId) return banner;
            }
        }
        return null;
    }

    public static InfoSingleton getInstance() {
        InfoSingleton localInstance = instance;
        if (localInstance == null) {
            synchronized (InfoSingleton.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new InfoSingleton();
                }
            }
        }
        return localInstance;
    }
}
