package com.vanbios.beaconsranging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.estimote.sdk.SystemRequirementsChecker;
import com.vanbios.beaconsranging.database.DBHelperBeacons;
import com.vanbios.beaconsranging.fragments.CommonFragment;
import com.vanbios.beaconsranging.fragments.FrgLogs;
import com.vanbios.beaconsranging.fragments.FrgMain;
import com.vanbios.beaconsranging.service.ServiceListenerBeacon;
import com.vanbios.beaconsranging.singleton.InfoSingleton;
import com.vanbios.beaconsranging.util.BluetoothUtils;
import com.vanbios.beaconsranging.util.ToastUtils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Ihor Bilous on 08.12.2015.
 */
public class MainActivity extends AppCompatActivity {

    private static long backPressExitTime;
    private static Context context;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        fragmentManager = getSupportFragmentManager();

        addFragment(new FrgMain());

        BluetoothUtils.setBluetooth(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBHelperBeacons myDbHelper = new DBHelperBeacons(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clean_db_ads_enter:
                InfoSingleton.getInstance().getDataSourceLocal().cleanDBAdsEnter();
                ToastUtils.showClosableToast(context, "ADS and Enter notifications removed!", 1);
                return true;
            case R.id.action_clean_db_logs:
                InfoSingleton.getInstance().getDataSourceLocal().cleanDBLogs();
                ToastUtils.showClosableToast(context, "Logs removed!", 1);
                Intent intentFrgOrdersAll = new Intent(FrgLogs.BROADCAST_FRG_LOGS);
                intentFrgOrdersAll.putExtra(FrgLogs.PARAM_STATUS_FRG_LOGS,
                        FrgLogs.STATUS_NEW_LOG_INSERTED);
                AppController.getContext().sendBroadcast(intentFrgOrdersAll);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(context, ServiceListenerBeacon.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(context, ServiceListenerBeacon.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }


    public void addFragment(CommonFragment f) {
        treatFragment(f, true);
    }

    public void replaceFragment(CommonFragment f) {
        treatFragment(f, false);
    }

    public Fragment getTopFragment() {
        return fragmentManager.findFragmentById(R.id.fragment_container);
    }

    private void treatFragment(Fragment f, boolean addToBackStack) {
        String tag = f.getClass().getSimpleName();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.fragment_container, f, tag);
        if (addToBackStack) ft.addToBackStack(tag);
        ft.commit();
    }

    public void popFragment() {
        fragmentManager.popBackStack();
    }

    public void popFragmentByTag(String tag) {
        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getTopFragment();
        if (fragment instanceof CommonFragment) {
            if (backPressExitTime + 2000 > System.currentTimeMillis()) {
                System.exit(0);
                this.finish();
            } else {
                ToastUtils.showClosableToast(this, getString(R.string.press_again_to_exit), 1);
                backPressExitTime = System.currentTimeMillis();
            }
        } else popFragment();
    }
}

