package com.vanbios.beaconsranging.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.adapters.ViewPagerFragmentAdapter;

import static butterknife.ButterKnife.findById;


/**
 * Created by Ihor Bilous on 09.12.2015.
 */
public class FrgMain extends CommonFragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_main, container, false);
        setViewPager();
        return view;
    }

    private void setViewPager() {
        ViewPager pager = findById(view, R.id.pagerFrgMain);
        ViewPagerFragmentAdapter adapterPager = new ViewPagerFragmentAdapter(getChildFragmentManager());
        adapterPager.addFragment(new FrgBeaconsList(), getResources().getString(R.string.tab_beacons_list));
        adapterPager.addFragment(FrgLogs.newInstance(1), getResources().getString(R.string.tab_entry_exit_stores));
        adapterPager.addFragment(FrgLogs.newInstance(2), getResources().getString(R.string.tab_push_messages));
        adapterPager.addFragment(FrgLogs.newInstance(3), getResources().getString(R.string.tab_entry_exit_beacons));
        adapterPager.addFragment(new FrgTransmitting(), getResources().getString(R.string.tab_emulation));
        adapterPager.addFragment(new FrgSearch(), getResources().getString(R.string.tab_search));

        pager.setAdapter(adapterPager);
        pager.setOffscreenPageLimit(6);

        TabLayout tabs = findById(view, R.id.tabsFrgMain);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public String getTitle() {
        return FrgMain.class.getSimpleName();
    }
}
