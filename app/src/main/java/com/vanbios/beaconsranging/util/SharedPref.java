package com.vanbios.beaconsranging.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ihor Bilous on 08.12.2015.
 */
public class SharedPref {

    private SharedPreferences sharedPreferences;
    private final static String APP_PREFERENCES = "com.vanbios.beaconsranging";

    private final static String IS_FIRST_START = "is_first_start";

    public SharedPref(Context context) {
        this.sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setIsFirstStartApp(boolean isFirstStart){
        sharedPreferences.edit().putBoolean(IS_FIRST_START, isFirstStart).apply();
    }
    public boolean isFirstStartApp(){
        return sharedPreferences.getBoolean(IS_FIRST_START, true);
    }

}
