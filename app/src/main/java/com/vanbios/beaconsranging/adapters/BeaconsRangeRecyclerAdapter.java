package com.vanbios.beaconsranging.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.objects.BeaconRangeInfo;

import java.util.ArrayList;

import static butterknife.ButterKnife.findById;

/**
 * Created by Ihor Bilous on 28.01.2016.
 */
public class BeaconsRangeRecyclerAdapter extends RecyclerView.Adapter<BeaconsRangeRecyclerAdapter.MainViewHolder> {

    private ArrayList<BeaconRangeInfo> beaconsList;
    private static final int GREEN = 1, YELLOW = 2, RED = 3;


    public BeaconsRangeRecyclerAdapter(ArrayList<BeaconRangeInfo> list) {
        beaconsList = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GREEN:
                return new ViewHolderGreen(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_log_green, parent, false));
            case YELLOW:
                return new ViewHolderYellow(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_log_yellow, parent, false));
            case RED:
                return new ViewHolderRed(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_log_red, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.tvText.setText(beaconsList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return beaconsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        BeaconRangeInfo beacon = beaconsList.get(position);
        return beacon.getTypeByRSSI(beacon.getRssi());
    }


    static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView tvText;

        public MainViewHolder(View view, TextView tvText) {
            super(view);
            this.tvText = tvText;
        }
    }

    private static class ViewHolderGreen extends MainViewHolder {
        public ViewHolderGreen(View view) {
            super(view, findById(view, R.id.tvItemLogGreen));
        }
    }

    private static class ViewHolderRed extends MainViewHolder {
        public ViewHolderRed(View view) {
            super(view, findById(view, R.id.tvItemLogRed));
        }
    }

    private static class ViewHolderYellow extends MainViewHolder {
        public ViewHolderYellow(View view) {
            super(view, findById(view, R.id.tvItemLogYellow));
        }
    }
}