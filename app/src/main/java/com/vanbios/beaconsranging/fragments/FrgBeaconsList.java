package com.vanbios.beaconsranging.fragments;

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
import android.widget.TextView;

import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.adapters.BeaconsRangeRecyclerAdapter;
import com.vanbios.beaconsranging.objects.BeaconRangeInfo;
import com.vanbios.beaconsranging.singleton.InfoSingleton;
import com.vanbios.beaconsranging.util.BeaconRangeInfoComparator;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.Unbinder;

import static butterknife.ButterKnife.bind;

/**
 * Created by Ihor Bilous on 29.12.2015.
 */
public class FrgBeaconsList extends Fragment {

    public static final String BROADCAST_FRG_BEACONS_LIST = "com.vanbios.beaconsranging.frgbeaconslist.broadcast";
    public final static String PARAM_STATUS_FRG_BEACONS_LIST = "beacons_list_updated";
    public final static int STATUS_BEACONS_LIST_UPDATED = 3;
    private BroadcastReceiver broadcastReceiver;

    private View view;
    @BindView(R.id.tvFrgBeaconsListEmpty)
    TextView tvEmptyList;
    @BindView(R.id.recyclerFrgBeaconsList)
    RecyclerView recyclerView;
    private BeaconsRangeRecyclerAdapter recyclerAdapter;
    private ArrayList<BeaconRangeInfo> beaconsList;
    private Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_beacons_list, container, false);
        beaconsList = new ArrayList<>();
        initViews();
        makeBroadcastReceiver();
        setVisibility();
        return view;
    }

    private void initViews() {
        unbinder = bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerAdapter = new BeaconsRangeRecyclerAdapter(beaconsList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void makeBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(PARAM_STATUS_FRG_BEACONS_LIST, 0);
                switch (status) {
                    case STATUS_BEACONS_LIST_UPDATED: {
                        beaconsList.clear();
                        beaconsList.addAll(InfoSingleton.getInstance().getBeaconsRangeList());
                        Collections.sort(beaconsList, new BeaconRangeInfoComparator());
                        recyclerAdapter.notifyDataSetChanged();
                        setVisibility();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST_FRG_BEACONS_LIST);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    private void setVisibility() {
        recyclerView.setVisibility(beaconsList.isEmpty() ? View.GONE : View.VISIBLE);
        tvEmptyList.setVisibility(beaconsList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
