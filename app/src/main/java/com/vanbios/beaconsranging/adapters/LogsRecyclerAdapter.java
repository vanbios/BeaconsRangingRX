package com.vanbios.beaconsranging.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.objects.LogBase;

import java.util.ArrayList;

import static butterknife.ButterKnife.findById;

/**
 * Created by Ihor Bilous on 27.01.2016.
 */
public class LogsRecyclerAdapter extends RecyclerView.Adapter<LogsRecyclerAdapter.MainViewHolder> {

    private ArrayList<LogBase> logList;
    private static final int GREEN = 1, RED = 2;


    public LogsRecyclerAdapter(ArrayList<LogBase> list) {
        logList = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GREEN:
                return new ViewHolderGreen(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_log_green, parent, false));
            case RED:
                return new ViewHolderRed(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_log_red, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.tvText.setText(logList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return logList.get(position).getType() == 1 ? GREEN : RED;
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
}
