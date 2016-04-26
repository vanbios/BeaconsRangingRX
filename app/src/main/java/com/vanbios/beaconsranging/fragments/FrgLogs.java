package com.vanbios.beaconsranging.fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.adapters.LogsRecyclerAdapter;
import com.vanbios.beaconsranging.objects.LogBase;
import com.vanbios.beaconsranging.util.DBStateLogger;
import com.vanbios.beaconsranging.util.DateUtils;
import com.vanbios.beaconsranging.util.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ihor Bilous on 15.01.2016.
 */
public class FrgLogs extends Fragment {

    public final static String BROADCAST_FRG_LOGS = "com.vanbios.beaconsranging.frglogs.broadcast";
    public final static String PARAM_STATUS_FRG_LOGS = "new_log_inserted";
    public final static int STATUS_NEW_LOG_INSERTED = 3;

    private static final String ARG_FRG_TYPE = "arg_frg_type";
    private int type;
    private View view;
    private TextView tvDate, tvEmptyList;
    private DatePickerDialog datePickerDialog;
    private RecyclerView recyclerView;
    private LogsRecyclerAdapter recyclerAdapter;
    private BroadcastReceiver broadcastReceiver;
    private static Calendar calendar;
    private ArrayList<LogBase> logList;


    public static FrgLogs newInstance(int type) {
        FrgLogs pageFragment = new FrgLogs();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_FRG_TYPE, type);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARG_FRG_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_logs, container, false);
        calendar = Calendar.getInstance();
        logList = new ArrayList<>();
        initViews();
        setDateField();
        makeBroadcastReceiver();
        showLogByDate(calendar.getTimeInMillis());
        return view;
    }


    private void initViews() {
        tvDate = (TextView) view.findViewById(R.id.tvFrgLogsDate);
        tvEmptyList = (TextView) view.findViewById(R.id.tvFrgLogsEmpty);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerFrgLogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerAdapter = new LogsRecyclerAdapter(logList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setDateField() {
        Calendar newCalendar = Calendar.getInstance();
        tvDate.setText(DateUtils.dateToString(newCalendar.getTime()));

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (newDate.getTimeInMillis() > System.currentTimeMillis()) {
                    ToastUtils.showClosableToast(getActivity(), getString(R.string.date_cant_be_future), 1);
                } else {
                    tvDate.setText(DateUtils.dateToString(newDate.getTime()));
                    showLogByDate(newDate.getTimeInMillis());
                    calendar = newDate;
                }
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void showLogByDate(long datetime) {
        logList.clear();
        switch (type) {
            case 1: {
                logList.addAll(new DBStateLogger().getStoreEntryLogsToShow(datetime));
                break;
            }
            case 2: {
                logList.addAll(new DBStateLogger().getAdvertisingLogsToShow(datetime));
                break;
            }
            case 3: {
                logList.addAll(new DBStateLogger().getBeaconZoneLogsToShow(datetime));
                break;
            }
        }
        recyclerAdapter.notifyDataSetChanged();
        setVisibility();
    }

    private void setVisibility() {
        recyclerView.setVisibility(logList.isEmpty() ? View.GONE : View.VISIBLE);
        tvEmptyList.setVisibility(logList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void makeBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(PARAM_STATUS_FRG_LOGS, 0);
                switch (status) {
                    case STATUS_NEW_LOG_INSERTED: {
                        showLogByDate(calendar.getTimeInMillis());
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST_FRG_LOGS);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
